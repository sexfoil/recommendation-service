package com.poliakov.recommendationservice.service;

import com.poliakov.recommendationservice.model.Crypto;
import com.poliakov.recommendationservice.model.CryptoDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface DataStorageService {

    Map<Crypto, List<CryptoDTO>> getStoredData();

    Map<Crypto, List<CryptoDTO>> getUploadedData();

    void saveUploadedData(MultipartFile file) throws IOException;

}
