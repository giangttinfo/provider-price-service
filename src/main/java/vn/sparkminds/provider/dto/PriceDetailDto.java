package vn.sparkminds.provider.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PriceDetailDto {

    @JsonProperty("e")
    private String eventType;

    @JsonProperty("E")
    private String eventTime;

    @JsonProperty("s")
    private String symbol;

    @JsonProperty("U")
    private long firstUpdateID;

    @JsonProperty("u")
    private long finalUpdateID;

    @JsonProperty("b")
    private List<List<Object>> buys;

    @JsonProperty("a")
    private List<List<Object>> sells;
}
