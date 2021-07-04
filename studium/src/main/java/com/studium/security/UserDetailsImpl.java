package com.studium.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.studium.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserDetailsImpl implements UserDetails {
	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;
	private String firstname;
	private String lastname;


	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String username, String password, String firstname, String lastname,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;

		this.authorities = authorities;
	}

	public static UserDetailsImpl build(User user) {
		GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName().name());



		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(authority);

		return new UserDetailsImpl(
				user.getId(), 
				user.getUsername(), 
				user.getPassword(),
				user.getFirstname(),
				user.getLastname(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		return Arrays.asList(new SimpleGrantedAuthority(user.getRole()));

		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

	@Override
	public String toString() {
		return "UserDetailsImpl{" +
				"id=" + id +
				", username='" + username + '\'' +
				", firstname='" + firstname + '\'' +
				", lastname='" + lastname + '\'' +
				", password='" + password + '\'' +
				", authorities=" + authorities +
				'}';
	}
}