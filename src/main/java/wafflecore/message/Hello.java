package wafflecore.message;

import static wafflecore.constants.Constants.*;
import static wafflecore.message.type.MessageType.*;
import wafflecore.message.type.MessageType;
import wafflecore.util.ByteArrayWrapper;
import wafflecore.util.MessageUtil;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Hello extends Message {
    @JsonIgnore
    public final MessageType messageType = HELLO;
    @JsonProperty("peers")
    private ArrayList<String> myPeers;
    @JsonProperty("genesis")
    private ByteArrayWrapper genesisId;
    @JsonProperty("blocks")
    private ArrayList<ByteArrayWrapper> knownBlocks;

    public Hello(
        @JsonProperty("peers") ArrayList<String> myPeers,
        @JsonProperty("genesis") ByteArrayWrapper genesisId,
        @JsonProperty("blocks") ArrayList<ByteArrayWrapper> knownBlocks)
    {
        this.myPeers = myPeers;
        this.genesisId = genesisId;
        this.knownBlocks = knownBlocks;
    }

    public Hello() {}

    public Envelope packToEnvelope() {
        return new Envelope(messageType, this);
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
