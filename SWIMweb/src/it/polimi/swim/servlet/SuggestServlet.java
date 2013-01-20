package it.polimi.swim.servlet;

import it.polimi.swim.model.AbilityRequest;
import it.polimi.swim.session.AbilityManagerRemote;

import java.io.IOException;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SuggestServlet extends HttpServlet {

	private static final long serialVersionUID = -2736959968486863204L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("AbilityManager/remote");
			AbilityManagerRemote abilityManager = (AbilityManagerRemote) ref;

			List<AbilityRequest> abilityRequests = abilityManager
					.getAbilityRequests();

			if (hasDecided(req, resp)) {
				String id = req.getParameter("ability");
				String state = req.getParameter("decision");

				abilityManager.updateAbilityRequestState(id, state);
				resp.sendRedirect("/SWIMweb/admin/suspended");
				return;
			}

			if (abilityRequests != null && abilityRequests.size() > 0) {

				req.setAttribute("suggests", abilityRequests);
				System.out
						.println("*** [SuggestServlet] suspended abilities catched under the attribute 'suggests' ***");
			} else {
				System.out
						.println("*** [SuggestServlet] suspended abilities not found ***");
			}

			getServletConfig().getServletContext()
					.getRequestDispatcher("/admin/suspended.jsp")
					.forward(req, resp);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// Not to implement, redirect
		System.out
				.println("*** [SuggestServlet] doPost, forwarding to suggest ***");
		getServletConfig().getServletContext()
				.getRequestDispatcher("/user/suggest")
				.forward(request, response);
	}

	private boolean hasDecided(HttpServletRequest req, HttpServletResponse resp) {
		if (req.getParameter("ability") != null
				&& !req.getParameter("ability").equals("")) {
			return true;
		} else {
			return false;
		}
	}

}
