package com.poliakov.recommendationservice.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CryptoDTO {

    @CsvBindByName(column = "symbol")
    private Crypto symbol;

    @CsvBindByName(column = "price")
    private BigDecimal price;

    @CsvBindByName(column = "timestamp")
    private Long timestamp;

}
