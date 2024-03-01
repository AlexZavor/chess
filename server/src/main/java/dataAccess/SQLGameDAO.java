package dataAccess;

import java.sql.*;
import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() {

    }

    @Override
    public void clear() {

    }

    @Override
    public void createGame(GameData gameData) {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {

    }
}
