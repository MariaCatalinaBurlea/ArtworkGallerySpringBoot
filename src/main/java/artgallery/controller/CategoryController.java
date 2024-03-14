package artgallery.controller;

import artgallery.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface CategoryController {
    @PostMapping(path = "/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/get")
    public ResponseEntity<List<Category>> getAllCategories(@RequestParam(required = false) String filterValue);

    @GetMapping(path = "/getById/{id}")
    ResponseEntity<Category> getById(@PathVariable Integer id);

    @PutMapping(path = "/update")
    ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> requestMap);

    @DeleteMapping(path="/delete/{id}")
    ResponseEntity<String> deleteCategory(@PathVariable Integer id);
}
