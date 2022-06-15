package org.cyber.reunion.demo.configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.cyber.reunion.demo.security.JwtTokenFilterConfigurer;
import org.cyber.reunion.demo.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	private static final String[] AUTH_ALLOWLIST = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};

	private static String H2_RESOURCE = "/h2-console/**/**";
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Disable CSRF (cross site request forgery)
		http.csrf().disable();

		// No session will be created or used by spring security
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// Entry points
		http.authorizeRequests()//
				.antMatchers("/users/signin").permitAll()//
				.antMatchers("/users/signup").permitAll()//
				.antMatchers("/h2-console/**/**").permitAll()//
				.anyRequest()//
				.authenticated();

		// without permissions
		http.exceptionHandling().accessDeniedPage("/login");

		// Apply JWT
		http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

		// testing
//     http.httpBasic();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Un-secure H2 Database (for testing purposes, H2 console shouldn't be
		// unprotected in production)
		List<String> allowList = new ArrayList<>();
		allowList.addAll(Arrays.asList(AUTH_ALLOWLIST));
		allowList.add(H2_RESOURCE);
		
		web.ignoring()//
				.antMatchers(allowList.stream().toArray(String[]::new));
		;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}