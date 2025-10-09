package vietravel.example.vietravel.Controller.AdminController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vietravel.example.vietravel.Model.ImageFile;
import vietravel.example.vietravel.Service.ImageFileService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/public/uploads")
@RequiredArgsConstructor
public class AdminUploadController {

    private final ImageFileService imageFileService;

    @PostMapping
    public ResponseEntity<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "description", required = false) String description
    ) throws IOException {
        String imageUrl = imageFileService.uploadImage(file, description);
        return ResponseEntity.ok(imageUrl);
    }


    @GetMapping({"", "/"})
    public ResponseEntity<List<ImageFile>> getAllImages() {
        return ResponseEntity.ok(imageFileService.getAllImages());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        boolean deleted = imageFileService.deleteImage(id);
        return deleted
                ? ResponseEntity.ok("Image deleted successfully.")
                : ResponseEntity.notFound().build();
    }

}
