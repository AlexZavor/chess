package ui;

import model.GameData;
import request.*;
import response.*;
import serverFacade.ServerFacade;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class LoggedInUI {

    private final String username;
    private final String authToken;
    private final ArrayList<GameData> games = new ArrayList<>();

    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final Scanner scanner = new Scanner(System.in);
    private static final ServerFacade server = new ServerFacade();

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
                    server.logout(new LogoutRequest(authToken));
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

        CreateGameResponse response = server.createGame(new CreateGameRequest(gameName), authToken);

        if(response.code() == 200){
            out.println(SET_TEXT_COLOR_BLUE + "Made game - " + gameName);
        }else{
            out.println(SET_TEXT_COLOR_RED + "Failed to Create Game");
            printOptions();
        }
    }

    private void listGames(){
        ListGamesResponse response = server.listGames(new ListGamesRequest(authToken));
        if(response.code() == 200) {
            List<GameData> games = response.games();
            int i = 0;
            this.games.clear();
            for (var game : games) {
                out.println(i + " - " + game.gameName() +
                        " : white - " + game.whiteUsername() +
                        " | black - " + game.blackUsername());
                this.games.add(game);
                i++;
            }
        } else {
            out.println(SET_TEXT_COLOR_RED + response.message());
        }
    }

    private void joinGame(){
        var game = getGame();

        String player;
        while (true){
            String options;
            if(game.whiteUsername() == null && game.blackUsername() == null){
                options = "[WHITE|BLACK]";
            }else if (game.whiteUsername() == null){
                options = "[WHITE]";
            }else if (game.blackUsername() == null){
                options = "[BLACK]";
            }else{
                out.println(SET_TEXT_COLOR_RED + "Game Already full");
                break;
            }
            player = getString("What position? " + options);
            if((player.equalsIgnoreCase("white") && game.whiteUsername() == null) ||
                (player.equalsIgnoreCase("black") && game.blackUsername() == null)){
                server.joinGame(new JoinGameRequest(player.toUpperCase(), game.gameID()), authToken);
                startGame(game);
                break;
            } else {
                out.println(SET_TEXT_COLOR_RED + "Please enter valid selection " + options);
            }
        }

    }

    private void joinObserver(){
        var game = getGame();
        server.joinGame(new JoinGameRequest("", game.gameID()), authToken);
        startGame(game);
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

    private GameData getGame(){
        listGames();
        out.println(SET_TEXT_COLOR_BLUE + "Enter number of Game to join");
        int gameIndex;
        while(true){
            gameIndex = getInput();
            if(gameIndex < games.size()){
                break;
            } else {
                out.println(SET_TEXT_COLOR_RED + "Please enter valid game number");
            }
        }
        return games.get(gameIndex);
    }

    private void startGame(GameData game){
        GameUI gameUI = new GameUI(game);
        gameUI.run();
        printHeader();
        printOptions();
    }
}
