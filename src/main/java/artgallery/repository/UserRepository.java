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

    List<UserWrapper> getAllUsers();
    List<String> getAllAdmins();

    @Transactional
    @Modifying
    int updateStatus(@Param("status") String status, @Param("id") int id);

    User findByEmail(String email);
}
