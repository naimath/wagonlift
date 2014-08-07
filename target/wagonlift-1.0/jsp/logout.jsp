<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Logout</title>
</head>
<body>

	< % session.invalidate(); %> You are now logged out!!

	<a href="${pageContext.request.contextPath}/login">go back</a>


</body>
</html>