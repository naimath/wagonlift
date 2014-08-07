package com.wagonlift.utils;

import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MailUtils {
	
	/**
	 * Sets the mail header.
	 * @param mailGreeting
	 * @param email
	 * @return
	 */
	private final Logger logger = Logger.getLogger(MailUtils.class);
	public String setMailHeader(String mailGreeting, String email) {
		return mailGreeting+" "+email.split("@")[0]+",";
	}
	
	/**
	 * Sets the salutation.
	 * @param salutationWord
	 * @param salutationName
	 * @return
	 */
	public String setMailSalutation(String salutationWord, String salutationName) {
		return salutationWord+"\n"+salutationName;
	}
	
	/**
	 * Sets spacing and line breaks in message.
	 * @param header
	 * @param content
	 * @param salutation
	 * @return
	 */
	public String setEntireMessage(String header, String content, String salutation) {
		return header+"\n\n\n\t"+content+"\n\n\n"+salutation;
	}
	
	/**
	 * Prepare and Send email.
	 * @param mailSender	the mailsender object used to send email
	 * @param from			the from email address
	 * @param to			the to email address
	 * @param subject		the email subject
	 * @param message		the email message
	 * @return				true / false
	 */
	public boolean prepareAndSendMail(MailSender mailSender, String from, String to, String subject, String message) {
		try {
			SimpleMailMessage simpleMail = prepareSimpleMailObject(from, to, subject, message, null);
			if(simpleMail==null) {
				return false;
			}
			
			return sendMail(mailSender, simpleMail);
			
		} catch (Exception e) {
			logger.debug(getTimestamp()+" Some exception while sending email to: "+to+"\nexception message:\n"+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Prepare and Send email.
	 * @param mailSender	the mailsender object used to send email
	 * @param from			the from email address
	 * @param to			the to email address
	 * @param subject		the email subject
	 * @param message		the email message
	 * @return				true / false
	 */
	public boolean prepareAndSendMail(MailSender mailSender, String from, String to, String subject, String message, String bcc) {
		try {
			SimpleMailMessage simpleMail = prepareSimpleMailObject(from, to, subject, message, bcc);
			if(simpleMail==null) {
				return false;
			}
			
			return sendMail(mailSender, simpleMail);
			
		} catch (Exception e) {
			logger.debug(getTimestamp()+" Some exception while sending email to: "+to+"\nexception message:\n"+e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Prepares the SimpleMailMessage object to be used for sending email.
	 * @param from
	 * @param to
	 * @param subject
	 * @param message
	 * @return			the SimpleMailMessage obect or null
	 */
	private SimpleMailMessage prepareSimpleMailObject(String from, String to, String subject, String message, String bcc) {
		SimpleMailMessage simpleMail = null;
		try {
			simpleMail = new SimpleMailMessage();
			simpleMail.setFrom(from);
			simpleMail.setTo(to);
			simpleMail.setSubject(subject);
			simpleMail.setText(message);
			if(bcc!=null){
				simpleMail.setBcc(bcc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return simpleMail;
	}
	
	/**
	 * Send Email.
	 * 
	 * @param mailSender
	 * @param simpleMail
	 * @return				true / false
	 */
	private boolean sendMail(MailSender mailSender, SimpleMailMessage simpleMail) {
		try {
			logger.debug(getTimestamp()+ " Sending mail now");
			mailSender.send(simpleMail);
			logger.debug(getTimestamp()+ " Mail sent to " + Arrays.asList(simpleMail.getTo()));
			return true;
		} catch (MailAuthenticationException mae) {
			logger.debug(getTimestamp()+ " Authentication Failure using email => " + simpleMail.getFrom());
			logger.debug(mae.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Get current timestamp.
	 * @return		timestamp string
	 */
	@SuppressWarnings("deprecation")
	private String getTimestamp() {
		Date date = new Date();
		String timestamp = "["+date.getDate()+", "+date.getMonth()+", "+((int)date.getYear()+(int)1900)+"] "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
		return timestamp;
	}
	
}
