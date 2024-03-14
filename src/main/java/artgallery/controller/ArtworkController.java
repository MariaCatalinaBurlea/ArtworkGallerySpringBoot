package artgallery.controller;

import artgallery.wrapper.ArtworkWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/artwork")
public interface ArtworkController {
    @PostMapping(path="/add")
    ResponseEntity<String> addNewArtwork(@RequestBody Map<String, String> requestMap);

    @GetMapping(path = "/get")
    public ResponseEntity<List<ArtworkWrapper>> getAllArtworks(@RequestParam(required = false) String filterValue);

    @GetMapping(path = "/getById/{id}")
    ResponseEntity<ArtworkWrapper> getById(@PathVariable Integer id);

    @GetMapping(path = "/getByCategory/{id}")
    ResponseEntity<List<ArtworkWrapper>> getByCategory(@PathVariable Integer id);

    @PutMapping(path="/update")
    ResponseEntity<String> updateArtwork(@RequestBody(required=true) Map<String, String> requestMap);

    @DeleteMapping(path="/delete/{id}")
    ResponseEntity<String> deleteArtwork(@PathVariable Integer id);
}
