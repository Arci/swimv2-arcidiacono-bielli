function removeAbility(abilityName) {
	"use strict";
	if (document.getElementById("spanError")) {
		var spanError = document.getElementById("spanError");
		spanError.parentNode.removeChild(spanError);
	}
	deleteLiElement(abilityName);
	var abilityUl = document.getElementById("abilityUl");
	var childs = abilityUl.getElementsByTagName("li");
	if (childs.length === 0) {
		addSpanWarning(abilityUl, "you don't have any ability yet!");
	}
	addHidden('remove', abilityName);
	removeHidden('add', abilityName);
}

function addAbility(username) {
	"use strict";
	var abilityList = document.getElementById("abilityList");
	var abilityName = abilityList.options[abilityList.selectedIndex].value;
	if (document.getElementById("spanError")) {
		var spanError = document.getElementById("spanError");
		spanError.parentNode.removeChild(spanError);
	}
	if (!document.getElementById(abilityName)) {
		addLiElement(abilityName, username);
	} else {
		addSpanError("you already have this ability!");
	}
	if (document.getElementById("spanWarning")) {
		var spanWarning = document.getElementById("spanWarning");
		spanWarning.parentNode.removeChild(spanWarning);
	}
	addHidden('add', abilityName);
	removeHidden('remove', abilityName);
}

function addLiElement(abilityName, username) {
	"use strict";
	var li = document.createElement("li");
	li.setAttribute("id", abilityName);
	li.innerHTML = abilityName + " ";
	var input = document.createElement("input");
	input.setAttribute("type", "button");
	input.setAttribute("id", abilityName);
	input.setAttribute("onclick", "removeAbility('" + abilityName + "', '"
			+ username + "');");
	input.setAttribute("value", "Delete");
	li.appendChild(input);
	var abilityUl = document.getElementById("abilityUl");
	abilityUl.appendChild(li);
}

function deleteLiElement(abilityName) {
	"use strict";
	var abilityLi = document.getElementById(abilityName);
	abilityLi.parentNode.removeChild(abilityLi);
}

function addSpanError(message) {
	"use strict";
	var span = document.createElement("span");
	span.setAttribute("class", "error");
	span.setAttribute("id", "spanError");
	span.innerHTML = message;
	var form = document.getElementById("modifyAbilities");
	form.appendChild(span);
}

function addSpanWarning(ul, message) {
	"use strict";
	var span = document.createElement("span");
	span.setAttribute("class", "message");
	span.setAttribute("id", "spanWarning");
	span.innerHTML = message;
	ul.appendChild(span);
}

function addHidden(action, abilityName) {
	"use strict";
	var elements = document.getElementsByTagName("input");
	var exists = false;
	for ( var i = 0; i < elements.lenght; i++) {
		if (elements[i].type === "hidden" && elements[i].name === action
				&& elements[i].value === abilityName) {
			exists = true;
		}
	}
	if (!exists) {
		var hidden = document.createElement("input");
		hidden.setAttribute("type", "hidden");
		hidden.setAttribute("name", action);
		hidden.setAttribute("value", abilityName);
		var form = document.getElementById("modifyAbilities");
		form.appendChild(hidden);
	}
}

function removeHidden(action, abilityName) {
	"use strict";
	var elements = document.getElementsByTagName("input");
	var exists = false;
	var hidden = null;
	for ( var i = 0; i < elements.length; i++) {
		if (elements[i].type === "hidden" && elements[i].name === action
				&& elements[i].value === abilityName) {
			exists = true;
			hidden = elements[i];
		}
	}
	if (exists) {
		var form = document.getElementById("modifyAbilities");
		form.removeChild(hidden);
	}
}

function validEmail(email) {
	return /^[_a-z0-9+-]+(\.[_a-z0-9+-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)+$/
			.test(email);
}

function checkMail() {
	var email = document.getElementById("email").value;
	if (validEmail(email)) {
		console.log("email correct.");
		var image = document.createElement("img");
		image.setAttribute("src", "../img/vi.png");
		image.setAttribute("id", "images");
		var container = document.getElementById("emailContainer");
		container.appendChild(image);
	} else {
		console.log("email incorrect.");
		var image = document.createElement("img");
		image.setAttribute("src", "../img/ics.png");
		image.setAttribute("id", "images");
		var container = document.getElementById("emailContainer");
		container.appendChild(image);
	}
}
