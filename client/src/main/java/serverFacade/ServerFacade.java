package serverFacade;

import request.*;
import response.*;

import java.io.IOException;
import java.util.ArrayList;

public class ServerFacade {
    private static ClientCommunicator communicator;

    public ServerFacade(int port){
        communicator = new ClientCommunicator(port);
    }
    public ServerFacade(){}

    public RegisterResponse register(RegisterRequest request){
        try {
            return communicator.doRegister(request);
        } catch (IOException e) {
            System.out.println("IO Exception - " + e.getMessage());
            return new RegisterResponse(400,null,null,"ERR - IO Exception");
        }
    }
    public LoginResponse login(LoginRequest request){
        try {
            return communicator.doLogin(request);
        } catch (IOException e) {
            System.out.println("IO Exception - " + e.getMessage());
            return new LoginResponse(400,null,null,"ERR - IO Exception");
        }
    }
    public LogoutResponse logout(LogoutRequest request){
        try {
            return communicator.doLogout(request);
        } catch (IOException e) {
            System.out.println("IO Exception - " + e.getMessage());
            return new LogoutResponse(400,"ERR - IO Exception");
        }
    }
    public ListGamesResponse listGames(ListGamesRequest request){
        try {
            return communicator.doListGames(request);
        } catch (IOException e) {
            System.out.println("IO Exception - " + e.getMessage());
            return new ListGamesResponse(400,new ArrayList<>(),"ERR - IO Exception");
        }
    }
    public CreateGameResponse createGame(CreateGameRequest request, String authToken){
        try {
            return communicator.doCreateGame(request, authToken);
        } catch (IOException e) {
            System.out.println("IO Exception - " + e.getMessage());
            return new CreateGameResponse(400, 0,"ERR - IO Exception");
        }
    }
    public JoinGameResponse joinGame(JoinGameRequest request, String authToken){
        try {
            return communicator.doJoinGame(request, authToken);
        } catch (IOException e) {
            System.out.println("IO Exception - " + e.getMessage());
            return new JoinGameResponse(400,"ERR - IO Exception");
        }
    }
}
