<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Admin</title>
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
	%>
	<form name="admin" action="insert.html" method="post">
		<select name="ability">
			<%
				for (Ability ability : abilities) {
			%>
			<option value=<%=ability.getName()%>><%=ability.getName()%></option>
			<%
				}
			%>
		</select> <select name="user">
			<%
				for (User user : users) {
			%>
			<option value=<%=user.getUsername()%>><%=user.getUsername()%></option>
			<%
				}
			%>
		</select> <input type="submit" value="aggiungi">
	</form>
	<%
		} catch (NamingException e) {
			e.printStackTrace();
		}
	%>
</body>
</html>