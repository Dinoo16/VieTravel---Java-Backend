package vietravel.example.vietravel.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vietravel.example.vietravel.Model.ImageFile;
import vietravel.example.vietravel.Service.ImageFileService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/public/uploads")
@RequiredArgsConstructor
public class AdminUploadController {

    private final ImageFileService imageFileService;

    // Upload image
    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam(value = "description", required = false) String description) throws IOException {

        String imageUrl = imageFileService.uploadImage(file, description);
        return ResponseEntity.ok(imageUrl);
    }

    // Get image by id
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String fileName) throws MalformedURLException {
        Path filePath = Paths.get("uploads").resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // hoặc detect theo fileName nếu muốn
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    // Get all images metadata (no binary data)
    @GetMapping({"", "/"})
    public ResponseEntity<List<ImageFile>> getAllImages() {
        return ResponseEntity.ok(imageFileService.getAllImages());
    }

    // Delete image by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        boolean deleted = imageFileService.deleteImage(id);
        return deleted
                ? ResponseEntity.ok("Image deleted successfully.")
                : ResponseEntity.notFound().build();
    }

}
