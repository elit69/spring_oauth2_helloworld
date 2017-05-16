package com.app.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dao.UserDao;
import com.app.entities.User;

@Service
@PropertySource("classpath:application.properties")
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String givenUserName = authentication.getName();
		String givenPassword = authentication.getCredentials().toString();
		Session session = sessionFactory.openSession();
		Transaction txn = session.beginTransaction();
		try {
			User userDTO = userDao.getUserByNameLock(session, givenUserName);
			
			//user not found
			if (userDTO == null) {
				throw new UsernameNotFoundException("User NOT FOUND!");
			}
			
			//disable
			if (userDTO.isEnabled() == false){
				throw new DisabledException("Account Is Disable with too many failed login attempt");
			}
			
			//wrong password //count failed
			if (encoder.matches(givenPassword, userDTO.getPassword()) == false) {
				userDTO.setFailedAttempts(userDTO.getFailedAttempts() + 1);
				userDTO.setUpdatedAt(new Date());
				Integer limit = env.getProperty("login_attempt_limit", Integer.class);
				if (userDTO.getFailedAttempts() == limit) {
					userDTO.setEnabled(false);
					throw new DisabledException("Account Is Disable with too many failed login attempt");
				}
				throw new BadCredentialsException("Wrong Password!");
			}
			
			//reset failed attmept
			if(userDTO.getFailedAttempts() != 0){
				userDTO.setFailedAttempts(0);
				userDTO.setUpdatedAt(new Date());
			}
			
			//return session
			List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
			grantedAuths.add(new SimpleGrantedAuthority(userDTO.getUserRole()));
			return new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword(), grantedAuths);
		} catch (HibernateException e) {
			e.printStackTrace();
			throw e;
		} catch (AuthenticationException e1) {
			e1.printStackTrace();
			throw e1;
		} finally {
			try {
				txn.commit();
				session.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				throw e2;
			}
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
