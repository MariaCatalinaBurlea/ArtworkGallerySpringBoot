package artgallery.controllerImpl;

import artgallery.constants.ArtGalleryConstants;
import artgallery.entity.Category;
import artgallery.controller.CategoryController;
import artgallery.service.CategoryService;
import artgallery.utils.ArtGalleryUtils;
import artgallery.wrapper.ArtworkWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryControllerImplementation implements CategoryController {
    @Autowired
    CategoryService categoryService;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            return categoryService.addNewCategory(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories(String filterValue) {
        try {
            return categoryService.getAllCategories(filterValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            return categoryService.updateCategory(requestMap);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteCategory(Integer id) {
        try {
            return categoryService.deleteCategory(id);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Category> getById(Integer id) {
        try {
            return categoryService.getById(id);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new Category(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
