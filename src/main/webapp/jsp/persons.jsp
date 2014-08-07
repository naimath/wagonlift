<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Persons</title>
</head>
<body>
<h1>Persons</h1>

<p>Message: <c:out value="${message}" /></p>

<h3>People</h3>
<ol>
	<c:forEach items="${persons}" var="person">
		<li><c:out value="${person.name}" /> is present at <c:out value="${person.location}" /></li>
	</c:forEach>
</ol>
</body>
</html>