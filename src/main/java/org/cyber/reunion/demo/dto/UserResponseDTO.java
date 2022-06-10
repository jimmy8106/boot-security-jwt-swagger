package org.cyber.reunion.demo.dto;

import java.util.List;

import org.cyber.reunion.demo.models.AppUserRole;

public class UserResponseDTO {
	private Integer id;
	private String username;
	private String email;
	List<AppUserRole> appUserRoles;

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

	public List<AppUserRole> getAppUserRoles() {
		return appUserRoles;
	}

	public void setAppUserRoles(List<AppUserRole> appUserRoles) {
		this.appUserRoles = appUserRoles;
	}
}
