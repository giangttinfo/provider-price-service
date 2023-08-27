package vn.sparkminds.provider.service.impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import vn.sparkminds.provider.config.ProviderConfiguration;
import vn.sparkminds.provider.constant.Constants;
import vn.sparkminds.provider.handler.MessageHandler;
import vn.sparkminds.provider.service.WebSocketService;

// Import necessary packages

// Lombok annotation to generate logger field
@Slf4j
// Data annotation to generate getters, setters, equals, hashCode, and toString methods
@Data
// Spring service annotation to mark this class as a service component
@Service
public class WebSocketServiceImpl implements WebSocketService {

    // Injected instance of ProviderConfiguration for configuration properties
    private final ProviderConfiguration providerConfiguration;

    // Injected instance of WebSocketConnectionManager for managing WebSocket connections
    private final WebSocketConnectionManager binanceWssConnectionManagement;

    // Injected instance of MessageHandler to handle WebSocket messages
    private final MessageHandler messageHandler;

    // Constructor to initialize injected dependencies
    public WebSocketServiceImpl(@Lazy ProviderConfiguration providerConfiguration,
            @Qualifier(Constants.BINANCE_WSS_CONNECTION_MANAGER) WebSocketConnectionManager b2c2WssConnectionManagement,
            MessageHandler messageHandler) {
        this.providerConfiguration = providerConfiguration;
        this.binanceWssConnectionManagement = b2c2WssConnectionManagement;
        this.messageHandler = messageHandler;
    }

    // Method to start the WebSocket service
    @Override
    public boolean start() {
        if (!binanceWssConnectionManagement.isRunning()) {
            binanceWssConnectionManagement.start();
            log.info("Binance WSS started!!!");
            return true;
        }
        log.info("Binance WSS is running!!!");
        return false;
    }

    // Method to stop the WebSocket service
    @Override
    public boolean stop() {
        if (binanceWssConnectionManagement.isRunning()) {
            binanceWssConnectionManagement.stop();
            log.info("Binance WSS stopped!!!");
            return true;
        }
        log.info("Binance WSS is stopping!!!");
        return false;
    }

    // Method to start listening to a specific coin pair
    @Override
    public void startPair(String coinPair) {
        messageHandler.subscribe(coinPair);
    }
}
