package lifesup.com.jwtauth.repository;

import lifesup.com.jwtauth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name); //tim user co trong database k
    Boolean existsByUsername(String username); //username da co trong database chua khi tao du lieu
    Boolean existsByEmail(String email);


}
