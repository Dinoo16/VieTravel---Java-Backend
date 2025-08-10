package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vietravel.example.vietravel.Model.ImageFile;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {
}
