package ui;

import request.*;
import response.*;
import serverFacade.ServerFacade;
import static ui.EscapeSequences.*;

public class PreLoginUI extends UI{

    private ServerFacade server;

    public void run(int port){
        server = new ServerFacade(port);
        out.print(RESET_BG_COLOR);
        printHeader();
        printOptions();
        boolean quit = false;
        while(!quit){
            switch (getInput()){
                case 1:
                    printHelp();
                    break;
                case 2:
                    out.println(SET_TEXT_COLOR_BLUE + "Goodbye!");
                    quit = true;
                    break;
                case 3:
                    out.println(SET_TEXT_COLOR_BLUE + "--Login--");
                    login();
                    break;
                case 4:
                    out.println(SET_TEXT_COLOR_BLUE + "--Register--");
                    register();
                    break;
                default:
                    out.println(SET_TEXT_COLOR_RED + "Please Select from the options");
                    printOptions();
                    break;
            }
        }
    }

    private void printHeader(){
        out.print(ERASE_SCREEN);
        out.print(SET_TEXT_COLOR_RED + "♕" +
                SET_TEXT_COLOR_WHITE + " Welcome to CS 240 Chess Client " +
                SET_TEXT_COLOR_RED + "♕");
        out.println();
    }

    private void printOptions(){
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("    1 > Help");
        out.println("    2 > Quit");
        out.println("    3 > Login");
        out.println("    4 > Register");
    }

    private void printHelp(){
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("Type the number of the option you would like.");
        out.println("    1 > Help     - See this menu");
        out.println("    2 > Quit     - Leave 240 Chess");
        out.println("    3 > Login    - Log into existing account");
        out.println("    4 > Register - Create new account");
    }

    private void login(){
        var username = getString("Username");
        var password = getString("Password");
        LoginResponse response = server.login(new LoginRequest(username,password));

        if(response.code() == 200){
            var authToken = response.authToken();
            runLoggedIn(username, authToken);
        }else{
            out.println(SET_TEXT_COLOR_RED + "Failed to Login");
            printOptions();
        }
    }

    private void register(){
        var username = getString("Username");
        var password = getString("Password");
        var email = getString("E-mail");
        RegisterResponse response = server.register(new RegisterRequest(username,password,email));

        if(response.code() == 200){
            var authToken = response.authToken();
            runLoggedIn(username, authToken);
        }else if(response.code() == 403){
            out.println(SET_TEXT_COLOR_RED + "Username Already Taken");
        }else{
            out.println(SET_TEXT_COLOR_RED + "Failed to Register user");
        }
    }

    private void runLoggedIn(String username, String authToken){
        // make logged in ui with variables
        LoggedInUI loggedInUI = new LoggedInUI(username, authToken);
        loggedInUI.run();
        printHeader();
        printOptions();
    }
}
