package com.app.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.app.entities.User;

@Repository
public class UserDao {
	
	@SuppressWarnings("unchecked")
	public User getUserByNameLock(Session session, String userName) throws HibernateException{
		try {
			List<User> results = session.createCriteria(User.class)
					.add(Restrictions.eq("username", userName))
					.add(Restrictions.eq("isDeleted", false))
					.setMaxResults(1)
					.setLockMode(LockMode.PESSIMISTIC_READ)
					.list();
			if (results.size() == 1) {
				return results.get(0);
			}			
		} catch (HibernateException e) {
			e.printStackTrace();
			throw e;
		}
		return null;
	}
	
}
