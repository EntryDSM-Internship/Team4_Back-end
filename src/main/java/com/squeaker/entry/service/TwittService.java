package com.squeaker.entry.service;

import com.squeaker.entry.domain.payload.response.TwittResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface TwittService {
    List<TwittResponse> getTwitts(String token, Integer count);
    void addTwitt(String token, String content, MultipartFile[] files);
}
