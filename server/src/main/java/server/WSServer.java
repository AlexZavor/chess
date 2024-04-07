package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.*;

@WebSocket
public class WSServer {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    GameService gameService = new GameService();
    UserService userService = new UserService();

    private final Map<Integer, List<WSUser>> userMap = new HashMap<>();

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

    @OnWebSocketError
    public void onError(Throwable error){
        System.out.println("Error!" + error.getMessage());
    }

    private void join(Session session, JoinPlayer command) throws IOException {
        System.out.println("Join request");

        // Checking
        var game = gameService.getGame(command.gameID);
        if(game == null){
            sendError(session, "Bad Game ID");
            return;
        }
        var username = userService.getUsername(command.getAuthString());
        if(username == null){
            sendError(session, "Bad Auth Token");
            return;
        }
        if(command.playerColor == ChessGame.TeamColor.WHITE){
            if(!Objects.equals(game.whiteUsername(), username)){
                sendError(session, "Wrong team color");
                return;
            }
        }else
        if(command.playerColor == ChessGame.TeamColor.BLACK){
            if(!Objects.equals(game.blackUsername(), username)){
                sendError(session, "Wrong team color");
                return;
            }
        }

        // add new user to game
        userMap.computeIfAbsent(command.gameID, k -> new ArrayList<>());
        userMap.get(command.gameID).add(new WSUser(command.getAuthString(), session));

        session.getRemote().sendString(gson.toJson(new LoadGame(game)));

        notifyOtherUsers(game.gameID(), command.getAuthString(), "Player " + username + " has joined as " + command.playerColor.toString());
    }

    private void observe(Session session, JoinObserver command) throws IOException {
        System.out.println("Observe request");

        // testing
        var username = userService.getUsername(command.getAuthString());
        if(username == null){
            sendError(session, "Bad Auth Token");
            return;
        }
        var game = gameService.getGame(command.gameID);
        if(game == null){
            sendError(session, "Bad Game ID");
            return;
        }

        // add new user to game
        userMap.computeIfAbsent(command.gameID, k -> new ArrayList<>());
        userMap.get(command.gameID).add(new WSUser(command.getAuthString(), session));

        session.getRemote().sendString(gson.toJson(new LoadGame(game)));

        notifyOtherUsers(game.gameID(), command.getAuthString(), username + " has joined as an observer.");
    }

    private void move(Session session, MakeMove command) throws IOException {
        System.out.println("Move request");

        var username = userService.getUsername(command.getAuthString());
        var game = gameService.getGame(command.gameID);
        ChessGame.TeamColor teamColor;
        if(game.game().getGameOver()){
            sendError(session, "Can't move game has ended.");
            return;
        }
        if(Objects.equals(game.blackUsername(), username)){
            teamColor = ChessGame.TeamColor.BLACK;
        }else
        if(Objects.equals(game.whiteUsername(), username)){
            teamColor = ChessGame.TeamColor.WHITE;
        }else {
            sendError(session, "Observer can't move.");
            return;
        }

        // First checks
        if((game.game().getTeamTurn() != teamColor)){
            sendError(session, "not your turn.");
            return;
        }

        // Check piece
        var piece = game.game().getBoard().getPiece(command.move.getStartPosition());
        if(piece == null){
            sendError(session, "No piece there.");
            return;
        }
        if(piece.getTeamColor() != teamColor){
            sendError(session, "not your piece.");
            return;
        }

        try {
            gameService.makeMove(command.gameID, command.move);
        } catch (InvalidMoveException e) {
            sendError(session, "Invalid move.");
            return;
        }

        // update all players games
        game = gameService.getGame(command.gameID);
        for(var user : userMap.get(command.gameID)){
            user.session().getRemote().sendString(gson.toJson(
                    new LoadGame(game)
            ));
        }

        // Notify other players
        notifyOtherUsers(game.gameID(), command.getAuthString(), username + " played " + command.move);
        if(game.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
            notifyAllUsers(game.gameID(), "White is in Checkmate!, the game is over");
        }else if(game.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
            notifyAllUsers(game.gameID(), "Black is in Checkmate!, the game is over");
        }else if(game.game().isInCheck(ChessGame.TeamColor.WHITE)){
            notifyAllUsers(game.gameID(), "White is in Check!");
        }else if(game.game().isInCheck(ChessGame.TeamColor.BLACK)){
            notifyAllUsers(game.gameID(), "Black is in Check!");
        }

    }

    private void leave(Session session, Leave command) throws IOException {
        System.out.println("Leave request");
        userMap.get(command.gameID).remove(new WSUser(command.getAuthString(), session));

        var game = gameService.getGame(command.gameID);
        var user = userService.getUsername(command.getAuthString());

        if(Objects.equals(game.blackUsername(), user)){
            gameService.removePlayer(command.gameID, ChessGame.TeamColor.BLACK);
        }else if (Objects.equals(game.whiteUsername(), user)){
            gameService.removePlayer(command.gameID, ChessGame.TeamColor.WHITE);
        }

        notifyOtherUsers(game.gameID(), command.getAuthString(), user + " has left the game.");
    }

    private void resign(Session session, Resign command) throws IOException {
        System.out.println("Resign request");
        var username = userService.getUsername(command.getAuthString());
        var game = gameService.getGame(command.gameID);
        ChessGame.TeamColor teamWin;
        if(game.game().getGameOver()){
            sendError(session, "Game Already over.");
            return;
        }
        if(Objects.equals(game.blackUsername(), username)){
            teamWin = ChessGame.TeamColor.WHITE;
        }else
        if(Objects.equals(game.whiteUsername(), username)){
            teamWin = ChessGame.TeamColor.BLACK;
        }else {
            sendError(session, "Cannot resign.");
            return;
        }
        gameService.endGame(command.gameID);
        notifyAllUsers(command.gameID,
                username + " Has resigned from the game! " + teamWin + " Wins!"
        );
    }


    private UserGameCommand readJson(String message) {
        // Register a deserializer for the User game Command interface
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserGameCommand.class, new CommandDeserializer());
        Gson gsonCommand = builder.create();

        // Parse the json string
        return gsonCommand.fromJson(message, UserGameCommand.class);
    }

    private void notifyOtherUsers(int gameID, String authToken, String message) throws IOException {
        for(var user : userMap.get(gameID)){
            if(!user.authToken().equals(authToken)){
                if(user.session().isOpen()){
                    user.session().getRemote().sendString(gson.toJson(
                            new Notification(message)
                    ));
                }
            }
        }
    }

    private void notifyAllUsers(int gameID, String message) throws IOException {
        for(var user : userMap.get(gameID)){
            user.session().getRemote().sendString(gson.toJson(
                    new Notification(message)
            ));
        }
    }

    private void sendError(Session session, String message) throws IOException {
        session.getRemote().sendString(gson.toJson(new Error(message)));
    }

}
