package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import org.junit.jupiter.api.*;
import model.*;

import java.util.ArrayList;

public class DataAccessTests {
    AuthDAO auths = new SQLAuthDAO();
    UserDAO users = new SQLUserDAO();
    GameDAO games = new SQLGameDAO();

    @BeforeEach
    public void setup() {
        auths.clear();
        users.clear();
        games.clear();
    }

    @Test
    @DisplayName("Clear User")
    public void ClearUser(){
        try {
            // Make user, check its there, then clear
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
            users.getUser("John Doe");
            users.clear();
        } catch (DataAccessException e) {
            Assertions.fail();
        }
        try {
            // Getting same user should now fail
            users.getUser("John Doe");
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Create User - positive")
    public void CreateUserPos(){
        try {
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Create User - negative")
    public void CreateUserNeg(){
        try {
            // Create user with null username (should throw exception)
            users.createUser(new UserData(null, "1234", "mail@mail.yep"));
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Get User - positive")
    public void GetUserPos(){
        try {
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
            var user = users.getUser("John Doe");
            Assertions.assertNotNull(user);
            Assertions.assertEquals("mail@mail.yep", user.email());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Get User - negative")
    public void GetUserNeg(){
        try {
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
            // Get user that don't exist (should throw exception)
            users.getUser("I'm a fake user!");
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Clear Auth")
    public void ClearAuth(){
        AuthData auth;
        try {
            // Make user, make auth, then clear
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
            auth = auths.createAuth("John Doe");
            auths.getAuth(auth.authToken());
            auths.clear();
        } catch (DataAccessException e) {
            Assertions.fail();
            return;
        }
        try {
            // Getting same auth should now fail
            auths.getAuth(auth.authToken());
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Create Auth - positive")
    public void CreateAuthPos(){
        try {
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
            auths.createAuth("John Doe");
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Create Auth - negative")
    public void CreateAuthNeg(){
        try {
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
            var auth = auths.createAuth(null);
            auths.getAuth(auth.authToken());
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Get Auth - positive")
    public void GetAuthPos(){
        try {
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
            var auth = auths.createAuth("John Doe");
            auths.getAuth(auth.authToken());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Get Auth - negative")
    public void GetAuthNeg(){
        try {
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
            // Get user that don't exist (should throw exception)
            auths.createAuth("John Doe");
            auths.getAuth("LOOKATTHISREALAUTH");
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Delete Auth - positive")
    public void DeleteAuthPos(){
        AuthData auth;
        try {
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
            auth = auths.createAuth("John Doe");
            auths.getAuth(auth.authToken());
            auths.deleteAuth(auth.authToken());
        } catch (DataAccessException e) {
            Assertions.fail();
            return;
        }
        try {
            auths.getAuth(auth.authToken());
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Delete Auth - negative")
    public void DeleteAuthNeg(){
        AuthData auth;
        try {
            users.createUser(new UserData("John Doe", "1234", "mail@mail.yep"));
            auth = auths.createAuth("John Doe");
            auths.getAuth(auth.authToken());
        } catch (DataAccessException e) {
            Assertions.fail();
            return;
        }
        try {
            auths.deleteAuth("authTOakenTHAthISeal");
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Clear Game")
    public void ClearGame(){
        try {
            // make Game, try and clear
            games.createGame(new GameData(1, null, null,"ThisGame", new ChessGame()));
            games.getGame(1);
            games.clear();
        } catch (DataAccessException e) {
            Assertions.fail();
            return;
        }
        try {
            // Getting game should now fail
            games.getGame(1);
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Create Game - positive")
    public void CreateGamePos(){
        try {
            // Create a new game
            games.createGame(new GameData(1, null, null,"ThisGame", new ChessGame()));
            games.getGame(1);
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Create Game - negative")
    public void CreateGameNeg(){
        try {
            // Try to create a game with the same ID
            games.createGame(new GameData(1, null, null,"GameName", new ChessGame()));
            games.createGame(new GameData(1, null, null,"GameName2", new ChessGame()));
            var game = games.getGame(1);
            Assertions.assertNotEquals("GameName2", game.gameName());
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Get Game - positive")
    public void GetGamePos(){
        try {
            // Create a new game
            games.createGame(new GameData(1, null, null,"ThisGame", new ChessGame()));
            var game = games.getGame(1);
            Assertions.assertEquals("ThisGame", game.gameName());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Get Game - negative")
    public void GetGameNeg(){
        try {
            // Create a new game
            games.createGame(new GameData(1, null, null,"ThisGame", new ChessGame()));
            // Get game that don't exist (should throw exception)
            games.getGame(420);
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Update Game - positive")
    public void UpdateGamePos(){
        try {
            // Create a new game
            games.createGame(new GameData(1, null, null,"ThisGame", new ChessGame()));
            var game = games.getGame(1);
            Assertions.assertEquals("ThisGame", game.gameName());
            //Change name of the game
            games.updateGame(1, new GameData(1, null, null,"ThatGame", new ChessGame()));
            game = games.getGame(1);
            Assertions.assertEquals("ThatGame", game.gameName());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Update Game - negative")
    public void UpdateGameNeg(){
        try {
            // Update game that don't exist (should throw exception)
            games.updateGame(420, new GameData(1, null, null,"ThatGame", new ChessGame()));
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("List Games - positive")
    public void ListGamePos(){
        var game1 = new GameData(1, "player1", null,"ThisGame", new ChessGame());
        var game2 = new GameData(2, null, "PlayerYay","ThatGame", new ChessGame());
        ArrayList<GameData> gamesList = new ArrayList<>();
        gamesList.add(game1);
        gamesList.add(game2);
        // Create a new games
        games.createGame(game1);
        games.createGame(game2);
        // check list is correct
        var gamesListDAO = games.listGames();
        for(int i = 0; i < gamesList.size(); i++){
            Assertions.assertEquals(gamesList.get(i).gameID(), gamesListDAO.get(i).gameID());
            Assertions.assertEquals(gamesList.get(i).whiteUsername(), gamesListDAO.get(i).whiteUsername());
            Assertions.assertEquals(gamesList.get(i).gameName(), gamesListDAO.get(i).gameName());
        }
    }

    @Test
    @DisplayName("List Games - negative")
    public void ListGameNeg(){
        // Um? what is a negative case for the list games? it throws no exception, takes no data.
        // I'll just have it catch a different error
        var game1 = new GameData(1, "player1", null,"ThisGame", new ChessGame());
        var game2 = new GameData(1, null, "PlayerYay","ThatGame", new ChessGame());
        ArrayList<GameData> gamesList = new ArrayList<>();
        gamesList.add(game1);
        gamesList.add(game2);
        // Create a new games
        games.createGame(game1);
        games.createGame(game2);
        // check if list is say, missing something correct
        var gamesListDAO = games.listGames();
        Assertions.assertNotEquals(gamesList.size(), gamesListDAO.size());
    }
}
