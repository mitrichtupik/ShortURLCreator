package mitrich.rest.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public void save(User user) {
		this.userRepository.save(user);
	}

	public User findByUserName(String userName) {

		return this.userRepository.findByUserName(userName);
	}

}
