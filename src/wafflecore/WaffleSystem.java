package wafflecore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WaffleSystem {
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

                String date = getCurrentLocalDateTimeStr();
                writer.write("[" + date + "] " + msg + "\n");

                writer.close();
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeLog(String msg) {
        String logFilePath = WaffleConfig.getValue("LOG_FILE_PATH");
        writeFile(logFilePath, msg);
    }

    private static boolean isFileWritable(File file){
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
}
