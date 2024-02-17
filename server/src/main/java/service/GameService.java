package service;

import chess.ChessGame;
import dataAccess.*;
import model.*;
import request.*;
import response.*;

public class GameService {
    static int nextgameID = 1;
    public ListGamesResponse listGames(ListGamesRequest request) {
        AuthDAO auths = new MemoryAuthDAO();
        try {
            auths.getAuth(request.authToken());
        } catch (DataAccessException e) {
            return new ListGamesResponse(401, null, "Error: unauthorized");
        }
        GameDAO games = new MemoryGameDAO();
        return new ListGamesResponse(200, games.listGames(), null);
    }

    public CreateGameResponse createGame(CreateGameRequest request) {
        if(request.gameName().isEmpty()){
            return new CreateGameResponse(400, null, "Error: bad request");
        }
        AuthDAO auths = new MemoryAuthDAO();
        try {
            auths.getAuth(request.authToken());
        } catch (DataAccessException e) {
            return new CreateGameResponse(401, null, "Error: unauthorized");
        }
        GameDAO games = new MemoryGameDAO();
        int gameID = nextgameID;
        nextgameID ++;
        GameData game = new GameData(gameID,null,null, request.gameName(), new ChessGame());
        games.createGame(game);
        return new CreateGameResponse(200, gameID, null);
    }

    public JoinGameResponse joinGame(String authToken, JoinGameRequest request) {
        if(!(request.playerColor() == null) && !request.playerColor().equals("BLACK") && !request.playerColor().equals("WHITE")){
            return new JoinGameResponse(400, "Error: bad request - NAME");
        }
        System.out.println("joinGameCheck\n");
        AuthDAO auths = new MemoryAuthDAO();
        AuthData auth;
        try {
            auth = auths.getAuth(authToken);
        } catch (DataAccessException e) {
            return new JoinGameResponse(401, "Error: unauthorized");
        }
        GameDAO games = new MemoryGameDAO();
        GameData gameToCheck;
        GameData game;
        try {
            gameToCheck = games.getGame(request.gameID());
        } catch (DataAccessException e) {
            return new JoinGameResponse(400, "Error: bad request - CAN'T FIND GAME");
        }
        if(request.playerColor() == null){
                return new JoinGameResponse(200, null);
        }else if(request.playerColor().equals("WHITE")){
            if(gameToCheck.whiteUsername() != null) {
                return new JoinGameResponse(403, "Error: already taken");
            } else {
                game = new GameData(request.gameID(), auth.username(), gameToCheck.blackUsername(), gameToCheck.gameName(), gameToCheck.game());
            }
        }else {
            if(gameToCheck.blackUsername() != null) {
                return new JoinGameResponse(403, "Error: already taken");
            } else {
                game = new GameData(request.gameID(), gameToCheck.whiteUsername(), auth.username(), gameToCheck.gameName(), gameToCheck.game());
            }
        }
        try {
            games.updateGame(request.gameID(), game);
        } catch (DataAccessException e) {
            return new JoinGameResponse(400, "Error: bad request - CAN'T UPDATE GAME");
        }
        return new JoinGameResponse(200, null);
    }

}
