package DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class CurrencyResponseDto {
    @JsonProperty("Valute")
    private Map<String, CurrencyDto> valutes;
}