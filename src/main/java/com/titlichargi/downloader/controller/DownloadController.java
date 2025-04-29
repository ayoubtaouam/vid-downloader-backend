package com.titlichargi.downloader.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api")
public class DownloadController {
    @PostMapping("download")
    public ResponseEntity<String> download(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        return ResponseEntity.ok("url received: " + url);
    }
}
