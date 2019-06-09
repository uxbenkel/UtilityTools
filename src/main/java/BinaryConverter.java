import java.util.Scanner;

/**
 * 一个进制转换器,输入一个任意进制(M)的数字(X)和想要转换的进制(N),输出目标进制的结果(Y).
 * 总的进制范围限定二~十六进制,十一进制到十六进制中,10~15分别使用A~F表示;
 * 1、将10进制转换为任意进制(M=10);
 * 2、从任意进制转换为10进制(N=10);
 * 3、将任意进制转换为任意进制(此时十进值的中间变量设为z);
 */
public class BinaryConverter {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入转换之前数据是多少进制(2-16):");
        // 为方便判断输入的值是否合法,以String录入并用正则表达式判断是否合法,后续输入同此
        String M = binCheck(sc);
        // 将录入的String类型的M转换成int类型的m
        int m = Integer.parseInt(M);

        System.out.println("请输入要转换的" + numToZh(m) + "进制数据:");
        String X = sc.nextLine();
        // 判断原数据是否为十进值,若不是则需要先转换为十进值,设此中间变量为z,最终的值为Y
        int z = 0;

        // 录入要转让的数字X并校验
        X = numCheck(sc, m, X);
        if (m == 10) {
            z = Integer.parseInt(X);
        } else {
            // 将输入的字符串转换成字符数组
            char[] arr = X.toCharArray();
            // 遍历数组进行转换,得到的为十进值的中间变量z
            for (int i = 0; i < arr.length; i++) {
                int num = strToNum(arr[i] + "");
                z += num * Math.pow(m, arr.length - 1 - i);
            }
        }

        System.out.println("请输入要转换成多少进制(2-16):");
        String N = binCheck(sc);
        int n = Integer.parseInt(N);

        String Y;
        // 判断目标数据是否为十进值,若是则不需要进行下面的转换
        if (n == 10) {
            Y = z + "";
        } else {
            // 定义一个用于拼接的StringBuilder字符串
            StringBuilder sb = new StringBuilder();
            // 将输入的字符串x转换为int类型的数据
            if (z < n) {
                sb.append(numToStr(z));
            } else {
                // 循环取余拼接
                while (z / n > 0) {
                    sb.append(numToStr(z % n));
                    z /= n;
                }
                // 拼接最后的商
                sb.append(numToStr(z % n));
            }
            // 反转既得n进制数
            Y = sb.reverse().toString();
        }

        System.out.println("转换后的" + numToZh(n) + "进制数据结果为:" + Y);
        sc.close();
    }

    /**
     * 输入进制校验
     * @param sc 控制台输入对象
     * @return 校验通过返回的输入结果
     */
    private static String binCheck(Scanner sc) {
        String M = sc.nextLine();
        while (!M.matches("[2-9]|(1[0-6])")) {
            System.err.println("不能转换这样的进制,请重新输入:");
            M = sc.nextLine();
        }
        return M;
    }

    /**
     * 输入数据校验
     * @param sc 控制台输入对象
     * @param m  待校验的进制
     * @param x  待校验的结果
     * @return 校验通过返回的输入结果
     */
    private static String numCheck(Scanner sc, int m, String x) {
        switch (m) {
            case 16:
                while (!x.matches("([1-9]|[A-F])+(\\d|[A-F])*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 15:
                while (!x.matches("([1-9]|[A-E])+(\\d|[A-E])*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 14:
                while (!x.matches("([1-9]|[A-D])+(\\d|[A-D])*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 13:
                while (!x.matches("([1-9]|[A-C])+(\\d|[A-C])*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 12:
                while (!x.matches("([1-9]|[A-B])+(\\d|[A-B])*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 11:
                while (!x.matches("([1-9]|A)+(\\d|A)*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 10:
                while (!x.matches("[1-9]+[0-9]*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 9:
                while (!x.matches("[1-8]+[0-8]*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 8:
                while (!x.matches("[1-7]+[0-7]*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 7:
                while (!x.matches("[1-6]+[0-6]*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 6:
                while (!x.matches("[1-5]+[0-5]*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 5:
                while (!x.matches("[1-4]+[0-4]*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 4:
                while (!x.matches("[1-3]+[0-3]*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 3:
                while (!x.matches("[1-2]+[0-2]*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
            case 2:
                while (!x.matches("1+[0-1]*")) {
                    System.err.println("输入的数据不合法,请重新输入:");
                    x = sc.nextLine();
                }
                break;
        }
        return x;
    }

    /**
     * 进制数字与中文转换
     * @param num 待转换的数字
     * @return 转换结果
     */
    public static String numToZh(int num) {
        String zh;
        // 如果在合理范围内,转换为对应的中文
        switch (num) {
            case 2:
                zh = "二";
                break;
            case 3:
                zh = "三";
                break;
            case 4:
                zh = "四";
                break;
            case 5:
                zh = "五";
                break;
            case 6:
                zh = "六";
                break;
            case 7:
                zh = "七";
                break;
            case 8:
                zh = "八";
                break;
            case 9:
                zh = "九";
                break;
            case 10:
                zh = "十";
                break;
            case 11:
                zh = "十一";
                break;
            case 12:
                zh = "十二";
                break;
            case 13:
                zh = "十三";
                break;
            case 14:
                zh = "十四";
                break;
            case 15:
                zh = "十五";
                break;
            default:
                zh = "十六";
                break;
        }
        return zh;
    }

    /**
     * 数字转换 10~15 to A~F
     * @param num 待转换的数字
     * @return 转换结果
     */
    public static String numToStr(int num) {
        String str;
        switch (num) {
            case 10:
                str = "A";
                break;
            case 11:
                str = "B";
                break;
            case 12:
                str = "C";
                break;
            case 13:
                str = "D";
                break;
            case 14:
                str = "E";
                break;
            case 15:
                str = "F";
                break;
            default:
                str = num + "";
        }
        return str;
    }

    /**
     * 字符串解析 A~F to 10~15
     * @param str 待解析的字符串
     * @return 解析结果
     */
    public static int strToNum(String str) {
        int num;
        switch (str) {
            case "A":
                num = 10;
                break;
            case "B":
                num = 11;
                break;
            case "C":
                num = 12;
                break;
            case "D":
                num = 13;
                break;
            case "E":
                num = 14;
                break;
            case "F":
                num = 15;
                break;
            default:
                num = Integer.parseInt(str);
        }
        return num;
    }
}
