<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<link rel="stylesheet" type="text/css" href="../css/style.css" >
<script type="text/javascript" src="../js/modifyProfile.js"></script>
<title>SWIM - Profilo</title>
</head>
<body>
	
	<jsp:include page="/common/header.jsp">
		<jsp:param name="page" value="profile" />
	</jsp:include>
	
	<% if (session.getAttribute("User") != null) {
			User user = (User) session.getAttribute("User");
	%>
	<div id="pageContent">
		<div id="profileError">
			<% if (request.getAttribute("error") != null) {
				String error= (String) request.getAttribute("error");
				out.println("<span class=\"error\">" + error + "</span>");
			} %>
			<% if (request.getAttribute("result") != null) {
				String result= (String) request.getAttribute("result");
				out.println("<span class=\"message\">" + result + "</span>"); 
			} %>
		</div>
		<div id="modifyContainer">
			<div id="contactInformation" class="left">
				<fieldset>
					<legend class="text">Manage your contact information:</legend>
					<form id="modifyProfile" action='profile' method='post' accept-charset='UTF-8'>
						<p class="first"><label for="name">Name: </label><input type="text" id="name" name="name" value="<%=user.getName() %>" /></p>
						<p><label for="surname">Surname: </label><input type="text" id="surname" name="surname" value="<%=user.getSurname() %>" /></p>
						<p><label for="username">Username: </label><input type="text" id="username" name="username" value="<%=user.getUsername() %>" /></p>
						<br/><div id="emailContainer"><label for="email">Email: </label><input type="text" id="email" name="email" value="<%=user.getEmail() %>" onchange="checkMail();"/>  </div>
						<p><label for="city">City: </label><input type="text" id="city" name="city" value=<%
						if (user.getCity() == null) {
							out.println("\"insert your city.\" onfocus=\"this.value='';\"");
						} else {
							out.println(user.getCity());
						} %> /></p>
						<p><label for="phone">Phone: </label><input type="text" id="phone" name="phone" value=<%
						if (user.getPhone() == 0) {
							out.println("\"insert your phone number.\" onfocus=\"this.value='';\"");
						} else {
							out.println(user.getPhone());
						}
						%> /></p>
						<p><label for="password">Password*: </label><input type="password" id="password" name="password" /></p>
						* required to validate modifications<br/>
						<input type='submit' name='Submit' value='Submit' />
					</form>
				</fieldset>
			</div>
			<div id="abilitiesManager" class="right">
				<fieldset>
					<legend class="text">Manage your abilities:</legend>
					<form id="modifyAbilities" action='ability' method='post' accept-charset='UTF-8'>			
						<% 
							try {	
								Hashtable<String, String> env = new Hashtable<String, String>();
								env.put(Context.INITIAL_CONTEXT_FACTORY,
										"org.jnp.interfaces.NamingContextFactory");
								env.put(Context.PROVIDER_URL, "localhost:1099");
								InitialContext jndiContext = new InitialContext(env);
								Object ref = jndiContext.lookup("AbilityManager/remote");
								AbilityManagerRemote abilityManager = (AbilityManagerRemote) ref;
								
								List<Ability> allAbilities = abilityManager.getAbilityList();
								
								out.println("<select id=\"abilityList\">");
								for(Ability ability : allAbilities){
									out.println("<option value=\"" + ability.getName() + "\">" + ability.getName() + "</option>");
								}
								out.println("</select>");
								%><input type="button" id="abilityAdder" onclick="addAbility('<%=user.getUsername()%>');" value="Add" /><%
							} catch (NamingException e) {
								%><span class="error">Can't reach server</span><%
							}
							Set<Ability> abilities = user.getAbilities();
							if (abilities.isEmpty()) {
								out.println("<ul id=\"abilityUl\">");
								out.println("<span class=\"message\" id=\"spanWarning\">you don't have any ability yet!</span>");
							} else {
								out.println("<ul id=\"abilityUl\">");
								for (Ability a : abilities) {
										out.println("<li id=\"" + a.getName() +"\">" + a.getName() + 
												" <input type=\"button\" id=\"" +
												a.getName() +"\" onclick=\"removeAbility(\'" +
												a.getName() + "\', '" + 
												user.getUsername() + "');\" value=\"Delete\"></li>");
								}
							}
							out.println("</ul>");
						%>
						<input type='submit' name='Submit' value='Apply' />
					</form>
				</fieldset>
			</div>			
		</div>
		<br style="clear: both;">
	</div>
	<% } %>

	<jsp:include page="/common/footer.jsp"></jsp:include>
	
</body>
</html>