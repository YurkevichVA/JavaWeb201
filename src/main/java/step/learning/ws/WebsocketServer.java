package step.learning.ws;

import com.google.inject.Inject;
import step.learning.dao.ChatDao;
import step.learning.dto.entities.ChatMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(
        value = "/chat", // url ws-сервера (ws://.../chat)
        configurator = WebsocketConfigurator.class
)
public class WebsocketServer {
    private final static Set<Session> sessions =
            Collections.synchronizedSet(
                    new HashSet<>()
            ) ;
    private final ChatDao chatDao;
    private final Object synchObject = new Object();

    @Inject
    public WebsocketServer(ChatDao chatDao) {
        this.chatDao = chatDao;
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig sec) {
        // chatDao.install();
        session.getUserProperties().put("user", sec.getUserProperties().get("user"));
        sessions.add(session);
    }
    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }
    @OnMessage
    public void onMessage(String message, Session session) {
        Thread addToDbThread = new Thread(() -> {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUser((String) session.getUserProperties().get("user"));
            chatMessage.setMessage(message);

            synchronized (synchObject) {
                chatDao.add(chatMessage);
            }

            sendToAll(chatMessage.getUser() + ": " + chatMessage.getMessage());
        });
        addToDbThread.start();
        try {
            addToDbThread.join();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        }
    }
    @OnError
    public void onError(Throwable ex, Session session) {
        System.err.println("onError " + ex.getMessage());
    }
    private void sendToAll(String message) {
        for(Session session: sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                System.err.println("sendToAll " + ex.getMessage());
            }
        }
    }
}
