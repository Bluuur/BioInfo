import java.util.Arrays;
import java.util.Scanner;

/**
 * @author Zidong Zh
 * @date 2022/11/10
 * @repo
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

        int match1 = 9;
        int mismatch1 = -6;
        int gap1 = -2;

        int match2 = 9;
        int mismatch2 = -3;
        int gap2 = -2;

        int[][] matrix = rule == 2 ? localAlignment(seq1, seq2, match2, mismatch2, gap2) : localAlignment(seq1, seq2, match1, mismatch1, gap1);

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

    public static int[][] globalAlignment(char[] seq1, char[] seq2, int match, int mismatch, int gap) {

    }
}


