package com.poliakov.recommendationservice.it;

import com.poliakov.recommendationservice.RecommendationServiceApplication;
import com.poliakov.recommendationservice.dto.CryptoDTO;
import com.poliakov.recommendationservice.exception.UnsupportedValueException;
import com.poliakov.recommendationservice.service.CryptoService;
import com.poliakov.recommendationservice.utils.FilesReaderUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = RecommendationServiceApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CryptoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private List<CryptoDTO> testData;

    @Before
    public void setUp() throws IOException {
        testData = FilesReaderUtils.getDataFromResource("prices/BTC_values.csv");
    }

    @Test
    public void getNewestValue() throws Exception {
        double newest = testData.get(testData.size()-1).getPrice().doubleValue();

        mockMvc.perform(get("/btc/newest"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(newest)));
    }

    @Test
    public void getOldestValue() throws Exception {
        double oldest = testData.get(0).getPrice().doubleValue();

        mockMvc.perform(get("/btc/oldest"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(oldest)));
    }

    @Test
    public void getMinValue() throws Exception {
        double min = testData.stream()
                .map(CryptoDTO::getPrice)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO)
                .doubleValue();
        mockMvc.perform(get("/btc/min"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(min)));
    }

    @Test
    public void getMaxValue() throws Exception {
        double max = testData.stream()
                .map(CryptoDTO::getPrice)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO)
                .doubleValue();
        mockMvc.perform(get("/btc/max"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(max)));
    }

    @Test
    public void unsupportedValue() throws Exception {
        mockMvc.perform(get("/xxx/max"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("UNSUPPORTED_VALUE")));
    }

}
