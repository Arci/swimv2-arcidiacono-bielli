package it.polimi.swim.servlet;

import it.polimi.swim.enums.RequestState;
import it.polimi.swim.model.AbilityRequest;
import it.polimi.swim.session.AbilityManagerRemote;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
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
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, "localhost:1099");
		InitialContext jndiContext = new InitialContext(env);
		Object ref = jndiContext.lookup("AbilityManager/remote");
		AbilityManagerRemote abilityManager = (AbilityManagerRemote) ref;
		
		List<AbilityRequest> abilityRequests = abilityManager.getAbilityRequests();
		

		if (hasDecided(req, resp)){
			String id =  req.getParameter("ability");
			String state = req.getParameter("decision");
			
			abilityManager.updateAbilityRequestState(id, state);
			
//			if(suspended.equals(RequestState.ACCEPTED)){
//				System.out.println("*** [SuggestServlet] ability '" + suspended.getText() + "' accepted ***");
//			} else {
//				System.out.println("*** [SuggestServlet] ability '" + suspended.getText() + "' refuse ***");
//			}
//			System.out.println("*** [SuggestServlet] reload /admin/suspended.jsp ***");
//			
//			abilityRequests = abilityManager.getAbilityRequests();
			resp.sendRedirect("/SWIMweb/admin/suspended.jsp");
			return;
		}
		
		if(abilityRequests != null && abilityRequests.size() > 0){
			req.setAttribute("suggests", abilityRequests);
			System.out.println("*** [SuggestServlet] suspended abilities catched under the attribute 'suggests' ***");
		} else {
			System.out.println("*** [SuggestServlet] suspended abilities not found ***");
		}
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Not to implement, redirect
		System.out.println("*** [SuggestServlet] doPost, forwarding to suggest ***");
		getServletConfig().getServletContext()
						.getRequestDispatcher("/user/suggest")
						.forward(request, response);
	}
	
	private boolean hasDecided(HttpServletRequest req, HttpServletResponse resp){
		if (req.getParameter("ability") != null && !req.getParameter("ability").equals("")){
			 return true;
		} else return false;
	}

}
