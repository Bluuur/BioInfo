import java.util.Scanner;

/**
 * @author Zidong Zh
 * @date 2022/11/10
 * @repo <a href="https://github.com/Bluuur/BioInfo">...</a>
 */
public class Work01 {
    public static void main(String[] args) {
        char[] sequence1;
        char[] sequence2;

        sequence1 = getSequence();
        sequence2 = getSequence();
        sequence1 = "AACGTACTCAAGTCT".toCharArray();
        sequence2 = "TCGTACTCTAACGAT".toCharArray();
//        sequence1 = "ACGTC".toCharArray();
//        sequence2 = "CG".toCharArray();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Rule 1: match = 9, mismatch = -6, insertion = deletion = -2 (default)");
        System.out.println("Rule 2: match = 9, mismatch = -3, insertion = deletion = -2");
        System.out.print("Choose rule(1/2):");
        int rule = scanner.nextInt();

        System.out.print("Choose algorithm, 1 for Needleman-Wunsch(default), 2 for Smith-Waterman:");
        int algorithm = scanner.nextInt();

        int match1 = 9;
        int mismatch1 = -6;
        int gap1 = -2;

        int match2 = 9;
        int mismatch2 = -3;
        int gap2 = -2;

        int[][] scoreMatrix;

        if (algorithm == 2) {
            scoreMatrix = rule == 2 ? localAlignment(sequence1, sequence2, match2, mismatch2, gap2) : localAlignment(sequence1, sequence2, match1, mismatch1, gap1);
        } else {
            scoreMatrix = rule == 2 ? globalAlignment(sequence1, sequence2, match2, mismatch2, gap2) : globalAlignment(sequence1, sequence2, match1, mismatch1, gap1);
        }

        // print scoreMatrix
        for (int i = 0; i <= sequence1.length + 1; i++) {
            if (i <= 1) {
                System.out.printf("%4s", "");
            } else {
                System.out.printf("%4s", sequence1[i - 2]);
            }
        }

        System.out.println();

        int seq2Pointer = 0;
        for (int i = 0; i < scoreMatrix.length; i++) {
            if (i == 0) {
                System.out.printf("%4s", "");
            } else if (i < 2) {
                System.out.printf("%4s", sequence2[i - 1]);
            }
            for (int j = 0; j < sequence1.length + 1; j++) {
                System.out.printf("%4d", scoreMatrix[i][j]);
            }
            System.out.println();
        }


        int score = 0;
        if (algorithm == 1) {
            score = scoreMatrix[sequence1.length][sequence2.length];
        } else {
            int i = 0;
            int j = 0;
            int pointer1 = 0;
            int pointer2 = 0;
            for (; i < scoreMatrix.length; i++) {
                for (; j < scoreMatrix[i].length; j++) {
                    if (scoreMatrix[i][j] > score) {
                        score = scoreMatrix[i][j];
                        pointer1 = i;
                        pointer2 = j;
                    }
                }
            }
        }
        System.out.println("Score: " + score);

        // print best match
//        int[] track = algorithm == 1 ? globalBackTrack(scoreMatrix) : localBackTrack(scoreMatrix);
        int[] track = globalBackTrack(scoreMatrix);

        char[] outSeq1 = new char[track.length];
        char[] outSeq2 = new char[track.length];
        int nowAt1 = 0;
        int nowAt2 = 0;
        int pointer1 = 0;
        int pointer2 = 0;
        for (int i = track.length - 1; i >= 0; i--) {
            if (track[i] == 0) {
                continue;
            } else if (track[i] == 1) {
                outSeq1[nowAt1] = sequence1[pointer1];
                outSeq2[nowAt2] = sequence2[pointer2];
                nowAt1++;
                nowAt2++;
                pointer1++;
                pointer2++;
            } else if (track[i] == 2) {
                outSeq1[nowAt1] = '-';
                nowAt1++;
            } else if (track[i] == 3) {
                outSeq2[nowAt2] = '-';
                nowAt2++;
            }
        }
        System.out.print("Seq 1 best match:");
        for (int i = 0; i < outSeq1.length; i++) {
            if (outSeq1[i] != '\u0000') {
                System.out.print(outSeq1[i]);
            }
        }
        System.out.println();
        System.out.print("Seq 2 best match:");
        for (int i = 0; i < outSeq2.length; i++) {
            if (outSeq1[i] != '\u0000') {
                System.out.print(outSeq2[i]);
            }
        }


    }

    /**
     * Get input sequence
     *
     * @return input sequence
     */
    public static char[] getSequence() {
        char[] seq;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter sequence:");
        seq = scanner.nextLine().toCharArray();

        System.out.print("Input sequence: ");
        System.out.print(seq);
        System.out.println(", length: " + seq.length);

        return seq;
    }

    /**
     * Local alignment, Needleman-Wunsch
     *
     * @param sequence1 Input sequence 1
     * @param sequence2 Input sequence 2
     * @param match     Score for match
     * @param mismatch  Score for mismatch
     * @param gap       Score for gap (Insertion or deletion)
     * @return Score matrix
     */
    public static int[][] globalAlignment(char[] sequence1, char[] sequence2, int match, int mismatch, int gap) {
        int rowLen = sequence2.length + 1;
        int colLen = sequence1.length + 1;
        int[][] scoreMatrix = new int[rowLen][colLen];

        for (int i = 0; i < rowLen; i++) {
            for (int j = 0; j < colLen; j++) {
                if (i == 0 || j == 0) {
                    scoreMatrix[i][j] = i * gap + j * gap;
                } else {
                    int a = (sequence1[i - 1] == sequence2[j - 1] ? scoreMatrix[i - 1][j - 1] + match : scoreMatrix[i - 1][j - 1] + mismatch);
                    int b = scoreMatrix[i - 1][j] + gap;
                    int c = scoreMatrix[i][j - 1] + gap;
                    scoreMatrix[i][j] = Math.max(a, Math.max(b, c));
                }
            }
        }

        return scoreMatrix;
    }

    /**
     * Local alignment, Smith-Waterman
     *
     * @param sequence1     Input sequence 1
     * @param sequence2     Input sequence 2
     * @param match    Score for match
     * @param mismatch Score for mismatch
     * @param gap      Score for gap (Insertion or deletion)
     * @return Score matrix
     */
    public static int[][] localAlignment(char[] sequence1, char[] sequence2, int match, int mismatch, int gap) {
        int rowLen = sequence2.length + 1;
        int colLen = sequence1.length + 1;
        int[][] scoreMatrix = new int[rowLen][colLen];

        for (int i = 0; i <= sequence2.length; i++) {
            for (int j = 0; j <= sequence1.length; j++) {
                if (i == 0 || j == 0) {
                    scoreMatrix[i][j] = 0;
                } else {
                    int a = (sequence1[i - 1] == sequence2[j - 1] ? scoreMatrix[i - 1][j - 1] + match : scoreMatrix[i - 1][j - 1] + mismatch);
                    int b = scoreMatrix[i - 1][j] + gap;
                    int c = scoreMatrix[i][j - 1] + gap;
                    scoreMatrix[i][j] = Math.max(0, Math.max(a, Math.max(b, c)));
                }
            }
        }

        return scoreMatrix;
    }

    /**
     * Back tracking algorithm
     *
     * @param matrix score matrix
     * @return back track(read backward)
     */
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
//            System.out.println("now at (" + i + "," + j + ")");
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

            int a = matrix[j][i - 1];
            int b = matrix[j - 1][i - 1];
            int c = matrix[j - 1][i];

            int max = Math.max(a, Math.max(b, c));

            if (max == a) {
                track[t] = 2;
                i -= 1;
                t++;
            } else if (max == b) {
                track[t] = 1;
                i -= 1;
                j -= 1;
                t++;
            } else {
                track[t] = 3;
                j -= 1;
                t++;
            }
        }
        return track;
    }

    public static int[] localBackTrack(int[][] matrix) {
        int rowLen = matrix.length;
        int colLen = matrix[0].length;

        // init an array that record track (read backward)
        // 1: top left, 2: top, 3: left
        int[] track = new int[Math.max(rowLen, colLen) * 2];
        int t = 0;
//        int i = rowLen - 1;
//        int j = colLen - 1;

        // find max in score matrix
        int score = matrix[0][0];
        int i = 0;
        int j = 0;
        int pointer1 = 0;
        int pointer2 = 0;
        for (; i < rowLen; i++) {
            for (; j < colLen; j++) {
                if (matrix[i][j] > score) {
                    score = matrix[i][j];
                    pointer1 = i;
                    pointer2 = j;
                }
            }
        }

        while (i >= 0 && j >= 0) {
//            System.out.println("now at (" + i + "," + j + ")");

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

            int a = matrix[j][i - 1];
            int b = matrix[j - 1][i - 1];
            int c = matrix[j - 1][i];

            int max = Math.max(a, Math.max(b, c));
            if (a == 0 && b == 0 && c == 0) {
                return track;
            } else if (max == a) {
                track[t] = 2;
                i -= 1;
                t++;
            } else if (max == b) {
                track[t] = 1;
                i -= 1;
                j -= 1;
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
