package com.wagonlift.controller;

import java.io.IOException;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.wagonlift.services.UserService;

@Controller
public class HomeController {
	
	@Autowired private UserService userService;	
	private static final Logger logger = Logger.getLogger(HomeController.class);	
	
	@RequestMapping(value = "/login.do", method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView model = new ModelAndView();
		model.setViewName("login");
		return model;
	}

	@RequestMapping(value = "/home.do", method = RequestMethod.GET)
	public ModelAndView loginSuccess() {
		ModelAndView model = new ModelAndView();
		model.setViewName("home");
		return model;
	}
	
	@RequestMapping(value = "/about.do", method = RequestMethod.GET)
	public ModelAndView about() {
		ModelAndView model = new ModelAndView();
		model.setViewName("about");
		return model;
	}
	
	@RequestMapping(value = "/blog.do", method = RequestMethod.GET)
	public ModelAndView blog() {
		ModelAndView model = new ModelAndView();
		model.setViewName("blog");
		return model;
	}
	
	@RequestMapping(value = "/testimonials.do", method = RequestMethod.GET)
	public ModelAndView testimonials() {
		ModelAndView model = new ModelAndView();
		model.setViewName("testimonials");
		return model;
	}
	
	@RequestMapping(value = "/contact.do", method = RequestMethod.GET)
	public ModelAndView contact() {
		ModelAndView model = new ModelAndView();
		model.setViewName("contact");
		return model;
	}
	
	
}
