package com.poliakov.recommendationservice.utils;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import com.poliakov.recommendationservice.dto.CryptoDTO;
import com.poliakov.recommendationservice.exception.InvalidDataException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.List;

public class FilesReaderUtils {

    public static List<CryptoDTO> getDataFromResource(String filename) throws IOException {
        Resource resource = new ClassPathResource(filename);
        CsvToBeanBuilder<CryptoDTO> builder = new CsvToBeanBuilder<>(new FileReader(resource.getFile()));
        return getData(builder);
    }

    public static List<CryptoDTO> getDataFromFile(InputStream file) throws RuntimeException {
        CsvToBeanBuilder<CryptoDTO> builder = new CsvToBeanBuilder<>(new InputStreamReader(file));
        return getData(builder);
    }

    private static List<CryptoDTO> getData(CsvToBeanBuilder<CryptoDTO> builder) {
        return builder
                .withType(CryptoDTO.class)
                .build()
                .parse();
    }

}
