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
			}else{
				%><!--  default -->
				<jsp:include page="/common/header.jsp">
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
		<% if(request.getAttribute("error") != null){ %>
			<div class="error">
				<% out.println(request.getAttribute("error")); %>
			</div>
		<% }else{ %>
			<div>
				<div id="profile">
					<p class="nameSurname"><%=user.getName()%> <%=user.getSurname()%></p>
					<p><span class="text">Username: </span><%=user.getUsername()%></p>
					<p><span class="text">Email: </span><%=user.getEmail()%></p>
					<p>
					<span class="text">City: </span>
					<%if(user.getCity() != null){
						out.println(user.getCity());
					}else{
						out.println("<span class=\"warning\">no city yet</span>");
					}%>
					</p>
					<p>
					<span class="text">Phone: </span>
					<%if(user.getPhone() != 0){
						out.println(user.getPhone() );
					}else{
						out.println("<span class=\"warning\">no phone yet</span>");
					}%>
					</p>
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
					<span class="text">You have the following abilities:</span><br/>
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
						<p><a href="/SWIMweb/user/modifyProfile.jsp">Modify Profile</a></p>
					<% } %>
				</div>
				<% if(buttons){ %>
					<div id="buttons">
						<% if(request.getAttribute("friendState") != null
								&& request.getAttribute("friendState") != ""){
								if(request.getAttribute("friendState")=="friend"){
									// TODO per help
									// deve uscire una form da cui prendo
									// l'abilità su cui chiede aiuto
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
							} %>
					</div>
				<%	} %>
				</div>
				<br style="clear: both;">
			</div>		
		<% } %>
			
		<script type="text/javascript">	
			function addRequest(type,username,ability) {
				xmlhttp = new XMLHttpRequest();
				var url = null;
				var messageSpan = null;
				if(type == "help"){
					url = "/SWIMweb/user/friends?newHelper="+username+"&ability="+ability;
					messageSpan="helpMessage";
				}else if(type == "friend"){
					url = "/SWIMweb/user/friends?newFriend="+username;
					messageSpan="friendMessage";
				}
				console.log("AJAX REQUEST TO:\n" + url+ "\n");
				xmlhttp.onreadystatechange = function () {
				    if (xmlhttp.readyState == 4) {
				        if( xmlhttp.status == 200 ){ 
				        	console.log("RESPONSE TEXT:\n"+ xmlhttp.responseText);
					        console.log("RESPONSE XML:\n"+ xmlhttp.responseXML);
					        var response = xmlhttp.responseXML.getElementsByTagName("result")[0].childNodes[0].nodeValue;
					        console.log("VALUE:\n"+ response);
					        if(response == "OK"){
					        	manageMessage('message',messageSpan,'Request added successfully');
					        	manageButtonDiv(type);
					        } else {
								var error = xmlhttp.responseXML.getElementsByTagName("error")[0].childNodes[0].nodeValue;
								manageMessage('error',messageSpan,error);
							}
				        } else {
					        manageMessage('error',errorDiv,'Problems during the request');
				     	}
				    }
				};
				xmlhttp.open("GET", url, true);
				xmlhttp.send(null);
			};
	
			function manageButtonDiv(type){
				var buttonsDiv = document.getElementById('buttons'); 
				var friend = document.getElementById(type);
	            buttonsDiv.removeChild(friend);
			};
	
			function manageMessage(clazz,id,message){
				if(document.getElementById(id)){
					var buttonsDiv = document.getElementById('buttons');
					buttonsDiv.removeChild(document.getElementById(id));
				}
				var span = document.createElement("span");
	            span.setAttribute("class",clazz);
	            span.setAttribute("id",id);
	            span.innerHTML = message;
	            var buttonsDiv = document.getElementById('buttons'); 
	            buttonsDiv.appendChild(span);
			};
	</script>

	<jsp:include page="/common/footer.jsp"></jsp:include>
</body>
</html>