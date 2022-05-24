package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.model.Crypto;
import com.poliakov.recommendationservice.model.CryptoDTO;
import com.poliakov.recommendationservice.utils.FilesReaderUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class DataStorageServiceImpl implements DataStorageService {

    private static final String PATH_PREFIX = "prices/";
    private static final String PATH_SUFFIX = "_values.csv";

    private Map<Crypto, List<CryptoDTO>> cryptosMap;


    @PostConstruct
    public void initData() {
        cryptosMap = new EnumMap<>(Crypto.class);
        try {
            for (Crypto crypto : Crypto.values()) {
                String path = PATH_PREFIX.concat(crypto.name()).concat(PATH_SUFFIX);
                List<CryptoDTO> values = FilesReaderUtils.getDataCSV(path);
                cryptosMap.put(crypto, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // todo add logger
        }
    }

    @Override
    public Map<Crypto, List<CryptoDTO>> getCryptoData() {
        return cryptosMap;
    }

}
