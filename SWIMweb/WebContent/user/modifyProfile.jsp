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
		<div class="error">
			<% if (session.getAttribute("error") != null) {
				out.println(request.getAttribute("error")); 
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
			<div id="abilitiesDiv" class="right">
				<fieldset id="abilitiesFieldset">
					<legend>Manage your abilities:</legend>
					<% 
						out.println("<select id=\"abilityList\">");
						out.println("<option value=\"giardiniere\">giardiniere</option>");
						//TODO get abilities and fill combo box
						out.println("</select>");
						%>
						<input type="button" id="abilityAdder" onclick="addAbility('<%=user.getUsername()%>');" value="Add" />
						<%
						Set<Ability> abilities = user.getAbilities();
						if (abilities.isEmpty()) {
							out.println("<span class=\"warning\">you don't have any ability yet!</span>");
						} else {
							out.println("<ul>");
							for (Ability a : abilities) {
									out.println("<li>" + a.getName() + 
											" <input type=\"button\" id=\"" +
											a.getName() +"\" onclick=\"removeAbility(\'" +
											a.getName() + "\', '" + 
											user.getUsername() + "');\" value=\"Delete\"></li>");
							}
							out.println("</ul>");
						}
					%>
				</fieldset>
			</div>			
		</div>
		<br style="clear: both;">
	</div>
	<% } %>
	
	<script type="text/javascript">	
		function addAbility(username){
			xmlhttp = new XMLHttpRequest();
			var abilityList = document.getElementById("abilityList");
			var ability = abilityList.options[abilityList.selectedIndex].value;
			var url = "/SWIMweb/user/ability?action=add&username=" + username + "&ability=" + ability;
			console.log("AJAX REQUEST TO:\n" + url+ "\n");
			xmlhttp.onreadystatechange = function () {
			    if (xmlhttp.readyState == 4) {
			        if( xmlhttp.status == 200 ){ 
			        	console.log("RESPONSE TEXT:\n"+ xmlhttp.responseText);
				        var response = xmlhttp.responseXML.getElementsByTagName("result")[0].childNodes[0].nodeValue;
				        console.log("VALUE:\n"+ response);
				        if(response == "OK"){
				        	console.log("OK\n");
				        } else {
							var error = xmlhttp.responseXML.getElementsByTagName("error")[0].childNodes[0].nodeValue;
							manageMessage('error', error);
						}
				        addAbility(ability);
			        } else {
			        	manageMessage('error','Problems during the request');
			     	}
			    }
			};
			xmlhttp.open("GET", url, true);
			xmlhttp.send(null);
		};

		function removeAbility(ability, username){
			xmlhttp = new XMLHttpRequest();
			var url = "/SWIMweb/user/ability?action=remove&username=" + username + "&ability=" + ability;
			console.log("AJAX REQUEST TO:\n" + url+ "\n");
			xmlhttp.onreadystatechange = function () {
			    if (xmlhttp.readyState == 4) {
			        if( xmlhttp.status == 200 ){ 
			        	console.log("RESPONSE TEXT:\n"+ xmlhttp.responseText);
				        var response = xmlhttp.responseXML.getElementsByTagName("result")[0].childNodes[0].nodeValue;
				        console.log("VALUE:\n"+ response);
				        if(response == "OK"){
				        	console.log("OK\n");
				        } else {
							var error = xmlhttp.responseXML.getElementsByTagName("error")[0].childNodes[0].nodeValue;
							manageMessage('error', error);
						}
						removeAbility(ability);
			        } else {
			        	manageMessage('error','Problems during the request');
			     	}
			    }
			};
			xmlhttp.open("GET", url, true);
			xmlhttp.send(null);
		};

		function manageMessage(clazz,message){
			if(document.getElementById("message")){
				var abilitiesFieldset = document.getElementById('abilitiesFieldset');
				abilitiesFieldset.removeChild(document.getElementById("message"));
			}
			var span = document.createElement("span");
            span.setAttribute("class",clazz);
            span.setAttribute("id","message");
            span.innerHTML = message;
            var abilitiesFieldset = document.getElementById('abilitiesFieldset'); 
            abilitiesFieldset.appendChild(span);
		};

		function removeAbility(abilityID){
			//remove ability from the list (the entire <li>)
		};

		function addAbility(abilityID){
			//add ability to the list (creante new <li>)
		};
		
	</script>
	
	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>