<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="http://fonts.googleapis.com/css?family=Chivo:400,900" rel="stylesheet" />
<link href="/wagonlift-1.0/styles/homeDefault.css" rel="stylesheet"	type="text/css" media="all" />
<link href="/wagonlift-1.0/styles/homeFonts.css" rel="stylesheet"	type="text/css" media="all" />
<!--  link rel="stylesheet" href="/wagonlift-1.0/styles/bootstrap.css">
<script src="/wagonlift-1.0/js/bootstrap.js"></script>
<link rel="stylesheet" href="/wagonlift-1.0/styles/bootstrap-theme.css"-->
<link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700' rel='stylesheet' type='text/css' />
<link href="/wagonlift-1.0/styles/login.css" rel="stylesheet" type="text/css" />

<title><tiles:insertAttribute name="title" ignore="true" /></title>
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
