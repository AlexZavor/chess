package dataAccess;

import java.sql.SQLException;

import static java.lang.String.format;

public class SQLDAO {

    protected void createTable(String[] createStatements) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Unable to configure Database");
        }
    }

    protected void clearTable(String table){
        var statement = format("TRUNCATE %s", table);
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            System.out.printf("Failed to clear %s \n", table);
        }
    }
}
