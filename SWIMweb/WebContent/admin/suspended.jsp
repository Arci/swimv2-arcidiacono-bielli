<%@page import="it.polimi.swim.enums.RequestState"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../css/style.css" />
<title>SWIM - Richieste in sospeso</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="suspended" />
	</jsp:include>
	<div id="pageContent">
		<p>
			<jsp:include page="/admin/suspended/suggest"></jsp:include>
			<%
				if (request.getAttribute("suggests") != null){
					@SuppressWarnings("unchecked")
					List <AbilityRequest> suspended = (List<AbilityRequest>) request.getAttribute("suggests");
					for (AbilityRequest req : suspended){
						%><span><%out.println(req.getText() + " in state: <span class=\"message\">" + req.getState() + "</span>"); %></span>
						<a href="suspended/suggest?ability=<%=req.getId()%>&decision=<%=RequestState.ACCEPTED.toString() %>">Accept</a>
						<a href="suspended/suggest?ability=<%=req.getId()%>&decision=<%=RequestState.REJECTED.toString() %>">Refuse</a>
						<br> <%
					}
				}
			%>
		</p>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>