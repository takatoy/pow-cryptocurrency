package wafflecore.message;

import static wafflecore.constants.Constants.*;
import wafflecore.util.ByteArrayWrapper;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Hello extends Message {
    @JsonProperty("peers")
    private ArrayList<String> myPeers;
    @JsonProperty("genesis")
    private ByteArrayWrapper genesisId;
    @JsonProperty("blocks")
    private ArrayList<ByteArrayWrapper> knownBlocks;

    @Override @JsonIgnore
    public static int getMessageType() {
        return MSG_TYPE_HELLO;
    }

    @Override @JsonIgnore
    public static int getPayload() {
        MessageSerializer.serialize(this);
    }

    // getter
    public ArrayList<String> getMyPeers() {
        return myPeers;
    }
    public ByteArrayWrapper getGenesisId() {
        return genesisId;
    }
    public ArrayList<ByteArrayWrapper> getKnownBlocks() {
        return knownBlocks;
    }

    // setter
    public void setMyPeers(ArrayList<String> myPeers) {
        this.myPeers = myPeers;
    }
    public void setGenesisId(ByteArrayWrapper genesisId) {
        this.genesisId = genesisId;
    }
    public void setKnownBlocks(ArrayList<ByteArrayWrapper> knownBlocks) {
        this.knownBlocks = knownBlocks;
    }
}
