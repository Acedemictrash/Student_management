package com.example.student_ssm.Utils;

public class DateUtil {

    public static int getWeek(double y, double m, double d) {
        int c = (int) y / 100;
        y %= 100;
        if (m < 3) {
            m += 12;
            y -= 1;
        }
        int w = (int)(y + (int)(y / 4) + (int)(c / 4) - 2 * c + (int)(26 * (m + 1) / 10) + d - 1) % 7;

        return w;
    }

}
