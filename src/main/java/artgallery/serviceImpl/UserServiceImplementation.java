package artgallery.serviceImpl;

import artgallery.JWT.CustomerUsersDetailsService;
import artgallery.JWT.JWTFilter;
import artgallery.JWT.JWTUtil;
import artgallery.constants.ArtGalleryConstants;
import artgallery.entity.Category;
import artgallery.repository.UserRepository;
import artgallery.entity.User;
import artgallery.service.UserService;
import artgallery.utils.ArtGalleryUtils;
import artgallery.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.*;

@Slf4j
@Service
public class UserServiceImplementation implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    JWTFilter jwtFilter;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap); // for debugging

        try {
            if (!validateSignUpMap(requestMap)) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }

            User user = userRepository.findUserByEmail(requestMap.get("email"));
            if (!Objects.isNull(user)) {
                return ArtGalleryUtils.getResponseEntity("Email already exists!", HttpStatus.BAD_REQUEST);
            }

            user = getUserFromMap(requestMap, false);
            userRepository.save(user);
            return ArtGalleryUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
        } catch (ConstraintViolationException ex) {
            StringBuilder errorMessage = new StringBuilder();
            ex.getConstraintViolations().forEach(violation -> {
                if (errorMessage.length() > 0) {
                    errorMessage.append(";");
                }
                errorMessage.append("\n-> ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            });
            return ArtGalleryUtils.getResponseEntity(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("{}", ex);

        }

        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login {}");
        try {
            // Authenticate the user
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

            if (auth.isAuthenticated()) {
                // Check if the admin allows that user (approved)
                if (customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    artgallery.entity.User userDetail = customerUsersDetailsService.getUserDetail();
                    String token = jwtUtil.generateToken(userDetail.getEmail(), userDetail.getRole());
                    return new ResponseEntity<String>("{\"token\":\"" + token + "\"}", HttpStatus.OK);
                } else {
                    return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval!",
                            HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("{}", ex);
        }

        return new ResponseEntity<String>("{\"token\":\"" + "Bad request\"}", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        log.info("Inside getAllUsers");
        try {
            if (!jwtFilter.isAdmin()) {
                log.info("not admin");
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            } else {
                log.info("admin");
                return new ResponseEntity<>(userRepository.getAllUsers(), HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("{}", ex);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<String>> getAllEmailAdmins() {
        log.info("Inside getAllUsers");
        try {
            if (!jwtFilter.isAdmin()) {
                log.info("not admin");
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            } else {
                log.info("admin");
                return new ResponseEntity<>(userRepository.getAllEmailAdmins(), HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("{}", ex);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<UserWrapper> getById(Integer id) {
        try {
            return new ResponseEntity<>(userRepository.getUserById(id), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new UserWrapper(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isUser()) {
                log.info("user");
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }

            // Admin
            log.info("admin");
            if (validateUserMap(requestMap)) {
                Optional<User> userOptional = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (userOptional.isEmpty()) {
                    return ArtGalleryUtils.getResponseEntity("User ID does not exist!", HttpStatus.OK);
                }
                User existingUser = userOptional.get();
                String existingPassword = existingUser.getPassword();
                User user = getUserFromMap(requestMap, true);
                user.setPassword(existingPassword);
                log.info("User" + user);
                userRepository.save(user);
                return ArtGalleryUtils.getResponseEntity("User  was successfully updated!", HttpStatus.OK);
            }
        } catch (TransactionSystemException ex) {
            return handleTransactionSystemException(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("{}", ex);
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if(jwtFilter.isAdmin()) {
                Optional<User> optional = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()) {
                    userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    return ArtGalleryUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
                } else {
                    return ArtGalleryUtils.getResponseEntity("User id doesn't exist.", HttpStatus.OK);
                }
            } else {
                ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if (Objects.isNull(userObj)) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (!passwordEncoder.matches(requestMap.get("oldPassword"), userObj.getPassword())) {
                return ArtGalleryUtils.getResponseEntity("Incorrect password.", HttpStatus.BAD_REQUEST);
            }
            // Old password ok -> set password as new password
            userObj.setPassword(requestMap.get("newPassword"));
            userRepository.save(userObj);
            return ArtGalleryUtils.getResponseEntity("Password updated successfully!", HttpStatus.OK);
        } catch (TransactionSystemException ex) {
            return handleTransactionSystemException(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return ArtGalleryUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getByRole(String role) {
        log.info("Inside getAllUsers");
        try {
            if (!jwtFilter.isAdmin()) {
                log.info("not admin");
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            } else {
                log.info("admin");
                return new ResponseEntity<>(userRepository.getUserByRole(role), HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("{}", ex);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAll() {
        log.info("Inside getAllUsers");
        try {
            if (!jwtFilter.isAdmin()) {
                log.info("not admin");
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            } else {
                log.info("admin");
                return new ResponseEntity<>(userRepository.getAll(), HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("{}", ex);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteUser(Integer id) {
        try {
            if (jwtFilter.isUser()) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
            Optional optional = userRepository.findById(id);
            if (!optional.isEmpty()) {
                userRepository.deleteById(id);
                return ArtGalleryUtils.getResponseEntity("User deleted successfully!", HttpStatus.OK);
            } else {
                return ArtGalleryUtils.getResponseEntity("User id doesn't exist.", HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("firstName") && requestMap.containsKey("lastName") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap, boolean isUsedForUpdate) {

        User user = new User();

        if (isUsedForUpdate) {
            user.setId(Integer.parseInt(requestMap.get("id")));
            user.setRole(requestMap.get("role"));
        } else {
            log.info("entered here");
            user.setRole("user");
            user.setPassword(requestMap.get("password"));
            user.setStatus("false");
        }

        if(user.getRole().equals("admin")){
            user.setStatus("true");
        }
        user.setFirstName(requestMap.get("firstName"));
        user.setLastName(requestMap.get("lastName"));
        user.setEmail(requestMap.get("email"));
        user.setAddress(requestMap.get("address"));
        user.setContactNumber(requestMap.get("contactNumber"));

        return user;
    }

    private boolean validateUserMap(Map<String, String> requestMap) {
        if (!requestMap.containsKey("email")) {
            return false;
        }
        if (requestMap.containsKey("id")) {
            return true;
        }
        return false;
    }

    private ResponseEntity<String> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable rootCause = ex.getRootCause();
        if (rootCause instanceof ConstraintViolationException) {
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) rootCause;
            StringBuilder errorMessage = new StringBuilder();
            constraintViolationException.getConstraintViolations().forEach(violation -> {
                if (errorMessage.length() > 0) {
                    errorMessage.append(";");
                }
                errorMessage.append("\n-> ").append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
            });
            return ArtGalleryUtils.getResponseEntity(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        } else {
            ex.printStackTrace();
            log.error("{}", ex);
            return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
