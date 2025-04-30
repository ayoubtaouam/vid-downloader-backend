package com.titlichargi.downloader.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DownloadService {
    public ResponseEntity<String> downloadVideo(String url) {
        try {
            Process process = new ProcessBuilder("yt-dlp", url).start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                return ResponseEntity.ok("Download successful: " + url);
            }
            else {
                return ResponseEntity.status(500).body("Download failed with exit code: " + exitCode);
            }
        } catch(IOException | InterruptedException e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
