package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class UI {

    protected final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    protected final Scanner scanner = new Scanner(System.in);


    protected int getInput(String username){
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
    protected int getInput(){
        while (true){
            out.print(SET_TEXT_COLOR_GREEN + ">> ");
            int value;
            try{
                value = Integer.parseInt(scanner.nextLine());
                return value;
            }catch (NumberFormatException e){
                out.println(SET_TEXT_COLOR_RED + "Please enter the number representing your choice.");
            }
        }
    }

    protected String getString(String request){
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


    protected void setHeader(){
        out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_WHITE);
    }
    protected void setWhite(){
        out.print(SET_BG_COLOR_WHITE);
    }
    protected void setHighlightedWhite(){
        out.print(SET_BG_COLOR_GREEN);
    }
    protected void setBlack(){
        out.print(SET_BG_COLOR_BLACK);
    }
    protected void setHighlightedBlack(){
        out.print(SET_BG_COLOR_DARK_GREEN);
    }
    protected void setGold(){
        out.print(SET_BG_COLOR_YELLOW);
    }
    protected void setPieceWhite(){
        out.print(SET_TEXT_COLOR_RED);
    }
    protected void setPieceBlack(){
        out.print(SET_TEXT_COLOR_BLUE);
    }
}
