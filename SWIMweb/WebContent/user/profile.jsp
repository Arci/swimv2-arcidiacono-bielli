<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="../css/style.css" />
<title>SWIM - Profilo</title>
</head>
<body>
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="profile" />
	</jsp:include>

	<div id="pageContent">
		<% User user = (User) session.getAttribute("User");
		if(user==null){
			response.sendRedirect("/SWIMweb");
		}
		if(request.getAttribute("modify") != null && request.getAttribute("modify") == "true"){ %>
		<div id="profileModify">
			<form action="profileManager" method="post">
				<label for='name'>Name*: </label>
				<input type='text'name='name' id='name' value="<%= user.getName() %>"/></br>
				<label for='name'>Surname: </label>
				<input type='text'name='surname' id='surname' value="<%= user.getSurname() %>"/></br> 
				<label for='username'>UserName*:</label> 
				<input type='text' name='username' id='username' value="<%= user.getUsername() %>"/></br> 
				<label for='email'>Email Address*:</label> 
				<input type='text' name='email' id='email' value="<%= user.getEmail() %>"/></br> 
				<label for='name'>City: </label> 
				<input type='text'name='city' id='city' value="<%= user.getCity() %>"/></br> 
				<label for='name'>Phone: </label> 
				<input type='text'name='phone' id='phone' value="<%= user.getPhone() %>"/></br> 
				<select id="abilityChooser">
				<%
					try {
						Hashtable<String, String> env = new Hashtable<String, String>();
						env.put(Context.INITIAL_CONTEXT_FACTORY,
								"org.jnp.interfaces.NamingContextFactory");
						env.put(Context.PROVIDER_URL, "localhost:1099");
						InitialContext jndiContext = new InitialContext(env);
						Object ref = jndiContext.lookup("ProfileManager/remote");
						ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;
						List<Ability> abilities = profileManager.getAbilityList();
						for(Ability ability : abilities){
							out.println("<option value=\"" + ability.getName() + "\">" + ability.getName() + "</option>");
						}
					} catch (NamingException e) {
						e.printStackTrace();
					}
				%>
				</select>
				<!-- alla pressione di un bottone, se no già in user.getAbilities()
					 l'abilità nella combobox viene aggiunta alla lista  o trovare altro modo -->
				<textarea id="abilities">
				<%
				Set<Ability> abilities = user.getAbilities();
				for(Ability a : abilities){
					out.println(a.getName());
				}
				%>
				</textarea>
				<label for='password'>Password*:</label> 
				<input type='password' name='password' id='password'/></br>
				* are obligatory</br> 
				<input type="submit" value="Modifica">
			</form>
		</div>
		<% } else {  %>
		<div id="profile">
				<p class="nameSurname"><%= user.getName() %> <%= user.getSurname() %></p>
				<p><span class="text">Username: </span><%= user.getUsername() %></p>
				<p><span class="text">Email: </span><%= user.getEmail() %></p>
				<p><span class="text">City: </span><%= user.getCity() %></p>
				<p><span class="text">Phone: </span><%= user.getPhone() %></p>
				<p>
				<span class="text">You have the following abilities:</span></br>
				<% Set<Ability> abilities = user.getAbilities();
				if(abilities.isEmpty()){
					out.println("<span class=\"warning\">you don't have any ability yet!</span>");
				}else{
					out.println("<ul>");
					for(Ability a : abilities){
						out.println("<li>" + a.getName() + "</li>");
					}
					out.println("</ul>");
				}
				%>
				</p>
				<a href="/SWIMweb/user/profile.jsp?modify=true">Modify Profile</a>
		</div>
		<% } %>
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>