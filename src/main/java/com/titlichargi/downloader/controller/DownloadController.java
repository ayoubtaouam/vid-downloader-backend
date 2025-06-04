package com.titlichargi.downloader.controller;

import com.titlichargi.downloader.service.DownloadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

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
        String filename = downloadService.getFilename(url);
        Process process = new ProcessBuilder("yt-dlp", "-o", "-", url).start();
        response.setContentType("video/mp4");
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
                throw new Exception();
            }
        }
    }
}
