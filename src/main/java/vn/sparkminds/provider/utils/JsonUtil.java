package vn.sparkminds.provider.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtil() {
    }

    public static <T> T parseJson2Java(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.warn("JSON parser string convert to object exception: {}", e.getMessage());
            return null;
        }
    }

    public static String convertJava2StrJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("JSON parser object to string exception: {}", e.getMessage());
            return null;
        }
    }

    public static <T> T convertObjectToObject(Object object, Class<T> clazz) {
        return mapper.convertValue(object, clazz);
    }

    public static String convertJava2StrJsonWithInstant(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("JSON parser object to string exception: {}", e.getMessage());
            return null;
        }
    }

    public static <T> T parseJson2JavaWithInstant(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.warn("JSON parser string convert to object exception: {}", e.getMessage());
            return null;
        }
    }

    public static <T> T parseJson2WithType(String json, JavaType type) {
        if (StringUtils.isEmpty(json) || type == null) {
            return null;
        }
        try {
            return mapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.warn("Json parser from string to object error {}", e);
            return null;
        }
    }

    public static String getJSONFromURL(String path) {
        String jsonText = "";
        try (BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(new URL(path).openStream()))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonText += line + "\n";
            }
        } catch (Exception e) {
            log.error("Error cannot read file", e);
        }
        return jsonText;
    }

    public static <T> T parseJson2Java(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.warn("JSON parser string convert to object exception: {}", e.getMessage());
            return null;
        }
    }

    public static Map<String, Object> convertValueToMap(Object value) {
        return mapper.convertValue(value, new TypeReference<Map<String, Object>>() {});
    }
}
