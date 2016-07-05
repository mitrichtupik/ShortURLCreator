package mitrich.rest.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtTokenMissingException extends AuthenticationException {

	private static final long serialVersionUID = 3558166630744714700L;

	public JwtTokenMissingException(String msg) {
		super(msg);
	}

}
