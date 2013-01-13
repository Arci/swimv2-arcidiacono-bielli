package it.polimi.swim.servlet;

import it.polimi.swim.enums.UserType;
import it.polimi.swim.model.User;
import it.polimi.swim.session.HomeControlRemote;

import java.io.IOException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AccessServlet extends HttpServlet {

	private static final long serialVersionUID = -7715945730586850060L;

	public AccessServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getSession().getAttribute("User") != null) {
			System.out.println("*** [AccessServlet] logout ***");
			User user = (User) request.getSession().getAttribute("User");
			System.out.print("*** [AccessServlet] session was of '"
					+ user.getUsername() + "' user ***");
			request.getSession().invalidate();
			response.sendRedirect("/SWIMweb");
		} else {
			System.out.println("*** [AccessServlet] logout anomalo ***");
			request.getSession().invalidate();
			response.sendRedirect("/SWIMweb");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("HomeControl/remote");
			HomeControlRemote homeControl = (HomeControlRemote) ref;
			if (request.getParameter("username") != null
					&& request.getParameter("username") != ""
					&& request.getParameter("password") != null
					&& request.getParameter("password") != "") {
				User user = homeControl.checkLogin(
						request.getParameter("username"),
						request.getParameter("password"));
				if (user == null) {
					System.out
							.println("*** [AccessServlet] problem founding user ***");
					request.getSession().invalidate();
					request.setAttribute("headerError",
							"username or password incorrect");
					getServletConfig().getServletContext()
							.getRequestDispatcher("/")
							.forward(request, response);
				} else {
					request.getSession().setAttribute("User", user);
					if (user.getType().equals(UserType.NORMAL)) {
						System.out
								.println("*** [AccessServlet] redirect NORMAL user ***");
						response.sendRedirect("/SWIMweb/user/profile");
					} else if (user.getType().equals(UserType.ADMIN)) {
						System.out
								.println("*** [AccessServlet] redirect ADMIN user ***");
						response.sendRedirect("/SWIMweb/admin/suspended");
					}
				}
			} else {
				System.out
						.println("*** [AccessServlet] username/password null or empty ***");
				request.getSession().invalidate();
				request.setAttribute("headerError",
						"username or password incorrect");
				getServletConfig().getServletContext()
						.getRequestDispatcher("/").forward(request, response);
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
