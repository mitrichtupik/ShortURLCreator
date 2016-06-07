package mitrich.rest.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public void save(User user) {
		userRepository.save(user);
	}

	public User findByUserName(String userName) {

		return userRepository.findByUserName(userName);
	}

	public String passwordEncode(String password) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
		byte[] result = mDigest.digest(password.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			String hex = Integer.toHexString(0xff & result[i]);
			if (hex.length() == 1)
				sb.append('0');
			sb.append(hex);
		}
		return sb.toString();
	}

}
