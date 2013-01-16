function validEmail(email) {
	return /^[_a-z0-9+-]+(\.[_a-z0-9+-]+)*@[a-z0-9-]+(\.[a-z0-9-]+)+$/
			.test(email);
}

function checkMail() {
	var email = document.getElementById("email").value;
	if (validEmail(email)) {
		addImage("http://localhost:8080/SWIMweb/img/vi.png");
	} else {
		addImage("http://localhost:8080/SWIMweb/img/ics.png");
	}
}

function addImage(imagePath) {
	var container = document.getElementById("emailContainer");
	if (container.getElementsByTagName("img")[0]) {
		container.removeChild(container.getElementsByTagName("img")[0]);
	}
	var image = document.createElement("img");
	image.setAttribute("src", imagePath);
	image.setAttribute("class", "images");
	container.appendChild(image);
}
