<%@page import="it.polimi.swim.enums.HelpState"%>
<%@page
	import="java.util.*,javax.naming.Context,javax.naming.InitialContext,it.polimi.swim.model.*,it.polimi.swim.session.*,javax.naming.NamingException,java.text.SimpleDateFormat"%>

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
					Give a feedback to <span class='italic'><%=help.getHelper().getName()%> <%=help.getHelper().getSurname()%> </span>
					<% for(int i=1; i<6; i++){
						%><a href="helps?help=<%=help.getId()%>&vote=<%=i%>&state=close" 
						 onMouseOver="changeToFull('<%=i%>');" onMouseOut="backToEmpty('vote<%=i%>');">
						<img src="/SWIMweb/img/emptyStar.png" id="vote<%=i%>" class="images"/></a> <%
					} %>
				</div>
		<% } %>
	</div>
	<p><span class="text">Applicant user: </span>
		<span class="user"><%=help.getUser().getName()%> <%=help.getUser().getSurname()%></span> 
		<% if(!sessionUser.equals(help.getUser())) { %>
			<a href="profile?username=<%=help.getUser().getUsername()%>&from=helps">Visualizza Profilo</a>
		<% } %>
	</p>
	<p><span class="text">Helper user: </span>
		<span class="user"><%=help.getHelper().getName()%> <%=help.getHelper().getSurname()%></span> 
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
			%><img src="/SWIMweb/img/fullStar.png" class="images" /> <%;
		}
	}
	%>
</div>
<% if(!help.getState().equals(HelpState.PENDING)){%>
	<hr><div id="chat">
		<div id="messageList">
		<% 
		@SuppressWarnings("unchecked")
		List<Message> messages = (List<Message>) request.getAttribute("messages");
		SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM 'at' h:mm");
		if(messages == null || messages.isEmpty()){
			%><span id="initialWarning" class="message">There aren't any message yet!</span><%
		}else{
			Iterator<Message> itr = messages.iterator();
			Message message = null;
			while(itr.hasNext()){
				message = itr.next();
				if(message.getUser().equals(sessionUser)){
					%><div id="sessionUser" class="left">
						<div>
							<span class="text user"><%=sessionUser.getName() %>: </span>
							<span><%=message.getText()%></span>
						</div>
						<span class="smaller left">&nbsp;&nbsp;<%=new StringBuilder(dateFormat.format(message.getTimestamp()))%></span>
					</div><%
				} else {
					%><div id="otherUser" class="right">
						<div>
							<span class="text user"><%=help.getHelper().getName() %>: </span>
							<span><%=message.getText()%></span>
						</div>
						<span class="smaller right"><%=new StringBuilder(dateFormat.format(message.getTimestamp()))%>&nbsp;&nbsp;</span>
					</div><%
				} 
				if(itr.hasNext()){
					%><br/><br/><%
				}else{
					%><br/><%
				}
			}
		} %></div><div style="clear: both;"></div>
		<% if(help.getState().equals(HelpState.OPEN)){ %>
		<div id="replyForm">
			<p><input type="text" id="messageText" name="messageText" value="" />
			<input id="addReply" type="button" name="Submit" value="reply" onclick="insertMessage('<%=help.getId()%>','<%= sessionUser.getName()%>','<%=new StringBuilder(dateFormat.format(new Date().getTime()))%>');"/></p>
		</div>
		<% } %>
	</div>
<% } %>