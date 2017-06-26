package wafflecore.message;

import static wafflecore.constants.Constants.*;

public class ContentMessage extends Message {
    @Override
    public static int getMessageType() {
        return MSG_TYPE_CONTENT;
    }
}
