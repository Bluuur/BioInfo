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
        trackBack();
        System.out.println();
    }

    /**
     * local alignment
     */
    public void localAlignment() {
        System.out.println("---------- Local Alignment -----------");
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
        trackBack0();
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

    public void trackBack() {
        StringBuilder seqOut1 = new StringBuilder();
        StringBuilder seqOut2 = new StringBuilder();
        boolean flag = false; // 标志位，判断是否有多种情况
        int i, j;

        while (true) {
            seqOut1.append(' ');
            seqOut2.append(' ');
            flag = false;
            i = sequence2.length; // 行
            j = sequence2.length; //列
            while (trackMatrix[i][j] != 0) {
                switch (trackMatrix[i][j]) {
                    case 1: // 只来自左上角
                        seqOut1.append(sequence1[j - 1]);
                        seqOut2.append(sequence2[i - 1]);
                        i--;
                        j--;
                        break;
                    case 2: // 只来自左空格
                        seqOut1.append(sequence1[j - 1]);
                        seqOut2.append('_');
                        j--;
                        break;
                    case 3: // 来自 1 + 2
                        // 若kind已经为true，说明已经走了一个分支，此时不需要-2
                        if (!flag) {
                            trackMatrix[i][j] -= 2;
                            flag = true;
                        }
                        seqOut1.append(sequence1[j - 1]);
                        seqOut2.append('_');
                        j--;
                        break;
                    case 4: // 只来自上空格
                        seqOut1.append('_');
                        seqOut2.append(sequence2[i - 1]);
                        i--;
                        break;
                    case 5: // 来自 1 + 4
                    case 6: // 来自 2 + 4
                    case 7: // 来自 1 + 2 + 4
                        // 若kind已经为true，说明已经走了一个分支，此时不需要-4
                        if (!flag) {
                            trackMatrix[i][j] -= 4;
                            flag = true;
                        }
                        seqOut1.append('_');
                        seqOut2.append(sequence2[i - 1]);
                        i--;
                        break;
                }
            }
            if (!flag) {
                break;
            }
        }
        System.out.println("s1:" + seqOut1.reverse());
        System.out.println("s2:" + seqOut2.reverse());
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
