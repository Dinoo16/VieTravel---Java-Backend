package vietravel.example.vietravel.Model;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionId;

    private String name;

    private String description;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<Destination> destinations;
}

