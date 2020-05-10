package me.P3ntest.permissions.time;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class TimeFormatter {

    public static String formatMilliseconds(long millis) {
        if (millis == 0) {
            return "";
        } else if (millis < 1000) {
            millis = 1000;
        }

        long years = (long) Math.floor(TimeUnit.MILLISECONDS.toDays(millis) / 365);
        millis -= TimeUnit.DAYS.toMillis(years * 365);
        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        String string = "";

        if (years > 0)
            string += " " + years + "y";
        if (days > 0)
            string += " " + days + "d";
        if (hours > 0)
            string += " " + hours + "h";
        if (minutes > 0)
            string += " " + minutes + "m";
        if (seconds > 0)
            string += " " + seconds + "s";

        return(string.substring(1));
    }

    public static long formattedToMilliseconds(String[] formatted) throws InvalidDateException {
        long millis = 0;

        for (String s : formatted) {
            s = s.trim();
            long mod = 0;

            switch (s.substring(s.length() - 1)) {
                case "y":
                case "j":
                    mod = TimeUnit.DAYS.toMillis(365);
                    break;
                case "d":
                    mod = TimeUnit.DAYS.toMillis(1);
                    break;
                case "h":
                    mod = TimeUnit.HOURS.toMillis(1);
                    break;
                case "m":
                    mod = TimeUnit.MINUTES.toMillis(1);
                    break;
                case "s":
                    mod = 1000;
            }

            long value = 0;

            try {
                value = Integer.parseInt(s.substring(0, s.length() - 1));
            } catch (NumberFormatException e) {
                throw new InvalidDateException(s + " is not a valid time.");
            }

            millis = value * mod;
        }

        return millis;
    }

}
