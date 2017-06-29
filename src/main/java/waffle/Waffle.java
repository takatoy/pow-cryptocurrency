package waffle;

// import wafflecontrol.WaffleControlPanel;
import waffle.wafflecore.WaffleCore;

import waffle.wafflecore.message.*;
import waffle.wafflecore.message.type.*;
import waffle.wafflecore.*;
import waffle.wafflecore.model.*;
import waffle.wafflecore.util.*;

import java.util.Scanner;

class Waffle {
    public static void main(String[] args) {
        // WaffleControlPanel ctrl = new WaffleControlPanel();
        WaffleCore core = new WaffleCore();
        boolean isCoreRunning = false;
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("Enter command (run, keygen, exit): ");
            String cmd = scan.next();

            if (cmd.equals("run")) {
                if (isCoreRunning) {
                    System.out.println("Error: Core is already running.");
                } else {
                    isCoreRunning = true;
                    core.run();
                    System.out.println("Started running.");
                }
            } else if (cmd.equals("keygen")) {
                System.out.println("Error: Keygen is not supported yet.");
            } else if (cmd.equals("exit")) {
                // FORCE
                break;
            }
        }

        System.exit(0);
    }
}
