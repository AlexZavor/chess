package dataAccess;

import java.sql.*;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SQLUserDAO extends SQLDAO implements UserDAO {
    static private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public SQLUserDAO() {
        try {
            DatabaseManager.createDatabase();
            String[] createStatements = {
                    """
                        CREATE TABLE IF NOT EXISTS  users (
                          `username` varchar(128) NOT NULL,
                          `password` varchar(256) NOT NULL,
                          `email` varchar(128) NOT NULL,
                          PRIMARY KEY (`username`)
                        )
                        """
            };
            createTable(createStatements);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void clear() {
        clearTable("users");
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1,user.username());
                ps.setString(2,encoder.encode(user.password()));
                ps.setString(3,user.email());
                if(ps.executeUpdate() != 1) {
                    throw new DataAccessException("Can't Create user");
                }

            }
        } catch (SQLException e) {
            throw new DataAccessException("Can't create User");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var statement = "SELECT * FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)){
                ps.setString(1,username);
                ResultSet rs = ps.executeQuery();
                rs.next();
                var password = rs.getString("password");
                var email = rs.getString("email");
                return new UserData(username, password, email);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Can't find User");
        }
    }
}
