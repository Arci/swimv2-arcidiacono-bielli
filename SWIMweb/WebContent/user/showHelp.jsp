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
			%><img src="/SWIMweb/img/fullStar.png" class="rating" /> <%;
		}
	}
	%>
</div>
<% if(!help.getState().equals(HelpState.PENDING)){%>
	<hr><div id="chat">
		<div id="messages">
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

	function insertMessage(helpID, name, dateString) {
		xmlhttp = new XMLHttpRequest();
		var message = 	document.getElementById("messageText").value;
		if(message != ""){
			var url =  "/SWIMweb/user/helps?help=" + helpID + "&message="+message;
			console.log("AJAX REQUEST TO:\n" + url+ "\n");
			xmlhttp.onreadystatechange = function () {
			    if (xmlhttp.readyState == 4) {
			        if( xmlhttp.status == 200 ){ 
			        	console.log("RESPONSE TEXT:\n"+ xmlhttp.responseText);
				        console.log("RESPONSE XML:\n"+ xmlhttp.responseXML);
				        var response = xmlhttp.responseXML.getElementsByTagName("result")[0].childNodes[0].nodeValue;
				        console.log("VALUE:\n"+ response);
				        if(response == "OK"){
				        	addMessage(message, name, dateString);
				        } else {
							var error = xmlhttp.responseXML.getElementsByTagName("error")[0].childNodes[0].nodeValue;
							addError(error);
						};
			        } else {
			        	addError('Problems during the request');
			     	};
			    };
			};
			xmlhttp.open("GET", url, true);
			xmlhttp.send(null);
		}else{
			addError("You must enter a message!");
		};
	};

	function addError(message){
		if(document.getElementById("errorSpan")){
			var errorSpan = document.getElementById("errorSpan");
			errorSpan.parentNode.removeChild(errorSpan);
		}
		var span = document.createElement("span");
		span.setAttribute("class","error");
		span.setAttribute("id","errorSpan");
		span.innerHTML = message;
        var replyForm = document.getElementById('replyForm'); 
        replyForm.appendChild(span);
	};

	function addMessage(message, name, dateString){
		document.getElementById("messageText").value = "";
		var messageDiv = document.getElementById('messages'); 
		if(document.getElementById("initialWarning")){
			var initialWarning = document.getElementById("initialWarning");
			initialWarning.parentNode.removeChild(initialWarning);
		}else{
			var br1 = document.createElement("br");
		    messageDiv.appendChild(br1);
		}
		if(document.getElementById("errorSpan")){
			var errorSpan = document.getElementById("errorSpan");
			errorSpan.parentNode.removeChild(errorSpan);
		}
		var div = document.createElement("div");
		div.setAttribute("class","left");
		div.setAttribute("id","sessionUser");
		var internalDiv = document.createElement("div");
		var nameSpan = document.createElement("span");
		nameSpan.setAttribute("class","text user");
		nameSpan.innerHTML = name + ": ";
		internalDiv.appendChild(nameSpan);
		var messageSpan = document.createElement("span");
		messageSpan.innerHTML = message;
		internalDiv.appendChild(messageSpan);
		div.appendChild(internalDiv);
		var dateSpan = document.createElement("span");
		dateSpan.setAttribute("class","smaller");
		dateSpan.innerHTML = "&nbsp;&nbsp;" + dateString;
		div.appendChild(dateSpan);
        var messageDiv = document.getElementById('messages'); 
        messageDiv.appendChild(div);
        var br2 = document.createElement("br");
        messageDiv.appendChild(br2);
	};

	window.onkeyup=function(){
		//if press return then submit
		var tasto = window.event.keyCode;
		if (tasto == 13) {
			document.getElementById("addReply").click();
		}
	};
</script>