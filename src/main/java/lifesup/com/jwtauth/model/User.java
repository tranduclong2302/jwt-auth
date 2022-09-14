package lifesup.com.jwtauth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
        //ngan chan khong cho tao tai khoan va email trung lap
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank //khong duoc de trong
    @Size(min = 5, max = 60)
    private String name;
    @NotBlank //khong duoc de trong
    @Size(min = 5, max = 60)
    private String username;
    @NaturalId
    @NotBlank //khong duoc de trong
    @Size(max = 60)
    @Email
    private String email;
    @JsonIgnore //khong truyen password ra ben ngoai
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
    @Lob //LOB hoặc Large OBject đề cập đến một kiểu dữ liệu có độ dài thay đổi để lưu trữ các đối tượng lớn.
    private String avatar;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles = new HashSet<>(); //luu tap hop ca phan tu khac nhau khong trung lap

    public User(@NotBlank @Size(min = 5, max = 60) String name,
                @NotBlank @Size(min = 5, max = 60) String username,
                @NotBlank @Size(max = 60) @Email String email,
                @NotBlank @Size(min = 6, max = 100) String encode) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = encode;
    }
}
