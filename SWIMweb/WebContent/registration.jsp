<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<link rel="stylesheet" type="text/css" href="css/style.css" >
<script type="text/javascript" src="js/mail.js"></script>
<title>SWIM - SignIn</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="registration" />
	</jsp:include>
	
	<div id="pageContent">
		<% 
		boolean hasMissingParameters = false;
		boolean hasDuplicatedParameters = false;
		boolean hasPasswordError = false;
		Set <String> missingParameters = null;
		Set <String> duplicatedParameters = null;
		
		if(request.getAttribute("missingParameters") != null){ 
			hasMissingParameters = true;
			@SuppressWarnings("unchecked")
			Set <String> tmp = (Set <String>) request.getAttribute("missingParameters");
			missingParameters = tmp;
		}
		
		if(request.getAttribute("duplicatedParameters") != null){
			hasDuplicatedParameters = true;
			@SuppressWarnings("unchecked")
			Set <String> tmp = (Set <String>) request.getAttribute("duplicatedParameters");
			duplicatedParameters = tmp;
		}
		
		if(request.getAttribute("passwordError") != null){
			hasPasswordError = true;
		}
		%>
		<div id="registration">
			<form id='register' action='registrationManager' method='post' accept-charset='UTF-8'>
			
				<label for='name'>Name*: </label><br/>
				<input type='text'name='name' id='name'
				<%if(hasMissingParameters || hasDuplicatedParameters || hasPasswordError){%>
				value='<%=request.getParameter("name")%>'<%}%>/>
				<%if(hasMissingParameters && missingParameters.contains("name")){%>
				<span class='error'>&#9;*you must insert the name.</span><%}%>
				<br>
				
				<label for='surname'>Surname: </label><br/>
				<input type='text'name='surname' id='surname'
				<%if(hasMissingParameters || hasDuplicatedParameters || hasPasswordError){%>
				value='<%=request.getParameter("surname")%>'<%}%>/><br>
				 
				<div id="emailContainer">
					<label for='email'>Email Address*:</label><br/>
					<input type='text' id="email" name='email' onkeypress="checkMail();" onchange="checkMail();"
					<%if(hasMissingParameters || hasDuplicatedParameters || hasPasswordError){%>
					value='<%=request.getParameter("email")%>'<%}%> /> 
					<%if(hasMissingParameters && missingParameters.contains("email")){%>
					<span class='error'>&#9;*you must insert the email.</span><%}%>
					<%if(hasDuplicatedParameters && duplicatedParameters.contains("email")){ %>
					<span class='error'>&#9;*this email is already used.</span><%} %>
				</div>
				
				<label for='username'>Username*:</label><br/>
				<input type='text' name='username' id='username'
				<%if(hasMissingParameters || hasDuplicatedParameters || hasPasswordError){%>
				value='<%=request.getParameter("username")%>'<%}%>/>
				<%if(hasMissingParameters && missingParameters.contains("username")){%>
				<span class='error'>&#9;*you must insert the username.</span><%}%>
				<%if(hasDuplicatedParameters && duplicatedParameters.contains("username")){ %>
				<span class='error'>&#9;*this username is already used.</span><%} %>
				<br>
				
				<label for='password'>Password*:</label><br/>
				<input type='password' name='password' id='password'/>
				<%if(hasMissingParameters && missingParameters.contains("password")){%>
				<span class='error'>&#9;*you must insert the password.</span><%}%>
				<%if(hasPasswordError){ %>
				<span class='error'>&#9;*password and check password are not equal.</span><%}%>
				<br>
				
				<label for='checkPassword'>Check Password*:</label><br/>
				<input type='password' name='checkPassword' id='checkPassword'/> 
				<%if(hasMissingParameters && missingParameters.contains("checkPassword")){%>
				<span class='error'>&#9;*you must insert the check password.</span><%}%>
				<br>
				* required<br>
				<input type='submit' name='Submit' value='Submit' />
			</form>
			<%
			if(hasMissingParameters)
				request.removeAttribute("missingParameters");
			
			if(hasDuplicatedParameters)
				request.removeAttribute("duplicatedParameters");
			
			if(hasPasswordError)
				request.removeAttribute("passwordError");
				%>
		</div>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>