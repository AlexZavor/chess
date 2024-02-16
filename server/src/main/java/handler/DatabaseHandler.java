package handler;

import com.google.gson.*;
import responce.ClearResponse;
import spark.*;
import service.*;

public class DatabaseHandler {
    public String handleClear(Request request, Response response) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ClearResponse serviceResponse = (new DatabaseService()).clear();
        response.status(serviceResponse.code());
        return "{}";
    }
}
