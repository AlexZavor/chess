package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand{
    final Integer gameID;

    public JoinObserver(String authToken, Integer gameID) {
        super(authToken);
        commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }
}
