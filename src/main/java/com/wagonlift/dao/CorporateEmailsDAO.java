package com.wagonlift.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.morphia.Morphia;
import com.google.code.morphia.dao.BasicDAO;
import com.mongodb.Mongo;
import com.wagonlift.constants.Constants;
import com.wagonlift.models.CorporateEmails;

@Component
public class CorporateEmailsDAO extends BasicDAO<CorporateEmails, String> {
	
	@Autowired
	public CorporateEmailsDAO(Morphia morphia, Mongo mongo) {
		super(CorporateEmails.class, mongo, morphia, Constants.DATABASE_NAME);
	}
}
