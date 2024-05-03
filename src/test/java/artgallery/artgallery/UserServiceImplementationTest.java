package artgallery.artgallery;

import artgallery.JWT.CustomerUsersDetailsService;
import artgallery.JWT.JWTFilter;
import artgallery.JWT.JWTUtil;
import artgallery.entity.User;
import artgallery.repository.UserRepository;
import artgallery.serviceImpl.UserServiceImplementation;
import artgallery.wrapper.UserWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceImplementationTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomerUsersDetailsService customerUsersDetailsService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private JWTFilter jwtFilter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImplementation userService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_WhenValidData_ReturnsOK() {
        // Arrange
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("firstName", "John");
        requestMap.put("lastName", "Doe");
        requestMap.put("contactNumber", "1234567890");
        requestMap.put("email", "john.doe@example.com");
        requestMap.put("password", "password");

        when(userRepository.findUserByEmail("john.doe@example.com")).thenReturn(null);

        // Act
        ResponseEntity<String> response = userService.signUp(requestMap);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"Successfully Registered.\"}", response.getBody());
    }


    @Test
    void signUp_WhenEmailAlreadyExists_ReturnsBadRequest() {
        // Arrange
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("firstName", "John");
        requestMap.put("lastName", "Doe");
        requestMap.put("contactNumber", "1234567890");
        requestMap.put("email", "john.doe@example.com");
        requestMap.put("password", "password");

        when(userRepository.findUserByEmail("john.doe@example.com")).thenReturn(new User());

        // Act
        ResponseEntity<String> response = userService.signUp(requestMap);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\":\"Email already exists!\"}", response.getBody());
    }

    @Test
    void getAll_WhenAdmin_ReturnsListOfAllUsers(){
        // Arrange
        // To simulate the response of the method
        List<UserWrapper> userWrapperList = new ArrayList<>();

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(userRepository.getAll()).thenReturn(userWrapperList);

        // Act
        ResponseEntity<List<UserWrapper>> response = userService.getAll();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userWrapperList, response.getBody());
    }

    @Test
    void getAll_WhenUser_ReturnsUnauthorized(){
        // Arrange
        // To simulate the response of the method
        List<UserWrapper> userWrapperList = new ArrayList<>();

        when(jwtFilter.isUser()).thenReturn(true);
        when(userRepository.getAll()).thenReturn(userWrapperList);

        // Act
        ResponseEntity<List<UserWrapper>> response = userService.getAll();

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void deleteUser_WhenFound_ReturnOK(){
        // Arrange
        Optional<User> optionalUser = Optional.of(new User());

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(userRepository.findById(1)).thenReturn(optionalUser);

        // Act
        ResponseEntity<String> response = userService.deleteUser(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"User deleted successfully!\"}", response.getBody());
    }

    @Test
    void deleteUser_WhenNotFound_ReturnUserNotFoundResponse(){
        // Arrange
        Optional<User> optionalUser = Optional.of(new User());

        when(jwtFilter.isAdmin()).thenReturn(true);
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = userService.deleteUser(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\":\"User id doesn't exist.\"}", response.getBody());
    }


    @Test
    void deleteUser_WhenUserIsUnauthorized_ReturnUnauthorizedResponse(){
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);

        // Act
        ResponseEntity<String> response = userService.deleteUser(1);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\":\"Unauthorized access!\"}", response.getBody());
    }

    @Test
    void updateUser_WhenUserIsUnauthorized_ReturnUnauthorizedResponse(){
        // Arrange
        when(jwtFilter.isUser()).thenReturn(true);

        // Act
        ResponseEntity<String> response = userService.update(new HashMap<>());

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\":\"Unauthorized access!\"}", response.getBody());
    }
}
