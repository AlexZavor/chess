package ui;

import chess.ChessGame;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class LoggedInUI {

    private final String username;
    private final String authToken;
    private final ArrayList<Integer> gameIDs = new ArrayList<>();

    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final Scanner scanner = new Scanner(System.in);

    LoggedInUI(String username, String authToken){
        this.username = username;
        this.authToken = authToken;
    }

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
                    out.println(SET_TEXT_COLOR_BLUE + "Logging out " + username);
                    quit = true;
                    break;
                case 3:
                    out.println(SET_TEXT_COLOR_BLUE + "--Create Game--");
                    createGame();
                    break;
                case 4:
                    out.println(SET_TEXT_COLOR_BLUE + "--Games--");
                    listGames();
                    break;
                case 5:
                    out.println(SET_TEXT_COLOR_BLUE + "--Join Game--");
                    joinGame();
                    break;
                case 6:
                    out.println(SET_TEXT_COLOR_BLUE + "--Join as Observer--");
                    joinObserver();
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
        out.print(SET_TEXT_COLOR_RED + "|" +
                SET_TEXT_COLOR_WHITE + " Logged in as - " + username +
                SET_TEXT_COLOR_RED + " |");
        out.println();
    }

    private void printOptions(){
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("    1 > Help");
        out.println("    2 > Logout");
        out.println("    3 > Create Game");
        out.println("    4 > List Games");
        out.println("    5 > Join Game");
        out.println("    6 > Join as Observer");
    }

    private void printHelp(){
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("Type the number of the option you would like.");
        out.println("    1 > Help - See this menu");
        out.println("    2 > Logout - Logout " + username);
        out.println("    3 > Create Game - Create a new chess game");
        out.println("    4 > List Games - List current chess games");
        out.println("    5 > Join Game - Join an existing chess game");
        out.println("    6 > Join as Observer - Join an existing chess game as an observer");
    }

    private void createGame(){
        var gameName = getString("Game Name");
        out.println(SET_TEXT_COLOR_BLUE + "Made game - " + gameName);
        //TODO: actually make it request
    }

    private void listGames(){
        List<GameData> games = new ArrayList<>();
        games.add(new GameData(2, null, null, "Test Game", new ChessGame()));
        //TODO: actually make it request
        int i = 0;
        gameIDs.clear();
        for(var game : games){
            out.println(i + " - " + game.gameName() +
                        " : white player - " + game.whiteUsername() +
                        " black player - " + game.blackUsername());
            gameIDs.add(game.gameID());
        }
    }

    private void joinGame(){
        listGames();
        out.println(SET_TEXT_COLOR_BLUE + "Enter number of Game to join");
        int gameIndex;
        while(true){
            gameIndex = getInput();
            if(gameIndex < gameIDs.size()){
                break;
            } else {
                out.println(SET_TEXT_COLOR_RED + "Please enter valid game number");
            }
        }
        int gameID = gameIDs.get(gameIndex);
        GameData game = new GameData(2, "Test", null, "game", new ChessGame());
        //TODO: actually make it request

        String player;
        while (true){
            player = getString("What position? [WHITE|BLACK]");
            if((player.equals("WHITE") && game.whiteUsername().isEmpty()) ||
                (player.equals("BLACK") && game.blackUsername().isEmpty())){
                break;
            } else {
                out.println(SET_TEXT_COLOR_RED + "Please enter valid selection [WHITE|BLACK]");
            }
        }

        //startGame(game);
    }

    private void joinObserver(){
        listGames();
        out.println(SET_TEXT_COLOR_BLUE + "Enter number of Game to join");
        int gameIndex;
        while(true){
            gameIndex = getInput();
            if(gameIndex < gameIDs.size()-1){
                break;
            } else {
                out.println(SET_TEXT_COLOR_RED + "Please enter valid game number");
            }
        }
        int gameID = gameIDs.get(gameIndex);
        ChessGame game = new ChessGame();
        //TODO: actually make it request
        //startGame(game);
    }

    private int getInput(){
        while (true){
            out.print(SET_TEXT_COLOR_GREEN + "[User: " + username + "] > ");
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

}
