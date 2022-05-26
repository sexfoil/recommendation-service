package com.poliakov.recommendationservice.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoDTO {

    @CsvBindByName(column = "timestamp")
    private Long timestamp;

    @CsvBindByName(column = "symbol")
    private Crypto symbol;

    @CsvBindByName(column = "price")
    private BigDecimal price;

}
