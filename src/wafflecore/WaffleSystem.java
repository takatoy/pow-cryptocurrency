package wafflecore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class WaffleSystem {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWin() {
        return (OS.indexOf("win") >= 0);
    }

    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") >= 0 );
    }

    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    public static String getOS() {
        if (isWin()) {
            return "win";
        } else if (isUnix()) {
            return "uni";
        } else if (isMac()) {
            return "mac";
        } else {
            return "err";
        }
    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now();
    }

    public static String getCurrentLocalDateTimeStr() {
        LocalDateTime ldt = getCurrentLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return ldt.format(formatter);
    }
}
