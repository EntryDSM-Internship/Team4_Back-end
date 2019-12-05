package com.squeaker.entry.controller;

import com.squeaker.entry.domain.payload.response.TwittResponse;
import com.squeaker.entry.service.TwittServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/twitt")
public class TwittController {

    @Autowired
    TwittServiceImpl twittService;

    @GetMapping
    public List<TwittResponse> getTwitt(@RequestHeader("Authorization") String token, @RequestParam Integer count) {
        return twittService.getTwitts(token, count);
    }

}
