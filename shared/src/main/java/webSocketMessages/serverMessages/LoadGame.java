package webSocketMessages.serverMessages;

import model.GameData;

public class LoadGame extends ServerMessage{

    final GameData game;

    public LoadGame(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public GameData getGame(){
        return game;
    }
}
