package com.poliakov.recommendationservice.controller;

import com.poliakov.recommendationservice.model.Crypto;
import com.poliakov.recommendationservice.service.CryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;


@RestController
public class NormalizedDataController {

    @Autowired
    private CryptoService cryptoService;


    @GetMapping("/normalized/sorted/desc")
    public List<Crypto> getSortedNormalizedDesc() {
        return cryptoService.getDescSortedByNormalizedRangeCryptoList();
    }

    @GetMapping("/normalized/highest/{date}")
    public Crypto getHighestNormalizedCryptoByDate(@PathVariable String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            return cryptoService.getHighestNormalizedRangeByDay(localDate);
        } catch (DateTimeParseException e) {
            return null; // todo handle it
        }
    }

    private LocalDate parseDate(String date) {
        return null;
    }
}
