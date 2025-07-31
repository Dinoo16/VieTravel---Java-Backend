package vietravel.example.vietravel.Data;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vietravel.example.vietravel.Model.Region;
import vietravel.example.vietravel.Repository.RegionRepository;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RegionDataInitializer {

    private final RegionRepository regionRepository;

    @PostConstruct
    public void initRegions() {
        if (regionRepository.count() == 0) {
            List<Region> defaultRegions = Arrays.asList(
                    Region.builder().name("Northern").description("Northern Vietnam").build(),
                    Region.builder().name("Central").description("Central Vietnam").build(),
                    Region.builder().name("Southern").description("Southern Vietnam").build()
            );
            regionRepository.saveAll(defaultRegions);
        }
    }
}
