package service;

import chess.ChessGame;
import dataAccess.*;
import model.*;
import request.*;
import response.*;

public class GameService extends Service{

    // Variable to keep track of Game IDs for no repeats
    private static int nextGameID = 1;

    public ListGamesResponse listGames(ListGamesRequest request) {

        // Check Authorization
        if(isAuthorized(request.authToken())){
            return new ListGamesResponse(200, games.listGames(), null);
        }

        return new ListGamesResponse(401, null, "Error: unauthorized");
    }

    public CreateGameResponse createGame(String authToken, CreateGameRequest request) {

        // Check for bad input fields
        if(request.gameName() == null || request.gameName().isEmpty()){
            return new CreateGameResponse(400, null, "Error: bad request");
        }

        // Check Authorization
        if(isAuthorized(authToken)){
            int gameID = nextGameID;
            nextGameID++;
            GameData game = new GameData(gameID,null,null, request.gameName(), new ChessGame());
            games.createGame(game);
            return new CreateGameResponse(200, gameID, null);
        }

        return new CreateGameResponse(401, null, "Error: unauthorized");
    }

    public JoinGameResponse joinGame(String authToken, JoinGameRequest request) {

        // Check for bad input fields
        if(!(request.playerColor() == null) && !request.playerColor().equals("BLACK") && !request.playerColor().equals("WHITE")){
            return new JoinGameResponse(400, "Error: bad request");
        }

        // Check Authorization
        AuthData auth = getAuthorization(authToken);
        if(auth == null){
            return new JoinGameResponse(401, "Error: unauthorized");
        }

        // Find game to update
        GameData gameToCheck = getGame(request.gameID());
        if(gameToCheck == null){
            return new JoinGameResponse(400, "Error: bad request");
        }

        // Get new game data
        GameData updatedGame;
        if(request.playerColor() == null){
            // Add watcher
            return new JoinGameResponse(200, null);
        }else if(request.playerColor().equals("WHITE")){
            // Add White player
            if(gameToCheck.whiteUsername() != null) {
                return new JoinGameResponse(403, "Error: already taken");
            } else {
                updatedGame = new GameData(request.gameID(), auth.username(), gameToCheck.blackUsername(),
                                           gameToCheck.gameName(), gameToCheck.game());
            }
        }else {
            // Add Black player
            if(gameToCheck.blackUsername() != null) {
                return new JoinGameResponse(403, "Error: already taken");
            } else {
                updatedGame = new GameData(request.gameID(), gameToCheck.whiteUsername(), auth.username(),
                                           gameToCheck.gameName(), gameToCheck.game());
            }
        }

        // Update game
        try {
            games.updateGame(request.gameID(), updatedGame);
        } catch (DataAccessException e) {
            return new JoinGameResponse(400, "Error: bad request");
        }

        return new JoinGameResponse(200, null);
    }

}
