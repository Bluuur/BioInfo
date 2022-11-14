import java.util.Scanner;

/**
 * @author fanb
 * @email fanb@nwafu.edu.cn
 * 问题描述
 * 尝试找到两个完整的序列 S1 和 S2 之间的最佳比对。
 * 如果设定每个匹配字符为1分，每个空格为-2分，每个不匹配为-1分，
 * <p>
 * 求解策略
 * Needleman-Wunsch(尼德曼-翁施)算法：
 * 它的思路与 LCS 算法相似。这个算法也使用二维表格，一个序列沿顶部展开，一个序列沿左侧展开
 * s1位于dp数组的上方，s2位于dp数组的左方
 * dp[i][j]得分矩阵 表示s1[0:j] 与 s2[0:i]的最高得分
 * 状态转移方程(得分体系) dp[i][j] = max{
 * 1. dp[i-1][j-1] +/- 1 左上角
 * 2. dp[i][j-1] - 2     左边s2为空
 * 4. dp[i-1][j] - 2     上边s1为空
 * }
 * 回溯找出最优解 status[i][j] 表示dp[i][j] 的父节点 1.左上角 2.左边 4.上边
 * 若有多解也可表示：3=1+2，5=1+4，6=2+4，7=1+2+4
 * <p>
 * 时间复杂度 O(m*n)
 * 空间复杂度 O(m*n)
 */
public class Ref {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("输入s1:");
        String s1 = scanner.next();
        System.out.print("输入s2:");
        String s2 = scanner.next();
        scanner.close();

        Solution solution = new Solution(s1, s2);
        int ans = solution.needlemanWunsch(); // 求解最优值
        System.out.println("最优值：" + ans);
        solution.print(); // 打印最优解
    }
}

class Solution {
    String s1, s2; // s1上侧 s2左侧
    int m, n; // m列 n行
    int[][] dp; // 得分矩阵
    int[][] status; // 记录父节点，1(2^0)代表左上，2(2^1)代表来自左边（左空格），4(2^2)代表来自上边（上空格)

    // 构造器
    public Solution(String s1, String s2) {
        this.s1 = s1;
        this.s2 = s2;
        this.m = s1.length();
        this.n = s2.length();
        this.dp = new int[n + 1][m + 1];
        this.status = new int[n + 1][m + 1];
        status[0][0] = 0;
        dp[0][0] = 0;
    }

    // Needleman-Wunsch 全局序列比对算法
    public int needlemanWunsch() {
        // 初始化第0列
        for (int i = 1; i <= n; i++) {
            dp[i][0] = dp[i - 1][0] - 2;
            status[i][0] = 4;
        }
        // 初始化第0行
        for (int i = 1; i <= m; i++) {
            dp[0][i] = dp[0][i - 1] - 2;
            status[0][i] = 2;
        }
        // 动态规划，填满dp数组
        for (int i = 1; i <= n; i++) { // 行
            for (int j = 1; j <= m; j++) { // 列
                int leftTop = dp[i - 1][j - 1] + (s1.charAt(j - 1) == s2.charAt(i - 1) ? 9 : -6); // 左上角
                int left = dp[i][j - 1] - 2; // 来自左边 左空格
                int top = dp[i - 1][j] - 2; // 来自上边 上空格
                int state = 0;
                int max = Math.max(leftTop, left); // 当前位置最高得分
                max = Math.max(top, max);

                // 左上角
                if (leftTop == max) {
                    state += 1;
                }
                // 来自左边 左空格
                if (left == max) {
                    state += 2;
                }
                // 来自上边 上空格
                if (top == max) {
                    state += 4;
                }

                status[i][j] = state;
                dp[i][j] = max;
                // System.out.print(max + " ");
            }
            // System.out.println();
        }
        return dp[n][m];
    }

    // print 回溯dp二维数组输出最优解
    public void print() {
        StringBuilder s11 = new StringBuilder();
        StringBuilder s22 = new StringBuilder();
        boolean kind = false; // 标志位，判断是否有多种情况
        int i, j;

        while (true) {
            s11.append(' ');
            s22.append(' ');
            kind = false;
            i = n; // 行
            j = m; //列
            while (status[i][j] != 0) {
                switch (status[i][j]) {
                    case 1: // 只来自左上角
                        s11.append(s1.charAt(j - 1));
                        s22.append(s2.charAt(i - 1));
                        i--;
                        j--;
                        break;
                    case 2: // 只来自左空格
                        s11.append(s1.charAt(j - 1));
                        s22.append('_');
                        j--;
                        break;
                    case 3: // 来自 1 + 2
                        // 若kind已经为true，说明已经走了一个分支，此时不需要-2
                        if (!kind) {
                            status[i][j] -= 2;
                            kind = true;
                        }
                        s11.append(s1.charAt(j - 1));
                        s22.append('_');
                        j--;
                        break;
                    case 4: // 只来自上空格
                        s11.append('_');
                        s22.append(s2.charAt(i - 1));
                        i--;
                        break;
                    case 5: // 来自 1 + 4
                    case 6: // 来自 2 + 4
                    case 7: // 来自 1 + 2 + 4
                        // 若kind已经为true，说明已经走了一个分支，此时不需要-4
                        if (!kind) {
                            status[i][j] -= 4;
                            kind = true;
                        }
                        s11.append('_');
                        s22.append(s2.charAt(i - 1));
                        i--;
                        break;
                }
            }
            if (!kind) {
                break;
            }
        }
        System.out.println("s1:" + s11.reverse());
        System.out.println("s2:" + s22.reverse());
    }
}
