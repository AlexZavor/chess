package serviceTests;

import dataAccess.*;
import org.junit.jupiter.api.*;
import request.*;
import response.*;
import service.*;
import model.*;

public class ServiceTests {

    AuthDAO auths = new MemoryAuthDAO();
    UserDAO users = new MemoryUserDAO();
    GameDAO games = new MemoryGameDAO();

    UserService userService = new UserService();

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
        // Add new user
        RegisterResponse response = userService.register(new RegisterRequest("John Doe", null, "Jon@doe.mail"));

        // Check for fail response
        if(response.code() == 200){
            Assertions.fail();
        }

        // Check if user exists correctly
        try{
            users.getUser("John Doe");
            Assertions.fail();
        } catch (DataAccessException ignored) {}
    }

}
