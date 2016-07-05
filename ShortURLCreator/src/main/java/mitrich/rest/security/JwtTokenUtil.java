package mitrich.rest.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mitrich.rest.model.User;

@Component
public class JwtTokenUtil {

	@Value("${jwt.secret}")
	private String secret;

	public User parseToken(String token) {
		User u = null;

		try {
			Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
			u = new User();
			u.setUserName(body.getSubject());
			u.setId((String) body.get("userId"));
			u.setRole((String) body.get("role"));

		} catch (JwtException e) {
			e.printStackTrace();
		}

		return u;
	}

	public String generateToken(User u) {

		Claims claims = Jwts.claims().setSubject(u.getUserName());
		claims.put("userId", u.getId());
		claims.put("role", u.getRole());

		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, secret).compact();
	}
}
