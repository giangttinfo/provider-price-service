package vn.sparkminds.provider.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.sparkminds.provider.config.ProviderConfiguration;
import vn.sparkminds.provider.dto.EventDto;
import vn.sparkminds.provider.dto.PriceDto;
import vn.sparkminds.provider.dto.PriceQuantityDto;
import vn.sparkminds.provider.dto.enums.CoinPair;
import vn.sparkminds.provider.dto.enums.EventType;
import vn.sparkminds.provider.utils.ApiUtil;
import vn.sparkminds.provider.utils.JsonUtil;

// Spring component annotation to mark this class as a Spring-managed component
@Component
// Lombok annotation to generate constructor with required arguments
@RequiredArgsConstructor
// Lombok annotation to generate logger field
@Slf4j
// Data annotation to generate getters, setters, equals, hashCode, and toString methods
@Data
public class MessageHandler {

    // WebSocket session used to communicate with the client
    private WebSocketSession webSocketSession;

    // Injected instance of ProviderConfiguration
    private final ProviderConfiguration providerConfiguration;

    // Injected instance of ApiUtil
    private final ApiUtil apiUtil;

    // Maps to store buy and sell data for different coin pairs
    Map<String, Map<String, PriceQuantityDto>> buyMap = new ConcurrentHashMap<>();
    Map<String, Map<String, PriceQuantityDto>> sellMap = new ConcurrentHashMap<>();

    /**
     * Checks if a WebSocket session is open.
     *
     * @return true if a WebSocket session is open, false otherwise
     */
    public boolean isOpenWebSocketSession() {
        return this.webSocketSession != null;
    }

    /**
     * Sets the WebSocket session.
     *
     * @param webSocketSession the new WebSocket session
     */
    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }

    /**
     * Handles price messages received from Binance.
     *
     * @param payload the JSON payload of the price message
     */
    public void handlePriceMessage(String payload) {
        // Parse JSON payload into PriceDto object
        PriceDto response = JsonUtil.parseJson2Java(payload, PriceDto.class);
        if (response != null) {
            // Get coin pair and price data
            var streamType = CoinPair.getStreamType(response.getStream());
            String coinPair = streamType.name();
            var priceDetail = response.getData();
            if (priceDetail != null) {
                // Save buy and sell prices to corresponding maps
                savePriceBuySell(priceDetail.getBuys(), priceDetail.getSells(), buyMap, sellMap, coinPair);
            }
        }
    }

    // Private method to update buy and sell prices in the maps
    private void savePriceBuySell(List<List<Object>> buyPrices, List<List<Object>> sellPrices,
            Map<String, Map<String, PriceQuantityDto>> buyMap, Map<String, Map<String, PriceQuantityDto>> sellMap,
            String coinPair) {
        buyMap.computeIfPresent(
                coinPair,
                (key, value) -> updatePriceBuyOrSell(buyPrices, value));

        sellMap.computeIfPresent(
                coinPair,
                (key, value) -> updatePriceBuyOrSell(sellPrices, value));

    }

    // Private method to update price data in the buy or sell map
    private Map<String, PriceQuantityDto> updatePriceBuyOrSell(List<List<Object>> prices, Map<String, PriceQuantityDto> map) {
        prices.parallelStream().forEach(data -> {
            // Create PriceQuantityDto object from data
            var priceQuantity = PriceQuantityDto.builder()
                    .price(new BigDecimal((String) data.get(0)).setScale(8, RoundingMode.DOWN))
                    .quantity(new BigDecimal((String) data.get(1))).build();
            if (priceQuantity.getQuantity().doubleValue() > 0) {
                // If quantity is greater than 0, update map with new price data
                map.put(priceQuantity.getPrice().toPlainString(), priceQuantity);
            } else {
                // If quantity is 0, remove price data from map
                map.remove(priceQuantity.getPrice().toPlainString());
            }
        });
        return map;
    }

    /**
     * Handles WebSocket connection closure.
     */
    public void handleWebSocketClosed() {
        setWebSocketSession(null);
    }

    /**
     * Automatically subscribes to instrument events for orderbooks.
     */
    public void autoSubscribe() {
        log.info("Binance-Socket: [START] autoSubscribe");
        for (CoinPair coinPair : CoinPair.values()) {
            subscribe(coinPair.name());
        }
        log.info("Binance-Socket: [END] autoSubscribe");
    }

    /**
     * Sends a message to Binance via WebSocket.
     *
     * @param <T>           the type of the socket message
     * @param socketMessage the socket message to be sent
     * @return true if the message was sent successfully, false otherwise
     */
    public synchronized <T> boolean sendMessage(WebSocketMessage<T> socketMessage) {
        if (this.isOpenWebSocketSession()) {
            try {
                this.webSocketSession.sendMessage(socketMessage);
                return true;
            } catch (Exception e) {
                log.error("Binance-Socket: Send message failed: {}", e.getMessage());
                return false;
            }
        } else {
            log.error("Binance-Socket: Socket connection lost");
            return false;
        }
    }

    /**
     * Retrieves the first price for a given coin pair.
     *
     * @param coinPair the coin pair for which to retrieve the price
     */
    @SuppressWarnings("unchecked")
    public void getPriceFirst(String coinPair) {
        var binanceCoinPair = CoinPair.valueOf(coinPair);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("symbol", binanceCoinPair.getCoinPairInBinance());
        queryParams.put("limit", "1000");
        Map<String, Object> prices = apiUtil
                .callGetRestTemplate(
                        new StringBuilder().append(providerConfiguration.getHost())
                                .append(providerConfiguration.getOrderBookUrl()).toString(),
                        null, queryParams, Map.class);
        setPriceMap(binanceCoinPair, prices);
    }

    /**
     * Sets the buy and sell price maps for a given coin pair.
     *
     * @param coinPair the coin pair for which to set the price maps
     * @param prices   the prices retrieved from the API
     */
    @SuppressWarnings("unchecked")
    public void setPriceMap(CoinPair coinPair, Map<String, Object> prices) {
        List<List<Object>> buys = (List<List<Object>>) prices.get("bids");
        List<List<Object>> sells = (List<List<Object>>) prices.get("asks");
        initializePriceBuySell(buys, sells, buyMap, sellMap, coinPair);
    }

    // Private method to initialize buy and sell price maps
    private void initializePriceBuySell(List<List<Object>> buys, List<List<Object>> sells,
            Map<String, Map<String, PriceQuantityDto>> buyMap, Map<String, Map<String, PriceQuantityDto>> sellMap,
            CoinPair coinPair) {
        // Update buy prices
        buyMap.put(coinPair.name(), updatePriceBuyOrSell(buys, new HashMap<>()));

        // Update sell prices
        sellMap.put(coinPair.name(), updatePriceBuyOrSell(sells, new HashMap<>()));
    }

    /**
     * Subscribes to an event for a given coin pair.
     *
     * @param coinPair the coin pair for which to subscribe
     */
    public void subscribe(String coinPair) {
        // Get prices first from the API
        getPriceFirst(coinPair);

        var event = new EventDto();
        event.setMethod(EventType.SUBSCRIBE.getValue());
        var streamType = CoinPair.valueOf(coinPair);
        event.setParams(Arrays.asList(streamType.getStreamName()));
        event.setId(streamType.getId());
        String message = JsonUtil.convertJava2StrJson(event);
        if (message != null) {
            try {
                var textMessage = new TextMessage(message);
                this.sendMessage(textMessage);
                Thread.sleep(200);
            } catch (InterruptedException e) {
                log.error("Binance-Socket: Auto subscribe event instrument [{}], detail: {}", coinPair, e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
        log.warn("Binance-Socket: Subscribe event instrument [{}]", coinPair);
    }
}
