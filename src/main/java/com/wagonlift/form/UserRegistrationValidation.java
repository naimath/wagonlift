package com.wagonlift.form;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;


@Component("registrationValidator")
public class UserRegistrationValidation {
  public boolean supports(Class<?> klass) {
    return UserRegistrationForm.class.isAssignableFrom(klass);
  }

  public void validate(Object target, Errors errors) {
	  UserRegistrationForm registration = (UserRegistrationForm) target;
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstname",
        "NotEmpty.registration.userName",
        "User Name must not be Empty.");
    String userName = registration.getFirstname();
    if ((userName.length()) > 50) {
      errors.rejectValue("userName",
          "lengthOfUser.registration.userName",
          "User Name must not more than 50 characters.");
    }
    if (!(registration.getPassword()).equals(registration
        .getConfirmPassword())) {
      errors.rejectValue("password",
          "matchingPassword.registration.password",
          "Password and Confirm Password Not match.");
    }
  }
}