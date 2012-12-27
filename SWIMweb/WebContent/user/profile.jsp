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
	<%
		User user = null;
		boolean buttons = false;
		if (request.getAttribute("userLoaded") != null
				&& request.getAttribute("userLoaded") != "") {
			user = (User) request.getAttribute("userLoaded");
			buttons = true;
		} else if (session.getAttribute("User") != null) {
			user = (User) session.getAttribute("User");
			buttons = false;
		} else {
			response.sendRedirect("/SWIMweb");
		}
		
		if(request.getAttribute("from") != null && request.getAttribute("from") != ""){
			if(request.getAttribute("from").equals("search")){
				%><jsp:include page="/common/header.jsp">
					<jsp:param name="page" value="search" />
				</jsp:include><%
			}else if(request.getAttribute("from").equals("friends")){
				%><jsp:include page="/common/header.jsp">
					<jsp:param name="page" value="friends" />
				</jsp:include><%
			}else if(request.getAttribute("from").equals("helps")){
				%><jsp:include page="/common/header.jsp">
					<jsp:param name="page" value="profile" />
				</jsp:include><%
			}
		}else{
			%><jsp:include page="/common/header.jsp">
				<jsp:param name="page" value="profile" />
			</jsp:include><%
		}
	%>

	<div id="pageContent">
		<div id="profile">
			<p class="nameSurname"><%=user.getName()%> <%=user.getSurname()%></p>
			<p><span class="text">Username: </span><%=user.getUsername()%></p>
			<p><span class="text">Email: </span><%=user.getEmail()%></p>
			<p><span class="text">City: </span><%=user.getCity()%></p>
			<p><span class="text">Phone: </span><%=user.getPhone()%></p>
			<p><span class="text">Rating: </span>
			<%
				int rating = (Integer) request.getAttribute("rating");
				String star = "";	
				if (rating > 0) {	
					for (int i = 0; i < rating; i++) {
						star += "* ";
					}
				} else {
					out.println("<span class=\"warning\">no rating yet</span></li>");
				}
				out.println(star);
			%></p>
			<p>
			<span class="text">You have the following abilities:</span></br>
			<%
				Set<Ability> abilities = user.getAbilities();
				if (abilities.isEmpty()) {
					out.println("<span class=\"warning\">you don't have any ability yet!</span>");
				} else {
					out.println("<ul>");
					for (Ability a : abilities) {
						int abilityRating = (Integer) request.getAttribute(a
								.getName());
						if (abilityRating > 0) {
							star = "";
							for (int i = 0; i < abilityRating; i++) {
								star += "* ";
							}
							out.println("<li>" + a.getName() + " " + star + "</li>");
						} else {
							out.println("<li>"
									+ a.getName()
									+ " <span class=\"warning\">no rating yet</span></li>");
						}
					}
					out.println("</ul>");
				}
			%>
			</p>
			<% if(!buttons){ %>
				<p><a href="#">Modify Profile</a></p>
			<% } %>
		</div>
		<%
			if(buttons){
				%>
				<div id="buttons">
					<%
						if(request.getAttribute("friendState") != null
							&& request.getAttribute("friendState") != ""){
							if(request.getAttribute("friendState")=="friend"){
								%>
								<input type="button" id="help" onclick="addRequest('help','<%=user.getUsername()%>');" value="Ask for help" />
								<%
							}else if(request.getAttribute("friendState")=="pending"){
								%>
								<input type="button" id="help" onclick="addRequest('help','<%=user.getUsername()%>');" value="Ask for help" />
								<span class="message">Friendship Request is pending</span>
								<%
							}
						}else{
							%>
							<input type="button" id="help" onclick="addRequest('help','<%=user.getUsername()%>');" value="Ask for help" />
							<input type="button" id="friend" onclick="addRequest('friend','<%=user.getUsername()%>');" value="Ask friendship" />
							<% 
						}
					%>
				</div>
		<%
			}
		%>
		<script type="text/javascript">
			function newAjax(){

				// TODO per help
				// deve uscire una form da cui prendo
				// l'abilità su cui chiede aiuto
				
				var xmlhttp = null;
				try {
					xmlhttp = new XMLHttpRequest();
				} catch(e) {
				 try {
					 xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
				   } catch(e) {
					 xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
				   }
				}
				return xmlhttp;
			};
			
			function addRequest(type,username,ability)
			{
				var ajax = newAjax();
				var url = null;
				var errorDiv = null;
				if(type.equals("help")){
					url = "/SWIMweb/user/friends?newHelper="+username+"&ability="+ability;
					errorDiv="helpError";
				}else if(type.equals("friendship")){
					url = "/SWIMweb/user/friends?newFriend="+username;
					errorDiv="friendError";
				}
				ajax.onreadystatechange = function () {
				    if (ajax.readyState == 4) {
				        if( ajax.status == 200 ){ 
				        	addSpan('message',errorDiv,'Request added successfully');
				        	manageButtonDiv(type);
				        } else {
					        if(document.getElementById("friendError")){
					        	addSpan('error',errorDiv,'Problems during the request');
						    }
				        }
				    }
				};
				ajax.open("GET", url, true);
				ajax.send(null);
			};

			function manageButtonDiv(type){
				var friend = document.getElementById(type);
	            buttonsDiv.removeChild(friend);
	            if(document.getElementById(type+"Error")){
		        	buttonsDiv.removeChild(error,friendError);
			    }
			};

			function addSpan(clazz,id,message){
				var span = document.createElement("span");
	            span.setAttribute("class",clazz);
	            span.setAttribute("id",id);
	            span.innerHTML = message;
	            var buttonsDiv = document.getElementById('buttons'); 
	            buttonsDiv.appendChild(span);
			};
		</script>
		<br style="clear: both;">
	</div>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>