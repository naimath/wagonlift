<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<body>
	<h3>Welcome Registration Successfully.</h3>
	<table>
		<tr>
			<td>User Name :</td>
			<td><core:out value="${registrationForm.userName}" /></td>
		</tr>
		<tr>
			<td>Password :</td>
			<td><core:out value="${registrationForm.password}" /></td>
		</tr>
	</table>
</body>

</html>