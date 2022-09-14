package lifesup.com.jwtauth.repository;

import lifesup.com.jwtauth.model.Role;
import lifesup.com.jwtauth.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
