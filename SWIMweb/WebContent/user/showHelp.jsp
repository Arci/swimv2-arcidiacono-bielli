<%@page import="it.polimi.swim.enums.HelpState"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>

<div id="helpInformation">
	<%
		HelpRequest help = (HelpRequest) request.getAttribute("helpToShow");
		User sessionUser = (User) session.getAttribute("User");
	%>
	<div id="helpState">
		<span id="helpText" class="text">State: </span>
		<% if(help.getState().equals(HelpState.PENDING)){ %>
			<span id="state" class="capital blue"><%=help.getState().toString()%></span>
		<% } else if(help.getState().equals(HelpState.CLOSED)){ %>
			<span id="state" class="capital red"><%=help.getState().toString()%></span>
		<% } else if(help.getState().equals(HelpState.OPEN)){ %>
			<span id="state" class="capital green"><%=help.getState().toString()%></span>
		<% } %>
		<% if(sessionUser.equals(help.getUser()) && help.getState().equals(HelpState.OPEN)){ %>
				<input type="button" id="buttonClose" onclick="showFeedbacks();" value="Close this help request"/>
				<div id="votes" style="display: none;">
					give a feedback to: <span class='italic'><%=help.getHelper().getName()%> <%=help.getHelper().getSurname()%> </span>
					<% for(int i=1; i<6; i++){
						%><a href="helps?help=<%=help.getId()%>&vote=<%=i%>&state=close" 
						 onMouseOver="changeToFull('<%=i%>');" onMouseOut="backToEmpty('vote<%=i%>');">
						<img src="/SWIMweb/img/emptyStar.png" id="vote<%=i%>" class="rating"/></a> <%
					} %>
				</div>
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
			%><p><span class="text">Closing date: </span><%=help.getClosingDate()%><%
		} %>
	</p><%
	if (help.getVote() > 0) {	
		%><p><span class="text">Feedback: </span><%
		for (int i = 0; i < help.getVote(); i++) {
			%><img src="/SWIMweb/img/fullStar.png" class="rating" /> <%;
		}
	}
	%>
</div>
<div id="chat">

</div>
<script type="text/javascript">
	function showFeedbacks(){
		var helpText = document.getElementById("helpText");
		helpText.parentNode.removeChild(helpText);
		var state = document.getElementById("state");
		state.parentNode.removeChild(state);
		var buttonClose = document.getElementById("buttonClose");
		buttonClose.parentNode.removeChild(buttonClose);
		var votes = document.getElementById("votes");
		votes.style.display = "inline";
	};

	function changeToFull(id){
		var link = null;
		var number = parseInt(id);
		for(var i=1; i <= number; i++){
			link = document.getElementById("vote"+i);
			console.log("id: "+ id +" link: " + link);
			link.src="/SWIMweb/img/fullStar.png";
		};
	};

	function backToEmpty(id){
		var link = null;
		for(var i=1; i < 6; i++){
			link = document.getElementById("vote"+i);
			console.log("id: "+ id +" link: " + link);
			link.src="/SWIMweb/img/emptyStar.png";
		};
	};
</script>