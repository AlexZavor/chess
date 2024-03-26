package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    public final Integer gameID;

    public Resign(String authToken, Integer gameID) {
        super(authToken);
        commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }
}
