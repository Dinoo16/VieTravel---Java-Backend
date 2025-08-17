package vietravel.example.vietravel.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vietravel.example.vietravel.Model.Category;
import vietravel.example.vietravel.Model.Tour;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find by name
    List<Category> findByNameIn(List<String> names);

    //check if exist category by name
    boolean existsByNameIgnoreCase(String categoryName);

    @Query("SELECT t FROM Tour t JOIN t.categories c WHERE c.categoryId = :categoryId")
    List<Tour> findToursByCategoryId(@Param("categoryId") Long categoryId);

}
