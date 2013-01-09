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
	
	<% if (session.getAttribute("User") != null) {
			User user = (User) session.getAttribute("User");
	%>
	<div id="pageContent">
		<div>
			<% if (request.getAttribute("error") != null) {
				String error= (String) request.getAttribute("error");
				out.println("<span class=\"error\">" + error + "</span>");
			} %>
			<% if (request.getAttribute("result") != null) {
				String result= (String) request.getAttribute("result");
				out.println("<span class=\"message\">" + result + "</span>"); 
			} %>
		</div>
		<div>
			<div id="contactInformation" class="left">
				<fieldset>
					<legend>Manage your contact information:</legend>
					<form id="modifyProfile" action='profile' method='post' accept-charset='UTF-8'>
						<p><label for="name">Name: </label><input type="text" id="name" name="name" value="<%=user.getName() %>" /></p>
						<p><label for="surname">Surname: </label><input type="text" id="surname" name="surname" value="<%=user.getSurname() %>" /></p>
						<p><label for="username">Username: </label><input type="text" id="username" name="username" value="<%=user.getUsername() %>" /></p>
						<p><label for="email">Email: </label><input type="text" id="email" name="email" value="<%=user.getEmail() %>" /></p>
						<p><label for="city">City: </label><input type="text" id="city" name="city" value="<%=user.getCity() %>" /></p>
						<p><label for="phone">Phone: </label><input type="text" id="phone" name="phone" value="<%=user.getPhone() %>" /></p>
						
						<p><label for="password">Password*: </label><input type="password" id="password" name="password" /></p>
						<p><label for="checkPassword">Check Password*: </label><input type="password" id="checkPassword" name="checkPassword" /></p>
						* required to validate modifications<br/>
						<input type='submit' name='Submit' value='Submit' />
					</form>
				</fieldset>
			</div>
			<div id="abilitiesManager" class="right">
				<fieldset>
					<legend>Manage your abilities:</legend>
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
	
	<script type="text/javascript">	
		function removeAbility(abilityName){
			if(document.getElementById("spanError")){
				var spanError = document.getElementById("spanError");
				spanError.parentNode.removeChild(spanError);
			}
			deleteLiElement(abilityName);
			var abilityUl = document.getElementById("abilityUl");
			var childs = abilityUl.getElementsByTagName("li");
			if(childs.length == 0){
				addSpanWarning(abilityUl, "you don't have any ability yet!");
			}		
            addHidden('remove', abilityName);
            removeHidden('add', abilityName);
		};
		
		function addAbility(username){
			var abilityList = document.getElementById("abilityList");
			var abilityName = abilityList.options[abilityList.selectedIndex].value;
			if(document.getElementById("spanError")){
				var spanError = document.getElementById("spanError");
				spanError.parentNode.removeChild(spanError);
			}
			if(!document.getElementById(abilityName)){
				addLiElement(abilityName, username);
			}else{
				addSpanError("you already have this ability!");
			}
			if(document.getElementById("spanWarning")){
				var spanWarning = document.getElementById("spanWarning");
				spanWarning.parentNode.removeChild(spanWarning);
			}
            addHidden('add',abilityName);
            removeHidden('remove', abilityName);
		};

		function addLiElement(abilityName){			
			var li = document.createElement("li");
            li.setAttribute("id",abilityName);
            li.innerHTML = abilityName + " ";
            var input = document.createElement("input");
            input.setAttribute("type","button");
            input.setAttribute("id",abilityName);
            input.setAttribute("onclick","removeAbility('"+ abilityName +"', '"+ username +"');");
            input.setAttribute("value","Delete");
            li.appendChild(input);
            var abilityUl = document.getElementById("abilityUl");
            abilityUl.appendChild(li);
		};

		function deleteLiElement(abilityName){
			var abilityLi = document.getElementById(abilityName);
			abilityLi.parentNode.removeChild(abilityLi);
		};

		function addSpanError(message){
			var span = document.createElement("span");
			span.setAttribute("class","error");
			span.setAttribute("id","spanError");
			span.innerHTML = message;
            var form = document.getElementById("modifyAbilities");
			form.appendChild(span);
		};

		function addSpanWarning(ul , message){
			var span = document.createElement("span");
			span.setAttribute("class","message");
			span.setAttribute("id","spanWarning");
			span.innerHTML = message;
			ul.appendChild(span);
		};
		

		function addHidden(action, abilityName){
			var elements = document.getElementsByTagName("input");
			var exists = false;
			for(var i=0; i < elements.lenght; i++){
				if(elements[i].type == "hidden" 
						&& elements[i].name == action 
						&& elements[i].value == abilityName){
					exists = true;
				}
			}
			if(!exists){
				var hidden = document.createElement("input");
				hidden.setAttribute("type","hidden");
				hidden.setAttribute("name",action);
				hidden.setAttribute("value",abilityName);
	            var form = document.getElementById("modifyAbilities");
	            form.appendChild(hidden);
			}			
        };

        function removeHidden(action, abilityName){
        	var elements = document.getElementsByTagName("input");
			var exists = false;
			var hidden = null;
			for (var i=0; i < elements.length; i++){
				if(elements[i].type == "hidden" 
						&& elements[i].name == action 
						&& elements[i].value == abilityName){
					exists = true;
					hidden = elements[i];
				}
			}
			if(exists){
	            var form = document.getElementById("modifyAbilities");
	            form.removeChild(hidden);
			}
        };
	</script>
	
	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>