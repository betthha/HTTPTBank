package DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyDto {
    @JsonProperty("ID")
    private String id;

    @JsonProperty("NumCode")
    private String numCode;

    @JsonProperty("CharCode")
    private String charCode;

    @JsonProperty("Nominal")
    private int nominal;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Value")
    private double value;

    @JsonProperty("Previous")
    private double previous;
}