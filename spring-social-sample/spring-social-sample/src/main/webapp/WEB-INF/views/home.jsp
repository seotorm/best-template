<%@ page session="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Home</title>
	</head>
	<body>
	<c:if test="${not empty userId }">Welcome ${userId }. <a href="<c:url value="/logout" />">Logout</a></c:if>
	<c:if test="${empty userId }">
		<form name="login" action="<c:url value="/login" />">
			<a href="javascript:document.login.submit()">Login</a>
			(User ID: <input type="text" name="userId" size="10" value="" />)
		</form>
	</c:if>
	<ul>
		<li><a href="<c:url value="/signout" />">Disconnect</a></li>
		<li>
		<form name="facebook" action="<c:url value="/signin/facebook" />" method="POST">
			<a href="javascript:document.facebook.submit()">Connect to Facebook</a>
		    <input type="hidden" name="scope" value="email,publish_stream,offline_access" />
		</form>
		</li>
	</ul>
	
	<h3>Status: ${message}</h3>
	
	<c:if test="${not empty friends }">
	<h3>Your Facebook Friends</h3>
	<ul>
	<c:forEach items="${friends}" var="friend">
		<li><img src="http://graph.facebook.com/<c:out value="${friend.id}"/>/picture" align="middle"/><c:out value="${friend.name}"/></li>
	</c:forEach>
	</ul>
	</c:if>
	
	</body>
</html>