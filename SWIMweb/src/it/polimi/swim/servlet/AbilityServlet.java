package it.polimi.swim.servlet;

import it.polimi.swim.session.AbilityManagerRemote;

import java.io.IOException;
import java.util.Hashtable;

import javax.naming.Context;
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
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
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
		// TODO Auto-generated method stub
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
