package service;

import dataAccess.*;
import response.ClearResponse;

public class DatabaseService extends Service {

    public ClearResponse clear() {

        // Clear each of the databases
        AuthDAO auths = new MemoryAuthDAO();
        UserDAO users = new MemoryUserDAO();
        GameDAO games = new MemoryGameDAO();
        auths.clear();
        users.clear();
        games.clear();
        return new ClearResponse(200);
    }

}
