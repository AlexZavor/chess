package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.List;

public class MemoryUserDAO implements UserDAO{
    private static final List<UserData> users = new ArrayList<>();


    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if(users.contains(user)){
            throw new DataAccessException("Tried to add User that already existed.");
        }
        users.add(user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("User not found");
    }
}
