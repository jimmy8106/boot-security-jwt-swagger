package org.cyber.reunion.demo.configuration;

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

	@Autowired
	private JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();
	private static final String[] AUTH_WHITELIST = { "/authenticate", "/swagger-resources/**", "/swagger-ui/**",
			"/v3/api-docs", "/webjars/**" };

//  private static final String[] WHITELIST = {
//	        "/v2/api-docs",
//	        "/v3/api-docs",
//	        "/**/v3/api-docs",
//	        "/swagger-resources",
//	        "/swagger-resources/**",
//	        "/configuration/ui",
//	        "/configuration/security",
//	        "/swagger-ui.html",
//	        "**/swagger-ui.html",
//	        "/**/swagger-ui.html**",
//	        "/swagger-ui.html**",
//	        "/webjars/**"
//	};
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
				.antMatchers(AUTH_WHITELIST).permitAll()//
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
		// Allow swagger to be accessed without authentication
//    web.ignoring().antMatchers("/v2/api-docs")//
//        .antMatchers("/swagger-resources/**")//
//        .antMatchers("/swagger-ui.html")//
//        .antMatchers("/configuration/**")//
//        .antMatchers("/webjars/**")//
//        .antMatchers("/public")
//	  web.ignoring().anyRequest();

		// Un-secure H2 Database (for testing purposes, H2 console shouldn't be
		// unprotected in production)
		web.ignoring()//
				.antMatchers("/h2-console/**/**");
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