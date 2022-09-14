package lifesup.com.jwtauth.service;

import lifesup.com.jwtauth.model.Role;
import lifesup.com.jwtauth.model.RoleName;

import java.util.Optional;

public interface IRoleService {
    Optional<Role> findByName(RoleName name);
}
