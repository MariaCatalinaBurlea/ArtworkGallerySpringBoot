package artgallery.serviceImpl;

import artgallery.JWT.CustomerUsersDetailsService;
import artgallery.JWT.JWTFilter;
import artgallery.JWT.JWTUtil;
import artgallery.constants.ArtGalleryConstants;
import artgallery.repository.UserRepository;
import artgallery.entity.User;
import artgallery.service.UserService;
import artgallery.utils.ArtGalleryUtils;
//import artgallery.utils.EmailUtils;
import artgallery.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

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

            userRepository.save(getUserFromMap(requestMap));
            return ArtGalleryUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
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
                    return new ResponseEntity<String>("{\"token\":\"" + jwtUtil.generateToken(
                            userDetail.getEmail(), userDetail.getRole() + "\"}"),
                            HttpStatus.OK);
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
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isUser()) {
                log.info("user");
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            } else {
                log.info("admin");
                Optional<User> userOptional = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (userOptional.isEmpty()) {
                    return ArtGalleryUtils.getResponseEntity("User ID does not exist!", HttpStatus.OK);
                }
                userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
//                sendMailToAllAdmins(requestMap.get("status"), userOptional.get().getEmail(), userDAO.getAllAdmins());
                return ArtGalleryUtils.getResponseEntity("User status was successfully updated!", HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("{}", ex);
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if(userObj.equals(null)) {
                return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if(!userObj.getPassword().equals(requestMap.get("oldPassword"))) {
                return ArtGalleryUtils.getResponseEntity("Incorrect password.", HttpStatus.BAD_REQUEST);
            }
            // Old password ok -> set password as new password
            userObj.setPassword(requestMap.get("newPassword"));
            userRepository.save(userObj);
            return ArtGalleryUtils.getResponseEntity("Password updated successfully!", HttpStatus.OK);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return ArtGalleryUtils.getResponseEntity(ArtGalleryConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("firstName") && requestMap.containsKey("lastName") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User(requestMap.get("firstName"), requestMap.get("lastName"), requestMap.get("contactNumber"),
                requestMap.get("email"), requestMap.get("password"), requestMap.get("address"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

//    private void sendMailToAllAdmins(String status, String user, List<String> adminList) {
//        adminList.remove(jwtFilter.getCurrentUser());
//        if (status != null && status.equalsIgnoreCase("true")) {
//            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),
//                    "Account approved", "USER: " + user + "is approved by " + "ADMIN: " + jwtFilter.getCurrentUser(),
//                    adminList);
//        } else {
//            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),
//                    "Account disabled", "USER: " + user + "is disabled by " + "ADMIN: " + jwtFilter.getCurrentUser(),
//                    adminList);
//        }
//    }
}
