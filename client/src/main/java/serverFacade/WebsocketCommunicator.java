package serverFacade;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;

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

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
