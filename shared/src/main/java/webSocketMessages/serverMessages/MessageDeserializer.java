package webSocketMessages.serverMessages;

import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageDeserializer implements JsonDeserializer<ServerMessage> {
    @Override
    public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String typeString = jsonObject.get("serverMessageType").getAsString();
        ServerMessage.ServerMessageType serverMessageType = ServerMessage.ServerMessageType.valueOf(typeString);

        return switch(serverMessageType) {
            case LOAD_GAME -> context.deserialize(jsonElement, LoadGame.class);
            case NOTIFICATION -> context.deserialize(jsonElement, Notification.class);
            case ERROR -> context.deserialize(jsonElement, Error.class);
        };
    }
}
