import java.util.Scanner;

/**
 * @author Zidong Zh
 * @date 2022/11/10
 */
class ScoreRule {
    private int match;
    private int mismatch;
    private int gap;

    public ScoreRule(int match, int mismatch, int gap) {
        this.match = match;
        this.mismatch = mismatch;
        this.gap = gap;
    }

    public ScoreRule() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter score rule");
        System.out.print("match:");
        match = scanner.nextInt();
        System.out.print("mismatch:");
        mismatch = scanner.nextInt();
        System.out.print("gap:");
        gap = scanner.nextInt();
        System.out.println();
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }

    public int getMismatch() {
        return mismatch;
    }

    public void setMismatch(int mismatch) {
        this.mismatch = mismatch;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }
}