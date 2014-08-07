package com.wagonlift.services;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wagonlift.constants.Constants;
import com.wagonlift.models.LatLng;
import com.wagonlift.utils.HTMLUtils;
import com.wagonlift.utils.Utils;

@Service
public class DirectionService {

	
	/**
	 * Check whether a point (LatLong) exists within a Google Map. 
	 * @param origin String / latitude,longitude.
	 * @param destination String / latitude,longitude.
	 * @param lat : Search latitude value
	 * @param lng : Search longitude value.
	 * @return {@link JSONObject}
	 */
	public JSONObject getRouteDetails(String origin, String destination, double lat, double lng) {
		// String https_url =
		// "https://maps.googleapis.com/maps/api/directions/json?origin=28.587477,77.3683271&destination=28.6414985,77.3713848&key=AIzaSyCIv6MI0xqfX5Fwt-MAa_K7wUVJRBSRt2M&mode=driving&alternatives=true";
		List<String> overviewPolyline = Lists.newArrayList();
		Map<String, String> params = Maps.newHashMap();
		params.put("key", Constants.GOOGLE_DIRECTIONS_API_KEY);
		params.put("origin", origin);
		params.put("destination", destination);
		params.put("mode", "driving");
		params.put("alternatives", "true");
		String response = HTMLUtils.callHttpsURL(
				Constants.GOOGLE_DIRECTIONS_API_URL, params, Constants.HTTP_METHOD_GET);
		System.out.println(response);
		JSONObject directionDetails = JSONObject.fromObject(response);
		JSONArray routeArray = directionDetails.getJSONArray("routes");
		
		LatLng point = new LatLng(lat,lng);
		//int crossings=0;
		List<LatLng> path;
		JSONObject routeInfo = new JSONObject();
		routeInfo.put("origin", origin);
		routeInfo.put("destination", destination);
		for (int i = 0; i < routeArray.size(); i++) {
			JSONObject routeJson = routeArray.getJSONObject(i);
			overviewPolyline.add(routeJson.getJSONObject("overview_polyline")
					.getString("points"));
			 path=Utils.decodePoly(routeJson.getJSONObject(
					"overview_polyline").getString("points"));
			int k=0;
		    for(int j=0; j < path.size(); j++) {
	            LatLng a = path.get(j);
				 k = j + 1;
	            if (k >= path.size()) {
	                k = 0;
	            }
	            LatLng b = path.get(k);
	            if (pathContains(point, a, b)) {
	            	routeInfo.put("via", routeJson.getString("summary"));
	            }
	        }
		}
		return routeInfo;
	}

	
	/**
	 * Function return the drivers distance from the particular latitude/longitude.
	 * @param lat The latidude value from api.
	 * @param lng The longitude value from api.
	 * @return {@link JSONObject}
	 */
	public JSONObject getDriversDistanceDetails(double lat, double lng){
		JSONObject driverDistanceDetails = new JSONObject();
		JSONArray driverArray = new JSONArray();
		JSONObject driverJsonObject = new JSONObject();
		driverJsonObject.put("driver", "Himanshu Arora");
		driverJsonObject.put("location", "28.641630, 77.371386");
		driverJsonObject.put("distance", String.format("%.3f",Utils.distanceInKm(lat, lng, 28.641630, 77.371386)));
		driverArray.add(driverJsonObject);
		
		driverJsonObject = new JSONObject();
		driverJsonObject.put("driver", "Vishal Arora");
		driverJsonObject.put("location", "28.621357, 77.359464");
		driverJsonObject.put("distance", String.format("%.3f", Utils.distanceInKm(lat, lng, 28.621357, 77.359464)));
		driverArray.add(driverJsonObject);
		driverDistanceDetails.put("drivers", driverArray);
		return driverDistanceDetails;
	}
	
	
	/**
	 * Check weather given point exists between point a and point b. 
	 * @param point The searchable value.
	 * @param a The start point.
	 * @param b The 
	 * @return true/false
	 */
	private boolean pathContains(LatLng point, LatLng a, LatLng b) {

		double px = point.getLng();
		double py = point.getLat();

		double ax = a.getLng();
		double ay = a.getLat();
		double bx = b.getLng();
		double by = b.getLat();

		if (ay > by) {
			ax = b.getLng();
			ay = b.getLat();
			bx = a.getLng();
			by = a.getLat();
		}
		// alter longitude to cater for 180 degree crossings
		if (px < 0) {
			px += 360;
		}
		if (ax < 0) {
			ax += 360;
		}
		if (bx < 0) {
			bx += 360;
		}

		
		if (py == ay || py == by)
			py += 0.00000001;
		
		if ((py > by || py < ay) || (px > Math.max(ax, bx))){
			return false;
		}
		
		if (px < Math.min(ax, bx)){
			return true;
		}
		
		double red = (ax != bx) ? ((by - ay) / (bx - ax)): Double.MAX_VALUE;
		double blue = (ax != px) ? ((py - ay) / (px - ax)):Double.MAX_VALUE;
		return (blue >= red);
	}
}
