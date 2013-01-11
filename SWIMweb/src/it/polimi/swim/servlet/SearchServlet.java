package it.polimi.swim.servlet;

import it.polimi.swim.model.User;
import it.polimi.swim.session.FriendsManagerRemote;
import it.polimi.swim.session.SearchManagerRemote;

import java.io.IOException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.mail.search.SearchException;
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
		request.setAttribute("cities", getCities());
		redirect(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("cities", getCities());
		checkSearchType(request, response);
	}

	private void checkSearchType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			User user = (User) request.getSession().getAttribute("User");
			Set<User> results = new HashSet<User>();
			if (user != null) {
				// extended search
				System.out
						.println("*** [SearchServlet] user logged in: extended search ***");
				Hashtable<String, String> env = new Hashtable<String, String>();
				env.put(Context.INITIAL_CONTEXT_FACTORY,
						"org.jnp.interfaces.NamingContextFactory");
				env.put(Context.PROVIDER_URL, "localhost:1099");
				InitialContext jndiContext = new InitialContext(env);

				Object ref = jndiContext.lookup("FriendsManager/remote");
				FriendsManagerRemote friendsManager = (FriendsManagerRemote) ref;
				List<User> friends = friendsManager.getFriends(user);

				if (isSetOnlyCityPattern(request, response)) {
					// search only by city
					System.out
							.println("*** [SearchServlet] {extended search} (city search) search only by city ***");
					results = searchForCity((String) request
							.getParameter("city"));
				} else if (isSetPersonPattern(request, response)) {
					// search person
					System.out
							.println("*** [SearchServlet] {extended search} person search ***");
					if (areKeywordsSet(request, response)) {
						System.out
								.println("*** [SearchServlet] keywords set ***");
						if (isSetCityPattern(request, response)) {
							// search person and city
							System.out
									.println("*** [SearchServlet] {extended search} (person search) city search ***");
							String keywords = (String) request
									.getParameter("keywords");
							results = searchForPeople(keywords,
									(String) request.getParameter("city"));
						} else {
							// search only person
							System.out
									.println("*** [SearchServlet] {extended search} (person search) search without city ***");
							results = searchForPeople((String) request
									.getParameter("keywords"));
						}
					} else {
						// keyword error
						System.out
								.println("*** [SearchServlet] {extended search} (person search) no keywords ***");
						request.setAttribute("error",
								"you must enter one or more keyword to search!");
					}
				} else if (isSetAbilityPattern(request, response)) {
					// ability search
					System.out
							.println("*** [SearchServlet] {extended search} ability search ***");
					if (areKeywordsSet(request, response)) {
						System.out
								.println("*** [SearchServlet] keywords set ***");
						if (isSetCityPattern(request, response)) {
							// search ability and city
							System.out
									.println("*** [SearchServlet] {extended search} (ability search) city search ***");
							String keywords = (String) request
									.getParameter("keywords");
							results = searchForAbilities(keywords,
									(String) request.getParameter("city"));
						} else {
							// search only ability
							System.out
									.println("*** [SearchServlet] {extended search} (ability search) search without city ***");
							results = searchForAbilities((String) request
									.getParameter("keywords"));
						}
					} else {
						// keyword error
						System.out
								.println("*** [SearchServlet] {extended search} (ability search) no keywords ***");
						request.setAttribute("error",
								"you must enter one or more keyword to search!");
					}
				} else {
					// what to do error
					System.out
							.println("*** [SearchServlet] problem with type of search ***");
					request.setAttribute("error",
							"problem with the search engine");
				}

				Set<User> friendsResults = new HashSet<User>();
				Set<User> otherResults = new HashSet<User>();

				for (User resultUser : results) {
					for (User friend : friends) {
						if (resultUser.equals(friend)) {
							friendsResults.add(resultUser);
						} else if (!resultUser.equals(user)) {
							otherResults.add(resultUser);
						}
					}
				}

				if (friendsResults.isEmpty() && otherResults.isEmpty()
						&& areKeywordsSet(request, response)) {
					request.setAttribute("message", "sorry, no result found");
				} else {
					request.setAttribute("friendsResults", friendsResults);
					request.setAttribute("otherResults", otherResults);
				}

			} else {
				// normal search
				System.out
						.println("*** [SearchServlet] user not logged in: normal search ***");
				if (isSetPersonPattern(request, response)) {
					System.out
							.println("*** [SearchServlet] {normal search} person pattern set ***");
					if (areKeywordsSet(request, response)) {
						System.out
								.println("*** [SearchServlet] keywords set ***");
						String keywords = (String) request
								.getParameter("keywords");
						results = searchForPeople(keywords);

						request.setAttribute("otherResults", results);
					} else {
						// keyword error
						System.out
								.println("*** [SearchServlet] no keywords ***");
						request.setAttribute("error",
								"you must enter one or more keyword to search!");
					}
				} else {
					// what to do error
					System.out
							.println("*** [SearchServlet] problem with type of search ***");
					request.setAttribute("error",
							"problem with the search engine");
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SearchException se) {
			request.setAttribute("message", se.getMessage());
			System.out.println("*** [SearchServlet] " + se.getMessage()
					+ " ***");
		}
		request.setAttribute("searchType", request.getParameter("searchType"));
		request.setAttribute("keywords", request.getParameter("keywords"));
		redirect(request, response);

	}

	private boolean areKeywordsSet(HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getParameter("keywords") != null
				&& !request.getParameter("keywords").equals("")) {
			return true;
		}
		return false;
	}

	private boolean isSetAbilityPattern(HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getParameter("searchType") != null
				&& !request.getParameter("searchType").equals("")
				&& request.getParameter("searchType").equals("ability")) {
			return true;
		}
		return false;
	}

	private boolean isSetPersonPattern(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("searchType") != null
				&& !request.getParameter("searchType").equals("")
				&& request.getParameter("searchType").equals("person")) {
			return true;
		}
		return false;
	}

	private boolean isSetCityPattern(HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getParameter("city") != null
				&& !request.getParameter("city").equals("")
				&& !request.getParameter("city").equals("allCities")) {
			return true;
		}
		return false;
	}

	private boolean isSetOnlyCityPattern(HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getParameter("city") != null
				&& !request.getParameter("city").equals("")) {
			if (request.getParameter("keywords") == null
					|| request.getParameter("keywords").equals("")) {
				return true;
			}
		}
		return false;
	}

	private Set<User> searchForPeople(String keywords) throws SearchException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("SearchManager/remote");
			SearchManagerRemote searchManager = (SearchManagerRemote) ref;

			String[] keys = keywords.split(" ");
			Set<User> results = new HashSet<User>();
			for (int i = 0; i < keys.length; i++) {
				results.addAll(searchManager.searchPerson(keys[i]));
			}
			if (results.isEmpty() || results == null) {
				throw new SearchException("sorry, no result found");
			}
			return results;
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Set<User> searchForAbilities(String keywords)
			throws SearchException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("SearchManager/remote");
			SearchManagerRemote searchManager = (SearchManagerRemote) ref;

			String[] keys = keywords.split(" ");
			Set<User> results = new HashSet<User>();
			for (int i = 0; i < keys.length; i++) {
				results.addAll(searchManager.searchAbility(keys[i]));
			}
			if (results.isEmpty() || results == null) {
				throw new SearchException("sorry, no result found");
			}
			return results;
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Set<User> searchForAbilities(String keywords, String cityPattern)
			throws SearchException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("SearchManager/remote");
			SearchManagerRemote searchManager = (SearchManagerRemote) ref;

			String[] keys = keywords.split(" ");
			Set<User> results = new HashSet<User>();
			for (int i = 0; i < keys.length; i++) {
				results.addAll(searchManager
						.searchAbility(keys[i], cityPattern));
			}
			if (results.isEmpty() || results == null) {
				throw new SearchException("sorry, no result found");
			}
			return results;
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Set<User> searchForPeople(String keywords, String cityPattern)
			throws SearchException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("SearchManager/remote");
			SearchManagerRemote searchManager = (SearchManagerRemote) ref;

			String[] keys = keywords.split(" ");
			Set<User> results = new HashSet<User>();
			for (int i = 0; i < keys.length; i++) {
				results.addAll(searchManager.searchPerson(keys[i], cityPattern));
			}
			if (results.isEmpty() || results == null) {
				throw new SearchException("sorry, no result found");
			}
			return results;
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Set<User> searchForCity(String cityPattern) throws SearchException {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("SearchManager/remote");
			SearchManagerRemote searchManager = (SearchManagerRemote) ref;

			Set<User> results = new HashSet<User>();
			results.addAll(searchManager.searchByCity(cityPattern));

			if (results.isEmpty() || results == null) {
				throw new SearchException("sorry, no result found");
			}
			return results;
		} catch (NamingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<String> getCities() {
		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY,
					"org.jnp.interfaces.NamingContextFactory");
			env.put(Context.PROVIDER_URL, "localhost:1099");
			InitialContext jndiContext = new InitialContext(env);
			Object ref = jndiContext.lookup("SearchManager/remote");
			SearchManagerRemote searchManager = (SearchManagerRemote) ref;

			return searchManager.getCities();

		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void redirect(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		getServletConfig().getServletContext()
				.getRequestDispatcher("/search.jsp").forward(request, response);
	}

}
