package com.wagonlift.constants;

public class Constants {

	public static final String DATABASE_NAME = "wagonlift";
	
	// http methods
	public static final String HTTP_METHOD_POST	= "POST";
	public static final String HTTP_METHOD_GET 	= "GET";

	public static final String GOOGLE_DIRECTIONS_API_URL = "https://maps.googleapis.com/maps/api/directions/json";
	public static final String GOOGLE_DIRECTIONS_API_KEY = "AIzaSyCIv6MI0xqfX5Fwt-MAa_K7wUVJRBSRt2M";
	
	public static final String AUTHORIZED_DOMAIN = "wagonlift.com";
	public static final String MOTP_API_KEY = "0004-67f23e1d-53aebf38-13c3-eccff1ae";
	public static final String MOTP_PRIVATE_KEY = "0009-67f23e1d-53aebf38-13c8-54b9c031";
	
	public static final String MOTP_CREATE_OTP_URL = "https://api.mOTP.in/v1/"+ MOTP_API_KEY +"/";
	public static final String MOTP_VALIDATE_OTP_URL = "https://api.mOTP.in/v1/OTP/"+ MOTP_API_KEY +"/";
	
	public static final int BCRYPT_SALT = 12;
}
