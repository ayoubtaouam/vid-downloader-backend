package com.titlichargi.downloader.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DownloadService {
    public ResponseEntity<String> downloadVideo(String url) {
        try {
            Process process = new ProcessBuilder("yt-dlp", url).start();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
        return ResponseEntity.ok("url: " + url);
    }
}
