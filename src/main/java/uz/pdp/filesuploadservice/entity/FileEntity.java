package uz.pdp.filesuploadservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.*;
@Entity(name = "files")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FileEntity extends BaseEntity{
    @Column(unique = true,nullable = false)
    private String name;
    private String type;
    @Lob
    private byte[] data;
}
