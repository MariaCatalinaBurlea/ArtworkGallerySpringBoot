package artgallery.service;

import artgallery.wrapper.ArtworkWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ArtworkService {
    ResponseEntity<String> addNewArtwork(Map<String, String> requestMap);
    ResponseEntity<List<ArtworkWrapper>> getAllArtworks(String filterValue);
    ResponseEntity<String> updateArtwork(Map<String, String> requestMap);
    ResponseEntity<String> deleteArtwork(Integer id);
    ResponseEntity<ArtworkWrapper> getById(Integer id);
    ResponseEntity<List<ArtworkWrapper>> getByCategoryId(Integer id);
}
