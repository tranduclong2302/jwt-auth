package lifesup.com.jwtauth.sercurity.userprincal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lifesup.com.jwtauth.model.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserPrinciple implements UserDetails {
    private Long id;
    private String name;
    private String username;
    private String email;
    @JsonIgnore //khong truyen password ra ben ngoai
    private String password;
    private String avatar;
    private Collection<? extends GrantedAuthority> roles;

    public UserPrinciple() {
    }

    public UserPrinciple(Long id, String name, String username,
                         String email, String password, String avatar,
                         Collection<? extends GrantedAuthority> roles) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.roles = roles;
    }


    //vung` nho de luu user
    //nguoi dung dang nhap hien tai (co nhieu nguoi dung dang nhap)
    public static UserPrinciple build(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
        return new UserPrinciple(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getAvatar(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrinciple user = (UserPrinciple) o;
        return Objects.equals(id, user.id);
    }

}
