package vn.sparkminds.provider.utils;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiUtil {

    private final RestTemplate restTemplate;

    public <T> ResponseEntity<T> postForEntity(String url, Object body, Class<T> clazz) {
        HttpEntity<Object> entity = new HttpEntity<>(body);
        return restTemplate.exchange(url, HttpMethod.POST, entity, clazz);
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> clazz) {
        return restTemplate.getForEntity(url, clazz);
    }

    public <T> T callGetRestTemplate(String url, HttpHeaders headers, Map<String, String> queryParams, Class<T> clazz) {
        try {
            var uri = buildUriComponent(url, queryParams).toUriString();
            HttpEntity<String> requestBody = new HttpEntity<>(headers);
            ResponseEntity<T> response = restTemplate.exchange(uri, HttpMethod.GET, requestBody, clazz);
            return response.getBody();
        } catch (IllegalArgumentException | RestClientException e) {
            log.info("RestTemplateUtil: Call API error message: {}", e.getMessage());
            throw e;
        }
    }

    private UriComponents buildUriComponent(String url, Map<String, String> queryParams) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url);
        if (queryParams != null && !queryParams.isEmpty()) {
            for (Map.Entry<String, String> query : queryParams.entrySet()) {
                uriComponentsBuilder.queryParam(query.getKey(), query.getValue());
            }
        }
        return uriComponentsBuilder.build();
    }

    public HttpHeaders buildHeader(Map<String, String> queryParams) {
        HttpHeaders headers = new HttpHeaders();
        if (queryParams != null && !queryParams.isEmpty()) {
            for (Map.Entry<String, String> query : queryParams.entrySet()) {
                headers.set(query.getKey(), query.getValue());
            }
        }
        return headers;
    }
}
