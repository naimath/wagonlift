<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<body>
	<form:form method="Post" action="user/register.do" commandName="registrationForm">
		<table>
			<tr>
				<td>Login:<FONT color="red"><form:errors path="login" /></FONT></td>
			</tr>
			<tr>
				<td><form:input path="login" /></td>
			</tr>
			<tr>
				<td>First Name:<FONT color="red"><form:errors path="firstname" /></FONT></td>
			</tr>
			<tr>
				<td><form:input path="firstname" /></td>
			</tr>
			<tr>
				<td>Last Name:<FONT color="red"><form:errors path="lastname" /></FONT></td>
			</tr>
			<tr>
				<td><form:input path="lastname" /></td>
			</tr>
			<tr>
				<td>Password:<FONT color="red"><form:errors path ="password" /></FONT></td>
			</tr>

			<tr>
				<td><form:password path="password" /></td>
			</tr>

			<tr>
				<td>Confirm Password:<FONT color="red"><form:errors path ="confirmPassword" /></FONT></td>
			</tr>

			<tr>
				<td><form:password path="confirmPassword" /></td>
			</tr>

			<tr>
				<td>Email:<FONT color="red"><form:errors path="email" /></FONT></td>
			</tr>

			<tr>
				<td><form:input path="email" /></td>
			</tr>
			
			<tr>
				<td>Phone:<FONT color="red"><form:errors path="phoneno" /></FONT></td>
			</tr>

			<tr>
				<td><form:input path="phoneno" /></td>
			</tr>

			<tr>
				<td><input type="submit" value="Submit" /></td>
			</tr>

		</table>
	</form:form>
</body>
</html>