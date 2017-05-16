<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
    <head>
        <title>Spring Security Example</title>
    </head>
    <body>
       <h3>Spring Security Example</h3>
        <font color="red">
		   ${SPRING_SECURITY_LAST_EXCEPTION.message}
        </font>
	<form:form action="${pageContext.request.contextPath}/login" method="POST">
		Enter UserName:	<input type="text" name="username"/><br/><br/>
		Enter Password: <input type="password" name="password"/> <br/><br/>		
		<input type='checkbox' name="remember-me-param"/>Remember Me? <br/>	
		<input type="submit" value="Login"/>
	</form:form>
    <body>
</html>   