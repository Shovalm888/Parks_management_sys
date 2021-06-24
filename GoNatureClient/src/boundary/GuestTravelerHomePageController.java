package boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * this class is in charge of displaying the traveler home page in the system
 * @author group 20
 * @version 1.0
 *
 */
public class GuestTravelerHomePageController implements Initializable {
/**
 *  @param newOrderButton this is a button componet for new order in the system
 * @param myOrdersButton this is a button componet for my orders in the system
 * @param myMessagesButton this is a button componet for myMessages in the system
 * @param TravelersFirstNameOutput this is a text componet for travelers first name output in the system
 * @param travelerID this is the travelers ID 
 * @param Stage this is the stage we display to the client.
 */
	@FXML
	private Button newOrderButton;
	@FXML
	private Button myOrdersButton;
	@FXML
	private Button myMessagesButton;
	@FXML
	private Text travelerFirstNameOutput;
	
	
	
	private static int travelerID;
	static Stage stage;
	/**
	 * this method is a getter for the stage
	 * @return this stage parameter
	 */
	public static Stage getStage() {
		return stage;
	}
	/**
	 * this method is a getter for the travelerID
	 * @return travelerID field
	 */
	public static int getTravelerID() {
		return travelerID;
	}
/**
 * this is a setter for the travelerID
 * @param travelerID the travelers ID
 */
	public static void setTravelerID(int travelerID) {
		GuestTravelerHomePageController.travelerID = travelerID;
	}
/**
 * this method is in charge to display the main page to the client.
 * @param event this is the event that triggers the main page to appear.
 * @throws IOException
 */
	@FXML
	void displayMainPage(MouseEvent event) throws IOException {
		boolean isRemoved = TravelerControllerClient.removeOnlineTraveler(travelerID + "");
		if(isRemoved) {
			System.out.println(travelerFirstNameOutput.getText()+ " Successfully removed from online table.");
		} else {
			System.out.println("Failed to remove " + travelerID + "from online table.");
		}
		Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		Stage stage = (Stage) newOrderButton.getScene().getWindow();
		stage.setTitle("GoNature System");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}
	/**
	 * this method is in charge of displaying the new Order section in the system.
	 * @param event this is the event that makes the new order field display
	 * @throws IOException
	 */
	@FXML
	void displayNewOrderTravelerPage(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("GuestNewOrder.fxml"));
    	Stage stage = new Stage();
		stage.setTitle("New Order");
		stage.setScene(new Scene(root));
		GuestTravelerHomePageController.stage = (Stage) newOrderButton.getScene().getWindow();
		stage.setResizable(false);
		stage.show();
	}
	/**
	 * method that initallizes the class
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		travelerID = TravelerLoginPageController.getID();// Initialize traveler id
	}
	

}
