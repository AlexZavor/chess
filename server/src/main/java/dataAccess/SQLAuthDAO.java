package dataAccess;

import java.sql.*;
import java.util.UUID;

import model.AuthData;

public class SQLAuthDAO implements AuthDAO{
    public SQLAuthDAO() {
        try {
            DatabaseManager.createDatabase();
            try (var conn = DatabaseManager.getConnection()) {
                String[] createStatements = {
                        """
                        CREATE TABLE IF NOT EXISTS  auths (
                          `username` varchar(128) NOT NULL,
                          `authToken` varchar(256) NOT NULL,
                          PRIMARY KEY (`username`)
                        )
                        """
                };
                for (var statement : createStatements) {
                    try (var preparedStatement = conn.prepareStatement(statement)) {
                        preparedStatement.executeUpdate();
                    }
                }
            } catch (SQLException ex) {
                throw new DataAccessException("Unable to configure Auth Database");
            }
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clear() {
        var statement = "TRUNCATE auths";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            System.out.println("Failed to clear Auths");
        }
    }

    @Override
    public AuthData createAuth(String username) {
        AuthData auth = new AuthData(UUID.randomUUID().toString(), username);
        var statement = "INSERT INTO auths (username, authToken) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, auth.username());
                ps.setString(2, auth.authToken());
                if(ps.executeUpdate() != 1) {
                    throw new DataAccessException("Can't Create auth");
                }

            }
        } catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
        return auth;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var statement = "SELECT * FROM auths WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1,authToken);
                ResultSet rs = ps.executeQuery();
                rs.next();
                var username = rs.getString("username");
                return new AuthData(authToken, username);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Can't find auth");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var statement = "DELETE FROM auths WHERE authToken = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1,authToken);
                if(ps.executeUpdate() != 1) {
                    throw new DataAccessException("Did weird delete");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Can't delete User");
        }
    }
}
