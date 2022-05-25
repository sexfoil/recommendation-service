package com.poliakov.recommendationservice;

import com.poliakov.recommendationservice.dto.Crypto;
import com.poliakov.recommendationservice.dto.CryptoDTO;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.poliakov.recommendationservice.dto.Crypto.*;

public class TestsDataHelper {

    public static Map<Crypto, List<CryptoDTO>> getMockedStoredData() {
        Map<Crypto, List<CryptoDTO>> map = new EnumMap<>(Crypto.class);
        map.put(BTC, List.of(
                new CryptoDTO(1641009600000L, BTC, BigDecimal.valueOf(46813.21)),
                new CryptoDTO(1641020400000L, BTC, BigDecimal.valueOf(46979.61)),
                new CryptoDTO(1641031200000L, BTC, BigDecimal.valueOf(47143.98)),
                new CryptoDTO(1641546000000L, BTC, BigDecimal.valueOf(48088.61)),
                new CryptoDTO(1641603600000L, BTC, BigDecimal.valueOf(38991.89))
        ));
        map.put(DOGE, List.of(
                new CryptoDTO(1641013200000L, DOGE, BigDecimal.valueOf(0.1702)),
                new CryptoDTO(1641024400000L, DOGE, BigDecimal.valueOf(0.1722)),
                new CryptoDTO(1641513600000L, DOGE, BigDecimal.valueOf(0.16)),
                new CryptoDTO(1641729600000L, DOGE, BigDecimal.valueOf(0.1913)),
                new CryptoDTO(1643655600000L, DOGE, BigDecimal.valueOf(0.1415))
        ));
        map.put(ETH, List.of(
                new CryptoDTO(1641013200000L, ETH, BigDecimal.valueOf(0.002)),
                new CryptoDTO(1641074400000L, ETH, BigDecimal.valueOf(0.002))
        ));
        map.put(LTC, List.of(
                new CryptoDTO(1641013200000L, LTC, BigDecimal.valueOf(0.02)),
                new CryptoDTO(1643655600000L, LTC, BigDecimal.valueOf(0.02))
        ));
        map.put(XRP, List.of(
                new CryptoDTO(1641013200000L, XRP, BigDecimal.valueOf(0.055)),
                new CryptoDTO(1643655600000L, XRP, BigDecimal.valueOf(0.055))
        ));
        return map;
    }

    public static Map<Crypto, List<CryptoDTO>> getMockedUploadedData() {
        Map<Crypto, List<CryptoDTO>> map = new EnumMap<>(Crypto.class);
        map.put(BTC, List.of(
                new CryptoDTO(1641009600000L, BTC, BigDecimal.valueOf(46813.21)),
                new CryptoDTO(1641020400000L, BTC, BigDecimal.valueOf(46979.61)),
                new CryptoDTO(1641031200000L, BTC, BigDecimal.valueOf(47143.98)),
                new CryptoDTO(1641546000000L, BTC, BigDecimal.valueOf(48088.61)),
                new CryptoDTO(1641603600000L, BTC, BigDecimal.valueOf(38991.89))
        ));
        return map;
    }

}
