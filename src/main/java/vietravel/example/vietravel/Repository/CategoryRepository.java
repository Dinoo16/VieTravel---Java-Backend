package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find by name
    List<Category> findByNameIn(List<String> names);

    //check if exist category by name
    boolean existsByNameIgnoreCase(String categoryName);
}
