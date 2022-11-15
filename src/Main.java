import java.util.Date;
import java.util.Timer;

/**
 * @author Zidong Zh
 * @date 2022/11/14
 */
public class Main {
    public static void main(String[] args) {

        // receive from keyboard
//        ScoreRule scoreRule = new ScoreRule();
//        AlignmentConstructor alignmentConstructor = new AlignmentConstructor(scoreRule);

        // build in data
        ScoreRule rule1 = new ScoreRule(9, -6, -2);
        ScoreRule rule2 = new ScoreRule(9, -3, -2);
        char[] seq1 = "AACGTACTCAAGTCT".toCharArray();
        char[] seq2 = "TCGTACTCTAACGAT".toCharArray();
//        AlignmentConstructor alignmentConstructor = new AlignmentConstructor(seq1, seq2, rule1);
        AlignmentConstructor alignmentConstructor = new AlignmentConstructor(seq1, seq2, rule2);

        // print system time
        Date date = new Date();
        System.out.println("System time: " + date);
        alignmentConstructor.globalAlignment();

        System.out.println("System time: " + date);
        alignmentConstructor.localAlignment();
    }
}
