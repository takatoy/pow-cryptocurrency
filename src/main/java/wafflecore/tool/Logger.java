package wafflecore.tool;

import static wafflecore.constants.Constants.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static Logger logger = new Logger();
    private static File file;
    private static FileWriter writer;
    public static String logFilePath;

    private Logger() {
        logFilePath = LOG_FILE_PATH;

        try {
            file = new File(logFilePath);

            if (SystemUtil.isFileWritable(file)) {
                writer = new FileWriter(file, true);
            } else {
                throw new IOException(logFilePath + " cannot be written.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger getInstance() {
        return logger;
    }

    public static void log(String msg) {
        // String logFilePath = Config.getValue("LOG_FILE_PATH");
        String date = SystemUtil.getCurrentLocalDateTimeStr();
        // try {
        //     writer.write("[" + date + "] " + msg + "\n");
        //     writer.flush();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        System.out.println("[" + date + "] " + msg);
    }
}
