package lifesup.com.jwtauth.service;


import lifesup.com.jwtauth.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    Optional<User> findByUsername(String name); //tim user co trong database k
    Boolean existsByUsername(String username); //username da co trong database chua khi tao du lieu
    Boolean existsByEmail(String email);
    User save(User user);
    List<User> findAll();
}
