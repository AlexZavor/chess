package dataAccess;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() {
    }

    @Override
    public void clear() {

    }

    @Override
    public AuthData createAuth(String username) {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
