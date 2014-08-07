package com.wagonlift.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;
import com.wagonlift.constants.Constants;
import com.wagonlift.models.User;

@Component
public class UserDAO extends BasicDAO<User, String> {
	
	@Autowired
	public UserDAO(Morphia morphia, Mongo mongo) {
		super(User.class, mongo, morphia, Constants.DATABASE_NAME);
	}
	
	/**
	 * Return the users details for the admin.
	 * @return
	 */
	public List<User> getUserDetails(){
		List<User> users = this.getDatastore().createQuery(User.class).retrievedFields(true, "firstName", "lastName", "phoneNumber").asList();
		return users;
	}
	
}
