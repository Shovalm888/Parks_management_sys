package boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import entity.Order;
import entity.SubscriptionTraveler;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 
 * @author group 20
 * @version 1.0
 * 
 *
 * 
 */
public class TravelerLoginPageController implements Initializable {

	/**
	 * @param identificationInput this is a TextField component for
	 *                            identificationInput in the system.
	 * @param loginByComboBox     this is a ComboBox component for loginByComboBox
	 *                            in the system.
	 * @param loginButton         this is a Button component for loginButton in the
	 *                            system.
	 * @param goBackButton        this is a Button component for goBackButton in the
	 *                            system.
	 * @param loginError          this is a Text component for loginError in the
	 *                            system.
	 * @param identificationError this is a Text component for identificationError
	 *                            in the system.
	 * @param type                this is a TravelerType component for saving
	 *                            traveler type in the system.
	 * @param travelerName        this is a String component for saving traveler
	 *                            name in the system.
	 * @param travelerID          this is a int component for saving travelerID in
	 *                            the system.
	 */
	@FXML
	private TextField identificationInput;
	@FXML
	private ComboBox<String> loginByComboBox;
	@FXML
	private Button loginButton;
	@FXML
	private Button goBackButton;
	@FXML
	private Text loginError;
	@FXML
	private Text identificationError;

	private static TravelerType type;
	private static String travelerName;
	private static int travelerID;

	public static TravelerType getType() {
		return type;
	}

	public static String getName() {
		return travelerName;
	}

	public static int getID() {
		return travelerID;
	}


	public static void setTravelerName(String travelerName) {
		TravelerLoginPageController.travelerName = travelerName;
	}

	
	public static void setTravelerID(int travelerID) {
		TravelerLoginPageController.travelerID = travelerID;
	}

	public static void setType(TravelerType type) {
		TravelerLoginPageController.type = type;
	}

	/**
	 * this method is in charge of checking id number from user.
	 * there is 3 option ( ID,Subscription,OrderID) for logging.
	 * in case of success method will display traveler corresponding home-page accordingly.
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void checkIDNumber(MouseEvent event) throws IOException {
	
		if (checkIfInputIsValid()) {
			switch (loginByComboBox.getSelectionModel().getSelectedItem()) {
			case "ID":
				String id = identificationInput.getText();
				travelerID = Integer.parseInt(id);
				if (TravelerControllerClient.addOnlineTraveler(id)) {// check if traveler id is in the online travelers table.
					Traveler traveler = TravelerControllerClient.GetTravelerInfoByID(id);
					if (traveler != null) {// checks if traveler Identification number exists in the travelers table.
						TravelerLoginPageController.travelerName = traveler.getFirstName();// initialize traveler name for
						TravelerLoginPageController.type = traveler.getTravelerType();// initialize traveler type for
						TravelerLoginPageController.travelerID = traveler.getID();
						Parent root = FXMLLoader.load(getClass().getResource("TravelerHomePage.fxml"));
						Stage stage = (Stage) loginButton.getScene().getWindow();
						stage.setTitle("Traveler HomePage");
						stage.setScene(new Scene(root));
						stage.setResizable(false);
						stage.show();
						stage.setOnCloseRequest(e -> {
							TravelerControllerClient.removeOnlineTraveler(travelerID + "");
						});
					} else {// casual traveler.
						Parent root = FXMLLoader.load(getClass().getResource("GuestNewOrder.fxml"));
						Stage stage = (Stage) loginButton.getScene().getWindow();
						stage.setTitle("New order");
						stage.setScene(new Scene(root));
						stage.setResizable(false);
						stage.show();
						stage.setOnCloseRequest(e -> {
							TravelerControllerClient.removeOnlineTraveler(travelerID + "");
						});
					}
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Login Failed.");
					alert.setContentText("Traveler is already online.");
					alert.showAndWait();
				}
				break;

			case "Subscription":
				id = identificationInput.getText();// get subscription id number.
				SubscriptionTraveler subscriptionTraveler = TravelerControllerClient.GetTravelerInfoBySubscription(id);
				if (subscriptionTraveler != null) {// check if traveler exists in the SubscriptionTraveler table.
					if (TravelerControllerClient.addOnlineTraveler(subscriptionTraveler.getID() + "")) {// if he's online
						if (subscriptionTraveler.getTravelerType() == TravelerType.FamilySubscription
								|| subscriptionTraveler.getTravelerType() == TravelerType.SingleSubscription) {
							TravelerLoginPageController.travelerName = subscriptionTraveler.getFirstName();// initialize traveler name
							TravelerLoginPageController.type = subscriptionTraveler.getTravelerType();// initialize traveler type
							TravelerLoginPageController.travelerID = subscriptionTraveler.getID();// initialize traveler id
							Parent root = FXMLLoader.load(getClass().getResource("TravelerHomePage.fxml"));
							Stage stage = (Stage) loginButton.getScene().getWindow();
							stage.setTitle("Traveler HomePage");
							stage.setScene(new Scene(root));
							stage.setResizable(false);
							stage.show();
							stage.setOnCloseRequest(e -> {
								TravelerControllerClient.removeOnlineTraveler(travelerID + "");
							});
						}
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setHeaderText("Login Failed.");
						alert.setContentText("Traveler is already online.");
						alert.showAndWait();
					}
				} else {// the id exists, but traveler type is not subscription.
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Login Failed.");
					alert.setContentText(
							"Subscription number is not exists.\nPlease choose valid Subscription ID and try agian.");
					alert.showAndWait();
				}
				break;

			case "Invitation":
				String inviteNumber = identificationInput.getText();
				Order order = TravelerControllerClient.GetTravelerInfoByInvitation(inviteNumber);
				if (order != null) {//check if there order exists.
					if (TravelerControllerClient.addOnlineTraveler(order.getTravlerID() + "")) {// check if traveler is online.
						int travelerID = order.getTravlerID();
						Traveler traveler = TravelerControllerClient.GetTravelerInfoByID(travelerID + "");
						TravelerLoginPageController.travelerName = traveler.getFirstName();// initialize traveler name
						TravelerLoginPageController.type = traveler.getTravelerType();// initialize traveler type
						TravelerLoginPageController.travelerID = traveler.getID();// initialize traveler id
						Parent root = FXMLLoader.load(getClass().getResource("TravelerHomePage.fxml"));
						Stage stage = (Stage) loginButton.getScene().getWindow();
						stage.setTitle("Traveler HomePage");
						stage.setScene(new Scene(root));
						stage.setResizable(false);
						stage.show();
						stage.setOnCloseRequest(e -> {
							TravelerControllerClient.removeOnlineTraveler(travelerID + "");
						});
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setHeaderText("Login Failed.");
						alert.setContentText("Traveler is already online.");
						alert.showAndWait();
					}
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Login Failed.");
					alert.setContentText("Order ID is not exists.\nPlease choose valid Order ID and try agian.");
					alert.showAndWait();
				}
				break;
			default:
				System.out.println("Error in identification option.");
				break;
			}
		}
	}
	/**
	 * this method in charge of display MainPage.fxml page.
	 * @param event the mouse event
	 * @throws IOException
	 */
	@FXML
	void returnToMainPage(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		Stage stage = (Stage) loginButton.getScene().getWindow();
		stage.setTitle("GoNature System");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}
	/**
	 * this method check the user input.
	 * in case of wrong input enter, system will display corresponding alerts.
	 * @return true if input is valid, false otherwise.
	 */
	boolean checkIfInputIsValid() {
		boolean isValid = true;
		String textID = identificationInput.getText();
		
		if (loginByComboBox.getSelectionModel().getSelectedItem() == null) {//check if user select any of the option.
			loginError.setOpacity(1);
			loginByComboBox.setStyle("-fx-border-color : #e12929; -fx-background-color :  #ffffff75;"
					+ "-fx-border-radius : 5; -fx-background-radius : 5");
			isValid = false;
		} else {
			loginError.setOpacity(0);
			loginByComboBox.setStyle("-fx-border-color :  #000000; -fx-background-color :  #ffffff75;"
					+ "-fx-border-radius : 5; -fx-background-radius : 5");
		}
		if (textID.contentEquals("")) {
			identificationError.setText("* Enter your identification number");
			identificationError.setOpacity(1);
			identificationInput.setStyle("-fx-border-color : #e12929; -fx-background-color :  #ffffff;"
					+ "-fx-border-width : 3; -fx-border-style : solid;");
			isValid = false;
		} else if(!textID.matches("[0-9]+") && !identificationInput.getText().equals("")){
			identificationError.setText("* Enter numbers only");
			identificationError.setOpacity(1);
			identificationInput.setStyle("-fx-border-color : #e12929; -fx-background-color :  #ffffff;"
					+ "-fx-border-width : 3; -fx-border-style : solid;");
			isValid = false;
		} else {
			identificationError.setOpacity(0);
			identificationInput.setStyle("-fx-border-color : #000000; -fx-background-color :  #ffffff;"
					+ "-fx-border-width : 3; -fx-border-style : hidden hidden solid hidden;");
		}
		
		return isValid;
	}
/**
 * this method initialize TravelerLoginPageController class.
 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginByComboBox.getItems().add("ID");//set 3 options to combo box.
		loginByComboBox.getItems().add("Subscription");
		loginByComboBox.getItems().add("Invitation");
		loginByComboBox.getSelectionModel().selectedItemProperty().addListener(e -> {//add listener to traveler combo box option input
			switch (loginByComboBox.getSelectionModel().getSelectedItem()) {
			case "ID":
				identificationInput.setPromptText("Enter ID Number.");
				break;
			case "Subscription":
				identificationInput.setPromptText("Enter Subscription Number.");
				break;
			case "Invitation":
				identificationInput.setPromptText("Enter Invitation Number.");
				break;
			default:
				System.out.println("Error in choosing identification options.");
				break;
			}
		});
		loginButton.setTooltip(new Tooltip("Display your Home-Page."));
		goBackButton.setTooltip(new Tooltip("Returns to Main Page."));
		identificationInput.setTooltip(new Tooltip("Enter here your ID number."));
		loginByComboBox.setTooltip(new Tooltip("Login options."));
	}

}
