package it.polimi.swim.servlet;

import it.polimi.swim.session.AbilityManagerRemote;

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
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("AbilityManager/remote");
			AbilityManagerRemote abilityManager = (AbilityManagerRemote) ref;

			if (hasNewAbility(req, resp)) {
				String name = req.getParameter("newAbility");
				abilityManager.insertNewAbility(name);
				resp.sendRedirect("/SWIMweb/admin/newAbility");
				return;
			}

			req.setAttribute("abilities", abilityManager.getAbilityList());

			getServletConfig().getServletContext()
					.getRequestDispatcher("/admin/newAbility.jsp")
					.forward(req, resp);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
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
