package com.silencecorner.websokect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hai
 * description websocket测试项目
 * email hilin2333@gmail.com
 * date 2018/12/16 8:11 PM
 */
@RestController
@SpringBootApplication
public class WebSocketApplication {
  public static void main(String[] args){
    SpringApplication.run(WebSocketApplication.class,args);
  }

  @GetMapping("/echo/{echo}")
  public String echo(@PathVariable String echo){
    return echo;
  }
}
