package vn.sparkminds.provider.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PriceQuantityDto {

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("quantity")
    private BigDecimal quantity;

    private BigDecimal fxRate;

    private BigDecimal originPrice;
}
