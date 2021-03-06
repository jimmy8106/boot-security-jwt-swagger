package org.cyber.reunion.demo.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Size(min = 3, max = 20, message = "請輸入3-20碼")
	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String email;

	@Size(min = 4, message = "至少4碼")
	@Column(nullable = false)
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	List<AppUserRole> userAppRoles;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<AppUserRole> getAppUserRoles() {
		return userAppRoles;
	}

	public void setAppUserRoles(List<AppUserRole> userAppRoles) {
		this.userAppRoles = userAppRoles;
	}
}
