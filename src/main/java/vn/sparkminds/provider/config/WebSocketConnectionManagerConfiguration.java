package vn.sparkminds.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import vn.sparkminds.provider.constant.Constants;
import vn.sparkminds.provider.handler.ProviderWebSocketHandler;

@Component
public class WebSocketConnectionManagerConfiguration {

    private final ProviderConfiguration providerConfiguration;

    private final ProviderWebSocketHandler providerWebSocketHandler;

    public WebSocketConnectionManagerConfiguration(ProviderConfiguration providerConfiguration,
            @Lazy ProviderWebSocketHandler providerWebSocketHandler) {
        this.providerConfiguration = providerConfiguration;
        this.providerWebSocketHandler = providerWebSocketHandler;
    }

    @Bean(name = Constants.BINANCE_WSS_CONNECTION_MANAGER)
    public WebSocketConnectionManager createWssConnection() {
        final WebSocketClient client = new StandardWebSocketClient();
        var manager = new WebSocketConnectionManager(client, providerWebSocketHandler, providerConfiguration.getWssUrl());
        manager.setAutoStartup(false);
        return manager;
    }

}
