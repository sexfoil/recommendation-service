package com.poliakov.recommendationservice.utils;

import com.opencsv.bean.CsvToBeanBuilder;
import com.poliakov.recommendationservice.model.CryptoDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FilesReaderUtils {

    public static List<CryptoDTO> getDataCSV(String filename) throws IOException {
        Resource resource = new ClassPathResource(filename);
        CsvToBeanBuilder<CryptoDTO> builder = new CsvToBeanBuilder<>(new FileReader(resource.getFile()));
        return builder
                .withType(CryptoDTO.class)
                .build()
                .parse();
    }

}
