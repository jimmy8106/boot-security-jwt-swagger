package org.cyber.reunion.demo.security;

import javax.servlet.http.HttpServletRequest;

import org.cyber.reunion.demo.exception.CustomException;
import org.cyber.reunion.demo.models.AppUser;
import org.cyber.reunion.demo.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private AppUserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final AppUser user = userRepository.findByUsername(username);

		if (user == null) {
			throw new UsernameNotFoundException("User '" + username + "' not found");
		}

		return org.springframework.security.core.userdetails.User.withUsername(username).password(user.getPassword())
			.authorities(user.getAppUserRoles())
			.accountExpired(false)
			.accountLocked(false)
			.credentialsExpired(false)
			.disabled(false)
			.build();
	}

	public String signin(String username, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
			return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getAppUserRoles());
		} catch (AuthenticationException e) {
			throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	public String signup(AppUser appUser) {
		if (!userRepository.existsByUsername(appUser.getUsername())) {
			appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
			userRepository.save(appUser);
			return jwtTokenProvider.createToken(appUser.getUsername(), appUser.getAppUserRoles());
		} else {
			throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	public void delete(String username) {
		userRepository.deleteByUsername(username);
	}

	public AppUser search(String username) {
		AppUser appUser = userRepository.findByUsername(username);
		if (appUser == null) {
			throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
		}
		return appUser;
	}

	public AppUser whoami(HttpServletRequest req) {
		return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
	}

	public String refresh(String username) {
		return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getAppUserRoles());
	}

	
}
