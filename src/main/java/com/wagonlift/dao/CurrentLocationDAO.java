package com.wagonlift.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.wagonlift.constants.Constants;
import com.wagonlift.models.CurrentLocation;
import com.wagonlift.models.LatLng;
import com.wagonlift.models.RideRequest;

@Component
public class CurrentLocationDAO extends BasicDAO<CurrentLocation, String> {
	
	@Autowired
	public CurrentLocationDAO(Morphia morphia, Mongo mongo) {
		super(CurrentLocation.class, mongo, morphia, Constants.DATABASE_NAME);
	}
}
