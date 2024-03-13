package ui;

import model.GameData;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GameUI {
    GameData game;
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    private final Scanner scanner = new Scanner(System.in);

    GameUI(GameData game){
        this.game = game;
    }

    public void run(){

    }

}
