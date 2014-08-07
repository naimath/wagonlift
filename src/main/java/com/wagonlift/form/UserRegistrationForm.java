package com.wagonlift.form;


public class UserRegistrationForm {
		private String login;
        private String firstname;    
        private String lastname;
        private String password;       
        private String confirmPassword;       
        private String email;
        private String phoneno;
        
       
        public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public String getPhoneno() {
			return phoneno;
		}

		public void setPhoneno(String phoneno) {
			this.phoneno = phoneno;
		}

		public String getLastname() {
     			return lastname;
     	}

     	public void setLastname(String lastname) {
     			this.lastname = lastname;
     	}
     	
        public void setFirstname(String firstname) {
                this.firstname = firstname;
        }

        public String getFirstname() {
                return firstname;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public String getPassword() {
                return password;
        }

        public void setConfirmPassword(String confirmPassword) {
                this.confirmPassword = confirmPassword;
        }

        public String getConfirmPassword() {
                return confirmPassword;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getEmail() {
                return email;
        }

}