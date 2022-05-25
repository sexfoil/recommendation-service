package com.poliakov.recommendationservice.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CryptoDTO {

    @CsvBindByName(column = "timestamp")
    private Long timestamp;

    @CsvBindByName(column = "symbol")
    private Crypto symbol;

    @CsvBindByName(column = "price")
    private BigDecimal price;

}
