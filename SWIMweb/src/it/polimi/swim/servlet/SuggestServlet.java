package it.polimi.swim.servlet;

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

import com.sun.crypto.provider.ARCFOURCipher;

public class SuggestServlet extends HttpServlet {

	private static final long serialVersionUID = -2736959968486863204L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, "localhost:1099");
		InitialContext jndiContext;
		try {
			jndiContext = new InitialContext(env);
		Object ref = jndiContext.lookup("AbilityManager/remote");
		AbilityManagerRemote abilityManager = (AbilityManagerRemote) ref;
		
		List<AbilityRequest> abilityRequests = abilityManager.getAbilityRequests();
		if(abilityRequests != null && abilityRequests.size() > 0){
			req.setAttribute("suggests", abilityRequests);
			System.out.println("*** [SuggestServlet] suspended abilities catched under the attribute 'suggests' ***");
		} else {
			System.out.println("*** [SuggestServlet] suspended abilities not found ***");
		}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

}
