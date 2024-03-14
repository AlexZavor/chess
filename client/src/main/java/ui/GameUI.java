package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class GameUI {
    GameData game;
    static int BOARD_SIZE = 8;
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final Scanner scanner = new Scanner(System.in);

    GameUI(GameData game){
        this.game = game;
    }

    public void run(){
        //Draw board from White player
        printWhiteBoard();
        out.println();
        //Draw board from Black player
        printBlackBoard();
    }

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
        out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_WHITE);
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
}
