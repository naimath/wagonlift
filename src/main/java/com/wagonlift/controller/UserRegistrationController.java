package com.wagonlift.controller;

import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wagonlift.form.UserRegistrationForm;
import com.wagonlift.form.UserRegistrationValidation;

@Controller
public class UserRegistrationController {
        @Autowired
        private UserRegistrationValidation registrationValidation;

        public void setRegistrationValidation(
        		UserRegistrationValidation registrationValidation) {
                this.registrationValidation = registrationValidation;
        }

        // Display the form on the get request
        @RequestMapping(value = "/userRegistration.do", method = RequestMethod.GET)
        public String showRegistration(Map model) {
        		UserRegistrationForm registrationForm = new UserRegistrationForm();
                model.put("registrationForm", registrationForm);
                return "userRegistration";
        }

        
        // Process the form.
       
        @RequestMapping(value = "/userRegistration.do", method = RequestMethod.POST)
        public String processRegistration( @ModelAttribute("registrationForm") UserRegistrationForm registrationForm,    BindingResult result,Model model) {
                // set custom Validation by user
                registrationValidation.validate(registrationForm, result);
                if (result.hasErrors()) {
                        return "userRegistration";
                }
                JSONObject jsonObject = (JSONObject) JSONSerializer.toJSON(registrationForm);
                model.addAttribute("details", jsonObject.toString());
                return "redirect:/user/register.do";
        }
}