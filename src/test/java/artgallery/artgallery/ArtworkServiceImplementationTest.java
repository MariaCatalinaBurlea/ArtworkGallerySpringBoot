package artgallery.artgallery;

import artgallery.JWT.JWTFilter;
import artgallery.entity.Artwork;
import artgallery.repository.ArtworkRepository;
import artgallery.serviceImpl.ArtworkServiceImplementation;
import artgallery.wrapper.ArtworkWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ArtworkServiceImplementationTest {
    @Mock
    private ArtworkRepository artworkRepository;

    @Mock
    private JWTFilter jwtFilter;

    @InjectMocks
    private ArtworkServiceImplementation artworkService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addNewArtwork_WhenValidData_ReturnsOK(){
        // Arrange
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("title", "Test Artwork");
        requestMap.put("categoryId", "1");
        requestMap.put("price", "100");
        requestMap.put("size", "10x10");
        requestMap.put("description", "Test description");

        when(jwtFilter.isUser()).thenReturn(false);
        when(artworkRepository.save(any(Artwork.class))).thenReturn(null);

        // Act
        ResponseEntity<String> response = artworkService.addNewArtwork(requestMap);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Artwork added successfully\"}", response.getBody());
    }

    @Test
    void getAllArtworks_WhenReturnsListOfArtworks(){
        // Arrange
        // To simulate the response of the method
        List<ArtworkWrapper> artworkWrapperList = new ArrayList<>();

        // Setup the behavior of the repository , when calling getAllArtworks() it should return artworkWrapperList
         when(artworkRepository.getAllArtworks()).thenReturn(artworkWrapperList);

         // Act
        ResponseEntity<List<ArtworkWrapper>> response = artworkService.getAllArtworks("");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(artworkWrapperList, response.getBody());
    }

    @Test
    void updateArtwork_WhenValidData_ReturnsOK() throws Exception {
        // Arrange
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("id", "4");
        requestMap.put("title", "Test Artwork");
        requestMap.put("categoryId", "1");
        requestMap.put("price", "100");
        requestMap.put("size", "10x10");
        requestMap.put("description", "Test description");

        Optional<Artwork> optionalArtwork = Optional.of(new Artwork());
        when(jwtFilter.isUser()).thenReturn(false);

        // Invoke the private method using reflection
        Method validateArtworkMapMethod = ArtworkServiceImplementation.class.getDeclaredMethod("validateArtworkMap", Map.class, boolean.class);
        validateArtworkMapMethod.setAccessible(true);
        boolean validationResult = (boolean) validateArtworkMapMethod.invoke(artworkService, requestMap, true);

        when(artworkRepository.findById(4)).thenReturn(optionalArtwork);
        when(artworkRepository.save(any(Artwork.class))).thenReturn(null);

        // Act
        ResponseEntity<String> response = artworkService.updateArtwork(requestMap);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Artwork updated successfully!\"}", response.getBody());
    }

    @Test
    void deleteArtwork_WhenExistingId_ReturnsOK() {
        // Arrange
        Integer id = 1;
        Optional<Artwork> optionalArtwork = Optional.of(new Artwork());

        when(jwtFilter.isUser()).thenReturn(false);
        when(artworkRepository.findById(id)).thenReturn(optionalArtwork);

        // Act
        ResponseEntity<String> response = artworkService.deleteArtwork(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Artwork deleted successfully!\"}", response.getBody());
    }

    @Test
    void getById_WhenExistingId_ReturnsArtwork() {
        // Arrange
        Integer id = 1;
        ArtworkWrapper artwork = new ArtworkWrapper();

        when(artworkRepository.getArtworkById(id)).thenReturn(artwork);

        // Act
        ResponseEntity<ArtworkWrapper> response = artworkService.getById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(artwork, response.getBody());
    }

    @Test
    void getByCategoryId_WhenExistingId_ReturnsArtwork() {
        // Arrange
        Integer id = 1;
        List<ArtworkWrapper> artworkList = new ArrayList<>();

        when(artworkRepository.getArtworksByCategoryId(id)).thenReturn(artworkList);

        // Act
        ResponseEntity<List<ArtworkWrapper>> response = artworkService.getByCategoryId(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(artworkList, response.getBody());
    }
}
