package com.titlichargi.downloader.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class DownloadService {
    public ResponseEntity<Map<String, String>> downloadVideo(String url) throws IOException, InterruptedException {
        Path downloadPath = Paths.get("download");
        Process process = new ProcessBuilder("yt-dlp", "-P", downloadPath.toString(), "--no-simulate", "--print", "filename", url).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String fullPath = reader.readLine();
        Path path = Paths.get(fullPath);
        String filename = path.getFileName().toString();
        Map<String, String> response = Map.of(
                "message", "Download successful",
                "filename", filename,
                "path", fullPath
        );
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = Map.of(
                    "error", "Download failed with exit code: " + exitCode
            );
            return ResponseEntity.status(500).body(error);
        }
    }
}
