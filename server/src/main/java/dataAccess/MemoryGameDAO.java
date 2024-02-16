package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO{
    private static final List<GameData> games = new ArrayList<>();
    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public void createGame(GameData gameData) {
        games.add(gameData);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("No matching game");
    }

    @Override
    public ArrayList<GameData> listGames() {
        return (ArrayList<GameData>) games;
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                games.remove(game);
                games.add(gameData);
                return;
            }
        }
        throw new DataAccessException("No matching game");
    }
}
