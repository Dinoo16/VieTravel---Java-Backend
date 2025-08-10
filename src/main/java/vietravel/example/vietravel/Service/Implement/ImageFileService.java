package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vietravel.example.vietravel.Model.ImageFile;
import vietravel.example.vietravel.Repository.ImageFileRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    public String uploadImage(MultipartFile file, String description) throws IOException {
        ImageFile imageFile = ImageFile.builder()
                .fileName(file.getOriginalFilename())
                .description(description != null ? description : "")
                .contentType(file.getContentType())
                .data(file.getBytes())
                .build();

        ImageFile savedImage = imageFileRepository.save(imageFile);
        String imageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/public/uploads/")
                .path(savedImage.getId().toString())
                .toUriString();

        return imageUrl;
    }

    public Optional<ImageFile> getImage(Long id) {
        return imageFileRepository.findById(id);
    }

    public List<ImageFile> getAllImages() {
        return imageFileRepository.findAll();
    }

    public boolean deleteImage(Long id) {
        if (imageFileRepository.existsById(id)) {
            imageFileRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
