package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import services.user.CreateUser;
import tools.Handler;
import tools.Logger;
import tools.Security;
import tools.StdVar;
import tools.exceptions.SessionException;
import tools.sessions.SessionModel;
import tools.sessions.SessionPool;

/**
 * Servlet to register a new user
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/register/*")
public class Register extends HttpServlet {

	// ----- Attributes -----


	/** The logger */
	private Logger logger;

	/** The handler tool */
	private Handler handler;

	/** Security tool */
	private Security security;

	/** The session pool */
	private SessionPool sessionPool;

	/** The user creation service */
	private CreateUser createUser;

	/** Serial version number */
	private static final long serialVersionUID = 6127736970206786786L;


	// ----- Constructors -----


	public Register() {
		super();

		// Get instances
		this.logger = Logger.getInstance();
		this.handler = Handler.getInstance();
		this.security = Security.getInstance();
		this.sessionPool = SessionPool.getInstance();
		this.createUser = CreateUser.getInstance();
	}


	// ----- HTTP methods -----


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Prepare the JSON result
		JSONObject res = this.handler.getDefaultResponse();
		
		// Try to get the session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);
		Boolean isSessionAdmin = Boolean.parseBoolean(currentSession.getAttribute("adminSession"));
		
		if(isSessionAdmin || currentSession == null) {
			
			
			
		} else {
			
			res = this.handler.handleException(new SessionException("Cannot create an user with an existing session"), Handler.WEB_ERROR);
			
		}

		// Send the result
		resp.setCharacterEncoding(StdVar.APP_ENCODING);
		resp.setContentType(StdVar.JSON_CONTENT_TYPE);
		resp.getWriter().append(res.toJSONString());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO LATER : Une vérification par mail de l'utilisateur pour l'obliger à confirmer son mail

		// TODO : Faire l'enregistrement du nouvel utilisateur et créer sa session
		resp.getWriter().append("POST : Not implemented...");
	}

}
