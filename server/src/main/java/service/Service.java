package service;

import dataAccess.*;
import model.*;

public class Service {

    static final AuthDAO auths = new SQLAuthDAO();
    static final UserDAO users = new SQLUserDAO();
    static final GameDAO games = new SQLGameDAO();

    protected boolean isAuthorized(String authToken){
        try {
            auths.getAuth(authToken);
        } catch (DataAccessException e) {
            return false;
        }

        return true;
    }

    protected AuthData getAuthorization(String authToken){
        try {
            return auths.getAuth(authToken);
        } catch (DataAccessException e) {
            return null;
        }
    }

    protected GameData getGame(int gameID){
        try {
            return games.getGame(gameID);
        } catch (DataAccessException e) {
            return null;
        }
    }

}
