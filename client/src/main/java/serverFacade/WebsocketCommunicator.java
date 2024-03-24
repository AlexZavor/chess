package serverFacade;
import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import request.JoinGameRequest;
import response.JoinGameResponse;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.userCommands.JoinObserver;
import webSocketMessages.userCommands.JoinPlayer;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;
import java.net.URI;
import javax.websocket.*;

public class WebsocketCommunicator extends Endpoint {

    private final Session session;

    private final ServerMessageObserver observer;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public WebsocketCommunicator(ServerMessageObserver observer) throws Exception {
        this.observer = observer;
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
}
