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
    public String getFilename(String url) throws Exception {
        Process process = new ProcessBuilder("yt-dlp", "--print", "filename", url).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String fullPath = reader.readLine();
        Path path = Paths.get(fullPath);
        String filename = path.getFileName().toString();
        int exitCode = process.waitFor();
        if (exitCode == 0) {
            return filename;
        } else {
            throw new Exception("can't get filename");
        }
    }

}
