package serverFacade;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import request.*;
import response.*;

public class ClientCommunicator {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static int port;

    ClientCommunicator(int port){
        ClientCommunicator.port = port;
    }

    public RegisterResponse doRegister(RegisterRequest request) throws IOException {
        return gson.fromJson(doPush("/user", "", gson.toJson(request)), RegisterResponse.class);
    }

    public LoginResponse doLogin(LoginRequest request) throws IOException {
        return gson.fromJson(doPush("/session", "", gson.toJson(request)), LoginResponse.class);
    }

    public void doGet(String urlString) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        // Set HTTP request headers, if necessary
        // connection.addRequestProperty("Accept", "text/html");
        // connection.addRequestProperty("Authorization", "fjaklc8sdfjklakl");

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            // Get HTTP response headers, if necessary
            // Map<String, List<String>> headers = connection.getHeaderFields();

            // OR

            //connection.getHeaderField("Content-Length");

            InputStream responseBody = connection.getInputStream();
            // Read and process response body from InputStream ...
        } else {
            // SERVER RETURNED AN HTTP ERROR

            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
        }
    }

    private String doPush(String URLPath, String authToken, String body) throws IOException {
        URL url = new URL("http://localhost:" + port + URLPath);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // Set HTTP request headers
        connection.addRequestProperty("authorization", authToken);

        connection.connect();

        try(OutputStream requestBody = connection.getOutputStream()) {
            // Write request body to OutputStream ...
            requestBody.write(body.getBytes());
        }
        StringBuilder stringBuilder = getResponse(connection);
        return stringBuilder.toString();
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
