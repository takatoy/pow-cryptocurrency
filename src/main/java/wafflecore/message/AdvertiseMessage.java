package wafflecore.message;

import static wafflecore.constants.Constants.*;

public class AdvertiseMessage extends Message {
    @Override
    public static int getMessageType() {
        return MSG_TYPE_ADVERTISE;
    }
}
