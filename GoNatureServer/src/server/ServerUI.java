
package server;

import boundary.ServerController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The Class ServerUI is the GUI of the server.
 */
public class ServerUI extends Application{

	/** The sv. */
	private static GoNatureServer sv;
	
	/**
	 * The main method, this is a standard JavaFX main as learned in the course.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	}

	/**
	 * this method starts the application.
	 *
	 * @param primaryStage the primary stage
	 * @throws Exception the exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {			  		
		ServerController aFrame = new ServerController(); 
		// create StudentFrame
		aFrame.start(primaryStage);
		
	}
	
	/**
	 * this method runs the server
	 *
	 * @param p the port we are listening to.
	 */
	public static void runServer(String p) {
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(p); // Set port to 5555

		} catch (Throwable t) {
			ServerController.writeToServerConsole("ERROR - Could not connect!");
		}

		sv = new GoNatureServer(port);
		
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			ServerController.writeToServerConsole("ERROR - Could not listen for clients!");
		}
	}
	/**
	 * this method stops the server.
	 */
	public static void stopServer() {
		try {
			sv.close(); // Start listening for connections
		} catch (Exception ex) {
			ServerController.writeToServerConsole("ERROR - Could not close the server!");
		}
	}

}

