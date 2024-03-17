package clientTests;

import dataAccess.*;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import request.*;
import response.*;
import server.Server;
import serverFacade.ServerFacade;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static final AuthDAO auths = new SQLAuthDAO();
    private static final UserDAO users = new SQLUserDAO();
    private static final GameDAO games = new SQLGameDAO();

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear(){
        auths.clear();
        users.clear();
        games.clear();
    }


    @Test
    public void registerPositive() {
        // Add new user
        facade.register(new RegisterRequest("John Doe", "1234", "Email"));

        // Check if user exists correctly
        UserData user;
        try{
            user = users.getUser("John Doe");
        } catch (DataAccessException e) {
            Assertions.fail();
            return;
        }
        Assertions.assertEquals("John Doe", user.username());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Assertions.assertTrue(encoder.matches("1234", user.password()));
        Assertions.assertEquals("Email", user.email());
    }

    @Test
    public void registerNegative() {
        facade.register(new RegisterRequest("John Doe", "1234", "Email"));
        // Add user with same username
        RegisterResponse registerResponse = facade.register(new RegisterRequest("John Doe", "1456", "Jon@doe.mail"));

        // Check for fail response
        Assertions.assertEquals(403, registerResponse.code());

        // Check that user doesn't exist correctly
        try{
            UserData user = users.getUser("John Doe");
            Assertions.assertNotEquals("Jon@doe.mail", user.email());
        } catch (DataAccessException ignored) {}
    }

    @Test
    public void loginPositive() {
        facade.register(new RegisterRequest("John Doe", "1234", "Email"));

        LoginResponse response = facade.login(new LoginRequest("John Doe", "1234"));

        // Check to make sure auth is good
        try {
            auths.getAuth(response.authToken());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void loginNegative() {
        facade.register(new RegisterRequest("John Doe", "1234", "Email"));

        // Incorrect password
        LoginResponse response = facade.login(new LoginRequest("John Doe", "4321"));

        // Check to make sure auth does not exist
        try {
            auths.getAuth(response.authToken());
            Assertions.fail();
        } catch (DataAccessException ignored) {
        }
    }

    @Test
    public void logoutPositive() {
        facade.register(new RegisterRequest("John Doe", "1234", "Email"));

        LoginResponse response = facade.login(new LoginRequest("John Doe", "1234"));

        LogoutResponse logoutResponse = facade.logout(new LogoutRequest(response.authToken()));

        Assertions.assertEquals(200, logoutResponse.code());
        // Check to make sure auth is gone
        try {
            auths.getAuth(response.authToken());
            Assertions.fail();
        } catch (DataAccessException ignored) {
        }
    }

    @Test
    public void logoutNegative() {
        facade.register(new RegisterRequest("John Doe", "1234", "Email"));

        LoginResponse response = facade.login(new LoginRequest("John Doe", "1234"));

        // Incorrect auth token
        LogoutResponse logoutResponse = facade.logout(new LogoutRequest("12345"));

        Assertions.assertEquals(401, logoutResponse.code());
        // Check to make sure auth still does exist
        try {
            auths.getAuth(response.authToken());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }


    @Test
    public void createGamePositive(){
        // Add new user
        RegisterResponse registerResponse = facade.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        CreateGameResponse createGameResponse = facade.createGame(new CreateGameRequest("GameName"), registerResponse.authToken());

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
    public void createGameNegative(){
        users.clear();
        // Add new user
        RegisterResponse registerResponse = facade.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game - no game name
        CreateGameResponse createGameResponse = facade.createGame(new CreateGameRequest(null), registerResponse.authToken());

        // Check for any fail response
        Assertions.assertEquals(400, createGameResponse.code());

        // Check that game does not exist
        Assertions.assertEquals(new ListGamesResponse(200, new ArrayList<>(), null), facade.listGames(new ListGamesRequest(registerResponse.authToken())));
    }

    @Test
    public void listGamesPositive(){
        games.clear();
        // Add new user
        RegisterResponse registerResponse = facade.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        facade.createGame(new CreateGameRequest("GameName"), registerResponse.authToken());

        // List games
        ListGamesResponse listGamesResponse = facade.listGames(new ListGamesRequest(registerResponse.authToken()));

        // Check for any fail response
        Assertions.assertEquals(200, listGamesResponse.code());

        // Check if game list matches
        ArrayList<GameData> gameList = games.listGames();
        for (int i = 0; i < gameList.size(); i++){
            Assertions.assertEquals(gameList.get(i).gameID(), listGamesResponse.games().get(i).gameID());
            Assertions.assertEquals(gameList.get(i).whiteUsername(), listGamesResponse.games().get(i).whiteUsername());
            Assertions.assertEquals(gameList.get(i).blackUsername(), listGamesResponse.games().get(i).blackUsername());
            Assertions.assertEquals(gameList.get(i).gameName(), listGamesResponse.games().get(i).gameName());
        }
    }

    @Test
    public void listGamesNegative(){
        // Add new user
        RegisterResponse registerResponse = facade.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        facade.createGame(new CreateGameRequest("GameName"), registerResponse.authToken());

        // List games - unauthorized
        ListGamesResponse listGamesResponse = facade.listGames(new ListGamesRequest("abcdefghijklmno"));

        // Check for any fail response
        Assertions.assertEquals(401, listGamesResponse.code());
    }

    @Test
    public void joinGamePositive(){
        // Add new user
        RegisterResponse registerResponse = facade.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        CreateGameResponse createGameResponse = facade.createGame(new CreateGameRequest("GameName"), registerResponse.authToken());

        // Join that game
        JoinGameResponse joinGameResponse = facade.joinGame(new JoinGameRequest("WHITE", createGameResponse.gameID()), registerResponse.authToken());

        // Check for any fail response
        Assertions.assertEquals(200, joinGameResponse.code());

        // Check if game data matches
        try {
            GameData game = games.getGame(createGameResponse.gameID());
            Assertions.assertEquals("John Doe", game.whiteUsername());
        } catch (DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    public void joinGameNegative(){
        // Add new user
        RegisterResponse registerResponse = facade.register(new RegisterRequest("John Doe", "12345", "Jon@doe.mail"));

        // Create game
        CreateGameResponse createGameResponse = facade.createGame(new CreateGameRequest("GameName"), registerResponse.authToken());

        // Join that game - bad request
        JoinGameResponse joinGameResponse = facade.joinGame(new JoinGameRequest("why should I tell you?", createGameResponse.gameID()), registerResponse.authToken());

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

}
