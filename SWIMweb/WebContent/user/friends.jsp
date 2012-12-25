<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../css/style.css" />
<title>SWIM - Amici</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="friends" />
	</jsp:include>

	<div id="pageContent">
		<div id="friends">
			<p>
			<span class="text">These are your friends:</span></br>
			<%
				@SuppressWarnings("unchecked")
				List<User> friends = (List<User>) request.getAttribute("friends");
				if (friends == null) {
					out.println("<span class=\"warning\">you don't have any friends yet!</span>");
				}else{
					out.println("<ul>");
					for(User user : friends){
						out.println("<li>" + user.getName() + " " + user.getSurname() + 
								" <a href=\"profile?username=" +
								user.getUsername() + "&from=friends\">Visualizza Profilo</a></li>");
					}
					out.println("</ul>");
				}
			%>
			</p>
		</div>
		<div id="request">
			<p>
			<span class="text">These are your friendship request:</span></br>
			<%
				@SuppressWarnings("unchecked")
				List<Friendship> requests = (List<Friendship>) request.getAttribute("requests");
				if (requests == null) {
					out.println("<span class=\"warning\">you don't have any requests </span>");
				}else{
					out.println("<ul>");
					for(Friendship friendship : requests){
						out.println("<li>" + friendship.getFriend().getName() +  " " +
									friendship.getFriend().getSurname() +  
									" <a href=\"friends?friendship=" +
									friendship.getId() + 
									"&state=accept\">Accept</a> <a href=\"friends?friendship=" +
									friendship.getId() + "&state=reject\">Reject</a></li>");
					}
					out.println("</ul>");
				}
			%>
			</p>
		</div>
		<div id="pending">
			<p>
			<span class="text">These are your pending request:</span></br>
			<%
				@SuppressWarnings("unchecked")
				List<Friendship> pendings = (List<Friendship>) request.getAttribute("pendings");
				if (pendings == null) {
					out.println("<span class=\"warning\">you don't have any pending requests </span>");
				}else{
					out.println("<ul>");
					for(Friendship friendship : pendings){
						out.println("<li>" + friendship.getFriend().getName() + " " +
									friendship.getFriend().getSurname() + 
									" <a href=\"profile?username=" +
									friendship.getFriend().getUsername() + "&from=friends\">Visualizza Profilo</a></li>");
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