package vietravel.example.vietravel.Controller.PublicController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.CategoryService;
import vietravel.example.vietravel.dto.CategoryDto;
import vietravel.example.vietravel.dto.TourDto;

import java.util.List;

@RestController
@RequestMapping("/api/public/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Get category by id
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/{id}/tours")
    public ResponseEntity<List<TourDto>> getToursByCategory(@PathVariable Long id,   @RequestParam(required = false, defaultValue = "top") String sortBy) {
        return ResponseEntity.ok(categoryService.getToursByCategoryId(id, sortBy));
    }


}
