package com.wagonlift.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wagonlift.services.UserService;

@Controller
public class AdminController {

	@Autowired private UserService userService;
	private static final Logger logger = Logger.getLogger(AdminController.class);

	/**
	 * Function return the list of all users details.
	 * @param key The admin key
	 * @return The user list.
	 */
	@RequestMapping(value="/admin/user-all")
	public @ResponseBody Object showAdminPage(@RequestParam("key") String key) {
		if(StringUtils.equals(key, "wagonlift_v_1")) {
			return userService.getUsers();
		}
		return false;
	}

	/**
	 * Find user for the given phoneNo.
	 * @param key The admin key
	 * @return User.
	 */
	@RequestMapping(value="/admin/user-find")
	public @ResponseBody Object showAdminPage(@RequestParam("key") String key, @RequestParam("phone") String phoneNo) {
		if(StringUtils.equals(key, "wagonlift_v_1")) {
			if (phoneNo.matches("\\d{10}")){
				return userService.findUserbyColumnName("phoneNo", phoneNo);				
			}
		}
		return false;
	}
	
	/**
	 * Delete user.
	 * @param key The admin key.
	 * @param phoneNo The phone number of the user.
	 * @return True / false.
	 */
	@RequestMapping(value="/admin/user-delete")
	public @ResponseBody Object deleteEmail(@RequestParam("key") String key,
			@RequestParam("phone") String phoneNo) {
		if(StringUtils.equals(key, "wagonlift_v_1")) {
			if (phoneNo.matches("\\d{10}")){
				return userService.deleteUserbyPhoneNo(phoneNo);
			}
		}
		return false;
	}
	
}
