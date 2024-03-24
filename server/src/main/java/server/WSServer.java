package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.*;

@WebSocket
public class WSServer {
//    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = readJson(message);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> join(session, (JoinPlayer) command);
            case JOIN_OBSERVER -> observe(session, (JoinObserver) command);
            case MAKE_MOVE -> move(session, (MakeMove) command);
            case LEAVE -> leave(session, (Leave) command);
            case RESIGN -> resign(session, (Resign) command);
        }
    }

    private void join(Session session, JoinPlayer command) {
        System.out.println("Join request");
    }

    private void observe(Session session, JoinObserver command) {
        System.out.println("Observe request");
    }

    private void move(Session session, MakeMove command) {
        System.out.println("Move request");
    }

    private void leave(Session session, Leave command) {
        System.out.println("Leave request");
    }

    private void resign(Session session, Resign command) {
        System.out.println("Resign request");
    }


    private UserGameCommand readJson(String message) {
        // Register a deserializer for the User game Command interface
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserGameCommand.class, new CommandDeserializer());
        Gson gson = builder.create();

        // Parse the json string
        return gson.fromJson(message, UserGameCommand.class);
    }

}
