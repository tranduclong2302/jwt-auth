package lifesup.com.jwtauth.sercurity.jwt;

import io.jsonwebtoken.*;
import lifesup.com.jwtauth.sercurity.userprincal.UserPrinciple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider { //lớp tạo ra token
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class); //ghi log cua class ra

    // Key này sẽ được sử dụng để mã hóa và giải mã
    private String jwtSecret = "long131463@nuce.edu.vn";

    private int jwtExpiration = 3600; // Token có hạn trong vòng 24 giờ kể từ thời điểm tạo, thời gian tính theo giây

    //tạo ra jwt từ thông tin user
    public String generateToken(Authentication authentication){
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();

        // 1. Định nghĩa các claims: issuer, expiration, subject, id
        // 2. Mã hóa token sử dụng thuật toán HS512 và key bí mật
        // 3. Convert thành chuỗi URL an toàn
        // 4. Cộng chuỗi đã sinh ra với tiền tố Bearer
        return Jwts.builder().setSubject(userPrinciple.getUsername())
                .setIssuedAt(new Date()) //thời gian ban hành
                .setExpiration(new Date(new Date().getTime() + jwtExpiration*1000)) //xet tgian sống
                .signWith(SignatureAlgorithm.HS512, jwtSecret)  // 2. Mã hóa token sử dụng thuật toán HS512 và key bí mật
                .compact(); //đóng lại
        //login sẽ gọi sang hàm này
    }

    //Check token có hợp lệ hay không
    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            logger.error("Invalid JWT signature -> Message: {} ", e);
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT malformed -> Message: {} ", e);
        }
        catch (ExpiredJwtException e){
            logger.error("Invalid JWT expired -> Message: {} ", e);
        }
        catch (UnsupportedJwtException e){
            logger.error("Invalid JWT unsupported -> Message: {} ", e);
        }
        catch (IllegalArgumentException e){
            logger.error("Invalid JWT IllegalArgument -> Message: {} ", e);
        }
        return false;
    }

    //Lấy thông tin user từ jwt
    public String getUsernameFromJwtToken(String token){
        String username = Jwts.parser()
                .setSigningKey(jwtSecret).parseClaimsJws(token)
                .getBody()
                .getSubject();
        return username;
    }
}
