package com.poliakov.recommendationservice.controller;

import com.poliakov.recommendationservice.dto.Crypto;
import com.poliakov.recommendationservice.service.CryptoService;
import com.poliakov.recommendationservice.service.DataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;


@RestController
public class NormalizedDataController {

    @Autowired
    private CryptoService cryptoService;

    @Autowired
    private DataStorageService dataStorageService;


    @GetMapping("/normalized/sorted/desc")
    public List<Crypto> getSortedByNormalizedRangeDesc() {
        return cryptoService.getDescSortedByNormalizedRangeCryptoList();
    }

    @GetMapping("/normalized/highest/{date}")
    public Crypto getHighestNormalizedCryptoByDate(@PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return cryptoService.getHighestNormalizedRangeByDay(localDate);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(e.getMessage(), e.getParsedString(), e.getErrorIndex());
        }
    }

    @PostMapping("/normalized/highest/upload")
    public Map<Crypto, BigDecimal> getNormalizedRange(@RequestParam("file") MultipartFile file) {
        try {
            dataStorageService.saveUploadedData(file);
            return cryptoService.getCryptoNormalizedRange(dataStorageService.getUploadedData());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // todo handle it
    }

    private LocalDate parseDate(String date) {
        return null;
    }
}
