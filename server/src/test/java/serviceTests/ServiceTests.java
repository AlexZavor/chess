package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.*;
import request.*;
import response.*;
import service.*;
import model.*;

import java.util.ArrayList;

public class ServiceTests {

    AuthDAO auths = new MemoryAuthDAO();
    UserDAO users = new MemoryUserDAO();
    GameDAO games = new MemoryGameDAO();

    UserService userService = new UserService();
    GameService gameService = new GameService();
    DatabaseService databaseService = new DatabaseService();

    @BeforeEach
    public void setup() {
        auths.clear();
        users.clear();
        games.clear();
    }

    @Test
    @DisplayName("User Register - positive")
    public void RegisterNewUser(){
        // Add new user
        userService.register(new RegisterRequest("John Doe", "1234", "Jon@doe.mail"));

        // Check if user exists correctly
        UserData user;
        try{
            user = users.getUser("John Doe");
        } catch (DataAccessException e) {
            Assertions.fail();
            return;
        }
        Assertions.assertEquals("John Doe", user.username());
        Assertions.assertEquals("1234", user.password());
        Assertions.assertEquals("Jon@doe.mail", user.email());
    }

    @Test
    @DisplayName("User Register - negative")
    public void RegisterBadUser(){
        // Add new user - bad request
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", null, "Jon@doe.mail"));

        // Check for fail response
        Assertions.assertEquals(400, registerResponse.code());

        // Check if user exists correctly
        try{
            users.getUser("John Doe");
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

    @Test
    @DisplayName("Login User - Positive")
    public void LoginUser(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));
        // Logout
        userService.logout(new LogoutRequest(registerResponse.authToken()));
        // Login
        LoginResponse responseLogin = userService.login(new LoginRequest("John Doe", "12345"));

        // Check for fail response
        Assertions.assertEquals(200, responseLogin.code());

        // Check if user exists correctly
        try{
            auths.getAuth(responseLogin.authToken());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Login User - Negative")
    public void LoginBadUser(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));
        // Logout
        userService.logout(new LogoutRequest(registerResponse.authToken()));
        // Login - wrong password
        LoginResponse responseLogin = userService.login(new LoginRequest("John Doe", "54321"));

        // Check for fail response
        Assertions.assertEquals(401, responseLogin.code());

        // Check if user exists correctly
        try{
            auths.getAuth(responseLogin.authToken());
            Assertions.fail();
        } catch (DataAccessException ignored) {
        }
    }

    @Test
    @DisplayName("Logout User - Positive")
    public void LogoutUser(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));
        // Logout
        LogoutResponse responseLogout = userService.logout(new LogoutRequest(registerResponse.authToken()));

        // Check for any fail response
        Assertions.assertEquals(200, responseLogout.code());

        // Check that but auth token does not exist.
        try{
            auths.getAuth(registerResponse.authToken());
            Assertions.fail();
        } catch (DataAccessException ignored) {
        }
    }

    @Test
    @DisplayName("Logout User - Negative")
    public void LogoutBadUser(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));
        // Logout wrong auth token
        LogoutResponse responseLogout = userService.logout(new LogoutRequest("abcdefghijklmno"));

        // Check for any fail response
        Assertions.assertEquals(401, responseLogout.code());

        // Check that auth still exists correctly
        try{
            auths.getAuth(registerResponse.authToken());
        } catch (DataAccessException ignored) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Create Game - Positive")
    public void CreateNewGame(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest(registerResponse.authToken(), "GameName"));

        // Check for any fail response
        Assertions.assertEquals(200, createGameResponse.code());

        // Check if game exists correctly
        try{
            GameData game = games.getGame(createGameResponse.gameID());
            Assertions.assertEquals("GameName", game.gameName());
            Assertions.assertNull(game.blackUsername());
            Assertions.assertNull(game.whiteUsername());
            Assertions.assertEquals(createGameResponse.gameID(), game.gameID());
        } catch (DataAccessException ignored) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Create Game - Negative")
    public void CreateBadGame(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game - no game name
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest(registerResponse.authToken(), null));

        // Check for any fail response
        Assertions.assertEquals(400, createGameResponse.code());

        // Check that game does not exist
        Assertions.assertEquals(new ListGamesResponse(200, new ArrayList<>(), null), gameService.listGames(new ListGamesRequest(registerResponse.authToken())));
    }

    @Test
    @DisplayName("List Games - Positive")
    public void ListGames(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        gameService.createGame(new CreateGameRequest(registerResponse.authToken(), "GameName"));

        // List games
        ListGamesResponse listGamesResponse = gameService.listGames(new ListGamesRequest(registerResponse.authToken()));

        // Check for any fail response
        Assertions.assertEquals(200, listGamesResponse.code());

        // Check if game list matches
        ArrayList<GameData> gameList = games.listGames();
        Assertions.assertEquals(gameList, listGamesResponse.games());
    }

    @Test
    @DisplayName("List Games - Negative")
    public void ListBadGames(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        gameService.createGame(new CreateGameRequest(registerResponse.authToken(), "GameName"));

        // List games - unauthorized
        ListGamesResponse listGamesResponse = gameService.listGames(new ListGamesRequest("abcdefghijklmno"));

        // Check for any fail response
        Assertions.assertEquals(401, listGamesResponse.code());
    }

    @Test
    @DisplayName("Join Game - Positive")
    public void JoinGame(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest(registerResponse.authToken(), "GameName"));

        // Join that game
        JoinGameResponse joinGameResponse = gameService.joinGame(registerResponse.authToken(), new JoinGameRequest("WHITE", createGameResponse.gameID()));

        // Check for any fail response
        Assertions.assertEquals(200, joinGameResponse.code());

        // Check if game data matches
        try {
            GameData game = games.getGame(createGameResponse.gameID());
            Assertions.assertEquals(game.whiteUsername(), "John Doe");
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Join Game - Negative")
    public void JoinBadGame(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest(registerResponse.authToken(), "GameName"));

        // Join that game - bad request
        JoinGameResponse joinGameResponse = gameService.joinGame(registerResponse.authToken(), new JoinGameRequest("why should I tell you?", createGameResponse.gameID()));

        // Check for any fail response
        Assertions.assertEquals(400, joinGameResponse.code());

        // Check that game data matches
        try {
            GameData game = games.getGame(createGameResponse.gameID());
            Assertions.assertNull(game.whiteUsername());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    @DisplayName("Clear Database - Positive")
    public void ClearDatabase(){
        // Add new user
        RegisterResponse registerResponse = userService.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        CreateGameResponse createGameResponse = gameService.createGame(new CreateGameRequest(registerResponse.authToken(), "GameName"));

        // Join that game
        gameService.joinGame(registerResponse.authToken(), new JoinGameRequest("WHITE", createGameResponse.gameID()));

        // Check if game data matches
        try {
            GameData game = games.getGame(createGameResponse.gameID());
            Assertions.assertEquals(game.whiteUsername(), "John Doe");
            users.getUser("John Doe");
            auths.getAuth(registerResponse.authToken());
        } catch (DataAccessException e) {
            Assertions.fail();
        }

        // Clear database
        databaseService.clear();

        // Test if data is still there
        try {
            games.getGame(createGameResponse.gameID());
            Assertions.fail();
        } catch (DataAccessException ignored) {
        }
        try {
            users.getUser("John Doe");
            Assertions.fail();
        } catch (DataAccessException ignored) {
        }
        try {
            auths.getAuth(registerResponse.authToken());
            Assertions.fail();
        } catch (DataAccessException ignored) {
        }

    }
}
