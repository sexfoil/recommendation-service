package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.dto.Crypto;
import com.poliakov.recommendationservice.dto.CryptoDTO;
import com.poliakov.recommendationservice.exception.NoValuesException;
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
    private DataStorageService dataStorageService;

    /**
     *  Returns a list of crypto descending sorted by normalized range for the month
     *
     * @return  the list of {@link Crypto}
     */
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

    /**
     *  Returns the newest value of specified crypto for the month
     *
     * @param   crypto the specified {@link Crypto}
     * @return  the newest value {@link BigDecimal}
     * @throws NoValuesException if no value found
     */
    @Override
    public BigDecimal getNewestValue(Crypto crypto) {
        return getNewestValue(getStoredData(), crypto, null);
    }

    /**
     *  Returns the newest value of specified crypto for the specified day
     *
     * @param   data the map with key is specified {@link Crypto} and values of {@link CryptoDTO}
     * @param   crypto the specified {@link Crypto}
     * @param   searchDate the specified day
     * @return  the newest value {@link BigDecimal}
     * @throws NoValuesException if no value found
     */
    private BigDecimal getNewestValue(Map<Crypto, List<CryptoDTO>> data, Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(data, crypto, searchDate)
                .max(Comparator.comparing(CryptoDTO::getTimestamp))
                .orElseThrow(NoValuesException::new)
                .getPrice();
    }

    /**
     *  Returns the oldest value of specified crypto for the month
     *
     * @param   crypto the specified {@link Crypto}
     * @return  the oldest value {@link BigDecimal}
     * @throws NoValuesException if no value found
     */
    @Override
    public BigDecimal getOldestValue(Crypto crypto) {
        return getOldestValue(getStoredData(), crypto, null);
    }

    /**
     *  Returns the oldest value of specified crypto for the specified day
     *
     * @param   data the map with key is specified {@link Crypto} and values of {@link CryptoDTO}
     * @param   crypto the specified {@link Crypto}
     * @param   searchDate the specified day
     * @return  the oldest value {@link BigDecimal}
     * @throws NoValuesException if no value found
     */
    private BigDecimal getOldestValue(Map<Crypto, List<CryptoDTO>> data, Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(data, crypto, searchDate)
                .min(Comparator.comparing(CryptoDTO::getTimestamp))
                .orElseThrow(NoValuesException::new)
                .getPrice();
    }

    /**
     *  Returns a min value of specified crypto for the month
     *
     * @param   crypto the specified {@link Crypto}
     * @return  the min value {@link BigDecimal}
     * @throws NoValuesException if no value found
     */
    @Override
    public BigDecimal getMinValue(Crypto crypto) {
        return getMinValue(getStoredData(), crypto, null);
    }

    /**
     *  Returns a min value of specified crypto for the specified day
     *
     * @param   data the map with key is specified {@link Crypto} and values of {@link CryptoDTO}
     * @param   crypto the specified {@link Crypto}
     * @param   searchDate the specified day
     * @return  the min value {@link BigDecimal}
     * @throws NoValuesException if no value found
     */
    private BigDecimal getMinValue(Map<Crypto, List<CryptoDTO>> data, Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(data, crypto, searchDate)
                .map(CryptoDTO::getPrice)
                .min(Comparator.naturalOrder())
                .orElseThrow(NoValuesException::new);
    }

    /**
     *  Returns a max value of specified crypto for the month
     *
     * @param   crypto the specified {@link Crypto}
     * @return  the max value {@link BigDecimal}
     * @throws NoValuesException if no value found
     */
    @Override
    public BigDecimal getMaxValue(Crypto crypto) {
        return getMaxValue(getStoredData(), crypto, null);
    }

    /**
     *  Returns a max value of specified crypto for the specified day
     *
     * @param   data the map with key is specified {@link Crypto} and values of {@link CryptoDTO}
     * @param   crypto the specified {@link Crypto}
     * @param   searchDate the specified day
     * @return  the max value {@link BigDecimal}
     * @throws NoValuesException if no value found
     */
    private BigDecimal getMaxValue(Map<Crypto, List<CryptoDTO>> data, Crypto crypto, LocalDate searchDate) {
        return getCryptoDTOStream(data, crypto, searchDate)
                .map(CryptoDTO::getPrice)
                .max(Comparator.naturalOrder())
                .orElseThrow(NoValuesException::new);
    }

    /**
     *  Returns a crypto with the highest normalized range for the specified day
     *
     * @param   searchDate the specified day
     * @return  the crypto with the highest normalized range
     * @throws NoValuesException if no value found
     */
    @Override
    public Crypto getHighestNormalizedRangeByDay(LocalDate searchDate) {
        Map<Crypto, BigDecimal> cryptoMap = Arrays.stream(Crypto.values())
                .collect(Collectors.toMap(Function.identity(), crypto -> getNormalizedRange(getStoredData(), crypto, searchDate)));

        return cryptoMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(NoValuesException::new);
    }

    /**
     *  Returns a crypto with value of normalized range for the period of time
     *
     * @return  the map of crypto with normalized range value
     * @throws NoValuesException if no value found
     */
    @Override
    public Map<Crypto, BigDecimal> getCryptoNormalizedRange() {
        Map<Crypto, BigDecimal> map = new EnumMap<>(Crypto.class);
        Crypto key = getUploadedData().keySet().stream().findFirst().orElseThrow(NoValuesException::new);
        map.put(key, getNormalizedRange(getUploadedData(), key, null));
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
        return max.subtract(min).divide(min, MathContext.DECIMAL32);
    }

}
