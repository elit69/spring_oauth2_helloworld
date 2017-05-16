package com.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired 
	private CustomAuthenticationProvider customAuthenticationProvider;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth){		
		auth.authenticationProvider(customAuthenticationProvider);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.formLogin()
				.loginPage("/login")
			.and()
				.authorizeRequests()
				.antMatchers("/resources/**").permitAll()				
				.antMatchers("/api/**").permitAll()
			.and()
				.csrf().disable();						
		http.exceptionHandling().accessDeniedPage("/403");
	}
	
	public static void main(String[] args) {
		Long l1 = System.currentTimeMillis();
		System.out.println(new BCryptPasswordEncoder().encode("សដថាសដថ"));
		System.out.println(System.currentTimeMillis()-l1);
		
		Long l2 = System.currentTimeMillis();
		System.out.println(new BCryptPasswordEncoder().matches("សដថាសដថ", "$2a$10$.hxWsSw3wHCiXqh.VGH4aezS.U3ivDwpy/4vhsqmeAhzqrv2K1e4a"));
		System.out.println(System.currentTimeMillis()-l2);
		
		Long l3 = System.currentTimeMillis();
		System.out.println(new StandardPasswordEncoder().encode("សដថាសដថ"));
		System.out.println(System.currentTimeMillis()-l3);
		
		Long l4 = System.currentTimeMillis();
		System.out.println(new StandardPasswordEncoder().matches("សដថាសដថ", "a519a365276b73d5c7b44471a70d6c952d183bf1ca9c352b624849b09e9d06e3448eff083ff22356"));
		System.out.println(System.currentTimeMillis()-l4);
	}
	
}