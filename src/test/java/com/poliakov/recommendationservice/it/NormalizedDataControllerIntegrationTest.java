package com.poliakov.recommendationservice.it;

import com.poliakov.recommendationservice.RecommendationServiceApplication;
import com.poliakov.recommendationservice.dto.CryptoDTO;
import com.poliakov.recommendationservice.utils.FilesReaderUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = RecommendationServiceApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class NormalizedDataControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void getSortedByNormalizedRangeDesc() throws Exception {
        mockMvc.perform(get("/normalized/sorted/desc"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is("ETH")))
                .andExpect(jsonPath("$[1]", is("XRP")))
                .andExpect(jsonPath("$[2]", is("DOGE")))
                .andExpect(jsonPath("$[3]", is("LTC")))
                .andExpect(jsonPath("$[4]", is("BTC")));
    }

    @Test
    public void getHighestNormalizedCryptoByDate() throws Exception {
        mockMvc.perform(get("/normalized/highest/2022-01-10"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is("XRP")));
    }

    @Test
    public void invalidDateProvided() throws Exception {
        mockMvc.perform(get("/normalized/highest/2022-99-01"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("INCORRECT_REQUEST")));
    }

    @Test
    public void withoutDataDateProvided() throws Exception {
        mockMvc.perform(get("/normalized/highest/2022-01-30"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("NO_VALUES_IN_RANGE")));
    }

    @Test
    public void getNormalizedRange() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("prices/BTC_values.csv");
        MockMultipartFile mockFile = new MockMultipartFile("file", is);
        mockMvc.perform(fileUpload("/normalized/highest/upload").file(mockFile))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.BTC", is(0.4341211)));
    }

}
