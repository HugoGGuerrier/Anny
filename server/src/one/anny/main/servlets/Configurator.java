package one.anny.main.servlets;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import one.anny.main.db.Migrator;
import one.anny.main.tools.Config;
import one.anny.main.tools.Logger;
import one.anny.main.tools.StdVar;
import one.anny.main.tools.sessions.SessionModel;
import one.anny.main.tools.sessions.SessionPool;

/**
 * This servlet is used to initialize the web application. It is use by the administrator.
 * 
 * @author Emilie Siau
 * @author Hugo Guerrier
 */
@WebServlet("/configurator/*")
public class Configurator extends HttpServlet {

	// ----- Attributes -----


	/** Serial version number */
	private static final long serialVersionUID = 5707836903604706333L;


	// ----- Constructors -----


	public Configurator() {
		super();
	}


	// ----- HTTP methods -----


	/**
	 * This methods initialize the application
	 */
	public void init() {
		// Get the application base path
		ServletContext context = this.getServletContext();
		String basePath = context.getRealPath("./");

		// Set the application base path
		Config.setBasePath(basePath);

		// Get the logger
		Logger logger = Logger.getInstance();

		try {

			// Get the file reader of the configuration file
			Reader configReader = new FileReader(Paths.get(basePath + StdVar.CONFIG_FILE).toFile());

			// Initialize the application configuration
			Config.init(configReader);

			configReader.close();

			// Log the application configuration loading
			logger.log("Configuration loaded", Logger.INFO);

			// Migrate the database to the correct version
			Migrator migrator = Migrator.getInstance();
			if(migrator.migrate(Config.getDatabaseVersion())) {
				logger.log("Database migrated to version " + Config.getDatabaseVersion(), Logger.INFO);
			}

		} catch (IOException e) {

			logger.log("Cannot load the configuration", Logger.ERROR);
			logger.log(e, Logger.ERROR);

		} catch (SQLException e) {

			logger.log("Cannot migrate the database to the app version", Logger.ERROR);
			logger.log(e, Logger.ERROR);

		}

		// DEBUG SECTION
		if(Config.getEnv() == StdVar.DEVELOPMENT_ENV) {
			
			logger.log("Base path : " + basePath, Logger.INFO);
			logger.log("\n" + Config.display(), Logger.INFO);
			
		}
	}

	/**
	 * Method to get the current configuration
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SessionPool sessionPool = SessionPool.getInstance();

		// Get the current session
		SessionModel currentSession = sessionPool.getSession(req, resp, true);

		if(currentSession != null && currentSession.isAdmin()) {

			JSONObject res = new JSONObject();
			res = Config.getJSON();

			// Send the result
			resp.setContentType(StdVar.JSON_CONTENT_TYPE);
			resp.setCharacterEncoding(StdVar.APP_ENCODING);
			resp.getWriter().append(res.toJSONString());

			// Reload the configuration
			this.init();

		} else {

			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/404");
			dispatcher.forward(req, resp);

		}
	}

	/**
	 * Method to put a new configuration
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		SessionPool sessionPool = SessionPool.getInstance();
		
		// Get the current session
		SessionModel currentSession = sessionPool.getSession(req, resp, true);
		
		if(currentSession != null && currentSession.isAdmin()) {
			
			// TODO LATER : Faire la mise à jour de la configuration
			
		} else {
			
			RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/404");
			dispatcher.forward(req, resp);
			
		}
	}

}
