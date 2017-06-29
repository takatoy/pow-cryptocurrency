package waffle.wafflecore.tool;

import static waffle.wafflecore.constants.Constants.*;

// need to consider the config structure later...
public class Configurator {
    public void getConfig() {

    }

    public static String getValue(String name) {
        String val = "";

        if (name.equals("LOG_FILE_PATH")) {
            val = DATA_DIR + "/error.log";
        } else if (name.equals("LISTEN_PORT")) {
            val = "9001";
        }

        return val;
    }
}
