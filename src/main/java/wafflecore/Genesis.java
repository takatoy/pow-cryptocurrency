package wafflecore;

import wafflecore.model.Block;

class Genesis {
    public static Block genesisBlock;
    static {
        genesisBlock = new Block();
        genesisBlock.setPreviousHash(EMPTY_BYTES);
        genesisBlock.setId(EMPTY_BYTES);
    }

    public static Block getGenesisBlock() {
        return genesisBlock;
    }
}
