package uz.pdp.filesuploadservice.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.filesuploadservice.service.FileService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {
    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            fileService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error uploading file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<?> downloadFile(@PathVariable String fileName) {
        try {
            Resource resource = fileService.downloadFile(fileName);

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error downloading file: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteFile(@PathVariable String name) {
        try {
            fileService.deleteFile(name);
            return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error deleting file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<byte[]> getAllUploadedFiles() {
        try {
            List<Resource> resources = fileService.getAllUploadedFiles();


            if (!resources.isEmpty()) {
                Resource resource = resources.get(0);
                byte[] fileContent = IOUtils.toByteArray(resource.getInputStream());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentDispositionFormData("attachment", resource.getFilename());

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(fileContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

}
