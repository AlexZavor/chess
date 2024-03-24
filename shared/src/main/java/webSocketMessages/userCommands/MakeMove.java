package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class MakeMove extends UserGameCommand{
    final Integer gameID;

    final ChessMove move;

    public MakeMove(String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }
}
