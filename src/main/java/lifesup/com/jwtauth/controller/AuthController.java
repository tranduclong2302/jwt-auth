package lifesup.com.jwtauth.controller;

import lifesup.com.jwtauth.dto.request.SignInForm;
import lifesup.com.jwtauth.dto.request.SignUpForm;
import lifesup.com.jwtauth.dto.response.JwtResponse;
import lifesup.com.jwtauth.dto.response.ResponseMessage;
import lifesup.com.jwtauth.model.Role;
import lifesup.com.jwtauth.model.RoleName;
import lifesup.com.jwtauth.model.User;
import lifesup.com.jwtauth.sercurity.jwt.JwtProvider;
import lifesup.com.jwtauth.sercurity.userprincal.UserPrinciple;
import lifesup.com.jwtauth.service.IRoleService;
import lifesup.com.jwtauth.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/sign-up")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm){
        if (userService.existsByUsername(signUpForm.getUsername())){
            return new ResponseEntity<>(new ResponseMessage("The username existed! Please try again!"), HttpStatus.OK);
        }
        if (userService.existsByEmail(signUpForm.getEmail())){
            return new ResponseEntity<>(new ResponseMessage("The email existed! Please try again!"), HttpStatus.OK);
        }
         User user = new User(signUpForm.getName(),
                signUpForm.getUsername(),
                signUpForm.getEmail(),
                passwordEncoder.encode(signUpForm.getPassword()));
        Set<String> strRoles = signUpForm.getRoles();
        Set<Role> roles = new HashSet<>();
        strRoles.forEach(role ->{
            switch (role){
                case "admin":
                    Role admRole = roleService.findByName(RoleName.ADMIN).orElseThrow( () ->
                            new RuntimeException("Role not found"));
                    roles.add(admRole);
                    break;

                case "pm":
                    Role pmRole = roleService.findByName(RoleName.PM).orElseThrow( () ->
                            new RuntimeException("Role not found"));
                    roles.add(pmRole);
                    break;
                default:
                    //k để gì thì sẽ tạo là user
                    Role userRole = roleService.findByName(RoleName.USER).orElseThrow( () ->
                            new RuntimeException("Role not found"));
                    roles.add(userRole);
                    break;
            }
        });
        user.setRoles(roles);
        userService.save(user);
        return new ResponseEntity<>(new ResponseMessage("Create user success"), HttpStatus.OK);
    }

    @PostMapping("/signin")
        public ResponseEntity<?> login(@Valid @RequestBody SignInForm signInForm){
        //Xacs thuc tai khoan
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInForm.getUsername(),
                signInForm.getPassword()
        ));
        //SecurityContext được sử dụng để lưu trữ các chi tiết của người dùng
        // hiện được xác thực, còn được gọi là nguyên tắc
        //SecurityContextHolder là lớp trợ giúp, cung cấp quyền truy cập
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication); //token cuar he thong
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal(); //Gan lop user hien tai
        return ResponseEntity.ok(new JwtResponse(token, userPrinciple.getName(),userPrinciple.getAuthorities()));
    }

    @GetMapping("findall")
    public List<User> findAll(){
        return userService.findAll();
    }
}
