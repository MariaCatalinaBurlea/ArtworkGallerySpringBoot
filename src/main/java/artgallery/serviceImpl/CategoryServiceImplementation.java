package artgallery.serviceImpl;

import artgallery.JWT.JWTFilter;
import artgallery.constants.ArtGalleryConstants;
import artgallery.repository.CategoryRepository;
import artgallery.entity.Category;
import artgallery.service.CategoryService;
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
public class CategoryServiceImplementation implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    JWTFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isUser()) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            if (validateCategoryMap(requestMap, false)) {
                categoryRepository.save(getCategoryFromMap(requestMap, false));
                return ArtGalleryUtils.getResponseEntity("Category added successfully", HttpStatus.OK);
            }
            return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategories(String filterValue) {
        try {
            if (filterValue != null && !filterValue.isEmpty() && filterValue.equalsIgnoreCase("true")) {
                return new ResponseEntity<List<Category>>(categoryRepository.getAllCategories(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isUser()) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            if (validateCategoryMap(requestMap, true)) {
                Optional optional = categoryRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (optional.isEmpty()) {
                    return ArtGalleryUtils.getResponseEntity("Category id does not exist", HttpStatus.OK);
                }
                categoryRepository.save(getCategoryFromMap(requestMap, true));
                return ArtGalleryUtils.getResponseEntity("Category updated successfully!", HttpStatus.OK);
            }

            return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Category> getById(Integer id) {
        try {
            return new ResponseEntity<>(categoryRepository.getCategoryById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new Category(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteCategory(Integer id) {
        try {
            if (jwtFilter.isUser()) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional optional = categoryRepository.findById(id);
            if (!optional.isEmpty()) {
                categoryRepository.deleteById(id);
                return ArtGalleryUtils.getResponseEntity("Category deleted successfully!", HttpStatus.OK);
            } else {
                return ArtGalleryUtils.getResponseEntity("Category id doesn't exist.", HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateID) {
        if (requestMap.containsKey("name")) {
            if (requestMap.containsKey("id") && validateID) {
                return true;
            } else {
                return !validateID;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, boolean isUsedForUpdate) {
        Category category = new Category();
        if (isUsedForUpdate) {
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }
}
