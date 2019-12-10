package com.squeaker.entry.controller;

import com.squeaker.entry.service.TwittServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    TwittServiceImpl twittService;

    @PostMapping("/{twittId}")
    public void twittLike(@RequestHeader("Authorization") String token, @PathVariable Integer twittId) {
        twittService.twittLike(token, twittId);
    }

    @DeleteMapping("/{twittId}")
    public void twittUnLike(@RequestHeader("Authorization") String token, @PathVariable Integer twittId) {
        twittService.twittUnLike(token, twittId);
    }
}
