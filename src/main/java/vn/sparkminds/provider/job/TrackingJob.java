package vn.sparkminds.provider.job;

import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.sparkminds.provider.dto.PriceQuantityDto;
import vn.sparkminds.provider.handler.MessageHandler;

@Slf4j
@Component
@Data
@RequiredArgsConstructor
public class TrackingJob {

    private final MessageHandler messageHandler;

    @Scheduled(fixedDelay = 5000, initialDelay = 30000)
    public void process() {
        int countBuy = 0;
        int countSell = 0;
        for (Map.Entry<String, Map<String, PriceQuantityDto>> entry : messageHandler.getBuyMap().entrySet()) {
            countBuy += entry.getValue().size();
            log.info("Count {} - BUY: {} ", entry.getKey(), entry.getValue().size());
        }
        log.info("Count BUY size: {} ", countBuy);

        for (Map.Entry<String, Map<String, PriceQuantityDto>> entry : messageHandler.getSellMap().entrySet()) {
            countSell += entry.getValue().size();
            log.info("Count {} - SELL: {} ", entry.getKey(), entry.getValue().size());
        }
        log.info("Count SELL size: {} ", countSell);

    }

}
