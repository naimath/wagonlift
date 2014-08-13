package com.wagonlift.form;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import com.wagonlift.annotation.JsonTransient;
import com.wagonlift.models.Attachment;
import com.wagonlift.models.User;
import com.wagonlift.models.UserVehicle;


public class UserRegistrationForm {
	
	private String id;
	private String firstName;
	private String lastName;
	private String phoneNo;
	private String aboutMe;
	private boolean isActivated;
	private boolean isRegisterd;
	private String activationId;
	private boolean isAdmin;
	private String email;
	private String login;
	private String password;
	private String address;
	private Date created;
	private Attachment pictureFile;
	private Attachment documentFile;
	private boolean isEmailCorporate;
    private String confirmPassword;  
    private String aboutme;
	private Vehicle vehicle;
    @JsonTransient
    private MultipartFile profilepic;
    
    
    public String getAboutme() {
		return aboutme;
	}

	public void setAboutme(String aboutme) {
		this.aboutme = aboutme;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}


      
        
       
    public String getLogin() {
			return login;
		}

	public void setLogin(String login) {
			this.login = login;
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

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getPhoneNo() {
			return phoneNo;
		}

		public void setPhoneNo(String phoneNo) {
			this.phoneNo = phoneNo;
		}

		public String getAboutMe() {
			return aboutMe;
		}

		public void setAboutMe(String aboutMe) {
			this.aboutMe = aboutMe;
		}

		public boolean isActivated() {
			return isActivated;
		}

		public void setActivated(boolean isActivated) {
			this.isActivated = isActivated;
		}

		public boolean isRegisterd() {
			return isRegisterd;
		}

		public void setRegisterd(boolean isRegisterd) {
			this.isRegisterd = isRegisterd;
		}

		public String getActivationId() {
			return activationId;
		}

		public void setActivationId(String activationId) {
			this.activationId = activationId;
		}

		public boolean isAdmin() {
			return isAdmin;
		}

		public void setAdmin(boolean isAdmin) {
			this.isAdmin = isAdmin;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public Date getCreated() {
			return created;
		}

		public void setCreated(Date created) {
			this.created = created;
		}

		public Attachment getPictureFile() {
			return pictureFile;
		}

		public void setPictureFile(Attachment pictureFile) {
			this.pictureFile = pictureFile;
		}

		public Attachment getDocumentFile() {
			return documentFile;
		}

		public void setDocumentFile(Attachment documentFile) {
			this.documentFile = documentFile;
		}

		public boolean isEmailCorporate() {
			return isEmailCorporate;
		}

		public void setEmailCorporate(boolean isEmailCorporate) {
			this.isEmailCorporate = isEmailCorporate;
		}

		public void populate(User user,	UserVehicle userVehicle) {
			if(user!=null){
				setId(user.getId());
				setLogin(user.getLogin());
				setFirstName(user.getFirstName());
				setLastName(user.getLastName());
				setEmail(user.getEmail());
				setPassword(user.getPassword());
				setPhoneNo(user.getPhoneNo());
			}
			if(userVehicle !=null){
				vehicle.setNumber(userVehicle.getNumber());
				vehicle.setPictureFile(userVehicle.getPictureFile());
				vehicle.setRideType(userVehicle.getRideType());
			}
			
			
		}

		public MultipartFile getProfilepic() {
			return profilepic;
		}

		public void setProfilepic(MultipartFile profilepic) {
			this.profilepic = profilepic;
		}

}