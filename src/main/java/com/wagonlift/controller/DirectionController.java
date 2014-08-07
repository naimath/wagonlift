package com.wagonlift.controller;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wagonlift.services.DirectionService;

@Controller
@RequestMapping("/maps")
public class DirectionController {

	private String LAT_LONG_REGEX = "^\\-?\\d+(\\.\\d+)?,\\-?\\d+(\\.\\d+)?(;\\-?\\d+(\\.\\d+)?,\\-?\\d+(\\.\\d+)?)*$";
	@Autowired
	DirectionService directionService;
	
	
	@RequestMapping("direction")
	public @ResponseBody
	JSONObject getDirectionsInfo(@RequestParam("origin") String origin,
			@RequestParam("destination") String destination,@RequestParam(required=false,value="lat")String lat,@RequestParam(required=false,value="lng")String lng) {

		JSONObject retJsonObject = new JSONObject();
		/*if(!origin.matches(LAT_LONG_REGEX)){
			retJsonObject.put("error", "wrong origin value, it should be lat,long");
			return retJsonObject;
		}
		
		if(!destination.matches(LAT_LONG_REGEX)){
			retJsonObject.put("error", "wrong destination value, it should be lat,long");
			return retJsonObject;
		}
		*/
		retJsonObject = directionService.getRouteDetails(origin, destination, Double.valueOf(lat), Double.valueOf(lng));
		return retJsonObject;
	}


	@RequestMapping("distance")
	public @ResponseBody
	JSONObject getDistance(@RequestParam("location") String location) {
		JSONObject retJsonObject = new JSONObject();
		if(!location.matches(LAT_LONG_REGEX)){
			retJsonObject.put("error", "wrong value, it should be lat,long");
			return retJsonObject;
		}
		String[] locationSplit = location.split(",");
		retJsonObject = directionService.getDriversDistanceDetails(Double.valueOf(locationSplit[0]),Double.valueOf(locationSplit[1]));
		return retJsonObject;
	}	
	
	
}
