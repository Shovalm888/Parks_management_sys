package client;

import boundary.MainPageController;
import javafx.application.Application;
import javafx.stage.Stage;

/*
 * this is the client-UI of GoNature project.
 * 
 */
public class ClientUI extends Application {

	public static Stage mainStage;
	public static Object user;
	public static ClientGUI client;
	
/**
 * this is the main.
 * this method launch the GoNature Client application.
 * @param args the args.
 * @throws Exception exception
 */
	public static void main(String args[]) throws Exception {
		launch(args);
	}
/**
 * this method starts the application.
 * @param primaryStage this is the application stage.
 */
	@Override
	public void start(Stage primaryStage) throws Exception {// this start the fxml of the client.

		client = new ClientGUI("localhost", 5555);
		
		MainPageController aFrame = new MainPageController(); 
		// create StudentFrame
		aFrame.start(primaryStage);
	}

}
