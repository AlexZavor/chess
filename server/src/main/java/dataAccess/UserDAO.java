package dataAccess;

import model.UserData;

public interface UserDAO {
    void clear();
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
}
