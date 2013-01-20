package it.polimi.swim.servlet;

import it.polimi.swim.session.AbilityManagerRemote;
import it.polimi.swim.session.exceptions.AbilityException;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AbilityServlet extends HttpServlet {

	private static final long serialVersionUID = 2790612978510800588L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("AbilityManager/remote");
			AbilityManagerRemote abilityManager = (AbilityManagerRemote) ref;

			if (hasNewAbility(request, response)) {
				if (request.getParameter("newAbility").length() > 0) {
					try {
						abilityManager.getAbilityByName(request
								.getParameter("newAbility"));
						request.setAttribute("error",
								"you have already inserted this ability.");

					} catch (AbilityException e) {
						String name = request.getParameter("newAbility");
						abilityManager.insertNewAbility(name);
						request.setAttribute("message",
								"ability inserted successfully.");
					}
				}
			}

			request.setAttribute("abilities", abilityManager.getAbilityList());

			getServletConfig().getServletContext()
					.getRequestDispatcher("/admin/newAbility.jsp")
					.forward(request, response);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

	private boolean hasNewAbility(HttpServletRequest req,
			HttpServletResponse resp) {
		if (req.getParameter("newAbility") != null) {
			return true;
		} else {
			return false;
		}
	}
}
