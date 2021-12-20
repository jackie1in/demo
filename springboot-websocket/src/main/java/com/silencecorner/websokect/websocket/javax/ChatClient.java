package com.silencecorner.websokect.websocket.javax;

import com.silencecorner.websokect.model.Message;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
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
public class ChatClient{
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

  private LinkedBlockingQueue<Message> waitForSend = new LinkedBlockingQueue<>();
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
    logger.error("client on error",throwable);
  }

  /**
   * 连接关闭调用
   */
  @OnClose
  public void onClose(CloseReason reason) throws IOException {
    this.session.close();
    logger.info("Closing a WebSocket due to {}",reason.getReasonPhrase());
    connect();
  }


  /**
   * 收到服务端消息调用
   */
  @OnMessage
  public void onMessage(Message message) throws Exception {
    logger.info("client on message: " + message);
    Thread.sleep(2000);
    message.setTo("all");
    message.setContent("😀" + integer.getAndIncrement());
    sendMessage(message);
  }

  /**
   * 向服务端发送消息
   */
  public void sendMessage(Message message) throws IOException, EncodeException {
      waitForSend.offer(message);
  }

  public static void main(String[] args) {
    // CountDownLatch latch = new CountDownLatch(1);
    Scanner scanner = new Scanner(System.in);
    try {
      //后面的PathParam中文不行，只能使用英文字符
      ChatClient chatClient = new ChatClient("ws://localhost:9000/chat/hi" + new Random().nextInt());
      chatClient.connect();
      chatClient.sendMessage(new Message("java client","java server","first send msg!"));
      new Thread(() -> {
        while (true){
          try {
              Message message = chatClient.getWaitForSend().take();
              if (chatClient.getSession() != null) {
                chatClient.getSession().getBasicRemote().sendObject(message);
                System.out.printf("send msg: %s \n",message.toString());
              }
          } catch (IOException | EncodeException | InterruptedException e) {
            e.printStackTrace();
          }
        }
      }).start();
      while (scanner.hasNextLine()){
        chatClient.sendMessage(new Message("","all",scanner.nextLine()));
      }
      // Runtime.getRuntime().addShutdownHook(new Thread(latch::countDown));
      // latch.await();
    }catch (Exception e){
      // ignore
      e.printStackTrace();
    } finally {
      scanner.close();
    }
  }

  public Session getSession() {
    return session;
  }

  public LinkedBlockingQueue<Message> getWaitForSend() {
    return waitForSend;
  }
}
