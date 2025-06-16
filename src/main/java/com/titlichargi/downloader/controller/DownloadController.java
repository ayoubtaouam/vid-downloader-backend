package com.titlichargi.downloader.controller;

import com.titlichargi.downloader.service.DownloadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@RestController
@RequestMapping("api")
@CrossOrigin(origins = "*", exposedHeaders = "Content-Disposition")
public class DownloadController {
    private final DownloadService downloadService;

    public DownloadController(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @PostMapping("download")
    public void download(@RequestBody Map<String, String> body, HttpServletResponse response) throws Exception {
        String url = body.get("url");
        String formatId = body.get("formatId");
        String title = body.get("videoTitle");
        String extension = body.get("extension");

        String filename = downloadService.buildFilename(title, extension);

        Process process = new ProcessBuilder("yt-dlp", "-f", formatId, "-o", "-", url).start();

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        try(
                InputStream is = process.getInputStream();
                OutputStream os = response.getOutputStream();
                ) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
                os.flush();
            }
            int exitCode = process.waitFor();
            if(exitCode != 0) {
                InputStream errorStream = process.getErrorStream();
                String error = new String(errorStream.readAllBytes());
                System.err.println("yt-dlp failed: " + error);
                throw new RuntimeException("yt-dlp failed with exit code " + exitCode);
            } else System.out.println("exit code: " + exitCode);
        }
    }
    @PostMapping("info")
    public Map<String, Object> getInfo(@RequestBody Map<String, String> body) throws IOException, InterruptedException {
        String url = body.get("url");
        return downloadService.getVideoInfo(url);
    }
}
