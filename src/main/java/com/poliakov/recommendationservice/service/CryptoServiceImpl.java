package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.model.Crypto;
import com.poliakov.recommendationservice.model.CryptoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CryptoServiceImpl implements CryptoService {

    @Autowired
    DataStorageService dataStorageService;


    @Override
    public List<Crypto> getSortedCryptosDesc() {
        Map<Crypto, BigDecimal> map = Arrays.stream(Crypto.values())
                .collect(Collectors.toMap(Function.identity(), this::getNormalizedRange));

        return Arrays.stream(Crypto.values())
                .collect(Collectors.toMap(Function.identity(), this::getNormalizedRange))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getNewestValue(Crypto crypto) {
        return dataStorageService.getCryptoData().get(crypto)
                .stream()
                .max(Comparator.comparing(CryptoDTO::getTimestamp))
                .orElseThrow() // todo add custom exception
                .getPrice();
    }

    @Override
    public BigDecimal getOldestValue(Crypto crypto) {
        return dataStorageService.getCryptoData().get(crypto)
                .stream()
                .min(Comparator.comparing(CryptoDTO::getTimestamp))
                .orElseThrow() // todo add custom exception
                .getPrice();
    }

    @Override
    public BigDecimal getMinValue(Crypto crypto) {
        return dataStorageService.getCryptoData().get(crypto)
                .stream()
                .map(CryptoDTO::getPrice)
                .min(Comparator.naturalOrder())
                .orElseThrow(); // todo add custom exception
    }

    @Override
    public BigDecimal getMaxValue(Crypto crypto) {
        return dataStorageService.getCryptoData().get(crypto)
                .stream()
                .map(CryptoDTO::getPrice)
                .max(Comparator.naturalOrder())
                .orElseThrow(); // todo add custom exception
    }

    @Override
    public Crypto getHighestNormalizedRangeByDay(LocalDate date) { // todo implement
        return Crypto.DOGE;
    }


    private BigDecimal getNormalizedRange(Crypto crypto) {
        BigDecimal max = getMaxValue(crypto);
        BigDecimal min = getMinValue(crypto);
        return max.subtract(min).divide(min, MathContext.DECIMAL128);
    }

}
