package it.polimi.swim.servlet;

import it.polimi.swim.model.User;
import it.polimi.swim.session.SearchManagerRemote;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet {

	private static final long serialVersionUID = -6358743864537646180L;

	public SearchServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out
				.println("*** [SearchServlet] doGet, forwarding to search.jsp ***");
		redirect(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		checkSearchType(request, response);
	}

	private void checkSearchType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (issetToSearch(request, response)) {
			if (issetKeywords(request, response)) {
				if (isPersonToSearch(request, response)) {
					System.out
							.println("*** [SearchServlet] searching for people ***");
					searchForPeople(request, response);
				} else if (isAbilityToSearch(request, response)) {
					System.out
							.println("*** [SearchServlet] searching for abilities ***");
					searchForAbilities(request, response);
				} else {
					System.out
							.println("*** [SearchServlet] radio button hacked, forwarding to search.jsp ***");
					redirect(request, response);
				}
			} else {
				System.out
						.println("*** [SearchServlet] no keyword, forwarding to search.jsp ***");
				request.setAttribute("error", "keyword can not be empty");
				redirect(request, response);
			}
		} else {
			System.out
					.println("*** [SearchServlet] radio button not selected, forwarding to search.jsp ***");
			redirect(request, response);
		}
	}

	private void searchForPeople(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("SearchManager/remote");
			SearchManagerRemote searchManager = (SearchManagerRemote) ref;

			String keywords = request.getParameter("keywords");
			String[] key = keywords.split(" ");
			Set<User> results = new HashSet<User>();
			for (int i = 0; i < key.length; i++) {
				results.addAll(searchManager.searchPerson(key[i]));
			}
			if (results.isEmpty() || results == null) {
				System.out.println("*** [SearchServlet] no users result ***");
				request.setAttribute("message", "sorry no results found");
				redirect(request, response);
			} else {
				System.out
						.println("*** [SearchServlet] setting users results, forwarding to search.jsp ***");
				request.setAttribute("results", results);
				redirect(request, response);
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private void searchForAbilities(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("SearchManager/remote");
			SearchManagerRemote searchManager = (SearchManagerRemote) ref;

			String keywords = request.getParameter("keywords");
			String[] key = keywords.split(" ");
			Set<User> results = new HashSet<User>();
			for (int i = 0; i < key.length; i++) {
				results.addAll(searchManager.searchAbility(key[i]));
			}
			if (results.isEmpty() || results == null) {
				System.out
						.println("*** [SearchServlet] no result for ability ***");
				request.setAttribute("message", "sorry no results found");
				redirect(request, response);
			} else {
				System.out
						.println("*** [SearchServlet] setting ability results, forwarding to search.jsp ***");
				request.setAttribute("results", results);
				redirect(request, response);
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	private boolean issetToSearch(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("toSearch") != null) {
			return true;
		}
		return false;
	}

	private boolean issetKeywords(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("keywords") != null
				&& request.getParameter("keywords") != "") {
			return true;
		}
		return false;
	}

	private boolean isPersonToSearch(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("toSearch").equals("persons")) {
			return true;
		}
		return false;
	}

	private boolean isAbilityToSearch(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("toSearch").equals("abilities")) {
			return true;
		}
		return false;
	}

	private void redirect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		getServletConfig().getServletContext()
				.getRequestDispatcher("/user/search.jsp")
				.forward(request, response);
	}

}
