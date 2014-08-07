package com.wagonlift.services;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wagonlift.dao.CurrentLocationDAO;
import com.wagonlift.dao.OfferDAO;
import com.wagonlift.dao.RideDAO;
import com.wagonlift.dao.RideRequestDAO;
import com.wagonlift.models.CurrentLocation;
import com.wagonlift.models.LatLng;
import com.wagonlift.models.Offer;
import com.wagonlift.models.Ride;
import com.wagonlift.models.RideRequest;
import com.wagonlift.models.RideStatus;
import com.wagonlift.models.User;
import com.wagonlift.utils.Utils;

@Service
public class RideService{

	@Autowired
	RideRequestDAO rideRequestDAO;
	
	@Autowired
	OfferDAO offerDAO;
	
	@Autowired
	UserService userService;

	@Autowired
	CurrentLocationDAO currentLocationDAO;
	
	@Autowired
	RideDAO rideDAO;
	
	private static final Logger logger = Logger.getLogger(RideService.class);
	
	/**
	 * Create Ride Request for the rider.
	 * @param detailsJson
	 * @param userId
	 * @return
	 */
	public String createRideRequest(JSONObject detailsJson, String userId) {
		User user =  userService.findUserbyColumnName("id", userId);
		if(user==null){
			logger.info("User with id "+ userId + " is not found in the database");
			return null;
		}
		
		JSONObject sourceJson = detailsJson.getJSONObject("source");
		if(sourceJson.isNullObject()  || sourceJson.isEmpty()){
			logger.info("User with id "+ userId + " is not found in the database");
			return null;
		}
		
		JSONObject destinationJson = detailsJson.getJSONObject("destination");
		if(destinationJson.isNullObject()  || destinationJson.isEmpty()){
			logger.info("User with id "+ userId + " is not found in the database");
			return null;
		}
		
		
		LatLng source = new LatLng(sourceJson.getDouble("lat"), sourceJson.getDouble("lng"));
		LatLng destination = new LatLng(destinationJson.getDouble("lat"), destinationJson.getDouble("lng"));
		
		
		Date startTime = null;
		try {
			startTime = Utils.getDateFromString(detailsJson.getString("startTime"));
		} catch (ParseException e) {
			logger.info("Date parsing Error");
			e.printStackTrace();
			return null;
		}
		
		int noOfSeats =  detailsJson.getInt("noOfSeats");
		
		double totalDistance = Utils.distanceInKm(source.getLat(), source.getLng(), destination.getLat(), destination.getLng());
		
		RideRequest rideRequest = new RideRequest();
		rideRequest.setSource(source);
		rideRequest.setDestination(destination);
		rideRequest.setStartTime(startTime);
		rideRequest.setUserId(userId);
		rideRequest.setNoOfSeats(noOfSeats);
		rideRequest.setTotalDistance(totalDistance);
		rideRequestDAO.save(rideRequest);
		
		return rideRequest.getId();
	}
	

	/**
	 * Create offer for the driver.
	 * @param detailsJson
	 * @param userId
	 * @return
	 */
	public String createOfferRequest(JSONObject detailsJson, String userId) {
		User user =  userService.findUserbyColumnName("id", userId);
		if(user==null){
			logger.info("User with id "+ userId + " is not found in the database");
			return null;
		}
		
		JSONObject sourceJson = detailsJson.getJSONObject("source");
		
		if(sourceJson.isNullObject()  || sourceJson.isEmpty()){
			logger.info("User with id "+ userId + " is not found in the database");
			return null;
		}
		
		JSONObject destinationJson = detailsJson.getJSONObject("destination");
		if(destinationJson.isNullObject()  || destinationJson.isEmpty()){
			logger.info("User with id "+ userId + " is not found in the database");
			return null;
		}
		
		LatLng source = new LatLng(sourceJson.getDouble("lat"), sourceJson.getDouble("lng"));
		LatLng destination = new LatLng(destinationJson.getDouble("lat"), destinationJson.getDouble("lng"));
		
		String startTimeString = detailsJson.getString("startTime");
		if(startTimeString==null){
			logger.info("User with id "+ userId + " is not found in the database");
			return null;
		}
		
		Date startTime = null;
		try {
			startTime = Utils.getDateFromString(startTimeString);
		} catch (ParseException e) {
			logger.info("Date parsing Error");
			e.printStackTrace();
			return null;
		}

		int seatsRemaining =  detailsJson.getInt("noOfSeats");
		String polylinePoints = detailsJson.getString("polylinePoints");
		if(polylinePoints==null){
			logger.info("User with id "+ userId + " is not found in the database");
			return null;
		}
		
		Offer offer = new Offer();
		offer.setSource(source);
		offer.setDestination(destination);
		offer.setStartTime(startTime);
		offer.setUserId(userId);
		offer.setSeatsRemaining(seatsRemaining);
		offer.setRideStatus(RideStatus.NEW);
		offer.setPolylinePoints(polylinePoints);
		
		offerDAO.save(offer);
		return offer.getId();
	}


	/**
	 * Search for Drivers.
	 * @param userId The userId.
	 */
	public JSONObject searchDrivers(String requestID) {
		
		RideRequest rideRequest = rideRequestDAO.findOne("id", requestID);
		if(rideRequest==null){
			// send an error.
		}
		
		List<Offer> offers = offerDAO.getAllOffers(rideRequest.getStartTime());
		JSONObject retJson = new JSONObject();
		JSONArray offersArray = new JSONArray();
		List<LatLng> path;
		
		CurrentLocation currentLocation;;
		for(Offer offer : offers){
			
			 path=Utils.decodePoly(offer.getPolylinePoints());
				int k=0;
				boolean isContainSource = false;
			    for(int j=0; j < path.size(); j++) {
		            LatLng a = path.get(j);
					 k = j + 1;
		            if (k >= path.size()) {
		                k = 0;
		            }
		            LatLng b = path.get(k);
		            if (!isContainSource && Utils.pathContains(rideRequest.getSource(), a, b)){
		            	isContainSource = true;
		            }
		            
		            if( isContainSource	&& Utils.pathContains(rideRequest.getDestination(), a, b)) {
		            	JSONObject offerJson = new JSONObject();
		            	offerJson.put("id", offer.getId());
		            	offerJson.put("driverId", offer.getUserId());
		            	currentLocation = currentLocationDAO.findOne("userId", offer.getUserId());
		            	offerJson.put("distance", String.format("%.3f", Utils.distanceInKm(currentLocation.getLocation().getLat()
		            			, currentLocation.getLocation().getLng(), rideRequest.getSource().getLat(), rideRequest.getSource().getLng())));
		            	offersArray.add(offerJson);
		            	break;
		            }
		        }
		}
		
		retJson.put("offers", offersArray);
		return retJson;
	}


	
	
	public JSONObject getRidersInfo(JSONArray jsonArray, String userId, String startTime) throws ParseException {
		JSONObject jsonObject = null;
		
		User user = userService.findUserbyColumnName("id", userId);
		if(user == null){
			logger.info("getRidersInfo return null, User not Found.");
			return jsonObject;
		}
		
		Date startDate = Utils.getDateFromString(startTime);
		List<RideRequest> requestIds = rideRequestDAO.getUserRequests(startDate);
		JSONObject pathJson ;
		int count = 0;
		Map<Integer, Integer> pathUserCountMap = Maps.newHashMap();
		for(RideRequest rideRequest : requestIds){
			String requestUserId = rideRequest.getUserId();
			CurrentLocation currentLocation =  currentLocationDAO.findOne("userId", requestUserId);
			if(currentLocation==null){
				continue;
			}
			
			LatLng latLng = currentLocation.getLocation();
			for(int i=0; i<jsonArray.size(); i++){
				pathJson = jsonArray.getJSONObject(i);
				if(Utils.pathContains(pathJson.getString("path"), latLng)){
					count = 0;
					if(pathUserCountMap.get(i)==null){
						count =1;
					}else{
						count = pathUserCountMap.get(i);
						count = count + 1;
					}
					pathUserCountMap.put(i, count);
				}
			}
		}
		
		
		JSONArray updatedArray = new JSONArray();
		for(int i=0; i<jsonArray.size(); i++){
			JSONObject path = jsonArray.getJSONObject(i);
			path.put("users", pathUserCountMap.get(i));
			updatedArray.add(path);
		}
		
		jsonObject.put("paths", updatedArray);		
		return jsonObject;
	}


	/**
	 * 
	 * @param requestId
	 * @param offerId
	 * @return
	 */
	public boolean acceptOffer(String requestId, String offerId) {
		
		Ride ride = rideDAO.getRide(requestId, offerId);
		
		Offer offer = offerDAO.findOne("id", offerId);
		RideRequest rideRequest = rideRequestDAO.findOne("id", requestId);
		
		if(rideRequest== null){
			logger.info("no such request exists");
			return false;
		}
		
		if(offer==null){
			logger.info("no such offer id exists");
			return false;
		}
		
		if(ride == null){
			ride = new Ride();
			ride.setOfferId(offerId);

			if(offer.getRideStatus().equals(RideStatus.COMPLETED)){
				logger.info("Ride is already completed");
				return false;
			}
			
			ride.setOTP(UUID.randomUUID().toString());
			int seats = offer.getSeatsRemaining();
			int requestedSeats = rideRequest.getNoOfSeats(); 
			seats = seats - requestedSeats;
			if(seats < 0){
				logger.info("no seats remaining");
				return false;
			}
			
			offer.setSeatsRemaining(seats);
			ride.setSeats(seats);
			ride.setStartTime(offer.getStartTime());
			ride.setStatus(RideStatus.NEW);
			List<String> requestIds = Lists.newArrayList();
			rideRequest.setRequestAddressed(true);
			requestIds.add(requestId);
		}else{

			List<String> requestIds = ride.getRideRequests();
			if(requestIds.contains(requestId)){
				logger.info("request id already exists");
				return false;
			}

			if(ride.getStatus().equals(RideStatus.COMPLETED)){
				logger.info("request already completed");
				return false;
			}
			
			int seats = offer.getSeatsRemaining();
			int requestedSeats = rideRequest.getNoOfSeats(); 
			seats = seats - requestedSeats;
			if(seats < 0){
				logger.info("no seats remaining");
				return false;
			}
			offer.setSeatsRemaining(seats);
			rideRequest.setRequestAddressed(true);
			requestIds.add(requestId);
		}
		
		rideDAO.save(ride);
		offerDAO.save(offer);
		rideRequestDAO.save(rideRequest);
		return true;
	}
}
