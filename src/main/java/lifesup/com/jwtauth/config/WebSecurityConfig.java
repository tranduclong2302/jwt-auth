package lifesup.com.jwtauth.config;

import lifesup.com.jwtauth.sercurity.jwt.JwtAuthenticationEntryPoint;
import lifesup.com.jwtauth.sercurity.jwt.JwtProvider;
import lifesup.com.jwtauth.sercurity.jwt.JwtTokenFilter;
import lifesup.com.jwtauth.sercurity.userprincal.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Bean
    //Do lớp này k đánh component nên phải khởi tạo bean
    public JwtTokenFilter jwtTokenFilter(){
        return new JwtTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                // Ngăn chặn request từ một domain khác (cors)
                .and()
                .csrf()
                .disable()
                .authorizeRequests().antMatchers("/api/auth/**").permitAll() //cho phép truy cập tất cả các đường dẫn
                .anyRequest().authenticated()//bất cứ yêu cầu nào đều cần xác thực
                .and().exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Không sử dụng session lưu lại trạng thái của principal
                .and()
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);// Lớp lọc jwt token sẽ được thực thi trước các lớp lọc mặc định
    }
}
