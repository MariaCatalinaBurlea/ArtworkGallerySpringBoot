package artgallery.controller;

import artgallery.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RequestMapping(path = "/user")
public interface UserController {
    @PostMapping(path = "/signup")
    ResponseEntity<String> signUp(@Valid @RequestBody(required = true) Map<String, String> requestMap);

    @PostMapping(path = "/login")
    ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap);

    @GetMapping(path = "/checkToken")
    ResponseEntity<String> checkToken();

    @GetMapping(path = "/getByRole/{role}")
    ResponseEntity<List<UserWrapper>> getByRole(@PathVariable String role);

    @GetMapping(path = "/getAll")
    ResponseEntity<List<UserWrapper>> getAll();

    @GetMapping(path = "/getUsers")
    ResponseEntity<List<UserWrapper>> getAllUsers();

    @GetMapping(path = "/getAdminEmails")
    ResponseEntity<List<String>> getAllAdminEmails();

    @GetMapping(path = "/getById/{id}")
    ResponseEntity<UserWrapper> getById(@PathVariable Integer id);

    @PutMapping(path = "/update")
    ResponseEntity<String> update(@Valid @RequestBody(required = true) Map<String, String> requestMap);

    @PutMapping(path = "/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String, String> requestMap);

    @PutMapping(path = "/changePassword")
    ResponseEntity<String> changePassword(@RequestBody(required = true) Map<String, String> requestMap);

    @DeleteMapping(path = "/delete/{id}")
    ResponseEntity<String> deleteUser(@PathVariable Integer id);
}

