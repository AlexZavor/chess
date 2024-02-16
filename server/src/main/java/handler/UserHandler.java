package handler;

import com.google.gson.*;
import spark.*;
import request.*;
import service.*;

public class UserHandler {

    public String handleRegister(Request request, Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson( (new UserService()).register( gson.fromJson( request.body(), RegisterRequest.class ) ) );
    }

    public String handleLogin(Request request, Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson( (new UserService()).login( gson.fromJson( request.body(), LoginRequest.class ) ) );
    }

    public String handleLogout(Request request, Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LogoutRequest request1 = new LogoutRequest(request.headers("authorization"));
        return gson.toJson( (new UserService()).logout( request1 ) );
    }
}
