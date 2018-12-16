package com.silencecorner.websokect.websocket.javax;

import com.silencecorner.websokect.model.Message;
import java.io.IOException;
import javax.websocket.EncodeException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author hai
 * description 使用传统的方式
 * email hilin2333@gmail.com
 * date 2018/12/16 8:16 PM
 */
@Configuration
@EnableWebSocket
public class JavaxAutoConfiguration {
  @Bean
  public ChatEndpoint chatEndpoint() {
    return new ChatEndpoint();
  }

  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
  }
}
