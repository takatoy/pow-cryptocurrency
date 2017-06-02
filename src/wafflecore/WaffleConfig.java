package wafflecore;

public class WaffleConfig {
    public void readConfigFile() {

    }

    public static String getValue(String name) {
        String val = "";

        if (name.equals("LOG_FILE_PATH")) {
            val = System.getProperty("user.home") + "/error.log";
        }

        return val;
    }
}
