package wafflecore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SystemUtil {
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

    public static void writeFile(String filePath, String msg) {
        try {
            File file = new File(filePath);

            if (isFileWritable(file)) {
                FileWriter writer = new FileWriter(file, true);
                writer.write(msg + "\n");
                writer.close();
            } else {
                throw new IOException(filePath + " cannot be written.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFileWritable(File file){
        if (file.exists()) {
            if (file.isFile() && file.canWrite()) {
                return true;
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public static String bytesToStr(byte[] bytes) {
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (byte b: bytes) {
            sb.append(String.format("%02x", b&0xff));
        }
        return new String(sb);
    }
}
