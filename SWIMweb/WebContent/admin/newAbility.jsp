<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../css/style.css" />
<title>SWIM - Aggiunta nuova abilit&agrave;</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="newAbility" />
	</jsp:include>

	<div id="pageContent">
		<div id="newAbility">
			<fieldset>
				<legend class="text">Create a new Ability</legend>
				<form action="newAbility" method="get">
					<input type="text" name="newAbility" /><br> 
					<input type="submit" name="submit" value="Submit" />
				</form>
			</fieldset>
		</div>
		<div id="ability">
			<label>Abilities of the system:</label><br>
			<p>
				<%
					List<Ability> abilities = (List<Ability>) request
							.getAttribute("abilities");
					for (Ability ability : abilities) {
				%><span id="ability"><%=ability.getName()%></span><br>
				<%
					}
				%>
			</p>
		</div>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>