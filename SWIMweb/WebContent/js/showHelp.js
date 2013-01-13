function showFeedbacks() {
	"use strict";
	var helpText = document.getElementById("helpText");
	helpText.parentNode.removeChild(helpText);
	var state = document.getElementById("state");
	state.parentNode.removeChild(state);
	var buttonClose = document.getElementById("buttonClose");
	buttonClose.parentNode.removeChild(buttonClose);
	var votes = document.getElementById("votes");
	votes.style.display = "inline";
}

function changeToFull(id) {
	"use strict";
	var link = null;
	var number = parseInt(id);
	for ( var i = 1; i <= number; i++) {
		link = document.getElementById("vote" + i);
		link.src = "/SWIMweb/img/fullStar.png";
	}
}

function backToEmpty(id) {
	"use strict";
	var link = null;
	for ( var i = 1; i < 6; i++) {
		link = document.getElementById("vote" + i);
		link.src = "/SWIMweb/img/emptyStar.png";
	}
}

function insertMessage(helpID, name, dateString) {
	"use strict";
	var xmlhttp = new XMLHttpRequest();
	var message = document.getElementById("messageText").value;
	if (message !== "") {
		var url = "/SWIMweb/user/helps?help=" + helpID + "&message=" + message;
		console.log("AJAX REQUEST TO:\n" + url + "\n");
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState === 4) {
				if (xmlhttp.status === 200) {
					console.log("RESPONSE TEXT:\n" + xmlhttp.responseText);
					var response = xmlhttp.responseXML
							.getElementsByTagName("result")[0].childNodes[0].nodeValue;
					if (response === "OK") {
						addMessage(message, name, dateString);
					} else {
						var error = xmlhttp.responseXML
								.getElementsByTagName("error")[0].childNodes[0].nodeValue;
						addError(error);
					}
				} else {
					addError('Problems during the request');
				}
			}
		};
		xmlhttp.open("GET", url, true);
		xmlhttp.send(null);
	} else {
		addError("You must enter a message!");
	}
}

function addError(message) {
	"use strict";
	if (document.getElementById("errorSpan")) {
		var errorSpan = document.getElementById("errorSpan");
		errorSpan.parentNode.removeChild(errorSpan);
	}
	var span = document.createElement("span");
	span.setAttribute("class", "error");
	span.setAttribute("id", "errorSpan");
	span.innerHTML = message;
	var replyForm = document.getElementById('replyForm');
	replyForm.appendChild(span);
}

function addMessage(message, name, dateString) {
	"use strict";
	document.getElementById("messageText").value = "";
	var messageDiv = document.getElementById('messages');
	if (document.getElementById("initialWarning")) {
		var initialWarning = document.getElementById("initialWarning");
		initialWarning.parentNode.removeChild(initialWarning);
	} else {
		var br1 = document.createElement("br");
		messageDiv.appendChild(br1);
	}
	if (document.getElementById("errorSpan")) {
		var errorSpan = document.getElementById("errorSpan");
		errorSpan.parentNode.removeChild(errorSpan);
	}
	var div = document.createElement("div");
	div.setAttribute("class", "left");
	div.setAttribute("id", "sessionUser");
	var internalDiv = document.createElement("div");
	var nameSpan = document.createElement("span");
	nameSpan.setAttribute("class", "text user");
	nameSpan.innerHTML = name + ": ";
	internalDiv.appendChild(nameSpan);
	var messageSpan = document.createElement("span");
	messageSpan.innerHTML = message;
	internalDiv.appendChild(messageSpan);

	div.appendChild(internalDiv);
	var dateSpan = document.createElement("span");
	dateSpan.setAttribute("class", "smaller");
	dateSpan.innerHTML = "&nbsp;&nbsp;" + dateString;
	div.appendChild(dateSpan);
	messageDiv = document.getElementById('messages');
	messageDiv.appendChild(div);
	var br2 = document.createElement("br");
	messageDiv.appendChild(br2);
}

window.onkeyup = function() {
	"use strict";
	// if press return then submit
	var tasto = window.event.keyCode;
	if (tasto === 13) {
		document.getElementById("addReply").click();
	}
};