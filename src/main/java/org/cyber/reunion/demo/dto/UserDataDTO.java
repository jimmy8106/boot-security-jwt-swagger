package org.cyber.reunion.demo.dto;

import java.util.List;

import org.cyber.reunion.demo.models.AppUserRole;

import io.swagger.annotations.ApiModelProperty;

public class UserDataDTO {

	@ApiModelProperty(position = 0)
	private String username;
	@ApiModelProperty(position = 1)
	private String email;
	@ApiModelProperty(position = 2)
	private String password;
	@ApiModelProperty(position = 3)
	List<AppUserRole> appUserRoles;
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
		return appUserRoles;
	}
	public void setAppUserRoles(List<AppUserRole> appUserRoles) {
		this.appUserRoles = appUserRoles;
	}

}
