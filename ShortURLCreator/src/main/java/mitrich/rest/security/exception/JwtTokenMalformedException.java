package mitrich.rest.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenMalformedException extends AuthenticationException {

	private static final long serialVersionUID = 4915473317572017882L;

	public JwtTokenMalformedException(String msg) {
		super(msg);
	}

}
