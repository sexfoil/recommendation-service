package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.dto.Crypto;
import com.poliakov.recommendationservice.dto.CryptoDTO;

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
