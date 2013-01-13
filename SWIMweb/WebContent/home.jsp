<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<link rel="stylesheet" type="text/css" href="css/style.css" >
<title>SWIM - Homepage</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="search" />
	</jsp:include>

	<div id="pageContent">
		<%
			//inserimento fake users
			try {
				InitialContext jndiContext = new InitialContext();
				Object ref = jndiContext.lookup("InitializationManager/remote");
				InitializationManagerRemote initializationManager = (InitializationManagerRemote) ref;
				initializationManager.addFakeUsers();
				initializationManager.addFakeAbilities();
				initializationManager.addAbilitiesToUsers();
				//initializationManager.addFakeHelpRequest();
				initializationManager.addFakeFriendship();
			} catch (NamingException e) {
				e.printStackTrace();
			}
			getServletConfig().getServletContext()
					.getRequestDispatcher("/search.jsp")
					.forward(request, response);
		%>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>