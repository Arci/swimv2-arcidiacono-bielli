<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<link rel="stylesheet" type="text/css" href="../css/style.css" >
<script type="text/javascript" src="../js/showHelp.js"></script>
<title>SWIM - Richieste di aiuto</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="helps" />
	</jsp:include>

	<div id="pageContent">
		<% 
		if(request.getAttribute("helpToShow") == null){
			User user = (User) session.getAttribute("User");
			%>
			<div id="helpAsHelpers">
				<p>
					<span class="text">These are the help requests in which you
						are helping someone:</span><br/>
					<%
						@SuppressWarnings("unchecked")
						List<HelpRequest> giving = (List<HelpRequest>) request
								.getAttribute("giving");
						if (giving == null || giving.isEmpty()) {
							out.println("<span class=\"warning\">youre'not giving help in any help requests</span>");
						} else {
							out.println("<ul>");
							for (HelpRequest help : giving) {
								out.println("<li>You're helping <span class=\"italic\">"
										+ help.getUser().getName() + "</span> <span class=\"italic\">"
										+ help.getUser().getSurname() + "</span> for <span class=\"italic\">"
										+ help.getAbility().getName()
										+ "</span> <a href=\"helps?show=" +
										+ help.getId() +"\">Show</a></li>");
							}
							out.println("</ul>");
						}
					%>
				</p>
			</div>
			<div id="helpsHaveAsked">
				<p>
					<span class="text">These are the helps you have asked:</span><br/>
					<%
						@SuppressWarnings("unchecked")
						List<HelpRequest> open = (List<HelpRequest>) request
								.getAttribute("open");
						if (open == null || open.isEmpty()) {
							out.println("<span class=\"warning\">you don't have any open help requests</span>");
						} else {
							out.println("<ul>");
							for (HelpRequest help : open) {
								out.println("<li>You have asked help at <span class=\"italic\">"
										+ help.getHelper().getName()
										+ " "
										+ help.getHelper().getSurname()
										+ "</span> for <span class=\"italic\">"
										+ help.getAbility().getName()
										+ "</span> <a href=\"helps?show=" +
										+ help.getId() +"\">Show</a></li>");
							}
							out.println("</ul>");
						}
					%>
				</p>
			</div>
			<div id="helpsHaveRequest">
				<p>
					<span class="text">These are the pending helps asked to you:</span><br/>
					<%
						@SuppressWarnings("unchecked")
						List<HelpRequest> pendingAsHelper = (List<HelpRequest>) request
								.getAttribute("pendingAsHelper");
						if (pendingAsHelper == null) {
							out.println("<span class=\"warning\">nobody asked you help</span>");
						} else {
							out.println("<ul>");
							for (HelpRequest help : pendingAsHelper) {
								out.println("<li><span class=\"italic\">"
										+ help.getUser().getName()
										+ " "
										+ help.getUser().getSurname()
										+ "</span> asked you help for  <span class=\"italic\">"
										+ help.getAbility().getName()
										+ "</span> <a href=\"helps?help=" + help.getId()
										+ "&state=accept\">Accept</a> <a href=\"helps?help=" + help.getId()
										+ "&state=reject\">Reject</a> <a href=\"helps?show=" +
										+ help.getId() +"\">Show</a></li>");
							}
							out.println("</ul>");
						}
					%>
				</p>
			</div>
			<div id="helpsSuspended">
				<p>
					<span class="text">These are your pending helps request:</span><br/>
					<%
						@SuppressWarnings("unchecked")
						List<HelpRequest> pendingAsAsker = (List<HelpRequest>) request
								.getAttribute("pendingAsAsker");
						if (pendingAsAsker == null) {
							out.println("<span class=\"warning\">you don't have any pending requests</span>");
						} else {
							out.println("<ul>");
							for (HelpRequest help : pendingAsAsker) {
								out.println("<li>You have asked for help at <span class=\"italic\">"
										+ help.getHelper().getName() + " "
										+ help.getHelper().getSurname() + "</span> for <span class=\"italic\">"
										+ help.getAbility().getName()
										+ "</span> <a href=\"helps?show=" +
										+ help.getId() +"\">Show</a></li>");
							}
							out.println("</ul>");
						}
					%>
				</p>
			</div>
			<div id="helpsClosed">
				<p>
					<span class="text">These are the closed helps request:</span><br/>
					<%
						@SuppressWarnings("unchecked")
						List<HelpRequest> closed = (List<HelpRequest>) request
								.getAttribute("closed");
						if (closed == null) {
							out.println("<span class=\"warning\">you don't have any closed help requests </span>");
						} else {
							out.println("<ul>");
							for (HelpRequest help : closed) {
								out.println("<li>You have helped <span class=\"italic\">"
										+ help.getUser().getName() + " "
										+ help.getUser().getSurname() + "</span> for <span class=\"italic\">"
										+ help.getAbility().getName()
										+ "</span> <a href=\"helps?show=" +
										+ help.getId() +"\">Show</a></li>");
							}
							out.println("</ul>");
						}
					%>
				</p>
			</div>	
		<% }else if(request.getAttribute("helpToShow") != null){
			%> <jsp:include page="/user/showHelp.jsp" />
		<% } %>
		<span class="error">
			<% if(request.getAttribute("error") != null){
				out.println(request.getAttribute("error"));
			} %>
		</span>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>