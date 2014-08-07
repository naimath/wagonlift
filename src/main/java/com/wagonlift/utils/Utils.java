package com.wagonlift.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.wagonlift.models.Attachment;
import com.wagonlift.models.LatLng;

public class Utils {

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		    		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);

	 private static final String VALID_USERNAME_PATTERN_REGEX = "^[a-z0-9_-]{3,15}$";
	 
	public static boolean pathContains(String path, LatLng source){
		
		List<LatLng> paths = Lists.newArrayList();
		paths=Utils.decodePoly(path);
		int k=0;
		boolean isContainSource = false;
	    for(int j=0; j < paths.size(); j++) {
            LatLng a = paths.get(j);
			 k = j + 1;
            if (k >= paths.size()) {
                k = 0;
            }
            LatLng b = paths.get(k);
            if (!isContainSource && Utils.pathContains(source, a, b)){
            	isContainSource = true;
            	break;
            }
	    }
		return isContainSource;
	}
	
	/**
	 * Function to decode encoded polyString received from Google Map api.
	 * 
	 * @param encoded
	 *            : The overview polygon of the route.
	 * @return List of latitude/longitude.
	 */
	public static List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		/*
		 * for(int i=0;i<poly.size();i++){
		 * System.out.println("Point sent: Latitude: "
		 * +poly.get(i).getLat()+" Longitude: "+poly.get(i).getLng()); }
		 */
		return poly;
	}

	public static double distanceInKm(double lat1, double lng1, double lat2,
			double lng2) {
		double theta = lng1 - lng2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		// distance is in miles to convert distance in kilo meter, multiply by
		// 1.609344.
		dist = dist * 1.609344;
		return (dist);
	}

	/**
	 * This function converts decimal degrees to radians.
	 * 
	 * @param deg
	 * @return double
	 */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/**
	 * This function converts radians to decimal degrees
	 * 
	 * @param rad
	 * @return
	 */
	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}
	
	/**
	 * Get a random siz character password.
	 * 
	 * @return	the six character password
	 */
	public static String generateRandomPassword() {
		char[] chars = "abcdefghijklmnopqrstuvwxyzABSDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
		Random r = new Random(System.currentTimeMillis());
		int pwdLength = 6;
		char[] id = new char[pwdLength];
		for (int i = 0;  i < pwdLength;  i++) {
		    id[i] = chars[r.nextInt(chars.length)];
		}
		return new String(id);
	}
	
	public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
	}

	public static String getEmailDomain(String email) {
		String domainName = email.substring(email.indexOf('@') + 1);
		return domainName;
	}

	public static Attachment getAttachmentFromMultipart(MultipartFile docMultipartFile)
			throws IOException {
		Attachment attachment = null;
		File docFile = new File(docMultipartFile.getOriginalFilename());
		docMultipartFile.transferTo(docFile);
		String extension = docFile.getName().substring(docFile.getName().lastIndexOf("."));
		if(extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".png")){
			 attachment = new Attachment();
			 attachment.setFileName(docMultipartFile.getOriginalFilename());
			 attachment.setContent(FileUtils.readFileToByteArray(docFile));
		}
		docFile.delete();
		return attachment;
	}

	
	public static Date getDateFromString(String date) throws ParseException{
		DateFormat formatter = new SimpleDateFormat("d-MMM-yyyy,HH:mm:ss");
		return formatter.parse(date);
	}
	
	/**
	 * Check weather given point exists between point a and point b. 
	 * @param point The searchable value.
	 * @param a The start point.
	 * @param b The 
	 * @return true/false
	 */
	public static boolean pathContains(LatLng point, LatLng a, LatLng b) {

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


	public static boolean validateLogin(final String username) {
		Pattern pattern = Pattern.compile(VALID_USERNAME_PATTERN_REGEX);
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}

	
	public static boolean validatePhone(String phoneNo) {
		Pattern pattern = Pattern.compile("\\d{10}");
	    Matcher matcher = pattern.matcher(phoneNo);
		return matcher.matches();
	}
	
	public static boolean isNull(String value){
		return (value==null || StringUtils.isEmpty(value.trim()))? true : false;
	}
}

