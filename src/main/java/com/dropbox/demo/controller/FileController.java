package com.dropbox.demo.controller;

import com.dropbox.demo.model.FileEntity;
import com.dropbox.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<Long> uploadFile(@RequestParam("file") MultipartFile file) {
        Long fileId = fileService.uploadFile(file);
        return ResponseEntity.ok(fileId);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long fileId) throws FileNotFoundException {
        byte[] fileData = fileService.getFile(fileId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "file");
        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    @PutMapping("/{fileId}")
    public ResponseEntity<String> updateFile(@PathVariable Long fileId, @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(fileService.updateFile(fileId, file));
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable Long fileId) throws FileNotFoundException {
        return ResponseEntity.ok(fileService.deleteFile(fileId));
    }

    @GetMapping
    public ResponseEntity<List<FileEntity>> listFiles() {
        List<FileEntity> fileMetadataList = fileService.listFiles();
        return ResponseEntity.ok(fileMetadataList);
    }
}
