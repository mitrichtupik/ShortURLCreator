package mitrich.rest.filter;

import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;

import mitrich.rest.service.UserService;

public class AuthenticationService {

	@Autowired
	private UserService userService;

	public boolean authenticate(String authCredentials) {

		if (authCredentials == null)
			return false;
		// header value format will be "Basic encodedstring" for Basic
		// authentication. Example "Basic YWRtaW46YWRtaW4="
		final String encodedUserPassword = authCredentials.replaceFirst("Basic" + " ", "");
		String usernameAndPassword = null;
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(encodedUserPassword);
			usernameAndPassword = new String(decodedBytes, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();

		// we have fixed the userid and password as admin
		// call some UserService/LDAP here

		boolean authenticationStatus = "admin".equals(username) && "admin".equals(password);

		return authenticationStatus;
	}
}
