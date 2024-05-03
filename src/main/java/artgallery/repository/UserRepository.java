package artgallery.repository;

import artgallery.entity.User;
import artgallery.wrapper.UserWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByEmail(@Param("email") String email);

    UserWrapper getUserById(@Param("id") Integer id);
    List<UserWrapper> getUserByRole(@Param("role") String role);
    List<UserWrapper> getAll();
    List<UserWrapper> getAllUsers();
    List<String> getAllEmailAdmins();

    User findByEmail(String email);

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);
}
