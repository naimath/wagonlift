<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="http://fonts.googleapis.com/css?family=Chivo:400,900" rel="stylesheet" />
<link href="/wagonlift-1.0/css/homeDefault.css" rel="stylesheet"	type="text/css" media="all" />
<link href="/wagonlift-1.0/css/homeFonts.css" rel="stylesheet"	type="text/css" media="all" />
<link rel="stylesheet" href="/wagonlift-1.0/css/bootstrap.css">
<link rel="stylesheet" href="/wagonlift-1.0/css/bootstrap-theme.css">
<link rel="stylesheet" href="/wagonlift-1.0/css/jquery-ui-1.10.4.css">
<script src="/wagonlift-1.0/js/jquery-1.11.1.js"></script>
<script src="/wagonlift-1.0/js/jquery-ui-1.10.4.js"></script>
<script src="/wagonlift-1.0/js/jquery.validate.min.js"></script>
<script src="/wagonlift-1.0/js/bootstrap.js"></script>


<link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css' />
<link href="/wagonlift-1.0/css/login.css" rel="stylesheet" type="text/css" />

<title><tiles:insertAttribute name="title" ignore="true" /></title>
<script>
	var loginVal;
	var sidValue;
	$(function() {
		$( "#dialog-signin" ).dialog({autoOpen : false,	height:400,	width:430,    modal: true});
		$( "#dialog-signup" ).dialog({autoOpen : false,	height:300,	width:300,    modal: true});
		$( "#dialog-otp" ).dialog({autoOpen : false,	height:200,	width:200,    modal: true});	
		
		
		$( "#loginForm" ).submit(function( event ) { 
			  event.preventDefault();
			  var $form = $( this );
			  loginVal = $form.find( "input[name='username']" ).val();
			  var  userPasswordVal = $form.find( "input[name='userPassword']" ).val();			  
			  var url = $form.attr( "action" );
			  var posting = $.post( url, { login: loginVal, password:userPasswordVal } );
			  posting.done(function( data ) {
				   if(data){
					   document.getElementById('signInId').innerHTML='Hello '+loginVal;	
					   $( "#dialog-signin" ).dialog("close");
						
				   }else{
					   document.getElementById('loginFormErrorMessage').innerHTML='Invalid username/password ...';
				   }
			 	  
			  });

			});
		
		$( "#signUpForm" ).submit(function( event ) { 
			  event.preventDefault();
			  var $form = $( this );
			  loginVal = $form.find( "input[name='login']" ).val();
			  var passwordVal = $form.find( "input[name='password']" ).val();
			  var phonenoVal = $form.find( "input[name='phoneno']" ).val();
			  var url = $form.attr( "action" );
			  var posting = $.post( url, { login: loginVal, password:passwordVal ,phoneno:phonenoVal} );
			  posting.done(function( data ) {
				  var result = jQuery.parseJSON( data);
				   console.debug(result.Status);
				   if('Success' == result.Status){
						sidValue=result.Result;
						$("#dialog-otp").dialog("open");
				   }else{
					   document.getElementById('signUpFormErrorMessage').innerHTML=result.Result;
				   }
			 	  
			  });

			});
		
		$( "#otpForm" ).submit(function( event ) { 
			  event.preventDefault();
			  var $form = $( this );
			  var otpVal = $form.find( "input[name='otp']" ).val();
			  var url = $form.attr( "action" );
			  var posting = $.post( url, { login: loginVal, sid:sidValue ,code:otpVal} );
			  posting.done(function( data ) {
					if(data){
						$( "#dialog-otp" ).dialog("close");
						$( "#dialog-signup" ).dialog("close");
					}
			  });

			});
		
	 });
	
	function openSingIn(){
		var val=document.getElementById('signInId').innerHTML;
		if(val.indexOf("Hello") == -1){
			$("#dialog-signin").dialog("open");
		}else{
			document.getElementById('signInId').href="user/profile.do?login="+loginVal;
			document.getElementById('signInId').click();
		}
		
		 
	}
	function openSingUp(){
		 $("#dialog-signup").dialog("open");
	}

	
</script>
</head>
<body>
		<div id="wrapper">
			<tiles:insertAttribute name="header" />
			<tiles:insertAttribute name="menu" />
			<div id="page" class="container">
					<tiles:insertAttribute name="body" />
			</div>
		</div>
    	
		<div id="copyright" class="container">
			<tiles:insertAttribute name="footer" />
		</div>
		
</body>
</html>

