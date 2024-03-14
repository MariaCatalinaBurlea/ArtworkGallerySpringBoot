package artgallery.controllerImpl;

import artgallery.constants.ArtGalleryConstants;
import artgallery.controller.ArtworkController;
import artgallery.service.ArtworkService;
import artgallery.utils.ArtGalleryUtils;
import artgallery.wrapper.ArtworkWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ArtworkControllerImplementation implements ArtworkController {
    @Autowired
    ArtworkService artworkService;

    @Override
    public ResponseEntity<String> addNewArtwork(Map<String, String> requestMap) {
        try {
            log.info("Inside rest impl artwork");
            return artworkService.addNewArtwork(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ArtworkWrapper>> getAllArtworks(String filterValue) {
        try {
            return artworkService.getAllArtworks(filterValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ArtworkWrapper> getById(Integer id) {
        try {
            return artworkService.getById(id);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArtworkWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ArtworkWrapper>> getByCategory(Integer id) {
        try {
            return artworkService.getByCategoryId(id);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateArtwork(Map<String, String> requestMap) {
        try {
            log.info("Inside update artwork");
            return artworkService.updateArtwork(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteArtwork(Integer id) {
        try {
            return artworkService.deleteArtwork(id);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
