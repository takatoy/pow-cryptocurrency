package waffle.wafflecore.tool;

import static waffle.wafflecore.constants.Constants.*;
import waffle.Config;
import waffle.wafflecontrol.WaffleGraphic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static Logger logger = new Logger();
    private static WaffleGraphic wg;

    private Logger() {
    }

    public static void setGui(WaffleGraphic g) {
        wg = g;
    }

    public static Logger getInstance() {
        return logger;
    }

    public static void log(String msg) {
        String date = SystemUtil.getCurrentLocalDateTimeStr();

        if (Config.isGui()) {
            wg.addLog("[" + date + "] " + msg + "\n");
        } else {
            System.out.println("[" + date + "] " + msg);
        }
    }
}
