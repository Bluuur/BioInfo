import java.util.Date;
import java.util.Timer;

/**
 * @author Zidong Zh
 * @date 2022/11/14
 */
public class Main {
    public static void main(String[] args) {

//        ScoreRule scoreRule = new ScoreRule();
        ScoreRule rule1 = new ScoreRule(9, -6, -2);
        ScoreRule rule2 = new ScoreRule(9, -3, -2);

//        AlignmentConstructor alignmentConstructor = new AlignmentConstructor(scoreRule);
        char[] seq1 = "AACGTACTCAAGTCT".toCharArray();
        char[] seq2 = "TCGTACTCTAACGAT".toCharArray();
        AlignmentConstructor alignmentConstructor = new AlignmentConstructor(seq1, seq2, rule1);

        Date date = new Date();
        System.out.println("System time: " + date);
        alignmentConstructor.globalAlignment();
        System.out.println("System time: " + date);
        alignmentConstructor.localAlignment();
    }
}
