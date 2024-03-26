package service;

import model.*;
import dataAccess.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import request.*;
import response.*;

public class UserService extends Service {

    public RegisterResponse register(RegisterRequest request) {

        // Check for bad Data fields
        if(request.username() == null || request.password() == null || request.email() == null){
            return new RegisterResponse(400, null, null, "Error: bad request");
        }
        UserData user = new UserData(request.username(), request.password(), request.email());

        // Make user and authorization
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

        AuthData auth;
        try {
            // Check user exists
            UserData user = users.getUser(request.username());
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if(encoder.matches(request.password(), user.password())){
                // Authorize if password matches
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

        // Delete Authorization
        try {
            auths.deleteAuth(request.authToken());
        } catch (DataAccessException e) {
            return new LogoutResponse(401, "Error: unauthorized");
        }

        return new LogoutResponse(200, null);
    }

    public String getUsername(String authToken){
        try {
            return auths.getAuth(authToken).username();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
