package handler;

import response.ClearResponse;
import spark.*;
import service.*;

public class DatabaseHandler {
    public String handleClear(Response response) {
        ClearResponse serviceResponse = (new DatabaseService()).clear();
        response.status(serviceResponse.code());
        return "{}";
    }
}
