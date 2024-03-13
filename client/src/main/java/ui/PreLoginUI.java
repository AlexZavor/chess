package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PreLoginUI {

    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final Scanner scanner = new Scanner(System.in);

    public void run(){
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
        //TODO: actually make it request
        var authToken = "test";
        runLoggedIn(username, authToken);
    }

    private void register(){
        var username = getString("Username");
        var password = getString("Password");
        var email = getString("E-mail");
        //TODO: actually make it request
        var authToken = "test";
        runLoggedIn(username, authToken);
    }

    private int getInput(){
        while (true){
            out.print(SET_TEXT_COLOR_GREEN + ">> ");
//        out.print(scanner.nextLine());
            int value;
            try{
                value = Integer.parseInt(scanner.nextLine());
                return value;
            }catch (NumberFormatException e){
                out.println(SET_TEXT_COLOR_RED + "Please enter the number representing your choice.");
            }
        }
    }

    private String getString(String request){
        while(true){
            out.print(SET_TEXT_COLOR_GREEN + request + " > ");
            String data = scanner.nextLine();
            if(data.isEmpty()){
                out.println(SET_TEXT_COLOR_RED + "Please type a valid " + request);
            }else{
                return data;
            }
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
