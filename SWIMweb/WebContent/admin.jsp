<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
		try {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, "localhost:1099");
		InitialContext jndiContext = new InitialContext(env);
		Object ref = jndiContext.lookup("AdminManager/remote");
		AdminManagerRemote userManager = (AdminManagerRemote) ref;
		List<User> users = userManager.getUsers();
		List<Ability> abilities = userManager.getAbilities();
	%><select><%
				for (Ability ability : abilities) { %>
					<option value=<%=ability.getName() %>><%=ability.getName() %></option>
				<% } %>
			</select><%
			%><select><%
				for (User user : users) { %>
				<option value=<%=user.getUsername() %>><%=user.getUsername() %></option>
			<% } %>
		</select><%
		} catch (NamingException e) {
			e.printStackTrace();
		}
	%>
</body>
</html>