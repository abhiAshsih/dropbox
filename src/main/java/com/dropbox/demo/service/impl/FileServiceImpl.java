package com.dropbox.demo.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.dropbox.demo.model.FileEntity;
import com.dropbox.demo.repository.FileRepository;
import com.dropbox.demo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileRepository fileRepository;
    @Value("${aws.s3.bucketName:test}")
    private String bucketName;
    @Autowired
    private AmazonS3 amazonS3;

    public Long uploadFile(MultipartFile file) {
        try {
            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setCreatedAt(new Date());
            fileEntity.setSize(file.getSize());
            fileEntity.setFileType(file.getContentType());
            try {
                String fileName = file.getOriginalFilename();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload file on AWS S3");
            }
            fileRepository.save(fileEntity);
            return fileEntity.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public byte[] getFile(Long fileId) throws FileNotFoundException {
        Optional<FileEntity> optionalFileEntity = fileRepository.findById(fileId);
        if (optionalFileEntity.isPresent()) {
            try {
                S3Object object = amazonS3.getObject(bucketName, optionalFileEntity.get().getFileName());
                S3ObjectInputStream inputStream = object.getObjectContent();
                return IOUtils.toByteArray(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to get file from S3");
            }
        } else {
            throw new FileNotFoundException("File not found");
        }
    }

    public String updateFile(Long fileId, MultipartFile file) throws IOException {
        Optional<FileEntity> optionalFileEntity = fileRepository.findById(fileId);
        if (optionalFileEntity.isPresent()) {
            FileEntity fileEntity = optionalFileEntity.get();
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setSize(file.getSize());
            fileEntity.setFileType(file.getContentType());
            try {
                String fileName = file.getOriginalFilename();
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                amazonS3.putObject(bucketName, fileEntity.getFileName(), file.getInputStream(), metadata);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload file on AWS S3");
            }
            fileRepository.save(fileEntity);
            return "Successfully file updated";
        } else {
            throw new FileNotFoundException("File not found");
        }
    }

    public String deleteFile(Long fileId) throws FileNotFoundException {
        Optional<FileEntity> optionalFileEntity = fileRepository.findById(fileId);
        if (optionalFileEntity.isPresent()) {
            amazonS3.deleteObject(bucketName, optionalFileEntity.get().getFileName());
            fileRepository.delete(optionalFileEntity.get());
            return "Successfully deleted file";
        } else {
            throw new FileNotFoundException("File not found");
        }
    }

    public List<FileEntity> listFiles() {
        List<FileEntity> fileEntities = fileRepository.findAll();
        List<FileEntity> fileMetadataList = new ArrayList<>();
        for (FileEntity fileEntity : fileEntities) {
            FileEntity fileMetadata = new FileEntity();
            fileMetadata.setId(fileEntity.getId());
            fileMetadata.setFileName(fileEntity.getFileName());
            fileMetadata.setCreatedAt(fileEntity.getCreatedAt());
            fileMetadata.setSize(fileEntity.getSize());
            fileMetadata.setFileType(fileEntity.getFileType());
            fileMetadataList.add(fileMetadata);
        }
        return fileMetadataList;
    }
}
