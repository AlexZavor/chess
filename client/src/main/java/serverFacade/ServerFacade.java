package serverFacade;

import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResponse;
import response.RegisterResponse;

import java.io.IOException;

public class ServerFacade {
    private static ClientCommunicator communicator;

    public ServerFacade(int port){
        communicator = new ClientCommunicator(port);
    }

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
}
