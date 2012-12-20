<%@page import="it.polimi.swim.model.*,it.polimi.swim.enums.*"%>
<div id="header">
	<span class="logo">SWIM</span><span class="logo extended">"Small World hypotesIs Machine"</span>
	<% if(session.getAttribute("User") == null) { %>
	<div class="login">
		<form action="accessManager" method="post">
			Username: <input type="text" name="username" class="textinput" />
			Password: <input type="password" name="password" class="textinput" /> 
			<input type="submit" value="Login" />
		</form>
	</div>
	<% } else { %>
	<div class="logout">
		<form action="accessManager" method="post">
			<span>
			<% User user = (User) session.getAttribute("User");
			out.println("Banvenuto <i>"+ user.getUsername() +"</i>"); %>
			</span>
			<input type="hidden" value="logout" />
			<input class="submit" type="submit" value="Logout" />
		</form>
	</div>
	<% } %>
</div>
<div class="invertedshiftdown">
	<% if(session.getAttribute("User") == null) { %>
	<ul>
		<li class="current"><a href="./research.jsp" title="Research">Research</a></li>
	</ul>
	<% } else {
		User user = (User) session.getAttribute("User");
		if(user.getType().equals(UserType.NORMAL)){ %>
			<ul>
				<li class="current"><a href="./profile.jsp" title="Profile">Profile</a></li>
				<li><a href="./research.jsp" title="Search">Search</a></li>
				<li><a href="./friends.jsp" title="Friends">"Friends"</a></li>
				<li><a href="./helps.jsp" title="Helps">Helps</a></li>
				<li><a href="./abilitySuggestion.jsp" title="Ability Suggestion">Suggestion</a></li>
			</ul>
		<% }else if(user.getType().equals(UserType.ADMIN)){ %>
			<ul>
				<li class="current"><a href="./suspended.jsp" title="Suspended Requests">Suspended Requests</a></li>
				<li><a href="./newAbility.jsp" title="Add New Ability">Add New Ability</a></li>
			</ul>
		<% }
	 } %>
</div>
<br style="clear: both;" />