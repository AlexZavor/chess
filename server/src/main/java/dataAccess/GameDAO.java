package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear();
    void createGame(GameData gameData) throws DataAccessException;
    GameData getAuth(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames();
    void updateGame(int gameID, GameData gameData) throws DataAccessException;
}
