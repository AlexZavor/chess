package handler;

import response.ClearResponse;
import spark.*;
import service.*;

public class DatabaseHandler extends Handler{
    public String handleClear(Response response) {
        ClearResponse serviceResponse = (new DatabaseService()).clear();
        response.status(serviceResponse.code());
        response.body(gson.toJson( serviceResponse ));
        return "{}";
    }
}
