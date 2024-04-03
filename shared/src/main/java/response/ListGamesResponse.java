package response;

import model.GameData;

import java.util.ArrayList;

public record ListGamesResponse(int code, ArrayList<GameData> games, String message) {
}
