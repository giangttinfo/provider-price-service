package vn.sparkminds.provider.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PriceDto {

    @JsonProperty("stream")
    private String stream;

    @JsonProperty("data")
    private PriceDetailDto data;
}
