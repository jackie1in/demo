package com.silencecorner.websokect.websocket.javax;

import com.silencecorner.websokect.model.Message;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hai
 * description 客户端代码
 * email hilin2333@gmail.com
 * date 2018/12/16 8:51 PM
 */
@ClientEndpoint(decoders = MessageDecoder.class, encoders = MessageEncoder.class)
public class ChatClient {
  AtomicInteger integer = new AtomicInteger(0);
  // 日志记录
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  // 服务端的session
  private Session session;

  private String url;

  private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

  public ChatClient(String url) {
    this.url = url;
  }

  /**
   * 连接到服务端
   */
  public void connect(){
    try {
      executorService.scheduleAtFixedRate(() -> {
        if (session == null || !session.isOpen()){
          try {
            logger.info("client is connecting");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, new URI(url));
          }catch (Exception e){
            logger.info("connection fail");
          }
        }else{
          executorService.shutdown();
          executorService = Executors.newSingleThreadScheduledExecutor();
        }
      },0,5, TimeUnit.SECONDS);

    }catch (Exception e){
      //ignore
    }
  }

  /**
   * 连接成功调用
   */
  @OnOpen
  public void onOpen(Session session) {
    this.session = session;
    logger.info("Client on open");
  }

  /**
   * 发生错误调用
   */
  @OnError
  public void onError(Throwable throwable) throws IOException {
    this.session.close();
    logger.error("client on error");
    throwable.printStackTrace();
  }

  /**
   * 连接关闭调用
   */
  @OnClose
  public void onClose() throws IOException {
    this.session.close();
    logger.info("client on close");
    connect();
  }


  /**
   * 收到服务端消息调用
   */
  @OnMessage
  public void onMessage(Message message) throws Exception {
    logger.info("client on message: " + message);
    Thread.sleep(2000);
    message.setTo("server" + integer.getAndAdd(1));
    message.setContent("😀" + integer.get());
    sendMessage(message);
  }

  /**
   * 向服务端发送消息
   */
  public void sendMessage(Message message) throws IOException, EncodeException {
    session.getBasicRemote().sendObject(message);
  }

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    try {
      //后面的PathParam中文不行，只能使用英文字符
      ChatClient chatClient = new ChatClient("ws://localhost:9000/chat/hi");
      chatClient.connect();
      chatClient.sendMessage(new Message("java client","java server","first send msg!"));
    }catch (Exception e){

    }finally {
      latch.await();
    }
  }
}
