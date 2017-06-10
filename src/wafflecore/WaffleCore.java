package wafflecore;

import static wafflecore.constants.Constants.*;

import java.net.*;

class WaffleCore {
    public static void main(String[] args) {
        Listener listener = new Listener("127.0.0.1", 9001);
        listener.start();

        System.out.println("Program is still going");
    }
}
