package webSocketMessages.serverMessages;

public class Error extends ServerMessage{

    final String errorMessage;

    public Error(String message) {
        super(ServerMessageType.ERROR);
        this.errorMessage = message;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
