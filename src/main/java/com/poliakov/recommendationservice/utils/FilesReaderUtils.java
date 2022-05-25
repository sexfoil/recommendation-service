package com.poliakov.recommendationservice.utils;

import com.opencsv.bean.CsvToBeanBuilder;
import com.poliakov.recommendationservice.dto.CryptoDTO;
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

    public static List<CryptoDTO> getDataFromFile(InputStream file) throws FileNotFoundException {
        CsvToBeanBuilder<CryptoDTO> builder = new CsvToBeanBuilder<>(new InputStreamReader(file));
        return getData(builder);
    }

    private static List<CryptoDTO> getData(CsvToBeanBuilder<CryptoDTO> builder) throws FileNotFoundException {
        return builder
                .withType(CryptoDTO.class)
                .build()
                .parse();
    }

}
