package uz.pdp.filesuploadservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileDetailsDto {
    private String fileName;
    private String fileDownloadUri;
}
