package vietravel.example.vietravel.Service;

import vietravel.example.vietravel.dto.CategoryDto;
import vietravel.example.vietravel.dto.TourDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);

    CategoryDto getCategoryById(Long id);

    List<CategoryDto> getAllCategories();

    List<TourDto> getToursByCategoryId(Long categoryId, String sortBy);
}
