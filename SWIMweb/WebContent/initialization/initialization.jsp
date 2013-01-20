<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/style.css">
<script type="text/javascript" src="js/mail.js"></script>
<title>SWIM - SignIn</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="" />
	</jsp:include>

	<div id="pageContent">
		<%
		@SuppressWarnings("unchecked")
		List<String> initMessage = (List<String>) request.getAttribute("initMessage");
		@SuppressWarnings("unchecked")
		List<String> initError = (List<String>) request.getAttribute("initError");
	
		Iterator<String> itr = initMessage.iterator();
		while(itr.hasNext()){
			String next = itr.next();
			if(!itr.hasNext()){
				%><p class="text message last"><%=next%></p><%
			}else{
				%><p class="text message"><%=next%></p><%
			}
		}
		
		itr = initError.iterator();
		while(itr.hasNext()){
			String next = itr.next();
			if(!itr.hasNext()){
				%><p class="text error last"><%=next%></p><%
			}else{
				%><p class="text error"><%=next%></p><%
			}
		}
		%>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>