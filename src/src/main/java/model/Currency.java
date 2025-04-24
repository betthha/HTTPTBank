package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Currency {
    private String id;
    private String name;
    private String baseCurrency;
    private String priceChangeRange;
    private String description;
}