package com.wagonlift.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;
import com.wagonlift.constants.Constants;
import com.wagonlift.models.Badge;

@Component
public class BadgeDAO extends BasicDAO<Badge, String> {
	
	@Autowired
	public BadgeDAO(Morphia morphia, Mongo mongo) {
		super(Badge.class, mongo, morphia, Constants.DATABASE_NAME);
	}
}
