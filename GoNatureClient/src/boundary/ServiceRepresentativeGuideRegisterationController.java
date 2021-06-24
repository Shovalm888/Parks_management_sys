package boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.WorkerControllerClient;
import entity.Traveler;
import enums.TravelerType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/**
 * 
 *@author group 20 
 *@version 1.0

 */
public class ServiceRepresentativeGuideRegisterationController implements Initializable {
/**
 * @param idTextInput this is a TextField component for idTextInput in the system.
 *@param checkButton this is a Button component for checkButton in the system.
 *@param goBackButton this is a Button component for goBackButton in the system.
 *@param registerButton this is a Button component for registerButton in the system. 
 *@param ServiceRepresentativeRegisterationPath this is a String component for getting fxml ServiceRepresentativeRegisterationPath in the system. 
 *@param traveler this is a Traveler object component for saving traveler object in the system.
 */
	@FXML
	private TextField idTextInput;
	@FXML
	private Button checkButton;
	@FXML
	private Button goBackButton;
	@FXML
	private Button registerButton;
	
	private final String ServiceRepresentativeRegisterationPath = "ServiceRepresentativeRegisteration.fxml";
	private Traveler traveler = null;
/**
 * this method in charge of checking guide id number in DB.
 * in case of existing id number in 'Guide' or 'Subscription' Table in DB, system will display error message, otherwise register button will be visible. 
 * @param event
 */
	@FXML
	void checkGuideID(MouseEvent event){
		// check guide id
		registerButton.setVisible(false);
		if (idTextInput.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR, "Please enter id number.");
			alert.showAndWait();
			return;
		}
		String travelerID = idTextInput.getText();// get id number
		// now we need to check that id is now belong to guide or subscription traveler.
		traveler = WorkerControllerClient.getTravelerInfoByID(travelerID);

		if (traveler != null) {
			switch (traveler.getTravelerType()) {// check traveler type.
			case Guide:
				Alert alert = new Alert(AlertType.ERROR, "Traveler ID is already signed as Guide.");
				alert.showAndWait();
				return;
			case SingleSubscription:
				alert = new Alert(AlertType.ERROR, "Traveler ID is already signed as Single Subscription.");
				alert.showAndWait();
				return;
			case FamilySubscription:
				alert = new Alert(AlertType.ERROR, "Traveler ID is already signed as Family Subscription.");
				alert.showAndWait();
				return;
			default:// means we can sign him as guide.
				// show register button and send message that check succeed.
				registerButton.setVisible(true);
				alert = new Alert(AlertType.CONFIRMATION,
						"Check Finish.\nPlease click Register New Guide Button to finish registeration.");
				alert.getButtonTypes().removeAll(ButtonType.CANCEL);
				alert.showAndWait();
				return;
			}
		}
		else {
			//(int ID, String firstName, String surname, String email, String tel, TravelerType travelerType,int guideID) {
		}

	}
/**
 * this method in charge of display ServiceRepresentativeRegisterationPath.fxml page.
 * @param event
 * @throws IOException
 */
	@FXML
	void goBack(MouseEvent event) throws IOException {
		goBackButton.getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource(ServiceRepresentativeRegisterationPath));
		Stage stage = new Stage();
		stage.setTitle("Register Page");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}
/**
 * this method register new 'Guide' traveler.
 * @param event
 */
	@FXML
	void registerNewGuide(MouseEvent event) {
		// register new guide.
		traveler.setTravelerType(TravelerType.Guide);
		boolean isUpdated = WorkerControllerClient.updateTravelerType(traveler);
		if (!isUpdated) {
			Alert alert = new Alert(AlertType.ERROR, "Failed to register new guide.");
			alert.showAndWait();
			return;
		}
		Alert alert = new Alert(AlertType.CONFIRMATION, "Register new Guide succeed.");
		alert.getButtonTypes().removeAll(ButtonType.CANCEL);
		alert.showAndWait();
		checkButton.getScene().getWindow().hide();
	}
/**
 * this method initialize ServiceRepresentativeGuideRegister class
 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//set tool tip
		checkButton.setTooltip(new Tooltip("Verify the ID nubmer."));
		registerButton.setTooltip(new Tooltip("Register new Guide member."));
		goBackButton.setTooltip(new Tooltip("Returns to Register page options."));
	}

}
