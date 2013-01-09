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
			User sessionUser = (User) session.getAttribute("User");
			User userLoaded = (User) request.getAttribute("userLoaded");
			if(sessionUser.equals(userLoaded)){
				user = sessionUser;
				buttons = false;
				%><jsp:include page="/common/header.jsp">
					<jsp:param name="page" value="profile" />
				</jsp:include><%
			}else{
				user = userLoaded;
				buttons = true;
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
							<jsp:param name="page" value="helps" />
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
			}
		}else if (session.getAttribute("User") != null) {
			user = (User) session.getAttribute("User");
			buttons = false;
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
				<div id="profile" class="left">
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
					<div id="buttons" class="right">
						<div id="friendship">
						<% if(request.getAttribute("friendState") != null
								&& request.getAttribute("friendState") != ""){
								if(request.getAttribute("friendState").equals("pending")){
									%>
									 <span class="message">Friendship Request is pending</span>
									<%
								}else if(request.getAttribute("friendState").equals("friend")){
									%>
									 <span class="message">You are friends</span>
									<%
								}else{
									%>
									 <input type="button" id="friend" onclick="addRequest('friend','<%=user.getUsername()%>');" value="Ask friendship" />
									<% 
								}
							} %>
							</div><br/>
							<div id="helpRequest">
								<%
									if(user.getAbilities().isEmpty()){
										out.println("<span class=\"message\">This user doesn't have abilities, you cannot ask him for help</span>");
									}else{
										%><select id="helperAbilities"><%
											for(Ability ability : user.getAbilities()){
												out.println("<option value=\"" + ability.getName() + "\">" + ability.getName() + "</option>");
											}
										%></select>
										<input type="button" id="help" onclick="addRequest('help','<%=user.getUsername()%>');" value="Ask for help" />
										<%
									}
								%>
							</div>
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
				var okMessage = null;
				if(type == "help"){
					var helperAbilities = document.getElementById("helperAbilities");
					var ability = helperAbilities.options[helperAbilities.selectedIndex].value;
					url = "/SWIMweb/user/helps?newHelper="+username+"&ability="+ability;
					messageSpan="helpMessage";
					okMessage = 'Help request for \'' + ability + '\' added successfully';
				}else if(type == "friend"){
					url = "/SWIMweb/user/friends?newFriend="+username;
					messageSpan="friendMessage";
					okMessage = 'Friendship request added successfully';
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
					        	manageMessage('message',messageSpan, okMessage);
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
				var button = document.getElementById(type);
				if(type != "help"){
					button.parentNode.removeChild(button);
				}
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