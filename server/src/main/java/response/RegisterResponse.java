package response;

public record RegisterResponse(int code, String username, String authToken, String message) {
}
