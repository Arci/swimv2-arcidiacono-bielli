package it.polimi.swim.servlet;

import it.polimi.swim.servlet.exception.RegistrationParametersException;
import it.polimi.swim.session.ProfileManagerRemote;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegistrationServlet extends HttpServlet {

	private static final long serialVersionUID = 5019136891262592380L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		try {
			InitialContext jndiContext = new InitialContext();
			Object ref = jndiContext.lookup("ProfileManager/remote");

			ProfileManagerRemote profileManager = (ProfileManagerRemote) ref;

			this.checkParameters(request, response, profileManager);
			if (!request.getParameter("password").equals(
					request.getParameter("checkPassword"))) {
				request.setAttribute("passwordError", new String(
						"passwordError"));

				getServletConfig().getServletContext()
						.getRequestDispatcher("/registration.jsp")
						.forward(request, response);

				return;
			}

			Map<String, Object> parameters = new Hashtable<String, Object>();

			parameters.put("name", request.getParameter("name"));
			parameters.put("surname", request.getParameter("surname"));
			parameters.put("username", request.getParameter("username"));
			parameters.put("email", request.getParameter("email"));
			parameters.put("password", request.getParameter("password"));

			profileManager.insertNewUser(parameters);

			getServletConfig().getServletContext()
					.getRequestDispatcher("/accessManager")
					.forward(request, response);

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (RegistrationParametersException e) {
			request.setAttribute("missingParameters", e.getMissingParameters());
			request.setAttribute("duplicatedParameters",
					e.getDuplicatedParameters());
			getServletConfig().getServletContext()
					.getRequestDispatcher("/registration.jsp")
					.forward(request, response);
		}
	}

	private void checkParameters(HttpServletRequest request,
			HttpServletResponse response, ProfileManagerRemote profileManager)
			throws RegistrationParametersException {

		RegistrationParametersException rpe = new RegistrationParametersException();

		if (request.getParameter("name") == null
				|| request.getParameter("name") == "") {
			rpe.addMissingParameter("name");
		}

		if (request.getParameter("username") == null
				|| request.getParameter("username") == "") {
			rpe.addMissingParameter("username");
		} else if (!profileManager.isUsernameUnique(request
				.getParameter("username"))) {
			rpe.addDuplicatedParameter("username");
		}

		if (request.getParameter("email") == null
				|| request.getParameter("email") == "") {
			rpe.addMissingParameter("email");
		} else if (!profileManager.isEmailUnique(request.getParameter("email"))) {
			rpe.addDuplicatedParameter("email");
		}

		if (request.getParameter("password") == null
				|| request.getParameter("password") == "") {
			rpe.addMissingParameter("password");
		}

		if (request.getParameter("checkPassword") == null
				|| request.getParameter("checkPassword") == "") {
			rpe.addMissingParameter("checkPassword");
		}

		if (rpe.hasMissingParameters()) {
			throw rpe;
		}

		if (rpe.hasDuplicatedParameters()) {
			throw rpe;
		}
	}
}
