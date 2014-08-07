package com.wagonlift.dao;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.wagonlift.constants.Constants;
import com.wagonlift.models.RideRequest;

@Component
public class RideRequestDAO extends BasicDAO<RideRequest, String> {
	
	@Autowired
	public RideRequestDAO(Morphia morphia, Mongo mongo) {
		super(RideRequest.class, mongo, morphia, Constants.DATABASE_NAME);
	}

	public List<RideRequest> getUserRequests(Date startTime) {
		Query<RideRequest> q = this.getDatastore().find(RideRequest.class).field("startTime").lessThanOrEq(startTime).field("requestAddressed").equal(false);
		return q.asList();
	}
	
	
}
