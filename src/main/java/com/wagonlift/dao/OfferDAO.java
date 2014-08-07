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
import com.wagonlift.models.Offer;
import com.wagonlift.models.RideStatus;

@Component
public class OfferDAO extends BasicDAO<Offer, String> {
	
	@Autowired
	public OfferDAO(Morphia morphia, Mongo mongo) {
		super(Offer.class, mongo, morphia, Constants.DATABASE_NAME);
	}

	public List<Offer> getAllOffers(Date startTime) {
		Query<Offer> q = this.getDatastore().find(Offer.class).field("startTime").lessThanOrEq(startTime).field("rideStatus").equal(RideStatus.NEW.value());
		return q.asList();
	}
	
}
