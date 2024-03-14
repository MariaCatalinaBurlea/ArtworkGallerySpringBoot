package artgallery.repository;

import artgallery.entity.Category;
import artgallery.wrapper.ArtworkWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> getAllCategories();
    Category getCategoryById(@Param("id") Integer id);
}
