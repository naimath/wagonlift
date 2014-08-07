package com.wagonlift.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;
import com.wagonlift.constants.Constants;
import com.wagonlift.models.UserBlockList;

@Component
public class UserBlockListDAO extends BasicDAO<UserBlockList, String> {
	
	@Autowired
	public UserBlockListDAO(Morphia morphia, Mongo mongo) {
		super(UserBlockList.class, mongo, morphia, Constants.DATABASE_NAME);
	}
}
