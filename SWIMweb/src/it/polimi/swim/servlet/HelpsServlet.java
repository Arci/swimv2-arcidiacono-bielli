package it.polimi.swim.servlet;

import it.polimi.swim.enums.HelpState;
import it.polimi.swim.model.HelpRequest;
import it.polimi.swim.model.Message;
import it.polimi.swim.model.User;
import it.polimi.swim.session.HelpsManagerRemote;
import it.polimi.swim.session.exceptions.AbilityException;
import it.polimi.swim.session.exceptions.HelpException;
import it.polimi.swim.session.exceptions.UserException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HelpsServlet extends HttpServlet {

	private static final long serialVersionUID = 6322246263448006617L;

	public HelpsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (haveToManageHelp(request, response)) {
			System.out.println("*** [HelpsServlet] manage help ***");
			manageHelp(request, response);
		} else if (haveToAddHelp(request, response)) {
			System.out.println("*** [HelpsServlet] add help ***");
			addHelp(request, response);
		} else if (haveToShowHelp(request, response)) {
			System.out.println("*** [HelpsServlet] show help ***");
			showHelp(request, response);
		} else if (haveToAddMessage(request, response)) {
			System.out.println("*** [HelpsServlet] add message ***");
			addMessage(request, response);
		} else {
			getHelpsInformation(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		getHelpsInformation(request, response);
	}

	private boolean haveToManageHelp(HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getParameter("help") != null
				&& request.getParameter("help") != ""
				&& request.getParameter("state") != null
				&& request.getParameter("state") != "") {
			return true;
		}
		return false;
	}

	private void manageHelp(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("HelpsManager/remote");
			HelpsManagerRemote helpsManager = (HelpsManagerRemote) ref;

			if (request.getParameter("state").equals("accept")) {
				helpsManager.updateHelpRequest(HelpState.OPEN,
						Integer.parseInt(request.getParameter("help")));
			} else if (request.getParameter("state").equals("reject")) {
				helpsManager.updateHelpRequest(HelpState.REJECTED,
						Integer.parseInt(request.getParameter("help")));
			} else if (request.getParameter("state").equals("close")
					&& Integer.parseInt(request.getParameter("vote")) > 0
					&& Integer.parseInt(request.getParameter("vote")) < 6) {
				helpsManager.closeHelpRequest(HelpState.CLOSED,
						Integer.parseInt(request.getParameter("help")),
						Integer.parseInt(request.getParameter("vote")));
			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		getHelpsInformation(request, response);
	}

	private boolean haveToAddHelp(HttpServletRequest request,
			HttpServletResponse response) {
		if (request.getParameter("newHelper") != null
				&& request.getParameter("newHelper") != ""
				&& request.getParameter("ability") != null
				&& request.getParameter("ability") != "") {
			return true;
		}
		return false;
	}

	private void addHelp(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<response>");
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("HelpsManager/remote");
			HelpsManagerRemote helpsManager = (HelpsManagerRemote) ref;

			helpsManager.addRequest(
					(User) request.getSession().getAttribute("User"),
					request.getParameter("newHelper"),
					request.getParameter("ability"), new Date());

			out.println("<result>OK</result>");
		} catch (NamingException e) {
			out.println("<result>KO</result>");
			out.println("<error>can't reach server</error>");
		} catch (UserException ue) {
			out.println("<result>KO</result>");
			out.println("<error>" + ue.getMessage() + "</error>");
		} catch (HelpException he) {
			out.println("<result>KO</result>");
			out.println("<error>" + he.getMessage() + "</error>");
		} catch (AbilityException ae) {
			out.println("<result>KO</result>");
			out.println("<error>" + ae.getMessage() + "</error>");
		}
		out.println("</response>");
	}

	private boolean haveToShowHelp(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("show") != null
				&& request.getParameter("show") != "") {
			return true;
		}
		return false;
	}

	private void showHelp(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("HelpsManager/remote");
			HelpsManagerRemote helpsManager = (HelpsManagerRemote) ref;

			HelpRequest help = helpsManager.loadRequest(Integer
					.parseInt(request.getParameter("show")));

			request.setAttribute("helpToShow", help);

			List<Message> messages = helpsManager.getMessages(help.getId());

			request.setAttribute("messages", messages);

		} catch (NamingException e) {
			e.printStackTrace();
		} catch (HelpException he) {
			request.setAttribute("error", he.getMessage());
		}
		getServletConfig().getServletContext()
				.getRequestDispatcher("/user/helps.jsp")
				.forward(request, response);

	}

	private void addMessage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		out.println("<response>");
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("HelpsManager/remote");
			HelpsManagerRemote helpsManager = (HelpsManagerRemote) ref;

			helpsManager.addMessage(
					(User) request.getSession().getAttribute("User"),
					Integer.parseInt(request.getParameter("help")),
					request.getParameter("message"));

			out.println("<result>OK</result>");
		} catch (NamingException e) {
			out.println("<result>KO</result>");
			out.println("<error>can't reach server</error>");
		} catch (HelpException he) {
			out.println("<result>KO</result>");
			out.println("<error>" + he.getMessage() + "</error>");
		}
		out.println("</response>");
	}

	private boolean haveToAddMessage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("help") != null
				&& request.getParameter("help") != ""
				&& request.getParameter("message") != ""
				&& request.getParameter("message") != "") {
			return true;
		}
		return false;
	}

	private void getHelpsInformation(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			InitialContext jndiContext = new InitialContext();

			Object ref = jndiContext.lookup("HelpsManager/remote");
			HelpsManagerRemote helpsManager = (HelpsManagerRemote) ref;

			User user = (User) request.getSession().getAttribute("User");
			List<HelpRequest> giving = helpsManager.getGivingHelpRequests(user);
			request.setAttribute("giving", giving);
			List<HelpRequest> open = helpsManager.getOpenHelpRequests(user);
			request.setAttribute("open", open);
			List<HelpRequest> pendingAsHelper = helpsManager
					.getPendingAsHelper(user);
			request.setAttribute("pendingAsHelper", pendingAsHelper);
			List<HelpRequest> pendingAsAsker = helpsManager
					.getPendingAsAsker(user);
			request.setAttribute("pendingAsAsker", pendingAsAsker);
			List<HelpRequest> closed = helpsManager.getClosedHelpRequests(user);
			request.setAttribute("closed", closed);

			getServletConfig().getServletContext()
					.getRequestDispatcher("/user/helps.jsp")
					.forward(request, response);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

}
