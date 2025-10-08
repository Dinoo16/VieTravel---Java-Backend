package vietravel.example.vietravel.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vietravel.example.vietravel.Model.ImageFile;
import vietravel.example.vietravel.Repository.ImageFileRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    private static final String UPLOAD_DIR = "uploads/";

    public String uploadImage(MultipartFile file, String description) throws IOException {
        // 1️⃣ Tạo thư mục nếu chưa tồn tại
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // 2️⃣ Tạo file vật lý
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + fileName);
        Files.write(filePath, file.getBytes());

        // 3️⃣ Tạo URL công khai cho file
        String imageUrl = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();

        // 4️⃣ Lưu thông tin ảnh vào DB
        ImageFile imageFile = ImageFile.builder()
                .fileName(fileName)
                .url(imageUrl)
                .description(description != null ? description : "")
                .contentType(file.getContentType())
                .build();

        imageFileRepository.save(imageFile);

        return imageUrl;
    }

    public Optional<ImageFile> getImage(Long id) {
        return imageFileRepository.findById(id);
    }

    public List<ImageFile> getAllImages() {
        return imageFileRepository.findAll();
    }

    public boolean deleteImage(Long id) {
        Optional<ImageFile> imageOpt = imageFileRepository.findById(id);
        if (imageOpt.isPresent()) {
            ImageFile image = imageOpt.get();
            // Xóa file vật lý
            try {
                Path filePath = Paths.get(UPLOAD_DIR + image.getFileName());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                System.err.println("Could not delete file: " + e.getMessage());
            }
            // Xóa bản ghi DB
            imageFileRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
