package lifesup.com.jwtauth.service.impl;

import lifesup.com.jwtauth.model.Role;
import lifesup.com.jwtauth.model.RoleName;
import lifesup.com.jwtauth.repository.RoleRepository;
import lifesup.com.jwtauth.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(RoleName name) {
        return roleRepository.findByName(name);
    }
}
