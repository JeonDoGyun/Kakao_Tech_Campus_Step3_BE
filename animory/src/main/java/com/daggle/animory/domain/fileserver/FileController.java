package com.daggle.animory.domain.fileserver;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class FileController {

    // 또는 로직이 복잡하다면 중간에 Service 추가
    private final FileRepository localFileRepository;

    @GetMapping("/file/{fileUrl}")
    public ResponseEntity<Resource> getFile(@PathVariable final String fileUrl) throws IOException {
        Path path = Paths.get(fileUrl);
        String contentType = Files.probeContentType(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename(fileUrl, StandardCharsets.UTF_8)
                        .build());
        headers.add(HttpHeaders.CONTENT_TYPE, contentType);
        Resource resource = localFileRepository.getFile(fileUrl);

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);

    }
}
