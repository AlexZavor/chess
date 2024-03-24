package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import serverFacade.ServerMessageObserver;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class GameUI implements ServerMessageObserver {
    GameData game;
    private final ChessGame.TeamColor playerTeam;
    private final boolean isObserver;
    private final String authToken;
    private final String username;
    static int BOARD_SIZE = 8;
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final Scanner scanner = new Scanner(System.in);

    GameUI(GameData game, String authToken, String username, ChessGame.TeamColor playerTeam, boolean isObserver){
        this.game = game;
        this.authToken = authToken;
        this.username = username;
        this.playerTeam = playerTeam;
        this.isObserver = isObserver;
    }

    public void run(){
        printHeader();
        printBoard(playerTeam);

        printOptions();
        boolean quit = false;
        while(!quit){
            switch (getInput()){
                case 1:
                    printHelp();
                    break;
                case 2:
                    out.println(SET_TEXT_COLOR_BLUE + "--Redraw Chess Board--");
                    printBoard(playerTeam);
                    printOptions();
                    break;
                case 3:
                    out.println(SET_TEXT_COLOR_BLUE + "Exiting game");
                    quit = true;
                    break;
                case 4:
                    out.println(SET_TEXT_COLOR_BLUE + "--Make Move--");
                    break;
                case 5:
                    out.println(SET_TEXT_COLOR_BLUE + "--Resign--");
                    break;
                case 6:
                    out.println(SET_TEXT_COLOR_BLUE + "--Highlight Legal Moves--");
                    break;
                default:
                    out.println(SET_TEXT_COLOR_RED + "Please Select from the options");
                    printOptions();
                    break;
            }
        }

    }


    // Menu draw methods

    private void printHeader(){
        out.print(ERASE_SCREEN);
        out.println(SET_TEXT_COLOR_BLUE + "-- " + game.gameName() + " --");
        out.println();
    }

    private void printOptions(){
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("    1 > Help");
        out.println("    2 > Redraw Board");
        out.println("    3 > Leave");
        if(game.game().getTeamTurn() != playerTeam){
            out.print(SET_TEXT_COLOR_LIGHT_GREY);
        }
        out.println("    4 > Make Move");
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("    5 > Resign");
        out.println("    6 > Highlight Legal Moves");
    }

    private void printHelp(){
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("Type the number of the option you would like.");
        out.println("    1 > Help - See this menu");
        out.println("    2 > Redraw Board - Redraw the game board on screen");
        out.println("    3 > Leave - Exit the game without resigning");
        out.println("    4 > Make Move - Make your move in the game");
        out.println("    5 > Resign - Resign from the game, ending the game and the opponent wins");
        out.println("    6 > Highlight Legal Moves - Highlights squares that a piece can move to");
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


    // White draw methods

    private void printWhiteBoard(){
        printWhiteHeader();
        for (int row = 0; row < BOARD_SIZE; row++) {
            printWhiteRow(row);
        }
        printWhiteHeader();
        out.println(RESET_BG_COLOR);
    }

    private void printWhiteHeader(){
        setHeader();
        out.print("    a  b  c  d  e  f  g  h    ");
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void printWhiteRow(int row){
        setHeader();
        out.print(" " + (BOARD_SIZE-row) + " ");

        int color = row%2;
        for (int col = 0; col < BOARD_SIZE; col++) {
            if(color == 0){
                setWhite();
                color = 1;
            }else{
                setBlack();
                color = 0;
            }
            ChessPiece piece = game.game().getBoard().getPiece(new ChessPosition(BOARD_SIZE-row,col+1));
            printPiece(piece);
        }


        setHeader();
        out.print(" " + (BOARD_SIZE-row) + " ");
        out.print(RESET_BG_COLOR);
        out.println();
    }


    // Black draw methods

    private void printBlackBoard(){
        printBlackHeader();
        for (int row = 0; row < BOARD_SIZE; row++) {
            printBlackRow(row);
        }
        printBlackHeader();
        out.println(RESET_BG_COLOR);
    }

    private void printBlackHeader(){
        setHeader();
        out.print("    h  g  f  e  d  c  b  a    ");
        out.print(RESET_BG_COLOR);
        out.println();
    }

    private void printBlackRow(int row){
        setHeader();
        out.print(" " + (row+1) + " ");

        int color = row%2;
        for (int col = 0; col < BOARD_SIZE; col++) {
            if(color == 0){
                setWhite();
                color = 1;
            }else{
                setBlack();
                color = 0;
            }
            ChessPiece piece = game.game().getBoard().getPiece(new ChessPosition(row+1,col+1));
            printPiece(piece);
        }


        setHeader();
        out.print(" " + (row+1) + " ");
        out.print(RESET_BG_COLOR);
        out.println();
    }


    // Draw help

    private void printBoard(ChessGame.TeamColor color){
        switch (color){
            case BLACK -> printBlackBoard();
            case WHITE -> printWhiteBoard();
        }
    }

    private void printPiece(ChessPiece piece){
        if(piece == null){
            out.print("   ");
            return;
        }
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            setPieceWhite();
        }else{
            setPieceBlack();
        }
        switch (piece.getPieceType()){
            case KING -> out.print(" K ");
            case PAWN -> out.print(" P ");
            case ROOK -> out.print(" R ");
            case QUEEN -> out.print(" Q ");
            case BISHOP -> out.print(" B ");
            case KNIGHT -> out.print(" N ");
        }
    }

    private void setHeader(){
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_WHITE);
    }
    private void setWhite(){
        out.print(SET_BG_COLOR_WHITE);
    }
    private void setBlack(){
        out.print(SET_BG_COLOR_BLACK);
    }
    private void setPieceWhite(){
        out.print(SET_TEXT_COLOR_RED);
    }
    private void setPieceBlack(){
        out.print(SET_TEXT_COLOR_BLUE);
    }

    // Web Socket functions

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case NOTIFICATION -> displayNotification(((Notification) message).getMessage());
            case ERROR -> displayError(((Error) message).getErrorMessage());
            case LOAD_GAME -> loadGame(((LoadGame) message).getGame());
        }
    }

    private void loadGame(GameData game) {
        this.game = game;
        printBoard(playerTeam);
        printOptions();
    }

    private void displayError(String errorMessage) {
        out.println(SET_TEXT_ITALIC + SET_TEXT_COLOR_RED + "Error: " + errorMessage);
        out.print(RESET_TEXT_ITALIC);
    }

    private void displayNotification(String message) {
        out.println(SET_TEXT_ITALIC + SET_TEXT_COLOR_BLUE + message);
        out.print(RESET_TEXT_ITALIC);
    }
}
