
 <!-- 
 By default, spring auto generates and configures a UsernamePasswordAuthenticationFilter bean. 
 This filter, by default, responds to the URL /j_spring_security_check when processing a login POST from your web-form. 
 For username field it uses 'j_username' and for password field it uses 'j_password'.
  -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>



<style>
.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

#login-box {
	width: 300px;
	padding: 20px;
	margin: 100px auto;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
}
</style>

<!-- Box Start-->
<div id="box_bg">

<div id="loginContent">
	<h1>Sign In</h1>
	
	<!-- Social Buttons -->
	<div class="social">	Sign in using social network:<br/>
	<div class="twitter"><a href="#" class="btn_1">Login with Twitter</a></div>
	<div class="fb"><a href="#" class="btn_2">Login with Facebook</a></div>
	</div>
	
	<form name='loginForm' 	action="<c:url value='/j_spring_security_check' />" method='POST'>
		<!-- Login Fields -->
		<div id="login">Sign in using your registered account:<br/>
		<input type="text" name='j_username' value="Username" onblur="if(this.value=='')this.value='Username';" onfocus="if(this.value=='Username')this.value='';"  class="login user"/>
		<input type='text' name='j_password' value='Password'  onfocus="if(this.value=='' || this.value == 'Password') {this.value='';this.type='password'}"  onblur="if(this.value == '') {this.type='text';this.value=this.defaultValue}" class="login password"/>
		</div>
		
		<!-- Green Button -->
		<input  class="button green" name="submit" type="submit" 	value="Sign In" />	
	</form>
	<!-- Checkbox -->
	<div class="checkbox">
	 <li>
		<fieldset>
		<![if !IE | (gte IE 8)]><legend id="title2" class="desc"></legend><![endif]>
		<!--[if lt IE 8]><label id="title2" class="desc"></label><![endif]-->
		<div>
			<span>
			<input id="Field" name="Field" type="checkbox" class="field checkbox" value="First Choice" tabindex="4" onchange="handleInput(this);" />
			<label class="choice" for="Field">Keep me signed in</label>
			</span>
		</div>
		</fieldset>
	 </li>
	</div>

</div>
</div>
<!-- Text Under Box -->
<div id="bottom_text">
	Don't have an account? <a href="${pageContext.request.contextPath}/userRegistration.do">Sign Up</a><br/>
	Remind <a href="#">Password</a>
</div>
