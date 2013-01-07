<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../css/style.css" />
<title>SWIM - Ricerca</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="search" />
	</jsp:include>

	<div id="pageContent">
		<div id="searchFormContainer">
			<fieldset>
				<legend class="text">Search</legend>
				<form id="searchForm" action="search" method="post" >
					<input type="radio" name="toSearch" value="persons" checked="checked"/> Person
					<input type="radio" name="toSearch" value="abilities" />Abilities<br/>
					<%
						if(request.getParameter("keywords") != null){
							out.println("<input type=\"text\" name=\"keywords\" value=\""+
									request.getParameter("keywords") +"\"/>");
						}else{
							out.println("<input type=\"text\" name=\"keywords\" />");
						}
					%><br/>
					<input type="submit" name="submit" value="Search" />
				</form>
				<span class="error">
					<%
						if (request.getAttribute("error") != null
								&& request.getAttribute("error") != "") {
							out.println(request.getAttribute("error"));
						}
					%>
				</span>
			</fieldset>
		</div>
		<div id="resultsContainer">
			<p class="resultList">
				<span class="message">
					<%
						if (request.getAttribute("message") != null
								&& request.getAttribute("message") != "") {
							out.println(request.getAttribute("message"));
						}
					%>
				</span>
				<%
					if (request.getAttribute("results") != null) {
						@SuppressWarnings("unchecked")
						Set<User> results = (Set<User>) request.getAttribute("results");
						out.println("<ul>");
						for(User user : results){
							out.println("<li>" + user.getName() + " " + user.getSurname() + 
									" <a href=\"profile?username=" +
									user.getUsername() + "&from=search\">Visualizza Profilo</a></li>");
						}
						out.println("</ul>");
					}
				%>
			</p>
		</div>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>