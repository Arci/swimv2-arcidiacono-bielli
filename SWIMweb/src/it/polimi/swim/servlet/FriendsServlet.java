package it.polimi.swim.servlet;

import it.polimi.swim.model.Friendship;
import it.polimi.swim.model.User;
import it.polimi.swim.session.FriendsManagerRemote;

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

public class FriendsServlet extends HttpServlet {

	private static final long serialVersionUID = -4806101806094670071L;

	public FriendsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		getUserInformation(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		getUserInformation(request, response);
	}

	private void getUserInformation(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("FriendsManager/remote");
			FriendsManagerRemote friendsManager = (FriendsManagerRemote) ref;

			User user = (User) request.getSession().getAttribute("User");
			List<User> friends = friendsManager.getFriends(user);
			System.out.println("*** [FriendshipServlet] friends list ok ***");
			request.setAttribute("friends", friends);
			List<Friendship> pendings = friendsManager.getPending(user);
			System.out.println("*** [FriendshipServlet] pendings list ok ***");
			request.setAttribute("pendings", pendings);
			List<Friendship> requests = friendsManager.getRequest(user);
			System.out.println("*** [FriendshipServlet] requests list ok ***");
			request.setAttribute("requests", requests);

			getServletConfig().getServletContext()
					.getRequestDispatcher("/user/friends.jsp")
					.forward(request, response);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
