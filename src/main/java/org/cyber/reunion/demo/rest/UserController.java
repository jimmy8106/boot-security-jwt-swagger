package org.cyber.reunion.demo.rest;

import javax.servlet.http.HttpServletRequest;

import org.cyber.reunion.demo.dto.UserDataDTO;
import org.cyber.reunion.demo.dto.UserResponseDTO;
import org.cyber.reunion.demo.models.AppUser;
import org.cyber.reunion.demo.security.UserDetailsServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/users")
@Tag(name = "users")
public class UserController {

	@Autowired
	private UserDetailsServiceImpl userService;
	@Autowired
	private ModelMapper modelMapper;

	@PostMapping("/signin")
	@Operation(summary = "登入")
	@ApiResponses(value = { //
			@ApiResponse(responseCode = "400", description = "Something went wrong"), //
			@ApiResponse(responseCode = "422", description = "Invalid username/password supplied") })
	public String login(//
			@Parameter(name="username") @RequestParam String username,
			@Parameter(name="password") @RequestParam String password) {
		return userService.signin(username, password);
	}

	@PostMapping("/signup")
	@Operation(summary = "註冊")
	@ApiResponses(value = { //
			@ApiResponse(responseCode = "400", description = "Something went wrong"), //
			@ApiResponse(responseCode = "403", description = "Access denied"), //
			@ApiResponse(responseCode = "422", description = "Username is already in use") })
	public String signup(@Parameter(name="Signup User") @RequestBody UserDataDTO user) {
		return userService.signup(modelMapper.map(user, AppUser.class));
	}

	
	@DeleteMapping(value = "/deleteUser/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Operation(summary = "刪除使用者",security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponses(value = { //
			@ApiResponse(responseCode = "400", description = "Something went wrong"), //
			@ApiResponse(responseCode = "403", description = "Access denied"), //
			@ApiResponse(responseCode = "404", description = "The user doesn't exist"), //
			@ApiResponse(responseCode = "500", description = "Expired or invalid JWT token") })
	public String delete(@Parameter(name="username") @PathVariable String username) {
		userService.delete(username);
		return username;
	}

	@GetMapping(value = "/getUser/{username}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Operation(summary = "查詢使用者名稱", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponses(value = { //
			@ApiResponse(responseCode = "400", description = "Something went wrong"), //
			@ApiResponse(responseCode = "403", description = "Access denied"), //
			@ApiResponse(responseCode = "404", description = "The user doesn't exist"), //
			@ApiResponse(responseCode = "500", description = "Expired or invalid JWT token") })
	public UserResponseDTO search(@Parameter(name="username") @PathVariable String username) {
		return modelMapper.map(userService.search(username), UserResponseDTO.class);
	}

	@GetMapping(value = "/queryUser")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
	@Operation(summary = "取得該使用者資料", security = { @SecurityRequirement(name = "bearer-key") })
	@ApiResponses(value = { //
			@ApiResponse(responseCode = "400", description = "Something went wrong"), //
			@ApiResponse(responseCode = "403", description = "Access denied"), //
			@ApiResponse(responseCode = "500", description = "Expired or invalid JWT token") })
	public UserResponseDTO whoami(HttpServletRequest req) {
		return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
	}

	@GetMapping("/refresh")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
	public String refresh(HttpServletRequest req) {
		return userService.refresh(req.getRemoteUser());
	}

}
