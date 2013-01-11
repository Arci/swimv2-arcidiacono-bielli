<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="/SWIMweb/css/style.css" />
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
				<form id="searchForm" action="search" method="post">
					<%
					User sessionUser = (User) session.getAttribute("User");
					if (sessionUser != null) { %>
						<input type="radio" name="searchType" value="person" checked="checked" /> Person 
						<input type="radio" name="searchType" value="ability" />Abilities<br />
					<% } else { %>
						<input type="hidden" name="searchType" value="person" />
					<% }
					if(request.getAttribute("keywords") != null){
						String keywords = (String) request.getAttribute("keywords");
						out.println("<input type=\"text\" name=\"keywords\" value="
							+ keywords + ">");
					}else{
						out.println("<input type=\"text\" name=\"keywords\" />");
					} %>
					<br />
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
				<span class="message">
				<%
				 	if (request.getAttribute("message") != null
				 			&& request.getAttribute("message") != "") {
				 		out.println(request.getAttribute("message"));
				 	}
				%> 
				</span>
			</fieldset>
		</div>

		<div id="resultsContainer">
			<div class="resultList">
				<%
					if (request.getAttribute("friendsResults") != null) {
						@SuppressWarnings("unchecked")
						Set<User> firendsResults = (Set<User>) request
								.getAttribute("friendsResults");
						if (!firendsResults.isEmpty()) {
							out.println("<br/><div id=\"firendsResults\">");
							out.println("<span class=\"text\">Results among friends:</span><br/>");
							out.println("<ul>");
							for (User user : firendsResults) {
								out.println("<li>" + user.getName() + " "
										+ user.getSurname()
										+ " <a href=\"user/profile?username="
										+ user.getUsername()
										+ "&from=search\">Show Profile</a></li>");
							}
							out.println("</ul></div>");
						}
					}
					if (request.getAttribute("otherResults") != null) {
						@SuppressWarnings("unchecked")
						Set<User> otherResults = (Set<User>) request
								.getAttribute("otherResults");
						if (!otherResults.isEmpty()) {
							if (sessionUser != null) {
								out.println("<hr>");
							}else{
								out.println("<br/>");
							}
							out.println("<div id=\"otherResults\">");
							out.println("<span class=\"text\">Results in SWIM:</span><br/>");
							out.println("<ul>");
							for (User user : otherResults) {
								if (sessionUser != null) {
									out.println("<li>" + user.getName() + " "
											+ user.getSurname()
											+ " <a href=\"user/profile?username="
											+ user.getUsername()
											+ "&from=search\">Show Profile</a></li>");
								} else {
									out.println("<li>" + user.getName() + " "
											+ user.getSurname()
											+ " <a href=\"registration.jsp\">Register to see his profile</a></li>");
								}

							}
							out.println("</ul></div>");
						}
					}
				%>
			</div>
		</div>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>