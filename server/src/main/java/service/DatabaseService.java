package service;

import response.ClearResponse;

public class DatabaseService extends Service {

    public ClearResponse clear() {

        // Clear each of the databases
        auths.clear();
        users.clear();
        games.clear();
        return new ClearResponse(200);
    }

}
