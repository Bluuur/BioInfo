import java.util.Scanner;

/**
 * @author Zidong Zh
 * @date 2022/11/10
 * @repo <a href="https://github.com/Bluuur/BioInfo">...</a>
 */
public class Work {
    public static void main(String[] args) {
        char[] sequence1;
        char[] sequence2;

        int match1 = 9;
        int mismatch1 = -6;
        int gap1 = -2;

        int match2 = 9;
        int mismatch2 = -3;
        int gap2 = -2;

        int[][] scoreMatrix = null;

        sequence1 = "AACGTACTCAAGTCT".toCharArray();
        sequence2 = "TCGTACTCTAACGAT".toCharArray();

        Scanner scanner = new Scanner(System.in);
//        System.out.println("Rule 1: match = 9, mismatch = -6, insertion = deletion = -2 (default)");
//        System.out.println("Rule 2: match = 9, mismatch = -3, insertion = deletion = -2");
//        System.out.print("Choose rule(1/2):");
//        int rule = scanner.nextInt();
        int rule = 1;

        System.out.print("Choose algorithm, 1 for Needleman-Wunsch(default), 2 for Smith-Waterman:");
        int algorithm = scanner.nextInt();
        System.out.println();

        // Get score matrix
        if (algorithm == 2) {
            scoreMatrix = rule == 2 ? localAlignment(sequence1, sequence2, match2, mismatch2, gap2) : localAlignment(sequence1, sequence2, match1, mismatch1, gap1);
        } else {
//            scoreMatrix = rule == 2 ? globalAlignment(sequence1, sequence2, match2, mismatch2, gap2) : globalAlignment(sequence1, sequence2, match1, mismatch1, gap1);
        }

        // Print score matrix
        // add 2 blank space
        for (int i = 0; i < sequence1.length + 2; i++) {
            if (i < 2) {
                System.out.printf("%4s", "");
            } else {
                System.out.printf("%4s", sequence1[i - 2]);
            }
        }
        // new line
        System.out.println();

        for (int i = 0; i < scoreMatrix.length; i++) {
            // print sequence 2
            if (i == 0) {
                System.out.printf("%4s", "");
            } else {
                System.out.printf("%4s", sequence2[i - 1]);
            }
            // print score matrix
            for (int j = 0; j < scoreMatrix[0].length; j++) {
                System.out.printf("%4s", scoreMatrix[i][j]);
            }
            // new line
            System.out.println();
        }

        // print score
        int score = 0;
        if (algorithm == 2) {
            for (int[] matrix : scoreMatrix) {
                for (int i : matrix) {
                    if (i > score) {
                        score = i;
                    }
                }
            }
        } else {
            score = scoreMatrix[sequence2.length][sequence1.length];
        }
        System.out.println();
        System.out.println("Score: " + score);

        // get best match
        int[] track = globalBackTrack(scoreMatrix);

//        System.out.println(Arrays.toString(track));

        char[] outSeq1 = new char[track.length];
        char[] outSeq2 = new char[track.length];
        int nowAt1 = 0;
        int nowAt2 = 0;
        int pointer1 = 0;
        int pointer2 = 0;
        for (int i = track.length - 1; i >= 0; i--) {
            if (track[i] == 1) {
                outSeq1[nowAt1] = sequence1[pointer1];
                outSeq2[nowAt2] = sequence2[pointer2];
                nowAt1++;
                pointer1++;
                nowAt2++;
                pointer2++;
            } else if (track[i] == 2) {
                outSeq1[nowAt1] = '-';
                outSeq2[nowAt2] = sequence2[pointer2];
                nowAt1++;
                nowAt2++;
                pointer2++;
            } else if (track[i] == 3) {
                outSeq1[nowAt1] = sequence1[pointer1];
                outSeq2[nowAt2] = '-';
                nowAt1++;
                pointer1++;
                nowAt2++;
            }
        }

        // print best match
        System.out.print("Seq 1 best match:");
        for (char c : outSeq1) {
            if (c != '\u0000') {
                System.out.print(c);
            }
        }
        System.out.println();
        System.out.print("Seq 2 best match:");
        for (char c : outSeq2) {
            if (c != '\u0000') {
                System.out.print(c);
            }
        }
    }


    public static int[][] localAlignment(char[] sequence1, char[] sequence2, int match, int mismatch, int gap) {
        int rowLen = sequence2.length + 1;
        int colLen = sequence1.length + 1;
        int[][] scoreMatrix = new int[rowLen][colLen];

        for (int i = 0; i <= sequence2.length; i++) {
            for (int j = 0; j <= sequence1.length; j++) {
                if (i == 0 || j == 0) {
                    scoreMatrix[i][j] = 0;
                } else {
                    int a = (sequence2[i - 1] == sequence1[j - 1] ? scoreMatrix[i - 1][j - 1] + match : scoreMatrix[i - 1][j - 1] + mismatch);
                    int b = scoreMatrix[i - 1][j] + gap;
                    int c = scoreMatrix[i][j - 1] + gap;
                    scoreMatrix[i][j] = Math.max(0, Math.max(a, Math.max(b, c)));
                }
            }
        }

        return scoreMatrix;
    }

    public static int[] globalBackTrack(int[][] matrix) {
        int rowLen = matrix.length;
        int colLen = matrix[0].length;

        // init an array that record track (read backward)
        // 1: top left, 2: top, 3: left
        int[] track = new int[Math.max(rowLen, colLen) * 2];
        int t = 0;
        int i = rowLen - 1;
        int j = colLen - 1;
        while (i >= 0 && j >= 0) {
        //System.out.println("now at (" + i + "," + j + ")");
            if (i == 0 && j != 0) {
                track[t] = 3;
                j -= 1;
                t++;
                continue;
            } else if (i != 0 && j == 0) {
                track[t] = 2;
                i -= 1;
                t++;
                continue;
            } else if (i == 0 && j == 0) {
                break;
            }

            int a = matrix[i - 1][j];
            int b = matrix[i - 1][j - 1];
            int c = matrix[i][j - 1];

            int max = Math.max(a, Math.max(b, c));

            if (max == b) {
                track[t] = 1;
                i -= 1;
                j -= 1;
                t++;
            } else if (max == a) {
                track[t] = 2;
                i -= 1;
                t++;
            } else {
                track[t] = 3;
                j -= 1;
                t++;
            }
        }
        return track;
    }
}