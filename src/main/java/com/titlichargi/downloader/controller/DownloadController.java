package com.titlichargi.downloader.controller;

import com.titlichargi.downloader.service.DownloadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api")
@CrossOrigin("*")
public class DownloadController {
    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @PostMapping("download")
    public ResponseEntity<String> download(@RequestBody Map<String, String> body) {
        String url = body.get("url");
        return downloadService.downloadVideo(url);
    }
}
