package com.securityapplication.application;

import java.security.Principal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SsoApplication{

	public static void main(String[] args) {
		SpringApplication.run(SsoApplication.class, args);
	}
	
	@SuppressWarnings({ "removal" })
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http.authorizeHttpRequests(authz->{
			try {
				authz.requestMatchers("/login**","/user","/userInfo")
			//	authz.requestMatchers("/ui")
						.authenticated()
						.and()
						.logout()
						.logoutSuccessUrl("/")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll()
						.and()
						.csrf()
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		return http.build();
	}
	
	@RequestMapping("/ui")
	public Principal user(Principal principal) {
		return principal;
	}
	
	@RequestMapping("/userInfo")
	public String userInfo(Principal principal) {
		@SuppressWarnings("deprecation")
		final OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
		@SuppressWarnings("deprecation")
		final Authentication authentication = oAuth2Authentication.getUserAuthentication();
		return authentication.getDetails().toString();
	}
}
