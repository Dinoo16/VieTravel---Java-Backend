package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import vietravel.example.vietravel.Model.Category;
import vietravel.example.vietravel.Model.Tour;
import vietravel.example.vietravel.Repository.CategoryRepository;
import vietravel.example.vietravel.Service.CategoryService;
import vietravel.example.vietravel.dto.CategoryDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        if(categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException("Category name already exists: " + dto.getName());
        }
        Category category = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .image(dto.getImage())
                .build();
        return toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (dto.getName() != null &&
                !dto.getName().equalsIgnoreCase(category.getName()) &&
                categoryRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new IllegalArgumentException("Category name already exists: " + dto.getName());
        }

        if (dto.getName() != null) {
            category.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            category.setDescription(dto.getDescription());
        }
        if (dto.getImage() != null) {
            category.setImage(dto.getImage());
        }
        return toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Không cho xóa nếu là category duy nhất của tour nào đó
        for (Tour tour : category.getTours()) {
            if (tour.getCategories().size() == 1) {
                throw new IllegalStateException("Cannot delete category. Tour '" + tour.getTitle() + "' requires at least one category.");
            }
        }

        // Gỡ liên kết
        for (Tour tour : category.getTours()) {
            tour.getCategories().remove(category);
        }

        categoryRepository.delete(category);
    }


    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return toDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getCategoryId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        dto.setImage(baseUrl + category.getImage());
        return dto;
    }
}
