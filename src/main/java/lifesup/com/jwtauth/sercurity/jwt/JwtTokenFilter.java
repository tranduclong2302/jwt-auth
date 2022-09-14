package lifesup.com.jwtauth.sercurity.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtTokenFilter extends OncePerRequestFilter { //Bộ lọc một lần cho mỗi yêu cầu
    private static Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

    private static String HEADER = "Authorization";

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            //lấy jwt từ request
            String token = getJwt(request);
            if (token != null && jwtProvider.validateToken(token)){
                //lấy username từ chuỗi jwt
                String username =jwtProvider.getUsernameFromJwtToken(token);
                //lấy thông tin người dùng bằng username
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null){
                    //Nếu người dùng hợp lệ, set thông tin cho security context
                    UsernamePasswordAuthenticationToken
                            authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    //SecurityContext được sử dụng để lưu trữ các chi tiết của người dùng
                    // hiện được xác thực, còn được gọi là nguyên tắc
                    //SecurityContextHolder là lớp trợ giúp, cung cấp quyền truy cập
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }catch (Exception e){
            logger.error("failed in set user authentication", e);
        }
        filterChain.doFilter(request, response);
    }

    public String getJwt(HttpServletRequest request){
        // Lấy token từ header
        String authHeader = request.getHeader(HEADER);
        if (authHeader != null && authHeader.startsWith("Bearer")){
            return authHeader.replace("Bearer " , "");
        }
        return null;
    }
}
