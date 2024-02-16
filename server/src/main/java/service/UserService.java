package service;

import model.*;
import dataAccess.*;
import request.*;
import responce.*;

public class UserService {
    public RegisterResponse register(RegisterRequest request) {
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();
        UserData user = new UserData(request.username(), request.password(), request.email());
        AuthData auth;
        try {
            users.createUser(user);
            auth = auths.createAuth(request.username());
        } catch (DataAccessException e) {
            return new RegisterResponse(403, null, null, "Error: already taken");
        }
        return new RegisterResponse(200, request.username(), auth.authToken(), null);
    }

    public LoginResponse login(LoginRequest request) {
        UserDAO users = new MemoryUserDAO();
        AuthDAO auths = new MemoryAuthDAO();
        AuthData auth;
        try {
            UserData user = users.getUser(request.username());
            if(user.password().equals(request.password())){
                auth = auths.createAuth(request.username());
            }else{
                throw new DataAccessException("Incorrect Password");
            }
        } catch (DataAccessException e) {
            return new LoginResponse(401, null, null, "Error: unauthorized");
        }
        return new LoginResponse(200, request.username(), auth.authToken(), null);
    }

    public LogoutResponse logout(LogoutRequest request) {
        AuthDAO auths = new MemoryAuthDAO();
        System.out.println(request.authToken() + "one two three");
        try {
            auths.deleteAuth(request.authToken());
        } catch (DataAccessException e) {
            return new LogoutResponse(401, "Error: unauthorized");
        }
        return new LogoutResponse(200, null);
    }
}
