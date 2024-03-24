package webSocketMessages.serverMessages;

public class Error extends ServerMessage{

    final String errorMessage;

    public Error(String message) {
        super(ServerMessage.ServerMessageType.NOTIFICATION);
        this.errorMessage = message;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
