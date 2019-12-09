package com.squeaker.entry.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @PostMapping("/{twittId}")
    public void addComment(@RequestHeader("Authorization") String token,
                           @PathVariable Integer twittId, @RequestParam String comment) {


    }
}
