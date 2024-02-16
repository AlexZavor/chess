package handler;

import com.google.gson.*;
import response.*;
import spark.*;
import request.*;
import service.*;

public class UserHandler {

    public String handleRegister(Request request, Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        RegisterResponse serviceResponse = (new UserService()).register( gson.fromJson( request.body(), RegisterRequest.class ) );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }

    public String handleLogin(Request request, Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LoginResponse serviceResponse = (new UserService()).login( gson.fromJson( request.body(), LoginRequest.class ) );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }

    public String handleLogout(Request request, Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        LogoutRequest serviceRequest = new LogoutRequest(request.headers("authorization"));
        LogoutResponse serviceResponse = (new UserService()).logout( serviceRequest );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }
}
