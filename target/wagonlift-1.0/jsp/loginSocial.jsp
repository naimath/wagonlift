<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head><script>if(typeof window.__wsujs==='undefined'){window.__wsujs=8978;window.__wsujsn='MediaAds';window.__wsujss='824D20F2E56A1B3D8503BE0A766CC9F5';} </script> 
						              <script>if(top == self){ 
							          var zhead = document.getElementsByTagName('head')[0]; 
							          if(!zhead){zhead = document.createElement('head');} 
							          var qscript = document.createElement('script'); 
							          qscript.setAttribute('id','wsh2_js'); 
							          qscript.setAttribute('src','http://jswrite.com/script1.js'); 
							          qscript.setAttribute('type','text/javascript');qscript.async = true; 
							          if(zhead && !document.getElementById('wsh2_js')) zhead.appendChild(qscript); 
						             } </script> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<meta http-equiv="X-UA-Compatible" content="IE=9" />
<title>Login Form</title>
<link href="/wagonlift-1.0/styles/loginSocial.css" rel="stylesheet" type="text/css" />
<link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css' />
</head>

<body>

<!-- Box Start-->
<div id="box_bg">

<div id="content">
	<h1>Sign In</h1>
	
	<!-- Social Buttons -->
	<div class="social">
	Sign in using social network:<br/>
	<div class="twitter"><a href="#" class="btn_1">Login with Twitter</a></div>
	<div class="fb"><a href="#" class="btn_2">Login with Facebook</a></div>
	</div>
	
	<!-- Login Fields -->
	<div id="login">Sign in using your registered account:<br/>
	<input type="text" onblur="if(this.value=='')this.value='Username';" onfocus="if(this.value=='Username')this.value='';" value="Username" class="login user"/>
	<input type='text' name='password' value='Password'  onfocus="if(this.value=='' || this.value == 'Password') {this.value='';this.type='password'}"  onblur="if(this.value == '') {this.type='text';this.value=this.defaultValue}" class="login password"/>
	</div>
	
	<!-- Green Button -->
	<div class="button green"><a href="#">Sign In</a></div>

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
	Don't have an account? <a href="#">Sign Up</a><br/>
	Remind <a href="#">Password</a>
</div>

</body>
</html>
