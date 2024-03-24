package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    final Integer gameID;

    public Resign(String authToken, Integer gameID) {
        super(authToken);
        commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }
}
