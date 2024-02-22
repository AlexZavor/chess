package handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.*;

public class Handler {

    protected static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    protected String getAuthToken(Request request){
        return request.headers("authorization");
    }
}
