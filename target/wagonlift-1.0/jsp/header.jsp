<div id="header-wrapper">
		<div id="header" class="container">
			<div  class="nav navbar-nav navbar-right"><a href="#" onclick="openSingIn()" id="signInId">Sign In </a>|<a href="#" onclick="openSingUp()"> Sign Up </a>|<a href="#" onclick="verifyOtp()"> Verify OTP </a></div>
			<div id="logo">				
				<h1><a href="#">Wagon Lift</a></h1>				
			</div>
		</div>
</div>

<div id="dialog-signin" title="Sign In">
<div id="loginContent">
	<div id="loginFormErrorMessage"></div>
	<!-- Social Buttons -->
	<div class="social">	Sign in using social network:<br/>
	<div class="twitter"><a href="#" class="btn_1">Login with Twitter</a></div>
	<div class="fb"><a href="#" class="btn_2">Login with Facebook</a></div>
	</div>
	
	<form name='loginForm' id="loginForm"	action="user/getlogin.do" method='POST'>
		<!-- Login Fields -->
		
		<div id="login">Sign in using your registered account:<br/>
		<input type="text" name='username' value="Username" onblur="if(this.value=='')this.value='Username';" onfocus="if(this.value=='Username')this.value='';"  class="login user"/>
		<input type='text' name='userPassword' value='Password'  onfocus="if(this.value=='' || this.value == 'Password') {this.value='';this.type='password'}"  onblur="if(this.value == '') {this.type='text';this.value=this.defaultValue}" class="login password"/>
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

<div id="dialog-signup" title="Sign Up">
	<form method="Post" action="user/register.do" id="signUpForm">
		<table>
			<tr>
				<td><div id="signUpFormErrorMessage"></div></td>
			</tr>
			<tr>
				<td>Login:<FONT color="red"><form:errors path="login" /></FONT></td>
			</tr>
			<tr>
				<td><input name="login" type="text" /></td>
			</tr>
			<tr>
				<td>Password:<FONT color="red"><form:errors path ="password" /></FONT></td>
			</tr>
			<tr>
				<td><input name="password" type="text"/></td>
			</tr>

			<tr>
				<td>Confirm Password:<FONT color="red"><form:errors path ="confirmPassword" /></FONT></td>
			</tr>

			<tr>
				<td><input name="confirmPassword" type="text"/></td>
			</tr>
			<tr>
				<td>Phone:<FONT color="red"><form:errors path="phoneno" /></FONT></td>
			</tr>

			<tr>
				<td><input name="phoneno" type="text"/></td>
			</tr>

			<tr>
				<td><input type="submit" value="Submit" /></td>
			</tr>

		</table>
	</form>
</div>

<div id="dialog-otp" title="OTP">
	
	<form method="Post" action="user/verifyotp.do" id="otpForm">
		<table>
			<tr>
				<td>Please enter miss call number in otp box</td>
			</tr>
			<tr>
				<td>OTP :<input type="text" name="otp"/></td>
			</tr>
			<tr>
				<td><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form>

</div>