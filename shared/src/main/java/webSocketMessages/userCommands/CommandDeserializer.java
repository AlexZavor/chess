package webSocketMessages.userCommands;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class CommandDeserializer implements JsonDeserializer<UserGameCommand> {
    @Override
    public UserGameCommand deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String typeString = jsonObject.get("commandType").getAsString();
        UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(typeString);

        return switch(commandType) {
            case JOIN_PLAYER -> context.deserialize(jsonElement, JoinPlayer.class);
            case JOIN_OBSERVER -> context.deserialize(jsonElement, JoinObserver.class);
            case MAKE_MOVE -> context.deserialize(jsonElement, MakeMove.class);
            case LEAVE -> context.deserialize(jsonElement, Leave.class);
            case RESIGN -> context.deserialize(jsonElement, Resign.class);
        };
    }
}
