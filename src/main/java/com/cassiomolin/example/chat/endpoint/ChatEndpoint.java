package com.cassiomolin.example.chat.endpoint;


import com.cassiomolin.example.chat.model.WebSocketMessage;
import com.cassiomolin.example.chat.model.payload.*;

import javax.enterprise.context.Dependent;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * WebSocket endpoint for the chat.
 *
 * @author cassiomolin
 */
@Dependent
@ServerEndpoint(
        value = "/chat",
        encoders = MessageEncoder.class,
        decoders = MessageDecoder.class,
        configurator = CdiAwareConfigurator.class)
public class ChatEndpoint {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        String username = session.getUserPrincipal().getName();
        broadcastUserConnected(username);
        broadcastAvailableUsers();
    }

    @OnMessage
    public void onMessage(Session session, WebSocketMessage message) {
        if (message.getPayload() instanceof TextMessageSentPayload) {
            TextMessageSentPayload payload = (TextMessageSentPayload) message.getPayload();
            broadcastTextMessage(session.getUserPrincipal().getName(), payload.getContent());
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        String username = session.getUserPrincipal().getName();
        broadcastUserDisconnected(username);
        broadcastAvailableUsers();
    }

    private void broadcastUserConnected(String username) {
        UserConnectedPayload payload = new UserConnectedPayload();
        payload.setUsername(username);
        broadcast(new WebSocketMessage(payload));
    }

    private void broadcastUserDisconnected(String username) {
        UserDisconnectedPayload payload = new UserDisconnectedPayload();
        payload.setUsername(username);
        broadcast(new WebSocketMessage(payload));
    }

    private void broadcastTextMessage(String username, String text) {
        TextMessageReceivedPayload payload = new TextMessageReceivedPayload();
        payload.setContent(text);
        payload.setUsername(username);
        broadcast(new WebSocketMessage(payload));
    }

    private void broadcastAvailableUsers() {

        Set<String> usernames = sessions.stream()
                .map(Session::getUserPrincipal)
                .map(Principal::getName)
                .distinct()
                .collect(Collectors.toSet());

        UsersAvailablePayload payload = new UsersAvailablePayload();
        payload.setUsernames(usernames);
        broadcast(new WebSocketMessage(payload));
    }

    private void broadcast(WebSocketMessage message) {
        synchronized (sessions) {
            sessions.forEach(session -> {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendObject(message);
                }
            });
        }
    }
}
