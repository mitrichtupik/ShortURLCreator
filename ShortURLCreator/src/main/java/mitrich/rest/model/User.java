package mitrich.rest.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "users")
public class User {

	@Id
	private String id;
	@NotNull
	private String userName;
	@NotNull
	@Size(min = 6)
	private String password;

	public User() {

	}

	public User(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	@JsonIgnore
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty()
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return String.format("User [id=%s, userName=%s, password=%s]", id, userName, password);
	}

}
