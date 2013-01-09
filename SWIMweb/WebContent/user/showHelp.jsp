<%@page import="it.polimi.swim.enums.HelpState"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException"%>

<div id="showHelp">
	<%
		HelpRequest help = (HelpRequest) request.getAttribute("helpToShow");
	%>
	<p>
		<span class="text ">State: </span>
		<% if(help.getState().equals(HelpState.PENDING)){ %>
			<span class="capital blue"><%=help.getState().toString()%></span>
		<% } else if(help.getState().equals(HelpState.CLOSED)){ %>
			<span class="capital red"><%=help.getState().toString()%></span>
		<% } else if(help.getState().equals(HelpState.OPEN)){ %>
			<span class="capital green"><%=help.getState().toString()%></span>
		<% } %>
	</p>
	<p><span class="text">Ability involved: </span><%=help.getAbility().getName()%></p>
	<p><span class="text">Helper user: </span>
	<%=help.getHelper().getName()%> <%=help.getHelper().getSurname()%> 
	</p>
	<p><span class="text">Applicant user: </span>
	<%=help.getUser().getName()%> <%=help.getUser().getSurname()%> 
	</p>
	<p><span class="text">Opening date: </span><%=help.getOpeningDate()%></p>
	<% if(help.getClosingDate() != null){
		%><p><span class="text">Closing date: </span><%=help.getClosingDate()%></p><%
	} %>
	<% if(help.getVote() > 0){
		%>	<p><span class="text">Feedback: </span><%=help.getVote()%></p><%
	} %>
</div>