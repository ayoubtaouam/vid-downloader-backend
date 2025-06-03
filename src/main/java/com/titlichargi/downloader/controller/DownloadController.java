package com.titlichargi.downloader.controller;

import com.titlichargi.downloader.service.DownloadService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

//    @PostMapping("download")
//    public ResponseEntity<Map<String, String>> download(@RequestBody Map<String, String> body) throws IOException, InterruptedException {
//        String url = body.get("url");
//        return downloadService.downloadVideo(url);
//    }

    @PostMapping("download")
    public void download(@RequestBody Map<String, String> body, HttpServletResponse response) throws Exception {
        Path path = Paths.get("download");
        String url = body.get("url");
        Process process = new ProcessBuilder("yt-dlp", "-P", path.toString(), "-o", "-", url).start();
        response.setContentType("video/mp4");
        response.setHeader("content-disposition", "attachment; filename=\"video.mp4\"");

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

    @GetMapping("file")
    public ResponseEntity<Resource> getFile(@RequestParam String filename) throws IOException {

        Path path = Paths.get("download").resolve(filename).normalize();
        System.out.println(path);
        InputStream is = Files.newInputStream(path);
        InputStreamResource resource = new InputStreamResource(is);
        return ResponseEntity.ok().
                contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }
}
