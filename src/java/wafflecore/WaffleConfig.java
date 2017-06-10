package wafflecore;

import static wafflecore.constants.WaffleConstants.*;

// need to consider the config structure later...
public class WaffleConfig {
    // public void readConfigFile() {

    // }

    public static String getValue(String name) {
        String val = "";

        if (name.equals("LOG_FILE_PATH")) {
            val = ROOT_DIR + "error.log";
            System.out.println(val);
        } else if (name.equals("LISTEN_PORT")) {
            val = "9001";
        }

        return val;
    }
}
