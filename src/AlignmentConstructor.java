import java.sql.SQLOutput;
import java.util.Scanner;

/**
 * @author Zidong Zh
 * @date 2022/11/10
 */
class AlignmentConstructor {
    private char[] sequence1;
    private char[] sequence2;
    private int[][] scoreMatrix;
    private int[][] trackMatrix;
    private ScoreRule scoreRule;
    //    private int[] track;
    private static int LEFT_TOP = 1;
    private static int LEFT = 2;
    private static int TOP = 4;


    /**
     * Constructor
     *
     * @param scoreRule score rule
     */
    public AlignmentConstructor(ScoreRule scoreRule) {
        this.sequence1 = getInputSequence(1);
        this.sequence2 = getInputSequence(2);
        this.scoreMatrix = new int[sequence2.length + 1][sequence1.length + 1];
        this.trackMatrix = new int[sequence2.length + 1][sequence1.length + 1];
        this.scoreRule = scoreRule;
    }

    public AlignmentConstructor(char[] sequence1, char[] sequence2, ScoreRule scoreRule) {
        this.sequence1 = sequence1;
        this.sequence2 = sequence2;
        this.scoreMatrix = new int[sequence2.length + 1][sequence1.length + 1];
        this.trackMatrix = new int[sequence2.length + 1][sequence1.length + 1];
        this.scoreRule = scoreRule;
    }

    public AlignmentConstructor() {
    }

    /**
     * input sequence
     *
     * @return
     */
    public char[] getInputSequence(int seqNO) {
        char[] sequence;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter sequence " + seqNO + " :");
        sequence = scanner.nextLine().toCharArray();

        System.out.print("Input sequence: ");
        System.out.print(sequence);
        System.out.println(", length: " + sequence.length);

        return sequence;
    }

    /**
     * global alignment
     */
    public void globalAlignment() {
        System.out.println("---------- Global Alignment -----------");
        System.out.println(scoreRule);
        int rowLen = sequence2.length + 1;
        int colLen = sequence1.length + 1;

        for (int i = 0; i < rowLen; i++) {
            for (int j = 0; j < colLen; j++) {
                if (i == 0 || j == 0) {
                    scoreMatrix[i][j] = i * scoreRule.getGap() + j * scoreRule.getGap();
                    if (i == 0 && j == 0) {
                        trackMatrix[i][j] = 0;
                    } else if (i == 0) {
                        trackMatrix[i][j] = LEFT;
                    } else {
                        trackMatrix[i][j] = TOP;
                    }
                } else {
                    int leftTop = (sequence2[i - 1] ==
                            sequence1[j - 1] ? scoreMatrix[i - 1][j - 1] + scoreRule.getMatch()
                            : scoreMatrix[i - 1][j - 1] + scoreRule.getMismatch());
                    int top = scoreMatrix[i - 1][j] + scoreRule.getGap();
                    int left = scoreMatrix[i][j - 1] + scoreRule.getGap();
                    int max = Math.max(leftTop, Math.max(top, left));

                    int trace = 0;

                    if (max == leftTop) {
                        trace += LEFT_TOP;
                    }
                    if (max == left) {
                        trace += LEFT;
                    }
                    if (max == top) {
                        trace += TOP;
                    }

                    scoreMatrix[i][j] = max;
                    trackMatrix[i][j] = trace;
                }
            }
        }

        printScoreMatrix();
        int score = scoreMatrix[sequence2.length][sequence1.length];
        System.out.println("Score: " + score);
        globalTrackBack();
        System.out.println();
    }

    /**
     * local alignment
     */
    public void localAlignment() {
        System.out.println("---------- Local Alignment -----------");
        System.out.println(scoreRule);
        int rowLen = sequence2.length + 1;
        int colLen = sequence1.length + 1;

        for (int i = 0; i <= sequence2.length; i++) {
            for (int j = 0; j <= sequence1.length; j++) {
                if (i == 0 || j == 0) {
                    scoreMatrix[i][j] = 0;
                    if (i == 0 && j == 0) {
                        trackMatrix[i][j] = 0;
                    } else if (i == 0) {
                        trackMatrix[i][j] = LEFT;
                    } else {
                        trackMatrix[i][j] = TOP;
                    }
                } else {
                    int a = (sequence2[i - 1] ==
                            sequence1[j - 1] ? scoreMatrix[i - 1][j - 1] + scoreRule.getMatch()
                            : scoreMatrix[i - 1][j - 1] + scoreRule.getMismatch());
                    int b = scoreMatrix[i - 1][j] + scoreRule.getGap();
                    int c = scoreMatrix[i][j - 1] + scoreRule.getGap();
                    scoreMatrix[i][j] = Math.max(0, Math.max(a, Math.max(b, c)));
                }
            }
        }

        printScoreMatrix();
        int score = 0;
        for (int[] matrix : scoreMatrix) {
            for (int i : matrix) {
                if (i > score) {
                    score = i;
                }
            }
        }
        System.out.println("Score: " + score);
        System.out.println();
        localTrackBack();

    }

    /**
     * print score matrix
     */
    public void printScoreMatrix() {
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
    }

    /**
     * track back from track matrix (global alignment)
     */
    public void globalTrackBack() {
        System.out.println();

        // print track matrix
//        for (int i = 0; i < trackMatrix.length; i++) {
//            for (int j = 0; j < trackMatrix[0].length; j++) {
//                System.out.printf("%3d", trackMatrix[i][j]);
//            }
//            System.out.println();
//        }
//        System.out.println();

        StringBuilder seqOut1 = new StringBuilder();
        StringBuilder seqOut2 = new StringBuilder();

        boolean flag = false;
        int i, j;

        while (true) {
            seqOut1.append(' ');
            seqOut2.append(' ');
            flag = false;
            i = sequence2.length;
            j = sequence2.length;
            while (trackMatrix[i][j] != 0) {
                switch (trackMatrix[i][j]) {

                    // leftTop
                    case 1:
                        seqOut1.append(sequence1[j - 1]);
                        seqOut2.append(sequence2[i - 1]);
                        i--;
                        j--;
                        break;

                    // left
                    case 2:
                        seqOut1.append(sequence1[j - 1]);
                        seqOut2.append('-');
                        j--;
                        break;

                    // 1 + 2 leftTop & left
                    case 3:
                        // -2 once at cross
                        if (!flag) {
                            trackMatrix[i][j] -= 2;
                            flag = true;
                        }
                        seqOut1.append(sequence1[j - 1]);
                        seqOut2.append('-');
                        j--;
                        break;

                    // top
                    case 4:
                        seqOut1.append('-');
                        seqOut2.append(sequence2[i - 1]);
                        i--;
                        break;

                    case 5: // 1 + 4
                    case 6: // 2 + 4
                    case 7: // 1 + 2 + 4
                        // -4 once at crossing
                        if (!flag) {
                            trackMatrix[i][j] -= 4;
                            flag = true;
                        }
                        seqOut1.append('-');
                        seqOut2.append(sequence2[i - 1]);
                        i--;
                        break;
                    default:
                        System.out.println("Error");
                        return;
                }
            }
            if (!flag) {
                break;
            }
        }
        System.out.println("Sequence1 best match: " + seqOut1.reverse());
        System.out.println("Sequence1 best match: " + seqOut2.reverse());
    }

    /**
     * track back from track matrix (local alignment)
     */
    public void localTrackBack() {

//        System.out.println();
//        for (int i = 0; i < trackMatrix.length; i++) {
//            for (int j = 0; j < trackMatrix[0].length; j++) {
//                System.out.printf("%3d", trackMatrix[i][j]);
//            }
//            System.out.println();
//        }

        System.out.println();

        StringBuilder seqOut1 = new StringBuilder();
        StringBuilder seqOut2 = new StringBuilder();

        int score = scoreMatrix[0][0];
        int pointer1 = 0;
        int pointer2 = 0;
        for (int i = 0; i < scoreMatrix.length; i++) {
            for (int j = 0; j < scoreMatrix[0].length; j++) {
                if (scoreMatrix[i][j] > score) {
                    score = scoreMatrix[i][j];
                    pointer1 = i;
                    pointer2 = j;
                }
            }
        }


        boolean flag = false;

        while (true) {
            seqOut1.append(' ');
            seqOut2.append(' ');
            flag = false;

            while (trackMatrix[pointer1][pointer2] != 0) {
                switch (trackMatrix[pointer1][pointer2]) {

                    // leftTop
                    case 1:
                        seqOut1.append(sequence1[pointer2 - 1]);
                        seqOut2.append(sequence2[pointer1 - 1]);
                        pointer1--;
                        pointer2--;
                        break;

                    // left
                    case 2:
                        seqOut1.append(sequence1[pointer2 - 1]);
                        seqOut2.append('-');
                        pointer2--;
                        break;

                    // 1 + 2 leftTop & left
                    case 3:
                        // -2 once at cross
                        if (!flag) {
                            trackMatrix[pointer1][pointer2] -= 2;
                            flag = true;
                        }
                        seqOut1.append(sequence1[pointer2 - 1]);
                        seqOut2.append('-');
                        pointer2--;
                        break;

                    // top
                    case 4:
                        seqOut1.append('-');
                        seqOut2.append(sequence2[pointer1 - 1]);
                        pointer1--;
                        break;

                    case 5: // 1 + 4
                    case 6: // 2 + 4
                    case 7: // 1 + 2 + 4
                        // -4 once at crossing
                        if (!flag) {
                            trackMatrix[pointer1][pointer2] -= 4;
                            flag = true;
                        }
                        seqOut1.append('-');
                        seqOut2.append(sequence2[pointer1 - 1]);
                        pointer1--;
                        break;
                    default:
                        System.out.println("Error");
                        return;
                }
            }
            if (!flag) {
                break;
            }
        }
        System.out.println("Sequence1 best match: " + seqOut1.reverse());
        System.out.println("Sequence1 best match: " + seqOut2.reverse());
    }

    public void trackBack0() {
        int rowLen = scoreMatrix.length;
        int colLen = scoreMatrix[0].length;

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

            int a = scoreMatrix[i - 1][j];
            int b = scoreMatrix[i - 1][j - 1];
            int c = scoreMatrix[i][j - 1];

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
    }

// getter & setter

    public char[] getSequence1() {
        return sequence1;
    }

    public void setSequence1(char[] sequence1) {
        this.sequence1 = sequence1;
    }

    public char[] getSequence2() {
        return sequence2;
    }

    public void setSequence2(char[] sequence2) {
        this.sequence2 = sequence2;
    }

    public int[][] getScoreMatrix() {
        return scoreMatrix;
    }

    public void setScoreMatrix(int[][] scoreMatrix) {
        this.scoreMatrix = scoreMatrix;
    }

    public int[][] getTrackMatrix() {
        return trackMatrix;
    }

    public void setTrackMatrix(int[][] trackMatrix) {
        this.trackMatrix = trackMatrix;
    }
}
