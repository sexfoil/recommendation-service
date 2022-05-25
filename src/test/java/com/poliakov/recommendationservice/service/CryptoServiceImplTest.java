package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.TestsDataHelper;
import com.poliakov.recommendationservice.dto.Crypto;
import com.poliakov.recommendationservice.dto.CryptoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.poliakov.recommendationservice.dto.Crypto.BTC;
import static com.poliakov.recommendationservice.dto.Crypto.DOGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CryptoServiceImplTest {

    @InjectMocks
    private CryptoServiceImpl cryptoService;

    @Mock
    private DataStorageServiceImpl dataStorageService;

    private final Map<Crypto, List<CryptoDTO>> STORED_DATA = TestsDataHelper.getMockedStoredData();
    private final Map<Crypto, List<CryptoDTO>> UPLOADED_DATA = TestsDataHelper.getMockedUploadedData();
    private final LocalDate TEST_DATE = LocalDate.of(2022, 1, 1);

    @BeforeEach
    public void setUp() {
        when(dataStorageService.getStoredData()).thenReturn(STORED_DATA);
    }

    @Test
    void getDescSortedByNormalizedRangeCryptoList() {
        List<Crypto> list = cryptoService.getDescSortedByNormalizedRangeCryptoList();

        assertEquals(list.size(), 5);
        assertEquals(list.get(0), DOGE);
        assertEquals(list.get(1), BTC);
    }


    @Test
    void testGetNewestValue() {
        BigDecimal max = cryptoService.getNewestValue(BTC);

        assertEquals(max, BigDecimal.valueOf(38991.89));
    }

    @Test
    void testGetNewestValueWithProvidedDate() {
        BigDecimal max = cryptoService.getNewestValue(STORED_DATA, BTC, TEST_DATE);

        assertEquals(max, BigDecimal.valueOf(47143.98));
    }

    @Test
    void testGetOldestValue() {
        BigDecimal max = cryptoService.getOldestValue(BTC);

        assertEquals(max, BigDecimal.valueOf(46813.21));
    }

    @Test
    void testGetOldestValueWithProvidedDate() {
        BigDecimal max = cryptoService.getOldestValue(STORED_DATA, BTC, TEST_DATE);

        assertEquals(max, BigDecimal.valueOf(46813.21));
    }

    @Test
    void testGetMinValue() {
        BigDecimal max = cryptoService.getMinValue(BTC);

        assertEquals(max, BigDecimal.valueOf(38991.89));
    }

    @Test
    void testGetMinValueWithProvidedDate() {
        BigDecimal max = cryptoService.getMinValue(STORED_DATA, BTC, TEST_DATE);

        assertEquals(max, BigDecimal.valueOf(46813.21));
    }

    @Test
    void testGetMaxValue() {
        BigDecimal max = cryptoService.getMaxValue(BTC);

        assertEquals(max, BigDecimal.valueOf(48088.61));
    }

    @Test
    void testGetMaxValueWithProvidedDate() {
        BigDecimal max = cryptoService.getMaxValue(STORED_DATA, BTC, TEST_DATE);

        assertEquals(max, BigDecimal.valueOf(47143.98));
    }

    @Test
    void testGetHighestNormalizedRangeByDay() {
        Crypto symbol = cryptoService.getHighestNormalizedRangeByDay(TEST_DATE);

        assertEquals(symbol, DOGE);
    }

    @Test
    void testGetCryptoNormalizedRange() {
        when(dataStorageService.getUploadedData()).thenReturn(UPLOADED_DATA);
        Map<Crypto, BigDecimal> map = cryptoService.getCryptoNormalizedRange();

        assertNotNull(map.get(BTC));
        assertEquals(map.get(BTC), BigDecimal.valueOf(0.2332977));
    }
}