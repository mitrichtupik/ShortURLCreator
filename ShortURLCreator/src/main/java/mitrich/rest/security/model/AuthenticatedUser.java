package mitrich.rest.security.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUser implements UserDetails {

	private static final long serialVersionUID = 6246856342529397880L;

	private String id;
	private String username;
	private String token;
	private Collection<? extends GrantedAuthority> authorities;

	public AuthenticatedUser(String id, String username, String token,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.token = token;
		this.authorities = authorities;
	}

	public String getId() {
		return id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
