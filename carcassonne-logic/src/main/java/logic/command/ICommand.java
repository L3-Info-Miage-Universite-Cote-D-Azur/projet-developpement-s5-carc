package logic.command;

import logic.Game;

public interface ICommand {
    boolean execute(Game game);
}
