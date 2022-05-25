package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.model.Crypto;
import com.poliakov.recommendationservice.model.CryptoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CryptoServiceImpl implements CryptoService {

    @Autowired
    DataStorageService dataStorageService;


    @Override
    public List<Crypto> getDescSortedByNormalizedRangeCryptoList() {
        return getDescSortedByNormalizedRangeCryptoList(null);
    }

    public List<Crypto> getDescSortedByNormalizedRangeCryptoList(LocalDate searchDate) {
        Map<Crypto, BigDecimal> cryptoMap = Arrays.stream(Crypto.values())
                .collect(Collectors.toMap(Function.identity(), this::getNormalizedRange));

        return cryptoMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal getNewestValue(Crypto crypto) {
        return getNewestValue(crypto, null);
    }

    public BigDecimal getNewestValue(Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(crypto, searchDate)
                .max(Comparator.comparing(CryptoDTO::getTimestamp))
                .orElseThrow() // todo add custom exception
                .getPrice();
    }

    @Override
    public BigDecimal getOldestValue(Crypto crypto) {
        return getOldestValue(crypto, null);
    }

    public BigDecimal getOldestValue(Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(crypto, searchDate)
                .min(Comparator.comparing(CryptoDTO::getTimestamp))
                .orElseThrow() // todo add custom exception
                .getPrice();
    }

    @Override
    public BigDecimal getMinValue(Crypto crypto) {
        return getMinValue(crypto, null);
    }

    public BigDecimal getMinValue(Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(crypto, searchDate)
                .map(CryptoDTO::getPrice)
                .min(Comparator.naturalOrder())
                .orElseThrow(); // todo add custom exception
    }

    @Override
    public BigDecimal getMaxValue(Crypto crypto) {
        return getMaxValue(crypto, null);
    }

    public BigDecimal getMaxValue(Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(crypto, searchDate)
                .map(CryptoDTO::getPrice)
                .max(Comparator.naturalOrder())
                .orElseThrow(); // todo add custom exception
    }

    @Override
    public Crypto getHighestNormalizedRangeByDay(LocalDate date) { // todo implement
        Map<Crypto, BigDecimal> cryptoMap = Arrays.stream(Crypto.values())
                .collect(Collectors.toMap(Function.identity(), crypto -> getNormalizedRange(crypto, date)));

        return cryptoMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(); // todo add custom exception
    }


    private Stream<CryptoDTO> getCryptoDTOStream(Crypto crypto, LocalDate searchDate) {
        Stream<CryptoDTO> cryptoDTOStream = dataStorageService.getCryptoData().get(crypto).stream();
        return searchDate == null
                ? cryptoDTOStream
                : cryptoDTOStream.filter(cryptoDTO -> isSearchDate(cryptoDTO, searchDate));
    }

    private boolean isSearchDate(CryptoDTO cryptoDTO, LocalDate date) {
        LocalDate cryptoDate = LocalDate.ofInstant(Instant.ofEpochMilli(cryptoDTO.getTimestamp()), ZoneId.systemDefault());
        return date.isEqual(cryptoDate);
    }

    private BigDecimal getNormalizedRange(Crypto crypto) {
        return getNormalizedRange(crypto, null);
    }

    private BigDecimal getNormalizedRange(Crypto crypto, LocalDate date) {
        BigDecimal max = getMaxValue(crypto, date);
        BigDecimal min = getMinValue(crypto, date);
        return max.subtract(min).divide(min, MathContext.DECIMAL128);
    }

}
