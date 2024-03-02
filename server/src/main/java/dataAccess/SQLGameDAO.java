package dataAccess;

import java.sql.*;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.GameData;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public SQLGameDAO() {
        try {
            DatabaseManager.createDatabase();
            try (var conn = DatabaseManager.getConnection()) {
                String[] createStatements = {
                        """
                        CREATE TABLE IF NOT EXISTS  games (
                          `gameID` int NOT NULL,
                          `whiteUsername` varchar(128),
                          `blackUsername` varchar(128),
                          `gameName` varchar(128),
                          `game` TEXT,
                          PRIMARY KEY (`gameID`)
                        )
                        """
                };
                for (var statement : createStatements) {
                    try (var preparedStatement = conn.prepareStatement(statement)) {
                        preparedStatement.executeUpdate();
                    }
                }
            } catch (SQLException ex) {
                throw new DataAccessException("Unable to configure games Database");
            }
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clear() {
        var statement = "TRUNCATE games";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Failed to clear Games");
        }
    }

    @Override
    public void createGame(GameData gameData) {
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameData.gameID());
                ps.setString(2, gameData.whiteUsername());
                ps.setString(3, gameData.blackUsername());
                ps.setString(4, gameData.gameName());
                ps.setString(5, gson.toJson(gameData.game(), ChessGame.class));
                if(ps.executeUpdate() != 1) {
                    throw new DataAccessException("Can't Create game");
                }

            }
        } catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var statement = "SELECT * FROM games WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)){
                ps.setInt(1,gameID);
                ResultSet rs = ps.executeQuery();
                rs.next();
                var whiteUsername = rs.getString("whiteUsername");
                var blackUsername = rs.getString("blackUsername");
                var gameName = rs.getString("gameName");
                ChessGame game = gson.fromJson(rs.getString("game"), ChessGame.class);
                return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Can't find User");
        }
    }

    @Override
    public ArrayList<GameData> listGames() {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        var gameID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        ChessGame game = gson.fromJson(rs.getString("game"), ChessGame.class);
                        result.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                    }
                }
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public void updateGame(int gameID, GameData gameData) throws DataAccessException {
        var statement = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, gameData.whiteUsername());
                ps.setString(2, gameData.blackUsername());
                ps.setString(3, gameData.gameName());
                ps.setString(4, gson.toJson(gameData.game(), ChessGame.class));
                ps.setInt(5, gameData.gameID());
                if(ps.executeUpdate() != 1) {
                    throw new DataAccessException("Can't Update game");
                }

            }
        } catch (SQLException  e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
