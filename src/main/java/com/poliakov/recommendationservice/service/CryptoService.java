package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.model.Crypto;
import com.poliakov.recommendationservice.model.CryptoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CryptoService {

    List<Crypto> getDescSortedByNormalizedRangeCryptoList();

    BigDecimal getNewestValue(Crypto crypto);

    BigDecimal getOldestValue(Crypto crypto);

    BigDecimal getMinValue(Crypto crypto);

    BigDecimal getMaxValue(Crypto crypto);

    Crypto getHighestNormalizedRangeByDay(LocalDate searchDate);

    Map<Crypto, BigDecimal> getCryptoNormalizedRange(Map<Crypto, List<CryptoDTO>> cryptoDTOS);

}
