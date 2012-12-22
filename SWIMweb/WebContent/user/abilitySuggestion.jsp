<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../css/style.css" />
<title>SWIM - Suggerisci nuove abilit&agrave;</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="abilitySuggestion" />
	</jsp:include>

	<div id="pageContent">
		<div id="requestFormContainer">
			<fieldset>
				<legend class="text">Suggest new ability</legend>
				<form id="requestForm" action="abilitySuggestion" method="post">
					<input type="text" name="suggestion" /></br> 
					<input type="submit" name="submit" value="Submit" />
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
		</br>
		<div id="otherRequest">
			<span class="text">This is the list of your suggestions:</span></br>
			<%
				User user = (User) session.getAttribute("User");
				if (user == null) {
					response.sendRedirect("/SWIMweb");
				}
				List<AbilityRequest> abilityRequests = user.getAbilityRequest();
				if (abilityRequests.isEmpty()) {
					out.println("<span class=\"warning\">you don't have any suspended request</span>");
				}else{
					out.println("<ul>"); 
					for (AbilityRequest req : abilityRequests) {
						out.println("<li>'<span class=\"italic\">" + req.getText()
								+ "'</span> is in state: <span class=\"text\">"
								+ req.getState().toString() + "</span></li>");
					}
					out.println("</ul>"); 
				}
			%>

		</div>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>