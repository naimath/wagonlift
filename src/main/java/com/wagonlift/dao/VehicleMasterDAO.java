package com.wagonlift.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;
import com.wagonlift.constants.Constants;
import com.wagonlift.models.VehicleMaster;

@Component
public class VehicleMasterDAO extends BasicDAO<VehicleMaster, String> {
	
	@Autowired
	public VehicleMasterDAO(Morphia morphia, Mongo mongo) {
		super(VehicleMaster.class, mongo, morphia, Constants.DATABASE_NAME);
	}
}
