package logic.command;

import logic.Game;
import stream.ByteInputStream;
import stream.ByteOutputStream;

public interface ICommand {
    CommandId getId();
    void encode(ByteOutputStream stream);
    void decode(ByteInputStream stream);
    boolean execute(Game game);
}
