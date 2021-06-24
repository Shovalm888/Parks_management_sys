package boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.WorkerControllerClient;
import entity.SubscriptionTraveler;
import entity.Traveler;
import enums.TravelerType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class ServiceRepresentativeFamilySubscriptionRegisterationController implements Initializable {
/**
 * @param registerNewSubscription this is a Pane component for registerNewSubscription in the system.
 * @param checkButton this is a Button component for checkButton in the system.
 * @param goBackButton this is a Button component for goBackButton in the system.
 * @param registerButton this is a Button component for registerButton in the system. 
 * @param firstName this is a TextField component for firstName in the system.
 * @param lastName this is a TextField component for lastName in the system.
 * @param ID this is a TextField component for ID in the system.
 * @param email this is a TextField component for email in the system.
 * @param phone this is a TextField component for phone in the system.
 * @param totalFamilyMembers this is a TextField component for totalFamilyMembers in the system. 
 * @param creditCard this is a TextField component for creditCard in the system.
 * @param traveler this is a Traveler object for saving traveler info in the system.
 * @param checkIDNumber this is a int object for saving checkIDNumber in the system.
 * @param isExists this is a boolean object for saving isExists object in the system.
 */
	@FXML
	private Pane registerNewSubscription;
	@FXML
	private Button checkButton;
	@FXML
	private Button goBackButton;
	@FXML
	private Button registerButton;
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField ID;
	@FXML
	private TextField email;
	@FXML
	private TextField phone;
	@FXML
	private TextField totalFamilyMembers;
	@FXML
	private TextField creditCard;

	Traveler traveler = null;
	private int checkIDNumber;
	private boolean isExists;
/**
 * this event is in charge of checking the ID from the user.
 * if id isn't 'Guide' or 'Subscription' Type then the register button will be visible, otherwise display alert.
 * @param event the mouse event
 */
	@FXML
	void checkID(MouseEvent event) {
		registerButton.setVisible(false);
		if (ID.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR, "Please enter id number.");
			alert.showAndWait();
			return;
		}
		String travelerID = ID.getText();// get id number
		// now we need to check that id is now belong to guide or subscription traveler.
		traveler = WorkerControllerClient.getTravelerInfoByID(travelerID);
		if (traveler != null) {// if traveler exists.
			firstName.setText("");
			lastName.setText("");
			phone.setText("");
			creditCard.setText("");
			email.setText("");
			totalFamilyMembers.setText("");
			isExists = true;
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
				firstName.setText(traveler.getFirstName());
				lastName.setText(traveler.getSurname());
				phone.setText(traveler.getTel());
				email.setText(traveler.getEmail());
				registerButton.setVisible(true);
				alert = new Alert(AlertType.CONFIRMATION,
						"Check Finish successfully.\nWe identify the ID as an existing member of GoNature.\nPlease fill in the requierd fields and click Register Button.");
				checkIDNumber = Integer.parseInt(ID.getText());// save the id that we made check on it.
				alert.getButtonTypes().removeAll(ButtonType.CANCEL);
				alert.showAndWait();
				return;
			}
		} else {
			isExists = false;
			registerButton.setVisible(true);
			Alert alert = new Alert(AlertType.CONFIRMATION,
					"Check Finish successfully.\nWe identify the ID as a new member of GoNature.\nPlease fill in the requierd fields and click Register Button.");
			checkIDNumber = Integer.parseInt(ID.getText());// save the id that we made check on it.
			alert.getButtonTypes().removeAll(ButtonType.CANCEL);
			alert.showAndWait();
			return;
		}
	}
/**
 * this method in charge of display ServiceRepresentativeRegisteration.fxml page.
 * @param event the mouse event
 * @throws IOException
 */
	@FXML
	void goBack(MouseEvent event) throws IOException {
		goBackButton.getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("ServiceRepresentativeRegisteration.fxml"));
		Stage stage = new Stage();
		stage.setTitle("Register Page");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}
/**
 * this method in charge of register New Family Subscription 
 * @param event the mouse event
 */
	@FXML
	void registerNewFamilySubscription(MouseEvent event) {
		// register new family subscription or update existing one.
		if(firstName.getText().equals("") || lastName.getText().equals("") || ID.getText().equals("")  || phone.getText().equals("") ||totalFamilyMembers.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR, "Please enter all of the requierd fields.");
			alert.showAndWait();
			return;
		}
		int realID = Integer.parseInt(ID.getText());
		if(checkIDNumber != realID){
			Alert alert = new Alert(AlertType.ERROR, "Checked ID has been changed!\nPlease Recheck the ID and Dont change it while registering.");
			alert.showAndWait();
			return;
		}
		if(Integer.parseInt(totalFamilyMembers.getText())<1) {
			Alert alert = new Alert(AlertType.ERROR, "Please enter valid Family Member number.");
			alert.showAndWait();
			return;
		}
		//everything is ok, now we will create/update the traveler as subscription traveler.
		
		if(isExists) {//if traveler exists we only update it.
			String name = firstName.getText();
			String surname = lastName.getText();
			int number = realID;
			String tel = phone.getText();
			String mail = email.getText();
			int total = Integer.parseInt(totalFamilyMembers.getText());
			String card = "";//default value
			if(!creditCard.getText().equals("")) {
				 card = creditCard.getText();
			}
			//now we wiil update the traveler.
			SubscriptionTraveler subscriptionTraveler = null;
			Traveler toUpdateTraveler =null;
			if(total == 1) {//if single subscription
				toUpdateTraveler = new Traveler(number, name, surname, mail, tel,TravelerType.SingleSubscription );
				subscriptionTraveler = new SubscriptionTraveler( number, name, surname, mail, tel, TravelerType.SingleSubscription, card, total);
			}else {//if family subscription.
				toUpdateTraveler = new Traveler(number, name, surname, mail, tel, TravelerType.FamilySubscription);
				subscriptionTraveler = new SubscriptionTraveler( number, name, surname, mail, tel, TravelerType.FamilySubscription, card, total);
			}
			boolean isUpdated = WorkerControllerClient.updateTravelerInfo(toUpdateTraveler);//update existing traveler.
			boolean isAdded = WorkerControllerClient.addNewFamilySubscription(subscriptionTraveler);//create new subscription traveler.
			
			if(isUpdated && isAdded) {//register succeed.
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"Traveler Registered successfully.");
				alert.getButtonTypes().removeAll(ButtonType.CANCEL);
				alert.showAndWait();
				registerButton.getScene().getWindow().hide();
				return;
			}else {//register failed
				Alert alert = new Alert(AlertType.ERROR,
						"Traveler Registered Failed.");
				alert.showAndWait();
				return;
			}
		}
		
		
		//create new traveler and subscriber.
		else {//isn't exists, create new traveler and new subscription traveler.
			String name = firstName.getText();
			String surname = lastName.getText();
			int number = realID;
			String tel = phone.getText();
			String mail = email.getText();
			int total = Integer.parseInt(totalFamilyMembers.getText());
			String card = "";//default value
			if(!creditCard.getText().equals("")) {
				 card = creditCard.getText();
			}
			//now we wiil update the traveler.
			SubscriptionTraveler subscriptionTraveler = null;
			Traveler createTraveler =null;
			if(total == 1) {//if single subscription
				createTraveler = new Traveler(number, name, surname, mail, tel,TravelerType.SingleSubscription );
				subscriptionTraveler = new SubscriptionTraveler( number, name, surname, mail, tel, TravelerType.SingleSubscription, card, total);
			} else {//if family subscription.
				createTraveler = new Traveler(number, name, surname, mail, tel, TravelerType.FamilySubscription);
				subscriptionTraveler = new SubscriptionTraveler( number, name, surname, mail, tel, TravelerType.FamilySubscription, card, total);
			}
			boolean isCreated = WorkerControllerClient.createNewTraveler(createTraveler);//update existing traveler.
			boolean isAdded = WorkerControllerClient.addNewFamilySubscription(subscriptionTraveler);//create new subscription traveler.
			
			if(isCreated && isAdded) {//register succeed.
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"Traveler Registered successfully.");
				alert.getButtonTypes().removeAll(ButtonType.CANCEL);
				alert.showAndWait();
				registerButton.getScene().getWindow().hide();
				return;
			} else {//register failed
				Alert alert = new Alert(AlertType.ERROR,
						"Traveler Registered Failed.");
				alert.showAndWait();
				return;
			}
		}
	}
/**
 * this method initialize ServiceRepresentative class.
 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {//set tool tip
		checkButton.setTooltip(new Tooltip("Verify the ID nubmer."));
		registerButton.setTooltip(new Tooltip("Register new Subscriber member."));
		goBackButton.setTooltip(new Tooltip("Returns to Register page options."));
	}

}
