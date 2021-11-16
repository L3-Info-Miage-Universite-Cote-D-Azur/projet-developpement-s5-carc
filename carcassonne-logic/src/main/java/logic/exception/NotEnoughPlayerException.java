package logic.exception;

public class NotEnoughPlayerException extends RuntimeException {
    public NotEnoughPlayerException(int count, int required) {
        super(String.format("Not enough players to start the game. Count:%d Needed:%d", count, required));
    }
}
