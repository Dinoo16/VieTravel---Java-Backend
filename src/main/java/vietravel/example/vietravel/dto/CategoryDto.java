package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Category name is required")
    @Size(max = 100, message = "Category name must be less than 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    private String image;
}
