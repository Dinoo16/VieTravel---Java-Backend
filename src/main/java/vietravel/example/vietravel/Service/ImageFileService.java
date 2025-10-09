package vietravel.example.vietravel.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vietravel.example.vietravel.Model.ImageFile;
import vietravel.example.vietravel.Repository.ImageFileRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;
    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String description) throws IOException {
        // 1️⃣ Upload file lên Cloudinary
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "vietravel_uploads")); // optional: folder name

        // Lấy URL từ kết quả
        String imageUrl = (String) uploadResult.get("secure_url");

        // Lưu thông tin ảnh vào DB
        ImageFile imageFile = ImageFile.builder()
                .fileName((String) uploadResult.get("original_filename"))
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
            try {
                // Xóa trên Cloudinary (optional)
                String publicId = extractPublicId(image.getUrl());
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            } catch (Exception e) {
                System.err.println("Could not delete from Cloudinary: " + e.getMessage());
            }
            imageFileRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private String extractPublicId(String url) {
        String[] parts = url.split("/");
        String filename = parts[parts.length - 1];
        return "vietravel_uploads/" + filename.substring(0, filename.lastIndexOf('.'));
    }
}
