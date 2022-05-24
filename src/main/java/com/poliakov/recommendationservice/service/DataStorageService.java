package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.model.Crypto;
import com.poliakov.recommendationservice.model.CryptoDTO;

import java.util.List;
import java.util.Map;

public interface DataStorageService {

    Map<Crypto, List<CryptoDTO>> getCryptoData();

}
