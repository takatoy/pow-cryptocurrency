package waffle;

import wafflecontrol.WaffleGraphic;
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
        Scanner scan = new Scanner(System.in);

        if (args.length == 1 && "cli".equals(args[0])) {
            core.run();
            System.out.println("Started running.");
        } else {

        }

        System.exit(0);
    }
}
