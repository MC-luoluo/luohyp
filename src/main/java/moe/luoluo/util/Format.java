package moe.luoluo.util;

import java.text.DecimalFormat;

public class Format {
    public static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public static String largeNumFormat(double num) {
        if (num < 100000) {
            return String.valueOf((int) num);
        }else if (num < 1.0E6) {
            return decimalFormat.format(num / 1000) + "K";
        } else if (num < 1.0E9) {
            return decimalFormat.format(num / 1.0E6) + "M";
        } else if (num < 1.0E12) {
            return decimalFormat.format(num / 1.0E9) + "B";
        } else {
            return decimalFormat.format(num / 1.0E12) + "T";
        }
    }
}
