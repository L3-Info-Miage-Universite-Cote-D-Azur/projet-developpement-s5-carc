package network.message;

import stream.ByteInputStream;
import stream.ByteOutputStream;

public abstract class Message {
    public abstract MessageId getId();
    public abstract void encode(ByteOutputStream stream);
    public abstract void decode(ByteInputStream stream);
}
