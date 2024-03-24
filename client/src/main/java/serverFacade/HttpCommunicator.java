package serverFacade;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import request.*;
import response.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpCommunicator {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static int port;

    HttpCommunicator(int port){
        HttpCommunicator.port = port;
    }

    public RegisterResponse doRegister(RegisterRequest request) throws IOException {
        return gson.fromJson(doPost("/user", "", gson.toJson(request)), RegisterResponse.class);
    }

    public LoginResponse doLogin(LoginRequest request) throws IOException {
        return gson.fromJson(doPost("/session", "", gson.toJson(request)), LoginResponse.class);
    }

    public LogoutResponse doLogout(LogoutRequest request) throws IOException {
        return gson.fromJson(doDelete(request.authToken()), LogoutResponse.class);
    }

    public ListGamesResponse doListGames(ListGamesRequest request) throws IOException {
        return gson.fromJson(doGet(request.authToken()), ListGamesResponse.class);
    }

    public CreateGameResponse doCreateGame(CreateGameRequest request, String authToken) throws IOException {
        return gson.fromJson(doPost("/game", authToken, gson.toJson(request)), CreateGameResponse.class);
    }

    public JoinGameResponse doJoinGame(JoinGameRequest request, String authToken) throws IOException {
        return gson.fromJson(doPut(authToken, gson.toJson(request)), JoinGameResponse.class);
    }

    private String doGet(String authToken) throws IOException {
        HttpURLConnection connection = getConnection("/game", authToken);
        connection.setRequestMethod("GET");

        connection.connect();

        StringBuilder stringBuilder = getResponse(connection);
        return stringBuilder.toString();
    }

    private String doPost(String URLPath, String authToken, String body) throws IOException {
        HttpURLConnection connection = getConnection(URLPath, authToken);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.connect();

        try(OutputStream requestBody = connection.getOutputStream()) {
            // Write request body to OutputStream ...
            requestBody.write(body.getBytes());
        }
        StringBuilder stringBuilder = getResponse(connection);
        return stringBuilder.toString();
    }

    private String doPut(String authToken, String body) throws IOException {
        HttpURLConnection connection = getConnection("/game", authToken);
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        connection.connect();

        try(OutputStream requestBody = connection.getOutputStream()) {
            // Write request body to OutputStream ...
            requestBody.write(body.getBytes());
        }
        StringBuilder stringBuilder = getResponse(connection);
        return stringBuilder.toString();
    }

    private String doDelete(String authToken) throws IOException {
        HttpURLConnection connection = getConnection("/session", authToken);
        connection.setRequestMethod("DELETE");
        connection.setDoOutput(true);

        connection.connect();

        StringBuilder stringBuilder = getResponse(connection);
        return stringBuilder.toString();
    }

    private static HttpURLConnection getConnection(String URLPath, String authToken) throws IOException {
        URL url = new URL("http://localhost:" + port + URLPath);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);

        // Set HTTP request headers
        connection.addRequestProperty("authorization", authToken);

        return connection;
    }

    private static StringBuilder getResponse(HttpURLConnection connection) throws IOException {
        InputStream responseBody;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            responseBody = connection.getInputStream();
            // Read response body from InputStream ...
        }
        else {
            // SERVER RETURNED AN HTTP ERROR
            responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
        }
        byte[] bytes = responseBody.readAllBytes();
        StringBuilder stringBuilder = new StringBuilder();
        for (byte aByte : bytes) {
            stringBuilder.append((char) aByte);
        }
        return stringBuilder;
    }
}
