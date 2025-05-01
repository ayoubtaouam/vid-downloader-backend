package com.titlichargi.downloader.controller;

import com.titlichargi.downloader.service.DownloadService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @GetMapping("file/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path path = Paths.get("download").resolve(filename).normalize();
            InputStream is = Files.newInputStream(path);
            InputStreamResource resource = new InputStreamResource(is);
            return ResponseEntity.ok().
                    contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
