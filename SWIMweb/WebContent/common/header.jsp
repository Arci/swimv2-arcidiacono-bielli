<%@page import="it.polimi.swim.model.*,it.polimi.swim.enums.*"%>
<div id="header">	
	<div id="top">
		<div class="logo">
			<span>
			<%
				if (session.getAttribute("User") != null) {
					User user = (User) session.getAttribute("User");
					if (user.getType().equals(UserType.ADMIN)) {
						out.println("<a href=\"/SWIMweb/admin/suspended.jsp\">SWIM</a>");
					} else if (user.getType().equals(UserType.NORMAL)) {
						out.println("<a href=\"/SWIMweb/user/profile\">SWIM</a>");
					}
				} else {
					out.println("<a href=\"/SWIMweb\">SWIM</a>");
				}
			%> </span>
			<span class="extended">"Small World hypotesIs Machine"</span>
		</div>
		<div class="headerError">
			<span class="error">
				<%
					if (request.getAttribute("headerError") != null
							&& request.getAttribute("headerError") != "") {
						out.println(request.getAttribute("headerError"));
					}
				%>
			</span>
		</div>
		<div class="access">
			<%
				if (session.getAttribute("User") == null) {
			%>
				
				<form action="accessManager" method="post">
					Username: <input type="text" name="username" class="textinput" />
					Password: <input type="password" name="password" class="textinput" /> 
					<input type="submit" class="submit" value="Login" />
				</form>
			<%
				} else {
			%>
				<form action="/SWIMweb/accessManager" method="get">
					<span>
					<%
						User user = (User) session.getAttribute("User");
							out.println("Banvenuto <i>" + user.getUsername() + "</i>");
					%>
					</span>
					<input type="submit" class="submit" value="Logout" />
				</form>
			<%
				}
			%>
		</div>
	</div>
	<div class="invertedshiftdown">
		<%
			if (session.getAttribute("User") == null) {
		%>
		<ul>
			<%
				if (request.getParameter("page").equals("home")) {
						out.println("<li class=\"current\">");
					} else {
						out.println("<li>");
					}
					out.println("<a href=\"/SWIMweb/\" title=\"Search\">Search</a></li>");
					if (request.getParameter("page").equals("registration")) {
						out.println("<li class=\"current\">");
					} else {
						out.println("<li>");
					}
					out.println("<a href=\"/SWIMweb/registration.jsp\" title=\"Register\">Register</a></li>");
			%>
		</ul>
		<%
			} else {
				User user = (User) session.getAttribute("User");
				if (user.getType().equals(UserType.NORMAL)) {
		%>
				<ul>
					<%
						if (request.getParameter("page").equals("profile")) {
									out.println("<li class=\"current\">");
								} else {
									out.println("<li>");
								}
								out.println("<a href=\"/SWIMweb/user/profile\" title=\"Profile\">Profile</a></li>");
								if (request.getParameter("page").equals("search")) {
									out.println("<li class=\"current\">");
								} else {
									out.println("<li>");
								}
								out.println("<a href=\"/SWIMweb/user/search.jsp\" title=\"Search\">Search</a></li>");
								if (request.getParameter("page").equals("friends")) {
									out.println("<li class=\"current\">");
								} else {
									out.println("<li>");
								}
								out.println("<a href=\"/SWIMweb/user/friends.jsp\" title=\"Friend\">Friend</a></li>");
								if (request.getParameter("page").equals("helps")) {
									out.println("<li class=\"current\">");
								} else {
									out.println("<li>");
								}
								out.println("<a href=\"/SWIMweb/user/helps.jsp\" title=\"Helps\">Helps</a></li>");
								if (request.getParameter("page")
										.equals("abilitySuggestion")) {
									out.println("<li class=\"current\">");
								} else {
									out.println("<li>");
								}
								out.println("<a href=\"/SWIMweb/user/abilitySuggestion\" title=\"Ability Suggestion\">Suggestion</a></li>");
					%>
				</ul>
			<%
				} else if (user.getType().equals(UserType.ADMIN)) {
			%>
				<ul>
					<%
						if (request.getParameter("page").equals("suspended")) {
									out.println("<li class=\"current\">");
								} else {
									out.println("<li>");
								}
								out.println("<a href=\"/SWIMweb/admin/suspended.jsp\" title=\"Suspended Requests\">Suspended Requests</a></li>");
								if (request.getParameter("page").equals("newAbility")) {
									out.println("<li class=\"current\">");
								} else {
									out.println("<li>");
								}
								out.println("<a href=\"/SWIMweb/admin/newAbility.jsp\" title=\"Add New Ability\">Add New Ability</a></li>");
					%>
				</ul>
			<%
				}
				}
			%>
	</div>
	<br style="clear: both;" />
</div>