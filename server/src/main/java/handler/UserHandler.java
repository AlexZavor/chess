package handler;

import response.*;
import spark.*;
import request.*;
import service.*;

public class UserHandler  extends Handler{

    public String handleRegister(Request request, Response response) {
        RegisterResponse serviceResponse = (new UserService()).register( gson.fromJson( request.body(), RegisterRequest.class ) );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }

    public String handleLogin(Request request, Response response) {
        LoginResponse serviceResponse = (new UserService()).login( gson.fromJson( request.body(), LoginRequest.class ) );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }

    public String handleLogout(Request request, Response response) {
        LogoutRequest serviceRequest = new LogoutRequest(getAuthToken(request));
        LogoutResponse serviceResponse = (new UserService()).logout( serviceRequest );
        response.status(serviceResponse.code());
        return gson.toJson( serviceResponse );
    }
}
