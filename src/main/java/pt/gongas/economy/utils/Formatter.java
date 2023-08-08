package pt.gongas.economy.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class Formatter {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    private final List<String> suffixes;

    public Formatter() {
        suffixes = Arrays.asList("", "K", "M", "B", "T", "Q", "QQ");
    }

    public String formatNumber(double value) {
        boolean negative = value < 0;
        int index = 0;
        value = Math.abs(value);

        double tmp;
        while ((tmp = value / 1000) >= 1) {
            if (index + 1 == suffixes.size())
                break; //acabo. caso seus sufixos sejam "K, M, B" e vc der um numero tipo 1000000000000 (1T) ele vai virar 1000B
            value = tmp;
            ++index;
        }

        return (negative ? "-" : "") +  DECIMAL_FORMAT.format(value) + suffixes.get(index);
    }

}
