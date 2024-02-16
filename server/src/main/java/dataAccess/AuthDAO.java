package dataAccess;

import model.AuthData;

public interface AuthDAO {
    void clear();
    AuthData createAuth() throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
}
