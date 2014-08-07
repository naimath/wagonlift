package com.wagonlift.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.wagonlift.constants.Constants;
import com.wagonlift.dao.CorporateEmailsDAO;
import com.wagonlift.dao.UserDAO;
import com.wagonlift.dao.UserVehicleDAO;
import com.wagonlift.models.Attachment;
import com.wagonlift.models.RideType;
import com.wagonlift.models.User;
import com.wagonlift.models.UserVehicle;
import com.wagonlift.utils.BCrypt;
import com.wagonlift.utils.MOTPUtils;
import com.wagonlift.utils.MailUtils;
import com.wagonlift.utils.Utils;

@Service
public class UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	MailSender mailSender;

	@Autowired
	CorporateEmailsDAO corporateEmailsDAO;
	
	@Autowired
	UserVehicleDAO userVehicleDAO;
	
	private static final Logger logger = Logger.getLogger(UserService.class);

	@Value("${mwb.admin.email}")
	private String adminEmailAddress;
	@Value("${mwb.bcc.email}")
	private String bccEmailAddress;
	@Value("${mwb.admin.subject}")
	private String adminCommonSubject;

	@Value("${mail.greetings}")
	private String mailGreetings;
	@Value("${mail.salutation.word}")
	private String mailSalutationWord;
	@Value("${mail.salutation.name}")
	private String mailSalutationName;

	@Value("${login.success.code}")
	private String successCode;
	@Value("${login.success.message}")
	private String successMessage;
	@Value("${login.failure.code}")
	private String failureCode;
	@Value("${login.failure.message}")
	private String failureMessage;
	@Value("${login.exists.code}")
	private String existsCode;
	@Value("${login.exists.message}")
	private String existsMessage;
	@Value("${login.notactivated.code}")
	private String notActivatedCode;
	@Value("${login.notactivated.message}")
	private String notActivatedMessage;
	@Value("${login.activated.code}")
	private String activatedCode;
	@Value("${login.activated.message}")
	private String activatedMessage;
	@Value("${mail.invalid.address.code}")
	private String invalidAddressCode;
	@Value("${mail.invalid.address.message}")
	private String invalidAddressMessage;
	@Value("${login.invalid-id.activate.code}")
	private String invalidActivationIdCode;
	@Value("${login.invalid-id.activate.message}")
	private String invalidActivationIdMessage;

	@Value("${mail.activate.subject}")
	private String activateSubject;
	@Value("${mail.activate.general}")
	private String activateGeneral;
	@Value("${mail.activate.url}")
	private String activateURL;
	@Value("${mail.activate.username}")
	private String activateUsername;

	@Value("${mail.forgotPassword.subject}")
	private String forgotPwdSubject;
	@Value("${mail.forgotPassword.general}")
	private String forgotPwdGeneral;
	@Value("${mail.forgotPassword.username}")
	private String forgotPwdUsername;
	@Value("${mail.forgotPassword.password}")
	private String forgotPwdPassword;

	@Value("${mail.changePassword.subject}")
	private String changePwdSubject;
	@Value("${mail.changePassword.username}")
	private String changePwdUsername;
	@Value("${mail.changePassword.general}")
	private String changePwdGeneral;

	
	@Value("${mobile.success.code}")
	private String mobileSuccessCode;
	@Value("${mobile.success.message}")
	private String mobileSuccessMessage;
	@Value("${mobile.failure.code}")
	private String mobileFailureCode;
	@Value("${mobile.failure.message}")
	private String mobileFailureMessage;
	@Value("${mobile.exists.code}")
	private String mobileExistsCode;
	@Value("${mobile.exists.message}")
	private String mobileExistsMessage;
	@Value("${mobile.notactivated.code}")
	private String mobileNotActivatedCode;
	@Value("${mobile.notactivated.message}")
	private String mobileNotActivatedMessage;
	@Value("${mobile.activated.code}")
	private String mobileActivatedCode;
	@Value("${mobile.activated.message}")

	
	private String mobileActivatedMessage;
	
	
	@Value("${mail.adminnotify.email}")
	private String adminNotifyEmail;
	
	@Value("${mail.adminnotify.subject}")
	private String adminNotifySubject;
	
	@Value("${mail.adminnotify.general}")
	private String adminNotifyGeneral;

	@Value("${mail.usernotify.subject}")
	private String userNotifySubject;
	
	@Value("${mail.usernotify.general}")
	private String userNotifyGeneral;
	
	
	/**
	 * Return all users present in the database.
	 * 
	 * @return
	 */
	public List<User> getUsers() {
		return userDAO.getUserDetails();
	}

	/**
	 * Return User for the given phone number.
	 * 
	 * @param phoneNo
	 *            The 10 digit phone number.
	 * @return User.
	 */
	public User findUserbyColumnName(String columnName, String value) {
		return userDAO.findOne(columnName, value);
	}

	/**
	 * Delete user for the given phone number.
	 * 
	 * @param phoneNo
	 * @return
	 */
	public boolean deleteUserbyPhoneNo(String phoneNo) {
		User user = userDAO.findOne("phoneNo", phoneNo);
		if (user != null) {
			userDAO.delete(user);
			return true;
		}
		return false;
	}

	/**
	 * Register User and send activation email
	 * 
	 * @param email
	 * @param password
	 * @param statusMap
	 * @return
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public Map<String, String> setUserProfile(String login, JSONObject detailsJson, MultipartFile picMultipartFile, MultipartFile vehiclePicMultipart) throws IllegalStateException, IOException {

		Map<String, String> statusMap = Maps.newHashMap();
		boolean isValid = true;
		
		User user = findUserbyColumnName("login", login);
		if(user == null){
			logger.info(" user not found with login name " + login);
			isValid = false;
		}
		
		isValid = validateUserProfile(detailsJson, isValid);
		
		// check ride details
		JSONObject vehicleJson = detailsJson.getJSONObject("vehicle");
		UserVehicle userVehicle = null;
		if(isValid && (!vehicleJson.isNullObject() || !vehicleJson.isEmpty())){
			userVehicle = new UserVehicle();
			userVehicle.setUserId(user.getId());
			String number = vehicleJson.getString("number");
			
			if(isValid && Utils.isNull(number)){
				logger.info(" number is missing in the json Object "+ vehicleJson);
				isValid = false;
			}
			
			userVehicle.setNumber(number);
			userVehicle.setPictureFile(Utils.getAttachmentFromMultipart(vehiclePicMultipart));
			
			String rideType = vehicleJson.getString("ridetype");
			if(isValid && Utils.isNull(rideType)){
				logger.info(" rideType is missing in the json Object "+ vehicleJson);
				isValid = false;
			}
			userVehicle.setRideType(RideType.fromString(rideType));
		}
		
		if(!isValid){
			statusMap.put("code", failureCode);
			statusMap.put("message", failureMessage);
			return statusMap;
		}


		String email = detailsJson.getString("email");
		if(isCorporateEmail(Utils.getEmailDomain(email))){
			user.setEmailCorporate(true);
		}

		Attachment pictureFile = Utils.getAttachmentFromMultipart(picMultipartFile);
		if(pictureFile!=null){
			user.setPictureFile(pictureFile);
		}
		
		user.setFirstName(detailsJson.getString("firstname"));
		user.setLastName(detailsJson.getString("lastname"));
		user.setEmail(email);
		user.setAboutMe(detailsJson.getString("aboutme"));
		user.setAddress(detailsJson.getString("address"));
		user.setActivated(User.STATUS_NOT_ACTIVATED);
		user.setAdmin(false);

		String content = activateGeneral + "\n\n\t" + activateURL
				+ user.getActivationId() + "\n\n\t" + activateUsername + "  "
				+ email;
		boolean sendMailStatus = sendMail(activateSubject, email, content);

		if (sendMailStatus == false) {
			statusMap.put("code", invalidAddressCode);
			statusMap.put("message", invalidAddressMessage);
			logger.info("/user/register return, with result == "
					+ statusMap.get("code"));
			return statusMap;
		}

		statusMap.put("code", notActivatedCode);
		statusMap.put("message", notActivatedMessage);

		userDAO.save(user);
		
		if(userVehicle!=null){
			userVehicleDAO.save(userVehicle);
		}

		logger.info("/user/register return, with result == "
				+ statusMap.get("code"));
		return statusMap;
	}


	/**
	 * Check if the email suffix exist in our database.
	 * @param emailSuffix The email Suffix
	 * @return true / false.
	 */
	private boolean isCorporateEmail(String emailSuffix) {
		if(corporateEmailsDAO.findOne("emailSuffix", emailSuffix)!=null){
			return true;
		}
		
		return false;
	}

	/**
	 * Change Password of the user and send eMail.
	 * 
	 * @param login
	 *            The email of the user
	 * @param oldPassword
	 *            The old password.
	 * @param newPassword
	 *            The new password.
	 * @return true if changed else false.
	 */
	public boolean changePassword(String login, String oldPassword,
			String newPassword) {
		login = login.trim().toLowerCase();
		oldPassword = oldPassword.trim();
		newPassword = newPassword.trim();

		User user = findUserbyColumnName("login", login);

		if (user == null) {
			logger.info("/user/changepassword return, with result == false, No user reqistered with this email");
			return false;
		}

		// if old password found incorrect
		if (!BCrypt.checkpw(oldPassword, user.getPassword())) {
			logger.info("/user/changepassword return, with result == false, old password is incorrect");
			return false;
		}

		user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(Constants.BCRYPT_SALT)));
		
		boolean updateStatus = userDAO.save(user) != null;
		
		if (updateStatus) {
			// inform user for change of password
			sendMail(changePwdSubject,login, changePwdGeneral + "\n\n\t"
					+ changePwdUsername + "  " + login);
			logger.info("/user/changepassword return, with result == true, user details updated in database");
			return true;

		} else {
			logger.info("/user/changepassword return, with result == false, unable to update user details");
			return false;
		}
	}

	private boolean sendMail(String subject, String email, String content) {
		// send mail to user at his email address
		MailUtils mailUtils = new MailUtils();

		String header = mailUtils.setMailHeader(mailGreetings, email);
		String salutation = mailUtils.setMailSalutation(mailSalutationWord,
				mailSalutationName);

		String from = adminEmailAddress;
		String to = email;
		String message = mailUtils
				.setEntireMessage(header, content, salutation);

		// send mail to user
		boolean sendMailStatus = mailUtils.prepareAndSendMail(mailSender, from,
				to, subject, message, bccEmailAddress);
		return sendMailStatus;
	}

	/**
	 * Service used for User forgot password. Send an email to the user with new password.
	 * @param email
	 * @return
	 */
	public boolean forgotPassword(String email) {

		User user = findUserbyColumnName("email", email);
		if (user == null) {
			logger.info("/user/forgotpassword return, with result == false");
			return false;
		}

		// generate new password
		String newPassword = Utils.generateRandomPassword();

		String content = forgotPwdGeneral + "\n\n\t" + forgotPwdUsername + "  "
				+ email + "\n\t" + forgotPwdPassword + "  " + newPassword;
		// send mail to user
		boolean sendMailStatus = sendMail(forgotPwdSubject, email, content);

		if (sendMailStatus) {
			user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(Constants.BCRYPT_SALT)));
			userDAO.save(user);
			logger.info("/user/forgotpassword return, with result == true");
			return true;
		} else {
			logger.info("/user/forgotpassword return, with result == false");
			return false;
		}
	}

	/**
	 * Activate User.
	 * 
	 * @param activationId
	 *            The activation Id.
	 * @param statusMap
	 *            Status Map.
	 * @return {@link Map}
	 */
	public Map<String, String> activateUser(String activationId) {

		Map<String, String> statusMap = Maps.newHashMap();
		User user = findUserbyColumnName("activationId", activationId);

		// validate if user is exists or not.
		if (user == null) {
			statusMap.put("code", failureCode);
			statusMap.put("message", failureMessage);
			logger.info("/user/activate/{activationId} return, with result == Failed to activate your account.");
			return statusMap;
		}

		String email = user.getEmail();
		if (email == null) {
			statusMap.put("code", invalidActivationIdCode);
			statusMap.put("message", invalidActivationIdMessage);
			logger.info("/user/activate/{activationId} return, with result == Invalid activation id.");
			return statusMap;
		}

		statusMap.put("email", email);
		if (user.isActivated() == User.STATUS_ACTIVATED) {
			statusMap.put("code", activatedCode);
			statusMap.put("message", activatedMessage);
			logger.info("/user/activate/{activationId} return, with result == Your account is already activated.");
			return statusMap;
		}

		user.setActivated(User.STATUS_ACTIVATED);
		userDAO.save(user);

		statusMap.put("code", activatedCode);
		statusMap.put("message", activatedMessage);

		return statusMap;
	}

	/**
	 * Chane user email.
	 * @param email user email
	 * @param oldEmail user old email.
	 * @param password user password.
	 * @return
	 */
	public Map<String, String> changeEmail(String email, String oldEmail,
			String password) {

		Map<String, String> statusMap = Maps.newHashMap();

		email = email.trim().toLowerCase();
		oldEmail = oldEmail.trim().toLowerCase();

		if (StringUtils.equalsIgnoreCase(email, oldEmail)) {
			statusMap.put("code", failureCode);
			statusMap.put("message", failureMessage
					+ ": new and old email are same");
			logger.info("/user/change-email return, with result == false, new and old email are same");
			return statusMap;
		}

		password = password.trim();

		User dbUserOldEmail = findUserbyColumnName("email", oldEmail);
		if (dbUserOldEmail == null) {
			statusMap.put("code", failureCode);
			statusMap.put("message", failureMessage
					+ ": no user registered with email=" + oldEmail);
			logger.info("/user/change-email return, with result == false, no user registered with email="
					+ oldEmail);
			return statusMap;
		}

		if (findUserbyColumnName("email", email) != null) {
			statusMap.put("code", failureCode);
			statusMap.put("message", failureMessage
					+ ": new email already exists in accout =" + email);
			logger.info("/user/change-email return, with result == false, new email already exists in accout ="
					+ email);
			return statusMap;
		}

		if (dbUserOldEmail.isActivated() == User.STATUS_ACTIVATED) {

			logger.info(dbUserOldEmail.getEmail());
			logger.info(dbUserOldEmail.getPassword());

			if (BCrypt.checkpw(password, dbUserOldEmail.getPassword())) {
				// change email address
				dbUserOldEmail.setEmail(email);
				dbUserOldEmail.setActivationId(UUID.randomUUID().toString());
				dbUserOldEmail.setActivated(User.STATUS_NOT_ACTIVATED);

				// send mail to user at his new email address
				String content = activateGeneral + "\n\n\t" + activateURL
						+ dbUserOldEmail.getActivationId() + "\n\n\t"
						+ activateUsername + "  " + email;
				// send mail
				boolean sendMailStatus = sendMail(activateSubject, oldEmail,
						content);

				if (sendMailStatus == false) {
					statusMap.put("code", failureCode);
					statusMap.put("message", failureMessage
							+ ":  account inactive for email=" + oldEmail);
					logger.info("/user/change-email return, with result == false, new email is incorrect, "
							+ email);
					return statusMap;

				} else {
					userDAO.save(dbUserOldEmail);
					statusMap.put("code", successCode);
					statusMap.put("message", successMessage);
					logger.info("/user/change-email return, with result == true");
					return statusMap;
				}

			} else {
				logger.info("/user/change-email return, with result == false, password incorrect");
				statusMap.put("code", failureCode);
				statusMap.put("message", failureMessage
						+ ":  password incorrect");
				return statusMap;
			}
		} else {
			statusMap.put("code", failureCode);
			statusMap.put("message", failureMessage
					+ ":  account inactive for email=" + oldEmail);
			logger.info("/user/change-email return, with result == false, account inactive for email="
					+ oldEmail);
			return statusMap;
		}
	}

	/**
	 * Resend Activation email
	 * @param email the user email.
	 * @param password the user password.
	 * @return true if mail sent else false.
	 */
	public boolean resendActivate(String email, String password) {

		User user = findUserbyColumnName("email", email);
		if (user == null) {
			logger.info("/user/resend-activate return, with result == false, user doesn't exist");
			return false;
		}

				String content = activateGeneral + "\n\n\t" + activateURL
				+ user.getActivationId() + "\n\n\t" + activateUsername + "  "
				+ email;

		// send mail to user
		boolean sendMailStatus = sendMail(activatedMessage, email, content);

		if (sendMailStatus == false) {
			logger.info("/user/resend-activate return, with result == false, cannot send email");
			return false;
		} else {
			logger.info("/user/resend-activate returned, with result == true");
			return true;
		}
	}

	/**
	 * verify login otp.
	 * 
	 * @param sid
	 * @param login
	 * @return True if verified , false if not verified.
	 */
	public boolean verifyLoginOTP(String login, String sid, String code) {

		User user = findUserbyColumnName("login", login);
		if (user == null) {
			logger.info("user not found with login " + login);
			return false;
		}

		String result = MOTPUtils.verifyMobileOTP(sid);
		logger.info("MOTP Response : " + result);
		if (result!=null && StringUtils.equals(result, code)) {
			user.setRegisterd(true);
			userDAO.save(user);
			return true;
		}

		logger.info("motp is not able to verify sid =" + sid + " for login = "
				+ login);
		return false;
	}

	public Map<String, Boolean> updateUserDocument(String phoneNo,
			MultipartFile docMultipartFile) throws IllegalStateException, IOException {

		Map<String, Boolean> statusMap = Maps.newHashMap();
		
		User user = findUserbyColumnName("phoneNo", phoneNo);
		boolean isValid = true;
		
		if(user == null){
			logger.info("User with "+phoneNo+" not found");
			isValid = false;
		}
		
		if(isValid && !user.isActivated()){
			logger.info("User with "+phoneNo+" is not activated");
			isValid = false;
		}
		
		Attachment attachment = Utils.getAttachmentFromMultipart(docMultipartFile);
		if(isValid && attachment==null){
			statusMap.put("success", false);
			return statusMap;
		}
		
		if(!isValid){
			statusMap.put("success", false);
			return statusMap;
		}
		
		user.setDocumentFile(attachment);
		userDAO.save(user);
		statusMap.put("success", true);
		
		return statusMap;
	}

	public Map<String, Boolean> uploadUserVehicleInfo(JSONObject detailsJson,
			MultipartFile picMultipartFile, String phoneNo) throws IOException {

		Map<String, Boolean> statusMap = Maps.newHashMap();
		
		String number;
		Attachment pictureFile = null;
		RideType rideType;

		boolean isValid = true;
		
		JSONObject jsonObject = detailsJson.getJSONObject("details");
		if(jsonObject.isNullObject()){
			isValid = false;
		}
		
		number = jsonObject.getString("number");

		// if name is not present, the it must return error.
		if(isValid && (number==null || StringUtils.isEmpty(number.trim()))){
			logger.info(" vehicle number is missing in the json Object ");
			isValid = false;
		}

		String rideTypeString = jsonObject.getString("rideType");
		// if name is not present, the it must return error.
		if(isValid && (rideTypeString==null || StringUtils.isEmpty(rideTypeString.trim()))){
			logger.info(" ride type is missing in the json Object ");
			isValid = false;
		}
		
		rideType = RideType.fromString(rideTypeString);
		if(rideType==null){
			logger.info(" invalid ride type in the json Object ");
			isValid = false;
		}
		
		if(!picMultipartFile.isEmpty() || picMultipartFile!=null)
			pictureFile = Utils.getAttachmentFromMultipart(picMultipartFile);
		
		User user = findUserbyColumnName("phoneNo", phoneNo);
		// check if the user is existing.
		if (isValid && user == null) {
			logger.info("user not found " + statusMap.get("code"));
			isValid = false;
		}
		
		if(!isValid){
			statusMap.put("status", false);
			return statusMap;
		}

		UserVehicle userVehicle = new UserVehicle();
		userVehicle.setNumber(number);
		userVehicle.setPictureFile(pictureFile);
		userVehicle.setRideType(rideType);
		userVehicle.setUserId(user.getId());
		
		userVehicleDAO.save(userVehicle);
		
		statusMap.put("status", true);
		return statusMap;

		
	}

	/**
	 * Save notify details of the user.
	 * @param name
	 * @param email
	 * @param subject
	 * @param message
	 * @return
	 */
	public ModelAndView notifyandSave(String name, String email,
			String subject, String message, ModelAndView modelAndView) {
		
		if(StringUtils.isEmpty(message)){
			message="";
		}
		
		if(StringUtils.isEmpty(subject)){
			subject = adminNotifySubject;
		}
		
		String content = adminNotifyGeneral + "\n\n\t" + "Name : "
				+ name + "\n\n\t" + "Message : " + message;
		
		boolean sendMailStatus = sendMail(subject, adminNotifyEmail, content);
		if(sendMailStatus){
			
			sendMailStatus = sendMail(userNotifySubject, email, userNotifyGeneral);
			modelAndView.addObject("message", "<font color='green' size=2><b>" + "Thank you for providing the details"
					+ "</b></font>");
			return modelAndView;
		}

		modelAndView.addObject("message", "<font color='red' size=2><b>" + "some error has been occurred"
				+ "</b></font>");
		return modelAndView;
	}

	/**
	 * Verify user login is available or not.
	 * @param login User input login.
	 * @return {@link Boolean} true if not found in the database else false.
	 */
	public boolean verifyUserRegisterParams(String login, String columnName) {
		if(findUserbyColumnName(columnName, login)!=null){
			logger.info("user found with login "+ login);
			return false;
		}
 		return true;
	}

	/**
	 * Verify user If not exist send motp.
	 * @param login input login name.
	 * @param password input password.
	 * @param phoneNo input phoneNo.
	 * @return true / false.
	 */
	public String register(String login, String password, String phoneNo) {
		
		if(findUserbyColumnName("login", login)!=null){
			logger.info("user found with login "+ login);
			return null;
		}
		
		if(findUserbyColumnName("phoneNo", phoneNo)!=null){
			logger.info("user found with phoneNo "+ phoneNo);
			return null;
		}
		
		User user = new User();
		user.setLogin(login);
		user.setPassword(password);
		user.setPhoneNo(phoneNo);
		user.setRegisterd(false);
		
		String sessionId = MOTPUtils.sendMobileOTP(phoneNo);
		logger.info("Motp session id "+ sessionId + " for login "+ login);
		if(StringUtils.isEmpty(sessionId)){
			logger.info("Motp session id is not generated for login "+ login);
			return null;
		}
		
		userDAO.save(user);
		return sessionId;
	}

	/**
	 * Get user Login.
	 * @param login
	 * @param password
	 * @return
	 */
	public boolean getUserLogin(String login, String password) {
		
		User user = validateUser(login);
		
		if(user == null){
			return false;
		}
		
		if(!BCrypt.checkpw(password, user.getPassword())){
			logger.info("User "+ login + "invalid password" );
			return false;
		}
		
		return true;
	}

	/**
	 * Function validates that user check if hes activated or not.
	 * @param login The user login
	 * @return
	 */
	private User validateUser(String login) {
		User user = findUserbyColumnName("login", login);
		
		if(user==null){
			logger.info("No user found with this login " + login);
			return null;
		}
		
		if(!user.isRegisterd() || !user.isActivated()){
			logger.info("User "+ login + "is not Registered / Activated" );
			return null;
		}
		
		return user;
	}

	/**
	 * Get User login
	 * @param login
	 * @return 
	 */
	public Map<String, Object> getUserDetails(String login) {
		Map<String, Object> resultMap = Maps.newHashMap();
		User user = validateUser(login);
		
		if(user == null){
			return null;
		}

		resultMap.put("user", user);
		UserVehicle userVehicle = userVehicleDAO.findOne("userId", user.getId());
		if(userVehicle!=null){
			resultMap.put("vehicle", userVehicle);	
		}
		
		return resultMap;
	}

	/**
	 * Update User Phone number
	 * @param login login input
	 * @param phoneNo update 
	 * @return
	 */
	public boolean updateUserPhone(String login, String phoneNo) {
		
		User user = validateUser(login);
		
		if(user==null){
			logger.info("user with login " + login + " not found.");
			return false;
		}
		
		if(StringUtils.equalsIgnoreCase(phoneNo, user.getPhoneNo())){
			logger.info("user with login " + login + " has same old number.");
			return false;
		}
		
		if(findUserbyColumnName("phoneNo", phoneNo)!=null){
			logger.info("Another user exist with this phone number.");
			return false;
		}
		
		String sessionId = MOTPUtils.sendMobileOTP(phoneNo);
		logger.info("Motp session id "+ sessionId + " for login "+ login);
		
		if(StringUtils.isEmpty(sessionId)){
			logger.info("Motp session id is not generated for login "+ login);
			return false;
		}
		
		user.setPhoneNo(phoneNo);
		user.setRegisterd(false);
		userDAO.save(user);
		return true;		
	}

	/**
	 * Check if the login has to upload the documents or not.
	 * @param login input login params.
	 * @return true if any error false 
	 */
	public boolean checkUserUploadDocument(String login) {
		User user = validateUser(login);
		if(user == null){
			logger.info("Either user is not found or not activated");
			return true;
		}
		
		if(user.isEmailCorporate()){
			return false;
		}
		
		return true;
	}
	
	
	/**
	 *  Validate Profile Params.
	 */
	private boolean validateUserProfile(JSONObject detailsJson, boolean isValid) {
		String firstName = detailsJson.getString("firstname");
		// if name is not present, the it must return error.
		if(isValid && Utils.isNull(firstName)){
			logger.info(" first name is missing in the json Object " + detailsJson);
			isValid = false;
		}

		String lastName = detailsJson.getString("lastname");
		// if name is not present, the it must return error.
		if(isValid && Utils.isNull(lastName)){
			logger.info(" last name is missing in the json Object " + detailsJson);
			isValid = false;
		}
		
		String email = detailsJson.getString("email");
		if( isValid && Utils.isNull(email)){
			logger.info(" email is missing in the json Object "+detailsJson);
			isValid = false;
		}
			
		if(isValid && (!Utils.validateEmail(email))){
			logger.info(" Invalid email in the json "+detailsJson);
			isValid = false;
		}
		return isValid;
	}
}
