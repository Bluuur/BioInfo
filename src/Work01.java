import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Zidong Zh
 * @date 2022/11/10
 * @repo <a href="https://github.com/Bluuur/BioInfo">...</a>
 */
public class Work01 {
    public static void main(String[] args) {
        char[] seq1;
        char[] seq2;

//        seq1 = getSequence();
//        seq2 = getSequence();
        seq1 = "AACGTACTCAAGTCT".toCharArray();
        seq2 = "TCGTACTCTAACGAT".toCharArray();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Rule 1: match = 9, mismatch = -6, insertion = deletion = -2 (default)");
        System.out.println("Rule 2: match = 9, mismatch = -3, insertion = deletion = -2");
        System.out.print("Choose rule(1/2):");
        int rule = scanner.nextInt();

//        System.out.println("Choose algorithm, 1 for Needleman-Wunsch, 2 for Smith-Waterman:");
//        int algorithm = scanner.nextInt();

        int match1 = 9;
        int mismatch1 = -6;
        int gap1 = -2;

        int match2 = 9;
        int mismatch2 = -3;
        int gap2 = -2;

        int[][] matrix = rule == 2 ? localAlignment(seq1, seq2, match2, mismatch2, gap2) : localAlignment(seq1, seq2, match1, mismatch1, gap1);


        // print result
        for (int i = 0; i <= seq1.length + 1; i++) {
            if (i <= 1) {
                System.out.printf("%4s", "");
            } else {
                System.out.printf("%4s", seq1[i - 2]);
            }
        }

        System.out.println();

        for (int i = 0; i < seq1.length + 1; i++) {
            if (i == 0) {
                System.out.printf("%4s", "");
            } else {
                System.out.printf("%4s", seq2[i - 1]);
            }
            for (int j = 0; j < seq2.length + 1; j++) {
                System.out.printf("%4d", matrix[j][i]);
            }
            System.out.println();
        }

        System.out.println("Score: "+matrix[seq1.length+1][seq2.length+1]);

        // print best match

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
     * @param seq1     Input sequence 1
     * @param seq2     Input sequence 2
     * @param match    Score for match
     * @param mismatch Score for mismatch
     * @param gap      Score for gap (Insertion or deletion)
     * @return Marking matrix
     */
    public static int[][] localAlignment(char[] seq1, char[] seq2, int match, int mismatch, int gap) {
        int[][] matrix = new int[seq1.length + 1][seq2.length + 1];

        for (int i = 0; i <= seq1.length; i++) {
            for (int j = 0; j <= seq2.length; j++) {
                if (i == 0 || j == 0) {
                    matrix[i][j] = i * gap + j * gap;
                } else {
                    int a = (seq1[i - 1] == seq2[j - 1] ? matrix[i - 1][j - 1] + match : matrix[i - 1][j - 1] + mismatch);
                    int b = matrix[i - 1][j] + gap;
                    int c = matrix[i][j - 1] + gap;
                    matrix[i][j] = Math.max(a, Math.max(b, c));
                }
            }
        }

        return matrix;
    }

    /**
     * Global alignment, Smith-Waterman
     *
     * @param seq1     Input sequence 1
     * @param seq2     Input sequence 2
     * @param match    Score for match
     * @param mismatch Score for mismatch
     * @param gap      Score for gap (Insertion or deletion)
     * @return Marking matrix
     */
    public static int[][] globalAlignment(char[] seq1, char[] seq2, int match, int mismatch, int gap) {
        int[][] matrix = new int[seq1.length + 1][seq2.length + 1];

        for (int i = 0; i <= seq1.length; i++) {
            for (int j = 0; j <= seq2.length; j++) {
                if (i == 0 || j == 0) {
                    matrix[i][j] = i * gap + j * gap;
                } else {
                    int a = (seq1[i - 1] == seq2[j - 1] ? matrix[i - 1][j - 1] + match : matrix[i - 1][j - 1] + mismatch);
                    int b = matrix[i - 1][j] + gap;
                    int c = matrix[i][j - 1] + gap;
                    matrix[i][j] = Math.max(0, Math.max(a, Math.max(b, c)));
                }
            }
        }

        return matrix;
    }

    public static int[] backTrack(int[][] matrix) {
        int rowLen = matrix.length;
        int colLen = matrix[0].length;

        // init an array that record track (read backward)
        // 0: top left, 1: top, -1: left
        int[] track = new int[Math.max(rowLen, colLen) * 2];
        int t = 0;
        int i = rowLen - 1;
        int j = colLen - 1;
        while (i >= 0 && j >= 0) {
            int a = matrix[j][i - 1];
            int b = matrix[j - 1][i - 1];
            int c = matrix[j - 1][i];

            int max = Math.max(a, Math.max(b, c));

            if (max == a) {
                track[t] = 1;
                i -= 1;
                t++;
            } else if (max == b) {
                track[t] = 0;
                i -= 1;
                j -= 1;
                t++;
            } else {
                track[t] = -1;
                j -= 1;
                t++;
            }
        }
        return track;
    }
}


