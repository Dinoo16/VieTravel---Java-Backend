package vietravel.example.vietravel.dto;

import jakarta.validation.constraints.*;
import jdk.jshell.Snippet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vietravel.example.vietravel.Model.Region;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DestinationDto {
    private Long id;

    @NotBlank(message = "Destination name is required")
    @Size(max = 100, message = "Destination name must be less than 100 characters")
    private String name;

    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotNull(message = "Region ID is required")
    private Long regionId;

    private List<Long> tourIds;

    private String backgroundImage;

}
