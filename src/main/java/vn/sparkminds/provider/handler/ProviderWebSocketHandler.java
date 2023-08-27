package vn.sparkminds.provider.handler;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.sparkminds.provider.dto.enums.EventType;

// Import necessary Spring and utility packages

// Lombok annotation to generate logger field
@Slf4j
// Spring component annotation to mark this class as a Spring-managed component
@Component
// Lombok annotation to generate constructor with required arguments
@RequiredArgsConstructor
public class ProviderWebSocketHandler implements WebSocketHandler {

    // Injected instance of MessageHandler to handle WebSocket messages
    private final MessageHandler messageHandler;

    // Method called when a WebSocket connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        log.info("Binance-Socket: WebSocket Connection Established");
        // Check if WebSocket session is open
        if (webSocketSession != null && webSocketSession.isOpen()) {
            // Set size limits for text and binary messages
            webSocketSession.setTextMessageSizeLimit(327680);
            webSocketSession.setBinaryMessageSizeLimit(327680);
            
            // Set the WebSocket session in the MessageHandler
            messageHandler.setWebSocketSession(webSocketSession);
            
            // Initialize buy and sell maps with ConcurrentHashMap
            messageHandler.setBuyMap(new ConcurrentHashMap<>());
            messageHandler.setSellMap(new ConcurrentHashMap<>());
            
            // Automatically subscribe to events
            messageHandler.autoSubscribe();
        }
    }

    // Method called when a WebSocket message is received
    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        try {
            // Get the payload of the WebSocket message
            final var payload = webSocketMessage.getPayload().toString();
            if (!StringUtils.isEmpty(payload)) {
                log.debug("Binance-Socket: Handle message: {}", payload);
                // Check if the payload contains a specific event type
                if (payload.indexOf(EventType.DEPTH_UPDATE.getValue()) != -1) {
                    // Handle the price message using the MessageHandler
                    messageHandler.handlePriceMessage(payload);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    // Method called when a WebSocket transport error occurs
    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) {
        log.info("Binance-Socket: WebSocket handle error", throwable.getCause());
    }

    // Method called when a WebSocket connection is closed
    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) {
        // Reset the WebSocket session in the MessageHandler
        messageHandler.setWebSocketSession(null);
        log.error("afterConnectionClosed {}:{}", closeStatus.getCode(), closeStatus.getReason());
    }

    // Method to indicate whether the handler supports partial messages
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
