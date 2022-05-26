package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.dto.Crypto;
import com.poliakov.recommendationservice.dto.CryptoDTO;
import com.poliakov.recommendationservice.exception.InvalidDataException;
import com.poliakov.recommendationservice.utils.FilesReaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:resource.properties")
@Service
public class DataStorageServiceImpl implements DataStorageService {

    @Value("${src.files.folder}")
    private String srcFolder;

    @Value("${src.files.pattern}")
    private String filePattern;

    @Value("${src.files.extension}")
    private String fileExtension;

    private Map<Crypto, List<CryptoDTO>> cryptosStored;
    private Map<Crypto, List<CryptoDTO>> cryptosUploaded;

    private final Logger logger = LoggerFactory.getLogger(DataStorageServiceImpl.class);

    @PostConstruct
    public void initData() {
        cryptosStored = new EnumMap<>(Crypto.class);
        try {
            for (Crypto crypto : Crypto.values()) {
                String filename = crypto.name().concat(filePattern).concat(".").concat(fileExtension);
                String path = srcFolder.concat("/").concat(filename);
                List<CryptoDTO> values = FilesReaderUtils.getDataFromResource(path);
                cryptosStored.put(crypto, values);

                logger.info(String.format("Read data from '%s' file. Successfully stored values for %s",
                        crypto.name().concat(filename), crypto.name()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("Can't read data from files. Cause: %s", e.getMessage()));
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
    public void saveUploadedData(MultipartFile file) throws InvalidDataException, IOException {
        cryptosUploaded = new EnumMap<>(Crypto.class);
        try {
            List<CryptoDTO> values = FilesReaderUtils.getDataFromFile(file.getInputStream());
            Crypto key = values.get(0).getSymbol();
            cryptosUploaded.put(key, values);

            logger.info(String.format("Stored data from uploaded '%s' file. Successfully stored values for %s",
                    file.getOriginalFilename(), key.name()));

        } catch (RuntimeException e) {
            logger.error("Can't store data from uploaded file.");

            throw new InvalidDataException(e.getMessage());
        }
    }

}
