package service;

import dataAccess.*;
import response.ClearResponse;

public class DatabaseService {
    public ClearResponse clear() {
        AuthDAO auths = new MemoryAuthDAO();
        UserDAO users = new MemoryUserDAO();
        GameDAO games = new MemoryGameDAO();
        auths.clear();
        users.clear();
        games.clear();
        return new ClearResponse(200);
    }
}
