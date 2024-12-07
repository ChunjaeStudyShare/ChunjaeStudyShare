package net.fullstack7.studyShare.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
/**
 * jwt 토큰 생성 예시
 * const jti = crypto.randomBytes(16).toString('hex');
 * const token = jwt.sign(
 *  { 
 *      userId: user.userId,
 *      email: user.email,
 *      name: user.name,
 *      jti: jti
 *  },
 *  process.env.JWT_SECRET,
 *  { 
 *      expiresIn: rememberMe ? '7d' : '1h',
 *      algorithm: 'HS256'  // SHA-256 명시
 *  }
 * );
 */
@Component
public class JwtUtil {

    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userId", String.class);
    }

    public String getJti(String token) {
        Claims claims = parseClaims(token);
        return claims.get("jti", String.class);
    }
}
