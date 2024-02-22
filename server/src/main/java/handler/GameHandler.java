package handler;

import request.*;
import response.*;
import service.GameService;
import spark.*;

public class GameHandler  extends Handler{

    public String handleListGames(Request request, Response response) {
        ListGamesRequest serviceRequest = new ListGamesRequest(getAuthToken(request));
        ListGamesResponse serviceResponse = (new GameService()).listGames( serviceRequest );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }

    public String handleCreateGame(Request request, Response response){
        CreateGameRequest serviceRequest = new CreateGameRequest(getAuthToken(request), gson.toJson(request.body()));
        CreateGameResponse serviceResponse = (new GameService()).createGame( serviceRequest );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }

    public String handleJoinGame(Request request, Response response){
        JoinGameRequest serviceRequest = gson.fromJson(request.body(), JoinGameRequest.class );
        JoinGameResponse serviceResponse = (new GameService()).joinGame(request.headers("authorization"), serviceRequest );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }
}
