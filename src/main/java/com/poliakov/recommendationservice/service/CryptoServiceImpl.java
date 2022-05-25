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
        return getNewestValue(getStoredData(), crypto, null);
    }

    public BigDecimal getNewestValue(Map<Crypto, List<CryptoDTO>> data, Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(data, crypto, searchDate)
                .max(Comparator.comparing(CryptoDTO::getTimestamp))
                .orElseThrow() // todo add custom exception
                .getPrice();
    }

    @Override
    public BigDecimal getOldestValue(Crypto crypto) {
        return getOldestValue(getStoredData(), crypto, null);
    }

    public BigDecimal getOldestValue(Map<Crypto, List<CryptoDTO>> data, Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(data, crypto, searchDate)
                .min(Comparator.comparing(CryptoDTO::getTimestamp))
                .orElseThrow() // todo add custom exception
                .getPrice();
    }

    @Override
    public BigDecimal getMinValue(Crypto crypto) {
        return getMinValue(getStoredData(), crypto, null);
    }

    public BigDecimal getMinValue(Map<Crypto, List<CryptoDTO>> data, Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(data, crypto, searchDate)
                .map(CryptoDTO::getPrice)
                .min(Comparator.naturalOrder())
                .orElseThrow(); // todo add custom exception
    }

    @Override
    public BigDecimal getMaxValue(Crypto crypto) {
        return getMaxValue(getStoredData(), crypto, null);
    }

    public BigDecimal getMaxValue(Map<Crypto, List<CryptoDTO>> data, Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(data, crypto, searchDate)
                .map(CryptoDTO::getPrice)
                .max(Comparator.naturalOrder())
                .orElseThrow(); // todo add custom exception
    }

    @Override
    public Crypto getHighestNormalizedRangeByDay(LocalDate date) {
        Map<Crypto, BigDecimal> cryptoMap = Arrays.stream(Crypto.values())
                .collect(Collectors.toMap(Function.identity(), crypto -> getNormalizedRange(getStoredData(), crypto, date)));

        return cryptoMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(); // todo add custom exception
    }

    @Override
    public Map<Crypto, BigDecimal> getCryptoNormalizedRange(Map<Crypto, List<CryptoDTO>> cryptoDTOS) {
        Map<Crypto, BigDecimal> map = new EnumMap<>(Crypto.class);
        Crypto key = cryptoDTOS.keySet().stream().findFirst().orElseThrow();
        map.put(key, getNormalizedRange(cryptoDTOS, key, null));
        return map;
    }


    private Map<Crypto, List<CryptoDTO>> getStoredData() {
        return dataStorageService.getStoredData();
    }

    private Map<Crypto, List<CryptoDTO>> getUploadedData() {
        return dataStorageService.getUploadedData();
    }

    private Stream<CryptoDTO> getCryptoDTOStream(Map<Crypto, List<CryptoDTO>> data, Crypto crypto, LocalDate searchDate) {
        Stream<CryptoDTO> cryptoDTOStream = data.get(crypto).stream();
        return searchDate == null
                ? cryptoDTOStream
                : cryptoDTOStream.filter(cryptoDTO -> isSearchDate(cryptoDTO, searchDate));
    }

    private boolean isSearchDate(CryptoDTO cryptoDTO, LocalDate date) {
        LocalDate cryptoDate = LocalDate.ofInstant(Instant.ofEpochMilli(cryptoDTO.getTimestamp()), ZoneId.systemDefault());
        return date.isEqual(cryptoDate);
    }

    private BigDecimal getNormalizedRange(Crypto crypto) {
        return getNormalizedRange(getStoredData(), crypto, null);
    }

    private BigDecimal getNormalizedRange(Map<Crypto, List<CryptoDTO>> data, Crypto crypto, LocalDate searchDate) {
        BigDecimal max = getMaxValue(data, crypto, searchDate);
        BigDecimal min = getMinValue(data, crypto, searchDate);
        return max.subtract(min).divide(min, MathContext.DECIMAL128);
    }

}
