package it.polimi.swim.servlet;

import it.polimi.swim.enums.RequestState;
import it.polimi.swim.model.Friendship;
import it.polimi.swim.model.User;
import it.polimi.swim.session.FriendsManagerRemote;

import java.io.IOException;
import java.io.PrintWriter;
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
		if (haveToManageFriendship(request, response)) {
			System.out.println("*** [FriendsServlet] manage friendship ***");
			manageFriendship(request, response);
			getUserInformation(request, response);
		} else if (haveToAddFriendship(request, response)) {
			System.out.println("*** [FriendsServlet] add friendship ***");
			addFriendship(request, response);
		} else {
			getUserInformation(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		getUserInformation(request, response);
		// Not to implement, redirect
		System.out
				.println("*** [FriendsServlet] doGet, forwarding to friends ***");
		getServletConfig().getServletContext()
				.getRequestDispatcher("/user/friends")
				.forward(request, response);
	}

	private boolean haveToManageFriendship(HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getParameter("friendship") != null
				&& request.getParameter("friendship") != ""
				&& request.getParameter("state") != null
				&& request.getParameter("state") != "") {
			return true;
		}
		return false;
	}

	private void manageFriendship(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("FriendsManager/remote");
			FriendsManagerRemote friendsManager = (FriendsManagerRemote) ref;

			if (request.getParameter("state").equals("accept")) {
				friendsManager.updateFriendship(RequestState.ACCEPTED,
						Integer.parseInt(request.getParameter("friendship")));
			} else if (request.getParameter("state").equals("reject")) {
				friendsManager.updateFriendship(RequestState.REJECTED,
						Integer.parseInt(request.getParameter("friendship")));
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}
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
			request.setAttribute("friends", friends);
			List<Friendship> pendings = friendsManager.getPending(user);
			request.setAttribute("pendings", pendings);
			List<Friendship> requests = friendsManager.getRequest(user);
			request.setAttribute("requests", requests);

			getServletConfig().getServletContext()
					.getRequestDispatcher("/user/friends.jsp")
					.forward(request, response);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private boolean haveToAddFriendship(HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getParameter("newFriend") != null
				&& request.getParameter("newFriend") != "") {
			return true;
		}
		return false;
	}

	private void addFriendship(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<response>");
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("FriendsManager/remote");
			FriendsManagerRemote friendsManager = (FriendsManagerRemote) ref;

			friendsManager.addRequest(
					(User) request.getSession().getAttribute("User"),
					request.getParameter("newFriend"));

			out.println("<value>OK</value>");
		} catch (Exception e) {
			out.println("<value>KO</value>");
		}
		out.println("</response>");
	}

}
