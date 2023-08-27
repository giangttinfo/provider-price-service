package vn.sparkminds.provider.dto.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType {
    SUBSCRIBE("SUBSCRIBE"), UNSUBSCRIBE("UNSUBSCRIBE"), DEPTH_UPDATE("depthUpdate");

    @JsonValue
    private String value;

    EventType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }
}
