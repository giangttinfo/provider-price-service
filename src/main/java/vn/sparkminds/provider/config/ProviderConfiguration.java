package vn.sparkminds.provider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Data
public class ProviderConfiguration {

    @Value("${provider.binance.providerName:}")
    private String providerName;

    @Value("${provider.binance.wss.url:}")
    private String wssUrl;

    @Value("${provider.binance.userName:}")
    private String userName;

    @Value("${provider.binance.api.host:}")
    private String host;
    
    @Value("${provider.binance.api.url.orderBook:}")
    private String orderBookUrl;
}
