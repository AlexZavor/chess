package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import request.*;
import serverFacade.*;
import webSocketMessages.serverMessages.*;
import webSocketMessages.serverMessages.Error;
import static ui.EscapeSequences.*;

public class GameUI  extends UI implements ServerMessageObserver {
    private final ServerFacade server;
    GameData game;
    private final ChessGame.TeamColor playerTeam;
    private final boolean isObserver;
    private final String authToken;
    private final String username;
    static int BOARD_SIZE = 8;

    GameUI(GameData game, String authToken, String username, ChessGame.TeamColor playerTeam, boolean isObserver){
        this.game = game;
        this.authToken = authToken;
        this.username = username;
        this.playerTeam = playerTeam;
        this.isObserver = isObserver;
        server = new ServerFacade(this);
        server.joinGameWS(new JoinGameRequest(playerTeam.toString(), game.gameID()), authToken, isObserver);
    }

    public void run(){
        boolean quit = false;
        while(!quit){
            switch (getInput(username)){
                case 1:
                    printHelp();
                    break;
                case 2:
                    printBoard(playerTeam);
                    printOptions();
                    break;
                case 3:
                    out.println(SET_TEXT_COLOR_BLUE + "Exiting game");
                    leave();
                    quit = true;
                    break;
                case 4:
                    out.println(SET_TEXT_COLOR_BLUE + "--Make Move--");
                    makeMove();
                    break;
                case 5:
                    out.println(SET_TEXT_COLOR_BLUE + "--Resign--");
                    resign();
                    break;
                case 6:
                    out.println(SET_TEXT_COLOR_BLUE + "--Highlight Legal Moves--");
                    highlightMoves();
                    break;
                default:
                    out.println(SET_TEXT_COLOR_RED + "Please Select from the options");
                    printOptions();
                    break;
            }
        }

    }

    private void leave() {
        server.leaveGame(authToken, game.gameID());
    }

    private void makeMove() {
        // First checks
        if(isObserver){
            out.println(SET_TEXT_COLOR_RED + "You can't move pieces as an observer.");
            return;
        }
        if((game.game().getTeamTurn() != playerTeam)){
            out.println(SET_TEXT_COLOR_RED + "It's not your turn.");
            return;
        }

        // Start position
        out.println(SET_TEXT_COLOR_BLUE + "Enter column as letter, enter row as number.");
        var column = getString("What piece? - column");
        int columnInt;
        var row = getString("What piece? - row");
        int rowInt;
        try{
            columnInt = charToInt(column);
            rowInt = Integer.parseInt(row);
            if(rowInt > 8){throw new NumberFormatException("Out of range");}
        } catch (NumberFormatException e) {
            out.println(SET_TEXT_COLOR_RED + "please enter as number or letter.");
            return;
        }
        var startPos = new ChessPosition(rowInt,columnInt);

        // Check piece
        var piece = game.game().getBoard().getPiece(startPos);
        if(piece == null){
            out.println(SET_TEXT_COLOR_RED + "There is no piece there.");
            return;
        }
        if(piece.getTeamColor() != playerTeam){
            out.println(SET_TEXT_COLOR_RED + "That is not your piece!");
            return;
        }

        // Get destination
        var destColumn = getString("Where to? - column");
        int destColumnInt;
        var destRow = getString("Where to? - row");
        int destRowInt;
        try{
            destColumnInt = charToInt(destColumn);
            destRowInt = Integer.parseInt(destRow);
        } catch (NumberFormatException e) {
            out.println(SET_TEXT_COLOR_RED + "please enter as number or letter.");
            return;
        }
        var destPos = new ChessPosition(destRowInt,destColumnInt);
        var move = new ChessMove(startPos,destPos,null);
        var validMoves = game.game().validMoves(startPos);
        if(!validMoves.contains(move)){
            out.println(SET_TEXT_COLOR_RED + "Not a valid move");
            return;
        }

        server.makeMove(authToken, game.gameID(), move);
    }

    private void resign() {
        if(!isObserver){
            server.resign(authToken, game.gameID());
        } else {
            out.println(SET_TEXT_COLOR_RED + "You can't resign as an observer!");
        }
    }

    private void highlightMoves() {
        printBoard(playerTeam);
        // TODO: set up how to highlight moves with drawing board
    }

    private int charToInt(String in) throws NumberFormatException{
        return switch (in.toLowerCase()) {
            case "a" -> 1;
            case "b" -> 2;
            case "c" -> 3;
            case "d" -> 4;
            case "e" -> 5;
            case "f" -> 6;
            case "g" -> 7;
            case "h" -> 8;
            default -> throw new NumberFormatException("Out of range");
        };
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
        if((game.game().getTeamTurn() != playerTeam) || isObserver){
            out.print(SET_TEXT_COLOR_LIGHT_GREY);
        }
        out.println("    4 > Make Move");
        out.print(SET_TEXT_COLOR_BLUE);
        if((game.game().getGameOver()) || isObserver){
            out.print(SET_TEXT_COLOR_LIGHT_GREY);
        }
        out.println("    5 > Resign");
        out.print(SET_TEXT_COLOR_BLUE);
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
        printHeader();
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
        out.print(SET_TEXT_COLOR_GREEN + "[User: " + username + "] > ");
    }

    private void displayError(String errorMessage) {
        out.println();
        out.println(SET_TEXT_ITALIC + SET_TEXT_COLOR_RED + "Error: " + errorMessage);
        out.print(RESET_TEXT_ITALIC);
        out.print(SET_TEXT_COLOR_GREEN + "[User: " + username + "] > ");
    }

    private void displayNotification(String message) {
        out.println();
        out.println(SET_TEXT_ITALIC + SET_TEXT_COLOR_BLUE + message);
        out.print(RESET_TEXT_ITALIC);
        out.print(SET_TEXT_COLOR_GREEN + "[User: " + username + "] > ");
    }
}
