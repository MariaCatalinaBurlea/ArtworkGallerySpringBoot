package artgallery.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ArtGalleryUtils {
    // utility methods used in any service central classes or classes
    private ArtGalleryUtils() {

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>("{\"message\":\"" + responseMessage + "\"}", httpStatus);
    }
}
