package uz.pdp.filesuploadservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.filesuploadservice.entity.FileEntity;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface FileRepository extends JpaRepository<FileEntity, UUID> {
    FileEntity findFileEntityByName(String fileName);
}
