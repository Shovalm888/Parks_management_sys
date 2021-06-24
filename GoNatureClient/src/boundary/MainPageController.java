package boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * This Class is the Controller of MainPage.fxml
 * This Class initiate the main page of the GoNature Client Gui.
 * 
 * @author Group-20.
 * @version 1.0
 */
public class MainPageController implements Initializable {
	
	/**
	 *  @param travelerButton this is a button componet for traveler in the system
	 * @param workerButton this is a button componet for worker in the system
	 * @param exitButton this is a image view componet for exit button in the system
	 * @param information this is button componet for information in the system
	 * @param facebook this is imageview componet for facebook in the system
	 * @param twitter this is imageview for twitter logo in the system
	 * @param instagram this is imageview componet for instagram in the system
	 * @param TravelerLoginPagePath this is a string that holds the path to the TravelerLoginPage
	 * @param WorkerLoginPagePath this is a string that holds the path to the WorkerLoginPagePath
	 * @param MainPagePath this is a string that holds the path to the MainPage
	 */
    @FXML
    private Button travelerButton;
    @FXML
    private Button workerButton;
    @FXML
    private ImageView exitButton;
    @FXML
    private ImageView facebook;
    @FXML
    private ImageView twitter;
    @FXML
    private ImageView instagram;
    
    final String TravelerLoginPagePath = "TravelerLoginPage.fxml";
    final String WorkerLoginPagePath = "WorkerLoginPage.fxml";
    final String MainPagePath = "MainPage.fxml";
    /**
     * this method displays the travelers home page window.
     * @param event this is the event that causes the display of the home page window
     * @throws IOException
     */
    @FXML
    void displayTravelersHomePage(MouseEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource(TravelerLoginPagePath));
    	Stage stage = (Stage)travelerButton.getScene().getWindow();
		stage.setTitle("Traveler Page");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
    }
    /**
     * this method displays the about page.
     * @param event this is the event that triggers the display of the about page.
     * @throws IOException
     */
    @FXML
    void openAboutPage(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("AboutUs.fxml"));
		Stage stage = new Stage();
		stage.setTitle("About us");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
    }
    /**
     * this method displays the worker home page window
     * @param event this is the event that triggers the display of the worker home page window
     * @throws IOException
     */
    @FXML
    void displayWorkersHomePage(MouseEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource(WorkerLoginPagePath));
    	Stage stage = (Stage)travelerButton.getScene().getWindow();
		stage.setTitle("Worker Page");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
    }
    
    /**
     * this method exits and closes the displayed window
     * @param event this is the event that exits and closes the window
     */
    @FXML
    void exitAndClose(MouseEvent event) {
        System.exit(0);
    }
    /**
     * this method starts the primary stage display
     * @param primaryStage this is the stage that we display to the client.
     * @throws IOException exception
     */
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(MainPagePath));
		primaryStage.setTitle("Go Nature System");
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	/**
	 * initallize the class
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		travelerButton.setTooltip(new Tooltip("displays Traveler Login Page."));
		workerButton.setTooltip(new Tooltip("displays Worker Login Page."));
	}
}
