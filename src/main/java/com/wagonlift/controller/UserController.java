package com.wagonlift.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.wagonlift.constants.Constants;
import com.wagonlift.form.UserRegistrationForm;
import com.wagonlift.models.User;
import com.wagonlift.models.UserVehicle;
import com.wagonlift.services.UserService;
import com.wagonlift.utils.BCrypt;
import com.wagonlift.utils.Utils;

@Controller
public class UserController {
	
	@Autowired private UserService userService;

	@Value("${login.failure.code}")
	private String failureCode;
	@Value("${login.failure.message}")
	private String failureMessage;
	
	@Value("${login.invalid-id.activate.code}")
	private String invalidActivationIdCode;
	@Value("${login.invalid-id.activate.message}")
	private String invalidActivationIdMessage;
	
	
	private static String INDIA_CODE = "+91";
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	@RequestMapping("/")
	public String index(){
		return "login";
	}

	@RequestMapping("/contact")
	public String contact(){
		return "contact";
	}

	@RequestMapping("/cgi-sys/defaultwebpage.cgi")
	public String apacheDefaultWebpage(){
		return "index";
	}
	
	/**
	 * Service notify user and admin related to the message received.
	 * @param name
	 * @param email
	 * @param subject
	 * @param message
	 * @return
	 */
	@RequestMapping(value = "/notifyme", method = RequestMethod.POST)
	public ModelAndView notifyAndSendEmail(@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("subject") String subject,
			@RequestParam("message") String message) {
		logger.info("/notify/save called, with details == name = " + name
				+ " email" + email + " subject" + subject + " message"
				+ message);
		ModelAndView modelAndView = new ModelAndView("contact");
		
		modelAndView.addObject("message", "<font color='red' size=2><b>" + "some error has been occurred"
				+ "</b></font>");
		
		if (StringUtils.isEmpty(name)){
			modelAndView.addObject("message", "<font color='red' size=2><b>" + "Name Required."
					+ "</b></font>");
			logger.info("/notify/save return ,missing name ");
			return modelAndView;
		}
		
		if(StringUtils.isEmpty(email)){
			modelAndView.addObject("message", "<font color='red' size=2><b>" + "Email Required."
					+ "</b></font>");
			logger.info("/notify/save return ,missing email ");
			return modelAndView;
		}

		if(!Utils.validateEmail(email)){
			modelAndView.addObject("message", "<font color='red' size=2><b>" + "Invalid Email."
					+ "</b></font>");
			logger.info("/notify/save return ,invalid email ");
			return modelAndView;
		}
		
		try{
			return userService.notifyandSave(name, email, subject, message, modelAndView);
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("/notify/save return ,some error has been occurred.");
			modelAndView.addObject("message", "<font color='red' size=2><b>" + "Some error has been occurred."
					+ "</b></font>");
		}
		
		logger.info("/notify/save return  ");
		return modelAndView;
	}
	
	
	/**
	 * Service used to verify user login, phone number and email.
	 * @param partURL Path Variable used for  login email and phone number.
	 * @param login user input login.
	 * @param phoneNo
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/user/verify-user-{partURL}", method = RequestMethod.POST)
	public @ResponseBody
	boolean verifyUserLogin(@PathVariable("partURL") String partURL, @RequestParam(value = "login", required = false) String login,
			@RequestParam(value = "phoneno", required = false) String phoneNo, @RequestParam(value = "email", required = false) String email) {
		boolean result = false;
		logger.info("/user/verify-user-"+partURL +" called ");

		if(StringUtils.equalsIgnoreCase(partURL, "login")){
			if (StringUtils.isEmpty(login)) {
				logger.info("/user/verify-user-login return false, empty / null login name ");
				return false;
			}
	
			if (!Utils.validateLogin(login)) {
				logger.info("/user/verify-user-login return false, login name not valid ");
				return false;
			}
			result = userService.verifyUserRegisterParams(StringUtils.lowerCase(login), "login");
			
		}else if(StringUtils.equalsIgnoreCase(partURL, "phoneno")){
		
			if (StringUtils.isEmpty(phoneNo)) {
				logger.info("/user/verify-user-phone return false, empty / null phone name ");
				return false;
			}

			if (!Utils.validatePhone(phoneNo)) {
				logger.info("/user/verify-user-phone return false, phone number is not valid ");
				return false;
			}
			result = userService.verifyUserRegisterParams(StringUtils.lowerCase(phoneNo), "phoneNo");
			
		}else if(StringUtils.equalsIgnoreCase(partURL, "email")){
		
			if (StringUtils.isEmpty(email)) {
				logger.info("/user/verify-user-email return false, empty / null email name ");
				return false;
			}

			if (!Utils.validateEmail(email)) {
				logger.info("/user/verify-user-email return false, email id is not valid ");
				return false;
			}
			result = userService.verifyUserRegisterParams(StringUtils.lowerCase(email), "email");
		}
		else{
			logger.info("/user/verify-user-"+partURL+ " is not valid url");	
		}
		logger.info("/user/verify-user-login return, with result =  " + result);
		return result;
	}
	
	/**
	 * Register user with password login phone no.
	 * @param login
	 * @param password
	 * @param phoneNo
	 * @return True if register else false.
	 */
	@RequestMapping(value = "/user/register.do", method = RequestMethod.POST)
	public @ResponseBody String registerUser(@RequestParam("login") String login,
			@RequestParam("password") String password,
			@RequestParam("phoneno") String phoneNo) {
		String result = null;
		logger.info("/user/register called, with login =  " + login + " password " + "****" + " phoneno " + phoneNo);
		
		if(StringUtils.isEmpty(login) || StringUtils.isEmpty(password) || StringUtils.isEmpty(phoneNo) ){
			logger.info("/user/register return, with result =  false, empty/null parameters");				
			return "{\"Status\":\"Failure\",\"Result\":\"empty parameters\"}";
			
		}
		
		if(!Utils.validateLogin(login)){			
			return "{\"Status\":\"Failure\",\"Result\":\"invalid login value\"}";
		}
		
		if(!Utils.validatePhone(phoneNo)){
			logger.info("/user/register return, with result =  false, invalid phone value");			
			return "{\"Status\":\"Failure\",\"Result\":\"invalid phone value\"}";
		}
		
		password = BCrypt.hashpw(password, BCrypt.gensalt(Constants.BCRYPT_SALT));
		
		result = userService
				.register(StringUtils.lowerCase(login),
						StringUtils.lowerCase(password),
						StringUtils.lowerCase(INDIA_CODE+phoneNo));
		
		logger.info("/user/register return, with result =  " + result);
		return result;
	}

	/**
	 * Verify mobile user.
	 * @param phoneNo The user phone number.
	 * @return True / False
	 */
	@RequestMapping(value="/user/verifyotp.do", method=RequestMethod.POST)
	public @ResponseBody boolean verifyUserOTP(@RequestParam("sid") String sid, @RequestParam("login") String login,
			@RequestParam("code") String code){
		
		logger.info("/user/verifyotp called, with sid= " + sid + " login=" + login +" code " + code);
		boolean result = false;
		
		if (Utils.isNull(login) || Utils.isNull(sid) || Utils.isNull(code)) {
			logger.info("/user/verifyotp return false, blank / empty sid / login");
			return false;
		}
		
		if(!Utils.validateLogin(login)){
			logger.info("/user/verifyotp return false, invalid login value");
			return false;
		}
		
		try {
			result = userService.verifyLoginOTP(login, sid, code);
		}catch(Exception e){
			logger.error("error in /user/mobile-auth return", e);
			return false;
		}
		
		logger.info("/user/verifyotp return return, with result "+result);
		return result;
	}

	

	/**
	 * POST /user/register Response: status object
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/user/set-profile", method = RequestMethod.POST)
	public @ResponseBody
	Object registerAuth(@RequestParam("login") String login, @RequestParam(value = "details", required = true) String profileDetails, 
			@RequestParam(value="profilepic", required = false) MultipartFile picMultipartFile,
			@RequestParam(value="vehiclePic", required = false) MultipartFile vehiclePicMultipart) {

		logger.info("/user/register called, with details == " + profileDetails +" login "+ login);

		Map<String, String> statusMap = Maps.newHashMap();
		statusMap.put("code", failureCode);
		statusMap.put("message", failureMessage);

		try {
			if (StringUtils.isEmpty(profileDetails) || StringUtils.isEmpty(login) || !Utils.validateLogin(login)) {
				logger.info("/user/register return, with result == "
						+ statusMap.get("code") + " invalid parameters");
				return statusMap;
			}
			
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(profileDetails);
			if(jsonObject.isNullObject()){
				logger.info("/user/register return, with result == " + statusMap.get("code"));
				return statusMap;
				
			}
			return userService.setUserProfile(login, jsonObject, picMultipartFile, vehiclePicMultipart);

		} catch (IOException e) {
			logger.error("error in reading files", e);
			e.printStackTrace();
		}
		catch (Exception e) {
			logger.error("error in /user/register", e);
			e.printStackTrace();
		}
		logger.info("/user/register return, with result == "
				+ statusMap.get("code"));
		return statusMap;
	}

	/**
	 * /user/changepassword
	 * @param email
	 * @param newPassword
	 * @return Status
	 */
	@RequestMapping(value="/user/changepassword", method=RequestMethod.POST)
	public boolean updateCredentials(@RequestParam(value="email", required=true) String login,
			@RequestParam(value="oldpassword", required=true) String oldPassword,
			@RequestParam(value="password", required=true) String newPassword) {
		
		logger.info("/user/changepassword called, with login == " + login);
		try {
			
			if (StringUtils.isEmpty(login) || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
				logger.info("/user/changepassword return, with result == false, [email, oldpassword, password] all required");
				return false;
			}
		
			return userService.changePassword(login, oldPassword, newPassword);

		} catch (Exception e) {
			logger.error("error in /user/changepassword", e);
			e.printStackTrace();
		}

		logger.info("/user/changepassword return, with result == false");
		return false;
	}

	/**
	 * Forgot Password.
	 * @param email The email of the user.
	 * @return boolean
	 */
	@RequestMapping(value="/user/forgotpassword", method=RequestMethod.GET)
	public boolean processForgotPassword(@RequestParam("email") String email) {
		
		logger.info("/user/forgotpassword called, with email == " + email);
		
		try {
			
			if(StringUtils.isEmpty(email) || !Utils.validateEmail(email)) {
				logger.info("/user/forgotpassword return, invalid email");
				return false;
			}
			
			return userService.forgotPassword(email);
			
		} catch (Exception e) {
			logger.error("error in /user/forgotpassword", e);
			e.printStackTrace();
		}
		logger.info("/user/forgotpassword return, with result == false");
		return false;
	}

	@RequestMapping(value="/user/activate.do/{activationId}", method=RequestMethod.GET)
	public @ResponseBody Map<String, String> activateAccount(@PathVariable("activationId") String activationId) {
		
		logger.info("/user/activate/{activationId} called, with activationId == " + activationId);
		Map<String, String> statusMap = Maps.newHashMap();
		activationId = activationId.trim();
		
		if(StringUtils.isEmpty(activationId)) {
			statusMap.put("code", invalidActivationIdCode);
			statusMap.put("message", invalidActivationIdMessage);
			logger.info("/user/activate/{activationId} return, with result == Invalid activation id.");
			return statusMap;
		}
		
		try {
			return userService.activateUser(activationId);
		} catch (Exception e) {
			logger.error("error in /user/activate/{activationId}", e);
		}

		statusMap.put("code", failureCode);
		statusMap.put("message", failureMessage);
		logger.info("/user/activate/{activationId} return, with result == Failed to activate your account.");
		return statusMap;
	}

	/**
	 * @param   email
	 * @param   oldEmail
	 * @param   password
	 * @return	true / false
	 */
	@RequestMapping(value="/user/change-email", method=RequestMethod.POST)
	public @ResponseBody Map<String, String> changeEmailAddress(@RequestParam(value="email", required=true) String email,
			@RequestParam(value="oldemail", required=true) String oldEmail,
			@RequestParam(value="password", required=true) String password) {
		
		logger.info("/user/change-email called, with old email == " + oldEmail+", and new email = "+email);
		Map<String, String> statusMap = Maps.newHashMap();
		try {
			
			if (StringUtils.isEmpty(email) || StringUtils.isEmpty(oldEmail) || StringUtils.isEmpty(password)) {
				statusMap.put("code", failureCode);
				statusMap.put("message", failureMessage);
				logger.info("/user/change-email return, with result == false, [email, oldpassword, password] all required");
				return statusMap;
			}
			
			return userService.changeEmail(email, oldEmail, password);
			
		} catch (Exception e) {
			logger.error("error in /user/change-email", e);
			e.printStackTrace();
		}
		
		logger.info("/user/change-email return, with result == false");
		statusMap.put("code", failureCode);
		statusMap.put("message", failureMessage);
		return statusMap;
	}

	@RequestMapping(value="/user/resend-activate", method=RequestMethod.POST)
	public boolean resendActivationMail(@RequestParam("email") String email,
			@RequestParam("password") String password){
		
		logger.info("/user/resend-activate called, with email == " + email);
		
		if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
			logger.info("/user/resend-activate return, with result == false, email and password required");
			return false;
		}
		
		try {
				return userService.resendActivate(email.trim(), password.trim());
		
		}catch(Exception e){
			logger.error("error in /user/resend-activate", e);
			e.printStackTrace();
		}
		
		logger.info("/user/resend-activate return, with result == false");
		return false;
	
	}
	
	@RequestMapping(value="/user/getlogin.do", method = RequestMethod.POST)
	public @ResponseBody boolean getUserLogin(@RequestParam("login") String login, @RequestParam("password") String password){
		
		boolean result = false;
		logger.info("/user/getlogin called with login "+ login + " password " +"*****");
		
		if(Utils.isNull(login) || Utils.isNull(password) || !Utils.validateLogin(login)){
			logger.info("/user/getlogin return, result = false, Invalid Parameters ");
			return false;
		}
		
		result  = userService.getUserLogin(login, password);
		logger.info("/user/getlogin return, result = "+ result);
		return result;
	}
	
	
	@RequestMapping(value="/user/profile.do", method=RequestMethod.GET)
	public String getProfile(@RequestParam("login") String login,Map model){
		
	    Map<String, Object> resultMap = getUserDetails(login);
	    User user = (User)resultMap.get("user" );
	    UserVehicle userVehicle = (UserVehicle)resultMap.get("vehicle" );
	    UserRegistrationForm registrationForm = new UserRegistrationForm();
	    registrationForm.populate(user,userVehicle);
        model.put("registrationForm", registrationForm);
		return "profile";
	}
	
	
	@RequestMapping(value="/user/profilePic.do", method=RequestMethod.GET)
	public void getProfilePic(@RequestParam("login") String login,Map model,HttpServletResponse  response){
		byte[] profilePicData=userService.getProfilePic(login);
		
		try {	
			  response.setContentType("image/jpeg");
			  OutputStream outputStream = response.getOutputStream();
			  outputStream.write(profilePicData);
			  outputStream.close();
		} catch (Exception e) {
		}
			
		
	}
	
	@RequestMapping(value="/user/updateprofile.do", method=RequestMethod.POST)
	public String updateProfile(@ModelAttribute("registrationForm") UserRegistrationForm registrationForm,@RequestParam(value="profilepic", required = false) MultipartFile picMultipartFile,
			@RequestParam(value="vehiclePic", required = false) MultipartFile vehiclePicMultipart,Model model){
		
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
				 public boolean apply(Object source, String name, Object value) {
					 try {
						 Field field = source.getClass().getDeclaredField(name);
						 Annotation[] annotations = field.getAnnotations();
						 for(Annotation annotation : annotations) {
							 if( annotation.toString().contains("JsonTransient") ){
								 return true;
							 }
						 }
					 } catch (SecurityException e) {
						 //Not interested in these cases
						 e.printStackTrace();
					 } catch (NoSuchFieldException e) {
					 //Not interested in these cases
					 }
				 return false;
				 }
		 });
		JSONObject jsonObject = JSONObject.fromObject( registrationForm, jsonConfig ); 
	
		//JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(registrationForm);
		if(jsonObject.isNullObject()){
			//	logger.info("/user/register return, with result == " + statusMap.get("code"));
			//	return statusMap;
			
		}
		try {
			userService.setUserProfile(registrationForm.getLogin(), jsonObject, picMultipartFile, vehiclePicMultipart);
		} catch (Exception e) {				
			e.printStackTrace();
		}
		return "profile";
	}

	

	@RequestMapping(value="/user/getdetails", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getUserDetails(@RequestParam("login") String login){
		
		Map<String, Object> result = null;
		logger.info("/user/getDetails called with login "+ login );
		
		if(Utils.isNull(login) || !Utils.validateLogin(login)){
			logger.info("/user/getlogin return, result = false, Invalid Parameters ");
			return null;
		}
		
		result  = userService.getUserDetails(login);
		logger.info("/user/getlogin return, result = ");
		return result;
	}
	
	/**
	 * update User phone number.
	 * @param phoneNo The user phone number.
	 * @return {@link Map} status.
	 */
	@RequestMapping(value="/user/update-phone", method=RequestMethod.POST)
	public @ResponseBody
	boolean updateUserPhoneNumber(@RequestParam("login") String login,
			@RequestParam("phoneno") String phoneNo) {
		
		logger.info("/user/sendotp called, with phone == " + phoneNo);
		boolean result = false;
		if (StringUtils.isEmpty(phoneNo) ||  !Utils.validatePhone(phoneNo)) {
			logger.info("/user/mobile-auth return, with result == false, invalid phoneNo");
			return false;
		}
		
		if (StringUtils.isEmpty(login) ||  !Utils.validateLogin(login)) {
			logger.info("/user/mobile-auth return, with result == false, invalid phoneNo");
			return false;
		}

		try {
			result  = userService.updateUserPhone(login, phoneNo);
		}catch(Exception e){
			logger.error("error in /user/mobile-auth return", e);
		}
		
		logger.info("/user/mobile-auth return return, with result == false");
		return result;
	}
	
	
	@RequestMapping("/user/check-doc-status")
	public @ResponseBody boolean doesUserUploadDocumen(@RequestParam("login") String login){
		
		boolean result = true;
		logger.info("/user/check-doc-status called, with login== " + login);
		if(Utils.isNull(login) ||  !Utils.validateLogin(login)){
			logger.info("/user/check-doc-status return false");	
		}
		
		result = userService.checkUserUploadDocument(login);
		return result;
		
	}
	
	/**
	 * Verify mobile user.
	 * @param phoneNo The user phone number.
	 * @return True / False
	 */
	@RequestMapping(value="/user/doc-upload", method=RequestMethod.POST)
	public @ResponseBody Map<String, Boolean> uploadDocument( @RequestParam("phoneno") String phoneNo, 
		@RequestParam(value = "doc") MultipartFile docMultipartFile){

		logger.info("/user/doc-upload called, with phoneNo== " + phoneNo);
		Map<String, Boolean> statusMap = Maps.newHashMap();
		if (StringUtils.isEmpty(phoneNo) || !phoneNo.matches("\\d{10}")) {
			logger.info("/user/doc-upload return, with result == false, invalid phoneNo");
			statusMap.put("status", false);
			return statusMap;
		}
		
		if(docMultipartFile==null || docMultipartFile.isEmpty()){
			logger.info("/user/doc-upload return, with result == false, empty multipart");
			statusMap.put("status", false);
			return statusMap;
		}
		
		String extension = docMultipartFile.getName().substring(docMultipartFile.getName().lastIndexOf("."));
		if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png")){
			logger.info("/user/doc-upload return, with result == false, Wrong format");
			statusMap.put("Status", false);
			return statusMap;
		}
		
		try {
			 return userService.updateUserDocument(phoneNo, docMultipartFile);
		}catch(Exception e){
			logger.error("error in /user/doc-upload return", e);
			e.printStackTrace();
		}
		
		logger.info("/user/doc-upload return return, with result == false");
		statusMap.put("status", false);
		return statusMap;
	}

	@RequestMapping(value = "/user/upload-vehicle-info", method = RequestMethod.POST)
	public @ResponseBody Map<String, Boolean> uploadUserVehicleInfo(@RequestParam(value = "details", required = true) String details, 
			@RequestParam(value="pic", required = false) MultipartFile picMultipartFile, @RequestParam("phoneno") String phoneNo) {

		logger.info("/user/register called, with details == ");
		Map<String, Boolean> statusMap = Maps.newHashMap();

		if (StringUtils.isEmpty(phoneNo) || !phoneNo.matches("\\d{10}")) {
			logger.info("/user/doc-upload return, with result == false, invalid phoneNo");
			statusMap.put("status", false);
			return statusMap;
		}

		try {
			if (StringUtils.isEmpty(details) ) {
				logger.info("/user/register return, with result == "
						+ statusMap.get("code"));
				statusMap.put("status", false);
				return statusMap;
			}
			JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(details);
			return userService.uploadUserVehicleInfo(jsonObject, picMultipartFile, phoneNo);

		} catch (IOException e) {
			logger.error("error in reading files", e);
			e.printStackTrace();
		}
		catch (Exception e) {
			logger.error("error in /user/register", e);
			e.printStackTrace();
		}
		
		logger.info("/user/register return, with result == "
				+ statusMap.get("code"));
		return statusMap;
	}
}
