package handler;

import com.google.gson.*;
import request.*;
import response.*;
import service.GameService;
import spark.*;

public class GameHandler {

    public String handleListGames(Request request, Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ListGamesRequest serviceRequest = new ListGamesRequest(request.headers("authorization"));
        ListGamesResponse serviceResponse = (new GameService()).listGames( serviceRequest );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }

    public String handleCreateGame(Request request, Response response){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CreateGameRequest serviceRequest = new CreateGameRequest(request.headers("authorization"), gson.toJson(request.body()));
        CreateGameResponse serviceResponse = (new GameService()).createGame( serviceRequest );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }

    public String handleJoinGame(Request request, Response response){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JoinGameRequest serviceRequest = gson.fromJson(request.body(), JoinGameRequest.class );
        JoinGameResponse serviceResponse = (new GameService()).joinGame(request.headers("authorization"), serviceRequest );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }
}
