package vn.sparkminds.provider.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.sparkminds.provider.service.WebSocketService;

@Configuration
public class InitializeConnection {

    @Bean
    public InitializingBean initialize(WebSocketService webSocketService) {
        return () -> {
            webSocketService.start();
        };
    }
}
