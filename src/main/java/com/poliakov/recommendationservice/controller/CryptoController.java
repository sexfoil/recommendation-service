package com.poliakov.recommendationservice.controller;

import com.poliakov.recommendationservice.dto.Crypto;
import com.poliakov.recommendationservice.exception.UnsupportedValueException;
import com.poliakov.recommendationservice.service.CryptoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
public class CryptoController {

    @Autowired
    private CryptoService cryptoService;

    private final Logger logger = LoggerFactory.getLogger(CryptoController.class);

    @GetMapping("/{crypto}/newest")
    public BigDecimal getNewestValueOfCrypto(@PathVariable String crypto) {
        return cryptoService.getNewestValue(getCrypto(crypto));
    }

    @GetMapping("/{crypto}/oldest")
    public BigDecimal getOldestValueOfCrypto(@PathVariable String crypto) {
        return cryptoService.getOldestValue(getCrypto(crypto));
    }

    @GetMapping("/{crypto}/min")
    public BigDecimal getMinValueOfCrypto(@PathVariable String crypto) {
        return cryptoService.getMinValue(getCrypto(crypto));
    }

    @GetMapping("/{crypto}/max")
    public BigDecimal getMaxValueOfCrypto(@PathVariable String crypto) {
        return cryptoService.getMaxValue(getCrypto(crypto));
    }


    private Crypto getCrypto(String crypto) {
        try {
            return Crypto.valueOf(crypto.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.error(String.format("Unsupported crypto value '%s'", crypto.toUpperCase()));
            throw new UnsupportedValueException(e.getMessage());
        }
    }

}
