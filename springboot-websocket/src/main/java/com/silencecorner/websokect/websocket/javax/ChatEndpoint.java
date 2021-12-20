package com.silencecorner.websokect.websocket.javax;

import com.silencecorner.websokect.model.Message;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value = "/chat/{username}", decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatEndpoint {
    private Session session;
    private static final Set<ChatEndpoint> chatEndpoints = new HashSet<>();
    private static volatile ReentrantReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock();
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {

        this.session = session;
        addSession(this, username);

        Message message = new Message();
        message.setFrom(username);
        message.setTo("all");
        message.setContent("Connected!");
        broadcast(message);
    }

    @OnMessage
    public void onMessage(Session session, Message message) throws IOException, EncodeException {
        message.setFrom(users.get(session.getId()));
        System.out.printf("receive msg: %s \n",message.toString());
        broadcast(message);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        removeSession(this);
        Message message = new Message();
        message.setFrom(users.get(session.getId()));
        message.setTo("all");
        message.setContent("Disconnected!");
        broadcast(message);

    }

    private static void addSession(ChatEndpoint endpoint, String username) {
        READ_WRITE_LOCK.writeLock().lock();
        chatEndpoints.add(endpoint);
        users.put(endpoint.getSession().getId(), username);
        READ_WRITE_LOCK.writeLock().lock();
    }

    private static void removeSession(ChatEndpoint endpoint) {
        READ_WRITE_LOCK.writeLock().lock();
        chatEndpoints.remove(endpoint);
        users.remove(endpoint.getSession().getId());
        READ_WRITE_LOCK.writeLock().lock();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private void broadcast(Message message) throws IOException, EncodeException {
        READ_WRITE_LOCK.readLock().lock();
        chatEndpoints.forEach(endpoint -> {
            try {
                if (!endpoint.equals(this)) {
                    endpoint.session.getBasicRemote()
                            .sendObject(message);
                }
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
        READ_WRITE_LOCK.readLock().unlock();
    }

    public Session getSession() {
        return session;
    }
}
