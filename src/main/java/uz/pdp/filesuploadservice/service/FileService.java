package uz.pdp.filesuploadservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private ResourceLoader resourceLoader;
    private final String uploadDir = "C://ForProjectFiles";

    public void uploadFile(MultipartFile file) throws IOException {
        String name = file.getOriginalFilename();
        if (name != null && name.contains("..")) {
            throw new IllegalArgumentException("Filename contains invalid path sequence: " + name);
        }

        try {
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
            Resource resource = resourceLoader.getResource("file:" + uploadDir + "/" + name);

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new IOException("File not found or cannot be read: " + name);
            }
        } catch (MalformedURLException e) {
            throw new IOException("Malformed URL for file: " + name, e);
        }
    }
    public void deleteFile(String name) throws IOException {
        try {
            Resource resource = resourceLoader.getResource("file:" + uploadDir + "/" + name);

            if (resource.exists()) {
                boolean deleted = resource.getFile().delete();
                if (!deleted) {
                    throw new IOException("Unable to delete file: " + name);
                }
            } else {
                throw new IOException("File not found: " + name);
            }
        } catch (MalformedURLException e) {
            throw new IOException("Malformed URL for file: " + name, e);
        }
    }

    public List<Resource> getAllUploadedFiles() throws IOException {
        String uploadDir = "C://ForProjectFiles";
        List<Resource> files = new ArrayList<>();
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Path.of(uploadDir))) {
            for (Path path : directoryStream) {
                if (Files.isRegularFile(path)) {
                    Resource resource = new UrlResource(path.toUri());
                    if (resource.exists() && resource.isReadable()) {
                        files.add(resource);
                    }
                }
            }
        }
        return files;
    }
}
