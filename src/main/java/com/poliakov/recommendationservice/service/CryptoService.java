package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.model.Crypto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CryptoService {

    List<Crypto> getSortedCryptosDesc();

    BigDecimal getNewestValue(Crypto crypto);

    BigDecimal getOldestValue(Crypto crypto);

    BigDecimal getMinValue(Crypto crypto);

    BigDecimal getMaxValue(Crypto crypto);

    Crypto getHighestNormalizedRangeByDay(LocalDate date);

}
