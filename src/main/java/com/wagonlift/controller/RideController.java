package com.wagonlift.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wagonlift.services.RideService;

@Controller
public class RideController {

	private static final Logger logger = Logger.getLogger(RideController.class);
	
	@Autowired
	RideService rideService;
	
	
	@RequestMapping(value="/ride/request" ,  method = RequestMethod.POST)
	public @ResponseBody String createRideRequest( @RequestParam("details") String details, @RequestParam("userid") String userId){
		
		String requestId = null;
		logger.info("/ride/request called, with details == " + details + " and userId " + userId);
		if (StringUtils.isEmpty(details) || StringUtils.isEmpty(userId)) { 
			logger.info("Invalid Parameters in the request");
			return requestId;
		}
		
		try{
			JSONObject detailsJson =  (JSONObject) JSONSerializer.toJSON(details);
			if(detailsJson.isNullObject() || detailsJson.isEmpty()){
				logger.info("details is missing in json object.");
				return requestId;
			}
			
			requestId = rideService.createRideRequest(detailsJson, userId);
			
		}catch (Exception e) {
			logger.info("Some Error has been occurred");
			e.printStackTrace();
		}
		
		return requestId;
	}
	
	@RequestMapping(value="/ride/offer",  method = RequestMethod.POST)
	public @ResponseBody String createOfferRequest( @RequestParam("details") String details, @RequestParam("userid") String userId){
		
		String requestId = null;
		logger.info("/ride/offer called, with details == " + details + " and userId " + userId);
		if (StringUtils.isEmpty(details) || StringUtils.isEmpty(userId)) { 
			logger.info("Invalid Parameters in the request");
			return requestId;
		}
		
		try{
			JSONObject detailsJson =  (JSONObject) JSONSerializer.toJSON(details);
			if(detailsJson.isNullObject() || detailsJson.isEmpty()){
				logger.info("/ride/offer return null, details is missing in json object.");
				return requestId;
			}
			
			requestId = rideService.createOfferRequest(detailsJson, userId);
			
		}catch (Exception e) {
			logger.info("/ride/offer return null, Some Error has been occurred");
			e.printStackTrace();
		}
		return requestId;
	}

	@RequestMapping(value="/ride/getDrivers")
	public @ResponseBody JSONObject searchDrivers(@RequestParam("requestId") String requestId){
		
		logger.info("/ride/getDrivers called with requestId "+ requestId);
		
		if (StringUtils.isEmpty(requestId)) { 
			logger.info("Invalid Parameters in the request");
			return null;
		}
		
		try{
				return rideService.searchDrivers(requestId);
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("/ride/getDrivers returns null, some Error has been occurred");
		}
		
		return null;
	}
	
	
	@RequestMapping(value="/ride/getRiders")
	public @ResponseBody JSONObject getRidersInfo(@RequestParam("paths") String paths, @RequestParam("userId") String userId, 
			@RequestParam("startTime")String startTime){
		JSONObject jsonObject = null;
		
		logger.info("/ride/getDrivers called with userId "+ userId + " and paths ");
		
		if( StringUtils.isEmpty(paths) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(startTime)){
			logger.info("/ride/getRiders returns null, missing params");
			return jsonObject;
		}
		
		jsonObject = (JSONObject) JSONSerializer.toJSON(paths);
		
		if(jsonObject.isNullObject() || jsonObject.isEmpty()){
			logger.info("/ride/getRiders returns null, json object is empty.");
			return null;
		}
		
		JSONArray pathArray = jsonObject.getJSONArray("paths");
		if(pathArray.isEmpty()){
			logger.info("/ride/getRiders returns null, json object is empty.");
			return null;
		}
		try{
			
			jsonObject = rideService.getRidersInfo(pathArray, userId, startTime);
			
		}catch (Exception e) {
			logger.info("/ride/getRiders returns null, some error has been occurred " + e);
		}
		return jsonObject;
	}
	
	@RequestMapping("/ride/acceptOffer")
	public boolean acceptOffer(@RequestParam("offerId") String offerId, @RequestParam("requestId")String requestId){
		boolean status = false;
		logger.info("/ride/acceptOffer called with offerId "+offerId+ " request Id "+ requestId);
		if(StringUtils.isEmpty(requestId) || StringUtils.isEmpty(offerId)){
			return false;
		}
		
		try{
			 status = rideService.acceptOffer(requestId, offerId);
		  }catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
}
