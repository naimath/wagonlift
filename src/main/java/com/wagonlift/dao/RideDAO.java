package com.wagonlift.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.google.code.morphia.query.Query;
import com.mongodb.Mongo;
import com.wagonlift.constants.Constants;
import com.wagonlift.models.Ride;
import com.wagonlift.models.RideStatus;

@Component
public class RideDAO extends BasicDAO<Ride, String> {
	
	@Autowired
	public RideDAO(Morphia morphia, Mongo mongo) {
		super(Ride.class, mongo, morphia, Constants.DATABASE_NAME);
	}
	
	public Ride getRide(String requestId, String offerId) {
		Query<Ride> q = this.getDatastore().find(Ride.class).field("offerId").equal(offerId).field("rideRequests").hasThisOne(requestId).field("status").equal(RideStatus.NEW);
		return q.get();
	}

}
