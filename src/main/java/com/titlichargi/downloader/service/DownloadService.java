package com.titlichargi.downloader.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class DownloadService {
    public String buildFilename(String title, String extension) {
        //title = title.replaceAll("[^a-zA-Z0-9\\-_\\.]", "_");
        return title + "." + extension;
    }

    public Map<String, Object> getVideoInfo(String url) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("yt-dlp", "--dump-json", url).start();
        InputStream is = process.getInputStream();
        String json = new String(is.readAllBytes());

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            InputStream errorStream = process.getErrorStream();
            String error = new String(errorStream.readAllBytes());
            System.err.println("yt-dlp failed: " + error);
            throw new RuntimeException("yt-dlp failed");
        }
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> fullInfo = mapper.readValue(json, Map.class);

        Map<String, Object> info = new HashMap<>();
        info.put("title", fullInfo.get("title"));
        info.put("thumbnail", fullInfo.get("thumbnail"));
        info.put("formats", fullInfo.get("formats"));
        return info;
    }
}
