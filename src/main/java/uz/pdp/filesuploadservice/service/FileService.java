package uz.pdp.filesuploadservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.filesuploadservice.entity.FileEntity;
import uz.pdp.filesuploadservice.repository.FileRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileService {
    @Autowired
    private FileRepository fileRepository;

    public void uploadFile(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        if (name != null && name.contains("..")) {
            throw new IllegalArgumentException("Filename contains invalid path sequence: " + name);
        }

        try {
            String uploadDir = "C://Users//sobir//OneDrive//Рабочий стол//Projects//Files-Upload-Service//src//main//resources//uploads";
            Path uploadPath = Path.of(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            assert name != null;
            Path filePath = uploadPath.resolve(name);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save file: " + name, e);
        }
    }

    public Resource downloadFile(String name) throws IOException {
        try {
            String uploadDir = "C://Users//sobir//OneDrive//Рабочий стол//Projects//Files-Upload-Service//src//main//resources//uploads";
            Path filePath = Path.of(uploadDir).resolve(name);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new IOException("File not found or cannot be read: " + name);
            }
        } catch (MalformedURLException e) {
            throw new IOException("Malformed URL for file: " + name, e);
        }
    }
    public Stream<FileEntity> getAllFiles() {
        return fileRepository.findAll().stream();
    }
}
