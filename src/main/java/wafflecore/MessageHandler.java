package wafflecore;

import static wafflecore.constants.Constants.*;
import static wafflecore.message.type.MessageType.*;
import static wafflecore.message.type.InventoryMessageType.*;
import wafflecore.message.type.MessageType;
import wafflecore.message.type.InventoryMessageType;
import wafflecore.message.*;
import wafflecore.model.*;
import wafflecore.util.MessageUtil;
import wafflecore.util.BlockUtil;
import wafflecore.util.TransactionUtil;
import wafflecore.util.Hasher;
import wafflecore.util.ByteArrayWrapper;
import wafflecore.tool.Logger;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.HashMap;

public class MessageHandler {
    private Logger logger = Logger.getInstance();
    private Inventory inventory;
    private ConnectionManager connectionManager;
    private BlockChainExecutor blockChainExecutor;

    public void handleMessage(Envelope env, String peerAddr) {
        switch (env.getMessageType()) {
            case HELLO:
                handleHello(
                    // MessageUtil.deserializeHello(env.getPayload()),
                    (Hello) env.getPayload(),
                    peerAddr);
                break;
            case INVENTORY:
                handleInventoryMessage(
                    // MessagetUtil.deserializeInvMsg(env.getPayload()),
                    (InventoryMessage) env.getPayload(),
                    peerAddr);
                break;
            default:
                break;
        }
    }

    public void handleHello(Hello hello, String peerAddr) {
        ExecutorService executor = WaffleCore.getExecutor();

        executor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                // WIP genesis

                ArrayList<ByteArrayWrapper> unknownBlockIds = new ArrayList<ByteArrayWrapper>();

                logger.log("Hello Received:" + peerAddr);

                ArrayList<ByteArrayWrapper> blockIds = hello.getKnownBlocks();
                for (ByteArrayWrapper id : blockIds) {
                    if (!blockChainExecutor.blocks.containsKey(id)) {
                        unknownBlockIds.add(id);
                    }
                }

                InventoryMessage invMsg = new InventoryMessage();
                invMsg.setInventoryMessageType(REQUEST);
                invMsg.setIsBlock(true);
                for (ByteArrayWrapper id : unknownBlockIds) {
                    invMsg.setObjectId(id);

                    Envelope env = invMsg.packToEnvelope();
                    connectionManager.asyncSend(MessageUtil.serialize(env), peerAddr);
                }

                return null;
            }
        });
    }

    public void handleInventoryMessage(InventoryMessage msg, String peerAddr) {
        switch (msg.getInventoryMessageType()) {
            case ADVERTISE:
                handleAdvertise(msg, peerAddr);
                break;
            case REQUEST:
                handleRequest(msg, peerAddr);
                break;
            case CONTENT:
                handleContent(msg, peerAddr);
                break;
            default:
                break;
        }
    }

    public void handleAdvertise(InventoryMessage msg, String peerAddr) {
        ExecutorService executor = WaffleCore.getExecutor();

        executor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                if (msg.getData() != null) {
                    throw new IllegalArgumentException();
                }

                ByteArrayWrapper id = msg.getObjectId();
                if (id == null) {
                    return null;
                }

                logger.log("Advertise Received:" + msg.getObjectId().toString());

                boolean haveObject = msg.getIsBlock() ?
                    inventory.blocks.containsKey(id) : inventory.memoryPool.containsKey(id);
                if (haveObject) return null;

                msg.setInventoryMessageType(REQUEST);

                Envelope env = msg.packToEnvelope();
                connectionManager.asyncSend(MessageUtil.serialize(env), peerAddr);

                return null;
            }
        });
    }

    public void handleRequest(InventoryMessage msg, String peerAddr) {
        ExecutorService executor = WaffleCore.getExecutor();

        executor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                if (msg.getData() != null) {
                    throw new IllegalArgumentException();
                }

                logger.log("Request Received:" + msg.getObjectId().toString());

                byte[] data;
                if (msg.getIsBlock()) {
                    data = inventory.blocks.get(msg.getObjectId());
                } else {
                    Transaction tx;
                    tx = inventory.memoryPool.get(msg.getObjectId());
                    data = tx.getOriginal();
                }

                msg.setInventoryMessageType(CONTENT);
                msg.setData(data);

                Envelope env = msg.packToEnvelope();

                connectionManager.asyncSend(MessageUtil.serialize(env), peerAddr);

                return null;
            }
        });
    }

    public void handleContent(InventoryMessage msg, String peerAddr) {
        ExecutorService executor = WaffleCore.getExecutor();

        executor.submit(new Callable<Void>() {
            @Override
            public Void call() {
                byte[] data = msg.getData();
                if (data.length > MAX_BLOCK_SIZE) throw new IllegalArgumentException();

                if (msg.getIsBlock()) {
                    ByteArrayWrapper id = BlockUtil.computeBlockId(data);
                    if (!id.equals(msg.getObjectId())) return null;

                    synchronized (inventory.blocks) {
                        if (inventory.blocks.containsKey(id)) return null;
                        inventory.blocks.put(id, data);
                    }

                    Block block = BlockUtil.deserialize(data);

                    logger.log("Block Received:" + msg.getObjectId().toString());

                    ByteArrayWrapper prevId = block.getPreviousHash();
                    if (!inventory.blocks.containsKey(prevId)) {
                        InventoryMessage newMsg = new InventoryMessage();
                        newMsg.setInventoryMessageType(REQUEST);
                        newMsg.setIsBlock(true);
                        newMsg.setObjectId(block.getPreviousHash());

                        Envelope env = newMsg.packToEnvelope();
                        connectionManager.asyncSend(MessageUtil.serialize(env), peerAddr);
                    }

                    blockChainExecutor.processBlock(data, prevId);
                } else {
                    ByteArrayWrapper id = ByteArrayWrapper.copyOf(Hasher.doubleSha256(data));
                    if (!id.equals(msg.getObjectId())) return null;
                    if (inventory.memoryPool.containsKey(id)) return null;

                    Transaction tx = TransactionUtil.deserialize(data);

                    if (tx.getInEntries().size() == 0) return null;

                    logger.log("Tx Received:" + msg.getObjectId().toString());

                    synchronized (inventory.memoryPool) {
                        inventory.memoryPool.put(id, tx);
                    }
                }

                msg.setInventoryMessageType(ADVERTISE);
                msg.setData(null);

                Envelope env = msg.packToEnvelope();

                connectionManager.asyncBroadcast(MessageUtil.serialize(env));

                return null;
            }
        });
    }

    // setter
    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
    public void setConnectionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    public void setBlockChainExecutor(BlockChainExecutor blockChainExecutor) {
        this.blockChainExecutor = blockChainExecutor;
    }
}
