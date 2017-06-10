package wafflecore;

import wafflecore.WaffleConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WaffleLogger {
    private static WaffleLogger waffleLogger = new WaffleLogger();
    private static File file;
    private static FileWriter writer;
    public static String logFilePath;

    private WaffleLogger() {
        logFilePath = WaffleConfig.getValue("LOG_FILE_PATH");

        try {
            file = new File(logFilePath);

            if (WaffleSystem.isFileWritable(file)) {
                writer = new FileWriter(file, true);
            } else {
                throw new IOException(logFilePath + " cannot be written.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WaffleLogger getInstance() {
        return waffleLogger;
    }

    public static void log(String msg) {
        String logFilePath = WaffleConfig.getValue("LOG_FILE_PATH");
        String date = WaffleSystem.getCurrentLocalDateTimeStr();
        try {
            writer.write("[" + date + "] " + msg + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
