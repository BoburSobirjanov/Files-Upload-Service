package uz.pdp.filesuploadservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FileConfig {
    @Bean
    public Path filePath() {
        return Paths.get("C://Users//sobir//OneDrive//Рабочий стол//Projects//Files-Upload-Service//src//main//resources//static");
    }
}
