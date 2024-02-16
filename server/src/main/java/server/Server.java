package server;

import spark.*;
import handler.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRouts();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private static void createRouts(){
        Spark.post("/user", ((request, response) ->
                (new UserHandler()).handleRegister(request, response)
                ));
        Spark.post("/session", ((request, response) ->
                (new UserHandler()).handleLogin(request, response)
                ));
        Spark.delete("/session", ((request, response) ->
                (new UserHandler()).handleLogout(request, response)
                ));
        Spark.delete("/db", ((request, response) ->
                (new DatabaseHandler()).handleClear(response)
                ));
        Spark.get("/game", ((request, response) ->
                (new GameHandler()).handleListGames(request, response)
                ));
        Spark.post("/game", ((request, response) ->
                (new GameHandler()).handleCreateGame(request, response)
                ));
        Spark.put("/game", ((request, response) ->
                (new GameHandler()).handleJoinGame(request, response)
                ));
    }
}
