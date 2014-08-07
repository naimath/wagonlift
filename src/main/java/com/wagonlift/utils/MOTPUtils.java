package com.wagonlift.utils;

import java.util.Map;

import net.sf.json.JSONObject;

import com.google.common.collect.Maps;
import com.wagonlift.constants.Constants;

public class MOTPUtils {

	public static String sendMobileOTP(String phoneNo){
		String response = HTMLUtils.callHttpsURL(
				Constants.MOTP_CREATE_OTP_URL+phoneNo, null, Constants.HTTP_METHOD_GET);
		if(response!=null){
			JSONObject responseJson = JSONObject.fromObject(response);
			if(responseJson.getString("Status").equals("Success")){
				return responseJson.getString("Result");
			}
		}
		return null;
	}
	
	
	public static String verifyMobileOTP(String sid){
		
		Map<String, String> params = Maps.newHashMap();
		params.put("private", Constants.MOTP_PRIVATE_KEY);
		
		String response = HTMLUtils.callHttpsURL(
				Constants.MOTP_VALIDATE_OTP_URL+sid, params, Constants.HTTP_METHOD_POST);
		if(response!=null){
			JSONObject responseJson = JSONObject.fromObject(response);
			if(responseJson.getString("Status").equals("Success")){
				return responseJson.getString("Result");
			}
		}
		return null;
	}

	
}
