package waffle.wafflecore.util;

import waffle.wafflecore.message.*;
import waffle.wafflecore.tool.Logger;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;

public class MessageUtil {
    private static Logger logger = Logger.getInstance();
    private static ObjectMapper mapper = new ObjectMapper();
    private static SimpleModule module = new SimpleModule();
    static {
        module.addDeserializer(Message.class, new MessageDeserializer());
        mapper.registerModule(module);
    }

    public static Envelope deserialize(byte[] data) {
        String dataStr = new String(data);

        try {
            Envelope env = mapper.readValue(data, Envelope.class);
            return env;
        } catch (Exception e) {
            logger.log("Invalid data received.");
        }

        return null;
    }

    public static Envelope[] deserializeArray(byte[] data) {
        String dataStr = new String(data);

        try {
            Envelope[] envs = mapper.readValue(data, Envelope[].class);
            return envs;
        } catch (Exception e) {
            logger.log("Invalid data received.");
        }

        return null;
    }

    public static byte[] serialize(Envelope env) {
        byte[] serialized = null;

        try {
            serialized = mapper.writeValueAsBytes(env);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serialized;
    }

    private static class MessageDeserializer extends StdDeserializer<Message> {
        protected MessageDeserializer(){
            this(null);
        }

        protected MessageDeserializer(final Class<?> vc){
            super(vc);
        }

        @Override
        public Message deserialize(final JsonParser parser, final DeserializationContext context)
        throws IOException, JsonProcessingException {
            final JsonNode node = parser.getCodec().readTree(parser);
            final ObjectMapper mapper = (ObjectMapper)parser.getCodec();
            if (node.has("invtype")) {
                return mapper.treeToValue(node, InventoryMessage.class);
            } else {
                return mapper.treeToValue(node, Hello.class);
            }
        }
    }

    // public static Message deserializeHello(byte[] data) {
    //     String dataStr = new String(data);

    //     try {
    //         Hello hello = mapper.readValue(dataStr, Hello.class);
    //         return hello;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return null;
    // }

    // public static InventoryMessage deserializeInvMsg(byte[] data) {
    //     String dataStr = new String(data);

    //     try {
    //         InventoryMessage msg = mapper.readValue(dataStr, InventoryMessage.class);
    //         return msg;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return null;
    // }

    // public static byte[] serialize(Hello hello) {
    //     byte[] serialized = null;

    //     try {
    //         serialized = mapper.writeValueAsBytes(hello);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return serialized;
    // }

    // public static byte[] serialize(InventoryMessage invMsg) {
    //     byte[] serialized = null;

    //     try {
    //         serialized = mapper.writeValueAsBytes(invMsg);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return serialized;
    // }
}
