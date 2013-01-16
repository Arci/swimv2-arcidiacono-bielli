<%@page import="it.polimi.swim.model.*,it.polimi.swim.enums.*"%>
<div id="header">	
	<div id="top">
		<div class="logo">
			<span>
			<%
				if (session.getAttribute("User") != null) {
					User user = (User) session.getAttribute("User");
					if (user.getType().equals(UserType.ADMIN)) {
						out.println("<a href=\"/SWIMweb/admin/admin\">SWIM</a>");
					} else if (user.getType().equals(UserType.NORMAL)) {
						out.println("<a href=\"/SWIMweb/user/profile\">SWIM</a>");
					}
				} else {
					out.println("<a href=\"/SWIMweb\">SWIM</a>");
				}
			%> </span>
			<span class="extended">"Small World hypotesIs Machine"</span>
		</div>
		<span class="headerError">
				<%
					if (request.getAttribute("headerError") != null
							&& request.getAttribute("headerError") != "") {
						out.println(request.getAttribute("headerError"));
					}
				%>
		</span>
		<div class="access">
			<%
				if (session.getAttribute("User") == null) {
			%>
				<form name="accessManager" action="accessManager" method="post">
					<input type="text" name="username" class="textinput" value="username" onfocus="this.value=''; this.style.color='black';" 
						onBlur="if(this.value==''){this.value='username'; this.style.color='#C0C0C0';}"/>
					<input type="password" name="password" class="textinput" value="password" onfocus="this.value=''; this.style.color='black';" 
						onBlur="if(this.value==''){this.value='password'; this.style.color='#C0C0C0';}"/>
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
				if (request.getParameter("page").equals("search")) {
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
								out.println("<a href=\"/SWIMweb/search\" title=\"Search\">Search</a></li>");
								if (request.getParameter("page").equals("friends")) {
									out.println("<li class=\"current\">");
								} else {
									out.println("<li>");
								}
								out.println("<a href=\"/SWIMweb/user/friends\" title=\"Friend\">Friends</a></li>");
								if (request.getParameter("page").equals("helps")) {
									out.println("<li class=\"current\">");
								} else {
									out.println("<li>");
								}
								out.println("<a href=\"/SWIMweb/user/helps\" title=\"Helps\">Helps</a></li>");
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
								out.println("<a href=\"/SWIMweb/admin/suspended\" title=\"Suspended Requests\">Suspended Requests</a></li>");
								if (request.getParameter("page").equals("newAbility")) {
									out.println("<li class=\"current\">");
								} else {
									out.println("<li>");
								}
								out.println("<a href=\"/SWIMweb/admin/newAbility\" title=\"Add New Ability\">Add New Ability</a></li>");
					%>
				</ul>
			<%
				}
				}
			%>
	</div>
	<br style="clear: both;" />
</div>