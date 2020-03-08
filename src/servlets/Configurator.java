package servlets;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import db.Migrator;
import tools.Config;
import tools.Logger;
import tools.StdVar;
import tools.sessions.SessionModel;
import tools.sessions.SessionPool;

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
	
	/**	Unique instance of the logger */
	private Logger logger = null;
	
	/** The session pool */
	private SessionPool sessionPool;

	
	// ----- Constructors -----
	
	
	public Configurator() {
		super();
		
		// Get instances
		this.logger = Logger.getInstance();
		this.sessionPool = SessionPool.getInstance();
	}
	

	// ----- HTTP methods -----


	/**
	 * This methods initialize the application
	 */
	public void init() {
		// Get the application base path
		ServletContext context = this.getServletContext();
		String basePath = context.getRealPath("./");
		
		System.out.println(basePath);
		
		// Set the application base path
		Config.setBasePath(basePath);

		// Get the logger
		this.logger = Logger.getInstance();

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
	}

	/**
	 * Method to get the current configuration
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Get the current session
		SessionModel currentSession = this.sessionPool.getSession(req, resp, true);
		boolean isSessionAdmin = Boolean.parseBoolean(currentSession.getAttribute("adminSession"));
		
		if(currentSession != null && isSessionAdmin) {
			JSONObject res = new JSONObject();
			res = Config.getJSON();

			// Send the result
			resp.setContentType(StdVar.JSON_CONTENT_TYPE);
			resp.setCharacterEncoding(StdVar.APP_ENCODING);
			resp.getWriter().append(res.toJSONString());

			// Reload the configuration
			this.init();
		} else {
			
			// TODO : rediriger vers la page 404
			
		}
	}

	/**
	 * Method to put a new configuration
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO : Mettre à jour la configuration

		// Reload the configuration
		this.init();
	}

}
