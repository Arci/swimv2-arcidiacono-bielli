function addRequest(type, username, ability) {
	xmlhttp = new XMLHttpRequest();
	var url = null;
	var messageSpan = null;
	var okMessage = null;
	if (type == "help") {
		var helperAbilities = document.getElementById("helperAbilities");
		var ability = helperAbilities.options[helperAbilities.selectedIndex].value;
		url = "/SWIMweb/user/helps?newHelper=" + username + "&ability="
				+ ability;
		messageSpan = "helpMessage";
		okMessage = 'Help request for \'' + ability + '\' added successfully';
	} else if (type == "friend") {
		url = "/SWIMweb/user/friends?newFriend=" + username;
		messageSpan = "friendMessage";
		okMessage = 'Friendship request added successfully';
	}
	console.log("AJAX REQUEST TO:\n" + url + "\n");
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200) {
				console.log("RESPONSE TEXT:\n" + xmlhttp.responseText);
				console.log("RESPONSE XML:\n" + xmlhttp.responseXML);
				var response = xmlhttp.responseXML
						.getElementsByTagName("result")[0].childNodes[0].nodeValue;
				console.log("VALUE:\n" + response);
				if (response == "OK") {
					manageMessage('message', messageSpan, okMessage);
					manageButtonDiv(type);
				} else {
					var error = xmlhttp.responseXML
							.getElementsByTagName("error")[0].childNodes[0].nodeValue;
					manageMessage('error', messageSpan, error);
				}
			} else {
				manageMessage('error', errorDiv, 'Problems during the request');
			}
		}
	};
	xmlhttp.open("GET", url, true);
	xmlhttp.send(null);
};

function manageButtonDiv(type) {
	var button = document.getElementById(type);
	if (type != "help") {
		button.parentNode.removeChild(button);
	}
};

function manageMessage(clazz, id, message) {
	if (document.getElementById(id)) {
		var buttonsDiv = document.getElementById('buttons');
		buttonsDiv.removeChild(document.getElementById(id));
	}
	var span = document.createElement("span");
	span.setAttribute("class", clazz);
	span.setAttribute("id", id);
	span.innerHTML = message;
	var buttonsDiv = document.getElementById('buttons');
	buttonsDiv.appendChild(span);
};
