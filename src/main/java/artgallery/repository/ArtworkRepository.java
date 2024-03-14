package artgallery.repository;

import artgallery.entity.Artwork;
import artgallery.wrapper.ArtworkWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork, Integer> {

    List<ArtworkWrapper> getAllArtworks();

    ArtworkWrapper getArtworkById(@Param("id") Integer id);

    List<ArtworkWrapper> getArtworksByCategoryId(@Param("id") Integer id);
}
