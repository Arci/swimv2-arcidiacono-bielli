<%@page import="it.polimi.swim.enums.HelpState"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>

<div id="helpInformation">
	<%
		HelpRequest help = (HelpRequest) request.getAttribute("helpToShow");
		User sessionUser = (User) session.getAttribute("User");
	%>
	<div id="helpState">
		<span class="text">State: </span>
		<% if(help.getState().equals(HelpState.PENDING)){ %>
			<span class="capital blue"><%=help.getState().toString()%></span>
		<% } else if(help.getState().equals(HelpState.CLOSED)){ %>
			<span class="capital red"><%=help.getState().toString()%></span>
		<% } else if(help.getState().equals(HelpState.OPEN)){ %>
			<span class="capital green"><%=help.getState().toString()%></span>
		<% } %>
		<% if(sessionUser.equals(help.getUser()) && help.getState().equals(HelpState.OPEN)){ %>
				<input type="button" id="buttonClose" onclick="showFeedbacks();" value="Close this help request"/>
				<p id="votes" style="display: none;">
					<span>give a feedback to: <%=help.getHelper().getName()%> <%=help.getHelper().getSurname()%> </span>
					<% for(int i=1; i<6; i++){
						%><a href="helps?help=<%=help.getId()%>&vote=<%=i%>&state=close"><%=i%></a> <%
					} %>
				</p>
		<% } %>
	</div>
	<p><span class="text">Applicant user: </span>
		<%=help.getUser().getName()%> <%=help.getUser().getSurname()%> 
		<% if(!sessionUser.equals(help.getUser())) { %>
			<a href="profile?username=<%=help.getUser().getUsername()%>&from=helps">Visualizza Profilo</a>
		<% } %>
	</p>
	<p><span class="text">Helper user: </span>
		<%=help.getHelper().getName()%> <%=help.getHelper().getSurname()%> 
		<% if(!sessionUser.equals(help.getHelper())) { %>
			<a href="profile?username=<%=help.getHelper().getUsername()%>&from=helps">Visualizza Profilo</a>
		<% } %>
	</p>
	<p><span class="text">Ability involved: </span><%=help.getAbility().getName()%></p>
	<p><span class="text">Opening date: </span><%=help.getOpeningDate()%></p>
		<% if(help.getClosingDate() != null){
			%><p><span class="text">Closing date: </span><%=help.getClosingDate()%></p><%
		} %>
	<p><span class="text">Feedback: </span>
		<%  if (help.getVote() > 0) {	
				String star = "";	
				for (int i = 0; i < help.getVote(); i++) {
					star += "* ";
				}
				out.println(star);
			} else {
				out.println("<span class=\"warning\">no rating yet</span></li>");
			}
	%></p>
</div>
<div id="chat">

</div>
<script type="text/javascript">
	function showFeedbacks(){
		var divFeedbacks = document.getElementById("votes");
		divFeedbacks.style.display = "inline";buttonClose
		var buttonClose = document.getElementById("buttonClose");
		buttonClose.parentNode.removeChild(buttonClose);
		
	};
</script>