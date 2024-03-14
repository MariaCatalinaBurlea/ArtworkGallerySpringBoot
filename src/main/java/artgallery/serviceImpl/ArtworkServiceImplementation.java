package artgallery.serviceImpl;

import artgallery.JWT.JWTFilter;
import artgallery.constants.ArtGalleryConstants;
import artgallery.repository.ArtworkRepository;
import artgallery.entity.Artwork;
import artgallery.entity.Category;
import artgallery.service.ArtworkService;
import artgallery.utils.ArtGalleryUtils;
import artgallery.wrapper.ArtworkWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ArtworkServiceImplementation implements ArtworkService {
    @Autowired
    ArtworkRepository artworkRepository;

    @Autowired
    JWTFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewArtwork(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isUser()) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            // Admin
            if (validateArtworkMap(requestMap, false)) {
                artworkRepository.save(getArtworkFromMap(requestMap, false));
                return ArtGalleryUtils.getResponseEntity("Artwork added successfully", HttpStatus.OK);
            }
            return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ArtworkWrapper>> getAllArtworks(String filterValue) {
        try {
            return new ResponseEntity<>(artworkRepository.getAllArtworks(), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateArtwork(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isUser()) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            // Admin
            if (!validateArtworkMap(requestMap, true)) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            Optional<Artwork> optional = artworkRepository.findById(Integer.parseInt(requestMap.get("id")));
            if (!optional.isEmpty()) {
                Artwork artwork = getArtworkFromMap(requestMap, true);
                artwork.setStatus(optional.get().getStatus());
                artworkRepository.save(artwork);
                return ArtGalleryUtils.getResponseEntity("Artwork updated successfully!", HttpStatus.OK);
            } else {
                return ArtGalleryUtils.getResponseEntity("Artwork id doesn't exist.", HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteArtwork(Integer id) {
        try {
            if (jwtFilter.isUser()) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional optional = artworkRepository.findById(id);
            if (!optional.isEmpty()) {
                artworkRepository.deleteById(id);
                return ArtGalleryUtils.getResponseEntity("Artwork deleted successfully!", HttpStatus.OK);
            } else {
                return ArtGalleryUtils.getResponseEntity("Artwork id doesn't exist.", HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ArtworkWrapper> getById(Integer id) {
        try {
            return new ResponseEntity<>(artworkRepository.getArtworkById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArtworkWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ArtworkWrapper>> getByCategoryId(Integer id) {
        try {
            return new ResponseEntity<>(artworkRepository.getArtworksByCategoryId(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Artwork getArtworkFromMap(Map<String, String> requestMap, boolean isUsedForUpdate) {
        // Get first the category(foreign key)
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Artwork artwork = new Artwork();
        if (isUsedForUpdate) {
            artwork.setId(Integer.parseInt(requestMap.get("id")));
        } else {
            // Product active by default
            artwork.setStatus("true");
        }

        artwork.setCategory(category);
        artwork.setTitle(requestMap.get("title"));
        artwork.setPrice(Integer.parseInt(requestMap.get("price")));
        artwork.setDescription(requestMap.get("description"));
        artwork.setSize(requestMap.get("size"));
        return artwork;
    }

    private boolean validateArtworkMap(Map<String, String> requestMap, boolean validateID) {
        if (requestMap.containsKey("title")) {
            if (requestMap.containsKey("id") && validateID) {
                return true;
            }
            return !validateID;
        }
        return false;
    }
}
