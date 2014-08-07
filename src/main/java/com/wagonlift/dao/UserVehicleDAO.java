package com.wagonlift.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;
import com.wagonlift.constants.Constants;
import com.wagonlift.models.UserVehicle;

@Component
public class UserVehicleDAO extends BasicDAO<UserVehicle, String> {
	
	@Autowired
	public UserVehicleDAO(Morphia morphia, Mongo mongo) {
		super(UserVehicle.class, mongo, morphia, Constants.DATABASE_NAME);
	}
}
