package responce;

public record LoginResponse(int code, String username, String authToken, String message) {
}
