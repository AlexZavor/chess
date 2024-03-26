package server;

import org.eclipse.jetty.websocket.api.Session;

public record WSUser(String authToken, Session session) {
}
