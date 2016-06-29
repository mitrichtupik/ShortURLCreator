package mitrich.rest.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import mitrich.rest.model.User;

public interface UserRepository extends MongoRepository<User, String> {

	public User findByUserName(String userName);

}
