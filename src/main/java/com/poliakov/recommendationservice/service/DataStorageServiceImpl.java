package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.model.Crypto;
import com.poliakov.recommendationservice.model.CryptoDTO;
import com.poliakov.recommendationservice.utils.FilesReaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class DataStorageServiceImpl implements DataStorageService {

    private static final String PATH_PREFIX = "prices/";
    private static final String PATH_SUFFIX = "_values.csv";

    private Map<Crypto, List<CryptoDTO>> cryptosStored;
    private Map<Crypto, List<CryptoDTO>> cryptosUploaded;


    @PostConstruct
    public void initData() {
        cryptosStored = new EnumMap<>(Crypto.class);
        try {
            for (Crypto crypto : Crypto.values()) {
                String path = PATH_PREFIX.concat(crypto.name()).concat(PATH_SUFFIX);
                List<CryptoDTO> values = FilesReaderUtils.getDataFromResource(path);
                cryptosStored.put(crypto, values);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // todo add logger
        }
    }

    @Override
    public Map<Crypto, List<CryptoDTO>> getStoredData() {
        return cryptosStored;
    }

    @Override
    public Map<Crypto, List<CryptoDTO>> getUploadedData() {
        return cryptosUploaded;
    }

    @Override
    public void saveUploadedData(MultipartFile file) throws IOException {
        cryptosUploaded = new EnumMap<>(Crypto.class);
        List<CryptoDTO> values = FilesReaderUtils.getDataFromFile(file.getInputStream());
        Crypto key = values.get(0).getSymbol();
        cryptosUploaded.put(key, values);
    }

}
