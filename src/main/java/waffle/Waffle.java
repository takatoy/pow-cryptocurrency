package waffle;

import waffle.wafflecontrol.WaffleGraphic;
import waffle.wafflecore.WaffleCore;

import waffle.wafflecore.message.*;
import waffle.wafflecore.message.type.*;
import waffle.wafflecore.*;
import waffle.wafflecore.model.*;
import waffle.wafflecore.util.*;

import java.util.Scanner;

class Waffle {
    public static void main(String[] args) {
        WaffleCore core = new WaffleCore();

        if (args.length == 1 && "cli".equals(args[0])) {
            Scanner scan = new Scanner(System.in);

            System.out.print("Listen Port Number: ");
            int listenPort = scan.nextInt();
            Config.setListenPort(listenPort);

            System.out.print("Peer Host Name (null -> -1): ");
            String peerHostName = scan.next();
            Config.setPeerHostName(peerHostName);

            System.out.print("Peer Port Number (null -> -1): ");
            int peerPort = scan.nextInt();
            Config.setPeerPort(peerPort);

            System.out.print("Mine? (true or false): ");
            boolean mine = scan.nextBoolean();
            Config.setIsMining(mine);

            System.out.println("Started running.");
            core.run();
        } else {
            WaffleGraphic gui = new WaffleGraphic();
        }
    }
}
