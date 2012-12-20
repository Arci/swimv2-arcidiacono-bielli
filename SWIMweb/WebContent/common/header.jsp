<%@page import="it.polimi.swim.model.*,it.polimi.swim.enums.*"%>
<div id="header">
	<span class="logo">SWIM</span>
	<span class="logo extended">"Small World hypotesIs Machine"</span>
	<div class="access">
	<% if(session.getAttribute("User") == null) { %>
		<form action="accessManager" method="post">
			Username: <input type="text" name="username" class="textinput" />
			Password: <input type="password" name="password" class="textinput" /> 
			<input type="submit" class="submit" value="Login" />
		</form>
	<% } else { %>
		<form action="../accessManager" method="post">
			<span>
			<% User user = (User) session.getAttribute("User");
			out.println("Banvenuto <i>"+ user.getUsername() +"</i>"); %>
			</span>
			<input type="hidden" value="logout" />
			<input type="submit" class="submit" value="Logout" />
		</form>
	<% } %>
	</div>
</div>
<div class="invertedshiftdown">
	<% if(session.getAttribute("User") == null) { %>
	<ul>
		<% if(request.getParameter("page").equals("home")) {
			out.println("<li class=\"current\">"); 
		}else{
			out.println("<li>");
		}
		out.println("<a href=\"home.jsp\" title=\"Search\">Search</a></li>"); %>
	</ul>
	<% } else {
		User user = (User) session.getAttribute("User");
		if(user.getType().equals(UserType.NORMAL)){ %>
			<ul>
				<% 
				if(request.getParameter("page").equals("profile")) {
					out.println("<li class=\"current\">"); 
				}else{
					out.println("<li>");
				}
				out.println("<a href=\"../user/profile.jsp\" title=\"Profile\">Profile</a></li>");
				if(request.getParameter("page").equals("search")) {
					out.println("<li class=\"current\">"); 
				}else{
					out.println("<li>");
				}
				out.println("<a href=\"../user/search.jsp\" title=\"Search\">Search</a></li>");
				if(request.getParameter("page").equals("friends")) {
					out.println("<li class=\"current\">"); 
				}else{
					out.println("<li>");
				}
				out.println("<a href=\"../user/friends.jsp\" title=\"Friend\">Friend</a></li>");
				if(request.getParameter("page").equals("helps")) {
					out.println("<li class=\"current\">"); 
				}else{
					out.println("<li>");
				}
				out.println("<a href=\"../user/helps.jsp\" title=\"Helps\">Helps</a></li>");
				if(request.getParameter("page").equals("abilitySuggestion")) {
					out.println("<li class=\"current\">"); 
				}else{
					out.println("<li>");
				}
				out.println("<a href=\"../user/abilitySuggestion.jsp\" title=\"Ability Suggestion\">Suggestion</a></li>");
				%>
			</ul>
		<% }else if(user.getType().equals(UserType.ADMIN)){ %>
			<ul>
				<% 
				if(request.getParameter("page").equals("suspended")) {
					out.println("<li class=\"current\">"); 
				}else{
					out.println("<li>");
				}
				out.println("<a href=\"../admin/suspended.jsp\" title=\"Suspended Requests\">Suspended Requests</a></li>");
				if(request.getParameter("page").equals("newAbility")) {
					out.println("<li class=\"current\">"); 
				}else{
					out.println("<li>");
				}
				out.println("<a href=\"../admin/newAbility.jsp\" title=\"Add New Ability\">Add New Ability</a></li>");
				%>
			</ul>
		<% }
	 } %>
</div>
<br style="clear: both;" />