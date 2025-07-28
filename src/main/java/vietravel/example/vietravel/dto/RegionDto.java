package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import vietravel.example.vietravel.Model.Destination;

import java.util.List;

@Data
public class RegionDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    private List<Destination> destinations;
}
