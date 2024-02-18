package service;

import dataAccess.*;
import model.*;

public class Service {

    protected boolean isAuthorized(String authToken){

        AuthDAO auths = new MemoryAuthDAO();
        try {
            auths.getAuth(authToken);
        } catch (DataAccessException e) {
            return false;
        }

        return true;
    }

    protected AuthData getAuthorization(String authToken){

        AuthDAO auths = new MemoryAuthDAO();
        try {
            return auths.getAuth(authToken);
        } catch (DataAccessException e) {
            return null;
        }
    }

    protected GameData getGame(int gameID){

        GameDAO games = new MemoryGameDAO();
        try {
            return games.getGame(gameID);
        } catch (DataAccessException e) {
            return null;
        }
    }

}
