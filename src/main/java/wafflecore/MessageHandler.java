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

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.HashMap;

public class MessageHandler {
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
                ArrayList<ByteArrayWrapper> unknownBlockIds = new ArrayList<ByteArrayWrapper>();

                ArrayList<ByteArrayWrapper> blockIds = hello.getKnownBlocks();
                for (ByteArrayWrapper id : blockIds) {
                    if (!blockChainExecutor.blocks.containsKey(id)) {
                        unknownBlockIds.add(id);
                    }
                }

                InventoryMessage invMsg = new InventoryMessage();
                invMsg.setInventoryMessageType(InventoryMessageType.REQUEST);
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

                boolean haveObject = msg.getIsBlock() ?
                    inventory.blocks.containsKey(id) : inventory.memoryPool.containsKey(id);
                if (haveObject) return null;

                msg.setInventoryMessageType(InventoryMessageType.REQUEST);

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

                byte[] data;
                if (msg.getIsBlock()) {
                    data = inventory.blocks.get(msg.getObjectId());
                } else {
                    Transaction tx;
                    tx = inventory.memoryPool.get(msg.getObjectId());
                    data = tx.getOriginal();
                }

                msg.setInventoryMessageType(InventoryMessageType.CONTENT);
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
                try{
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
                    ByteArrayWrapper prevId = block.getPreviousHash();
                    if (!inventory.blocks.containsKey(prevId)) {
                        InventoryMessage newMsg = new InventoryMessage();
                        newMsg.setInventoryMessageType(InventoryMessageType.REQUEST);
                        newMsg.setIsBlock(true);
                        newMsg.setObjectId(block.getPreviousHash());

                        Envelope env = newMsg.packToEnvelope();
                        connectionManager.asyncSend(MessageUtil.serialize(env), peerAddr);
                    } else {
                        blockChainExecutor.processBlock(data, prevId);
                    }
                } else {
                    ByteArrayWrapper id = ByteArrayWrapper.copyOf(Hasher.doubleSha256(data));
                    if (!id.equals(msg.getObjectId())) return null;
                    if (inventory.memoryPool.containsKey(id)) return null;

                    Transaction tx = TransactionUtil.deserialize(data);

                    if (tx.getInEntries().size() == 0) return null;

                    synchronized (inventory.memoryPool) {
                        inventory.memoryPool.put(id, tx);
                    }
                }

                msg.setInventoryMessageType(InventoryMessageType.ADVERTISE);
                msg.setData(null);

                Envelope env = msg.packToEnvelope();

                connectionManager.asyncBroadcast(MessageUtil.serialize(env));

                return null;
            }catch(Exception e) {
                e.printStackTrace();
            }
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
