package vietravel.example.vietravel.Controller.UserController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.CategoryService;
import vietravel.example.vietravel.dto.CategoryDto;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER') or hasRole('GUIDE')")
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


}
