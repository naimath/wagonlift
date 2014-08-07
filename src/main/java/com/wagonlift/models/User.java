package com.wagonlift.models;

import java.util.Date;
import java.util.UUID;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

/**
 * This class is used to store user related information.
 * @author Himanshu
 */

@Entity
public class User {

	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String phoneNo;
	private String aboutMe;
	private boolean isActivated;
	private boolean isRegisterd;
	private String activationId;
	private boolean isAdmin;
	private String email;
	private String login;
	private String password;
	private String address;
	private Date created;
	private Attachment pictureFile;
	private Attachment documentFile;
	private boolean isEmailCorporate;
	private Badge badge;

	public static final boolean STATUS_ACTIVATED	    =	true;
	public static final boolean STATUS_NOT_ACTIVATED	=	false;

	
	public User() {
		this.id = UUID.randomUUID().toString();
		this.activationId = UUID.randomUUID().toString();
		this.created = new Date();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}


	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	public String getActivationId() {
		return activationId;
	}

	public void setActivationId(String activationId) {
		this.activationId = activationId;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Attachment getPictureFile() {
		return pictureFile;
	}

	public void setPictureFile(Attachment pictureFile) {
		this.pictureFile = pictureFile;
	}

	public Attachment getDocumentFile() {
		return documentFile;
	}

	public void setDocumentFile(Attachment documentFile) {
		this.documentFile = documentFile;
	}

	public Badge getBadge() {
		return badge;
	}

	public void setBadge(Badge badge) {
		this.badge = badge;
	}

	public boolean isEmailCorporate() {
		return isEmailCorporate;
	}

	public void setEmailCorporate(boolean isEmailCorporate) {
		this.isEmailCorporate = isEmailCorporate;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public boolean isRegisterd() {
		return isRegisterd;
	}

	public void setRegisterd(boolean isRegisterd) {
		this.isRegisterd = isRegisterd;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
