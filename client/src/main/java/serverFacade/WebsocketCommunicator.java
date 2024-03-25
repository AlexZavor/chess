package serverFacade;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import request.JoinGameRequest;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebsocketCommunicator extends Endpoint {

    private final Session session;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public WebsocketCommunicator(ServerMessageObserver observer) throws Exception {
        URI uri = new URI("ws://localhost:8080/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                try {
                    ServerMessage serverMessage =
                            gson.fromJson(message, Notification.class);
                    observer.notify(serverMessage);
                } catch(Exception ex) {
                    observer.notify(new Error(ex.getMessage()));
                }
            }
        });
    }
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void doJoinObserver(JoinGameRequest request, String authToken) throws IOException {
        JoinObserver command = new JoinObserver(authToken, request.gameID());
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void doJoinPlayer(JoinGameRequest request, String authToken) throws IOException {
        JoinPlayer command = new JoinPlayer(authToken, request.gameID(),
                                            request.playerColor().equalsIgnoreCase("white") ?
                                                    ChessGame.TeamColor.WHITE:
                                                    ChessGame.TeamColor.BLACK
                                            );
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void doLeave(String authToken, int gameID) throws IOException {
        Leave command = new Leave(authToken, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void doMakeMove(String authToken, int gameID, ChessMove move) throws IOException {
        MakeMove command = new MakeMove(authToken, gameID, move);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }

    public void doResign(String authToken, int gameID) throws IOException {
        Resign command = new Resign(authToken, gameID);
        this.session.getBasicRemote().sendText(gson.toJson(command));
    }
}
