import moe.luoluo.util.Format;

import static java.lang.String.format;

public class test {
    public static void main(String[] args) {
        System.out.println("human.version");
        String test = "hello";
        test = "world";
        System.out.println(test);
        String num = "0%";
        num = format("%.2f", 2.53 / 5) + "%";
        System.out.println(num);
        String havent = null;
        System.out.println(havent);
    }
}
