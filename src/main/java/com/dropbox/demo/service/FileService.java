package com.dropbox.demo.service;

import com.dropbox.demo.model.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface FileService {
    Long uploadFile(MultipartFile file);
    byte[] getFile(Long fileId) throws FileNotFoundException;
    String updateFile(Long fileId, MultipartFile file) throws IOException;
    String deleteFile(Long fileId) throws FileNotFoundException;
    List<FileEntity> listFiles();
}
