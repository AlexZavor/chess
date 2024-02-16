package dataAccess;

import model.AuthData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO{
    private static final List<AuthData> auths = new ArrayList<>();
    @Override
    public void clear() {
        auths.clear();
    }

    @Override
    public AuthData createAuth(String username) throws DataAccessException {
        for (AuthData auth : auths) {
            if (auth.username().equals(username)) {
                throw new DataAccessException("auth already exists");
            }
        }
        AuthData auth = new AuthData(UUID.randomUUID().toString(), username);
        auths.add(auth);
        return auth;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        throw new DataAccessException("No matching authorization");
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if(!auths.removeIf(auth -> auth.authToken().equals(authToken))){
            throw new DataAccessException("No matching authorization to delete");
        }
    }
}
