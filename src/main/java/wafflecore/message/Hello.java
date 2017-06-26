package wafflecore.message;

import static wafflecore.constants.Constants.*;
import wafflecore.util.ByteArrayWrapper;

import java.util.ArrayList;

public class Hello extends Message {
    private ArrayList<String> myPeers;
    private ByteArrayWrapper genesisId;
    private ArrayList<ByteArrayWrapper> knownBlocks;

    @Override
    public static int getMessageType() {
        return MSG_TYPE_HELLO;
    }

    @Override
    public static int getPayload() {
        MessageSerializer.serialize(this);
    }
}
