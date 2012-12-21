<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<title>SWIM - SignIn</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="registration" />
	</jsp:include>

	<div id="pageContent">
		<div id="registration">
			<form id='register' action='' method='post' accept-charset='UTF-8'>
				<label for='name'>Name*: </label>
				<input type='text'name='name' id='name'/></br>
				<label for='name'>Surname: </label>
				<input type='text'name='surname' id='surname'/></br> 
				<label for='name'>City: </label> 
				<input type='text'name='city' id='city'/></br> 
				<label for='name'>Phone: </label> 
				<input type='text'name='phone' id='phone'/></br> 
				<label for='email'>Email Address*:</label> 
				<input type='text' name='email' id='email' /></br> 
				<label for='username'>UserName*:</label> 
				<input type='text' name='username' id='username'/></br> 
				<label for='password'>Password*:</label> 
				<input type='password' name='password' id='password' /></br> 
				<label for='password'>Check Password*:</label> 
				<input type='password' name='checkPassword' id='checkPassword' /></br> 
				* are obligatory</br>
				<input type='submit' name='Submit' value='Submit' />
			</form>
		</div>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>