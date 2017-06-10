package wafflecore;

import static wafflecore.constants.Constants.*;

// need to consider the config structure later...
public class Config {
    // public void readConfigFile() {

    // }

    public static String getValue(String name) {
        String val = "";

        if (name.equals("LOG_FILE_PATH")) {
            val = ROOT_DIR + "/error.log";
        } else if (name.equals("LISTEN_PORT")) {
            val = "9001";
        }

        return val;
    }
}
