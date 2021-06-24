package boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import client.WorkerControllerClient;
import entity.ParkWorker;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class ServiceRepresentativeHomePageController implements Initializable {
/**
 *  @param borderPane this is a Pane component for borderPane in the system.
 * @param sheet this is a Pane component for sheet in the system.
 * @param workerName this is a Text component for workerName in the system.
 * @param signUpType this is a Text ChoiceBox for signUpType in the system.
 * @param checkExist this is a Button component for checkExist in the system.
 * @param idInput this is a TextField component for idInput in the system.
 * @param firstNameInput this is a TextField component for firstNameInput in the system.
 * @param lastNameInput this is a TextField component for lastNameInput in the system.
 * @param phoneNumberInput this is a TextField component for phoneNumberInput in the system.
 * @param EmailText this is a TextField component for EmailText in the system.
 * @param park this is a Text component for park in the system.
 * @param logoutButton this is a Button component for logoutButton in the system.
 * @param cancelRegistration this is a Button component for cancelRegistration in the system.
 * @param proceedRegistration this is a Button component for proceedRegistration in the system.
 * @param totalFamilyInput this is a TextField component for totalFamilyInput in the system.
 * @param totalFamilyText this is a Text component for totalFamilyText in the system.
 * @param creditCardText this is a Text component for creditCardText in the system.
 * @param creditCard this is a TextField component for creditCard in the system.
 * @param familySheet this is a Pane component for familySheet in the system.
 * @param worker this is a Worker object for saving worker data in the system.
 * @param traveler this is a Traveler object for saving traveler data in the system.
 * @param proceed this is a boolean object for saving true,false data in the system.
 * @param existTraveler this is a boolean object for saving true,false data in the system.
 * @param card this is a String component for saving String in the system.
 * @param greenSquare this is a String object for saving css code.
 * @param redSquare this is a String object for saving css code.
 */
    @FXML
    private Pane borderPane;  
    @FXML
    private Pane sheet;   
    @FXML
    private Text workerName;
    @FXML
    private ChoiceBox<String> signUpType;
    @FXML
    private Button checkExist;
    @FXML
    private TextField idInput;
    @FXML
    private TextField firstNameInput;       
    @FXML
    private TextField lastNameInput;
    @FXML
    private TextField phoneNumberInput;
    @FXML
    private TextField EmailText;
    @FXML
    private Text park;
    @FXML
    private Button logoutButton;    
    @FXML
    private Button cancelRegistration;
    @FXML
    private Button proceedRegistration;   
    @FXML
    private TextField totalFamilyInput;
    @FXML
    private Text totalFamilyText;   
    @FXML
    private Text creditCardText;
    @FXML
    private TextField creditCard;   
    @FXML
    private Pane familySheet;

    
	private ParkWorker worker;
	private Traveler traveler = null;
	private boolean proceed, existTraveler;	
	private String card;
	
	final String greenSquare = "-fx-border-color : #5cb85c ; -fx-border-width : 2;";
	final String redSquare = "-fx-border-color :  #d9534f; -fx-border-style : dashed;";

	/**
	 * this method check if email is valid.
	 * @param email the email string.
	 * @return true if valid , false otherwise.
	 */
	//  ---- EMAIL ADDRESS VALIDATION ----   \\
	static boolean isValid(String email) 
	{
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}

/**
 * this method check if the input id is already exists as 'Guide' or 'Subscription'
 * @param event the mouse event.
 */
    @FXML
    void checkIdExistance(MouseEvent event) {
		// check guide id
		if (idInput.getText().equals("") || !idInput.getText().matches("[0-9]+")) {
			Alert alert = new Alert(AlertType.ERROR, "Please enter valid id number.");
			alert.showAndWait();
			return;
		}
		String travelerID = idInput.getText();// get id number
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
				//set names.
				existTraveler = true;
				allowEditting(true);
				//guy added code.
				firstNameInput.setText(traveler.getFirstName());
				lastNameInput.setText(traveler.getSurname());
				phoneNumberInput.setText(traveler.getTel());
				EmailText.setText(traveler.getEmail());
			}
		} else { 
			existTraveler = false;
			allowEditting(true);
		}
    }
/**
 * this method promote Existing Traveler.
 */
    private void promoteExistingTraveler() {
    	
    	if(!markEmptyFields()) return;
    	Alert alert;
    	
    	if(signUpType.getSelectionModel().getSelectedItem().contentEquals("Subscription")) {
			String name = firstNameInput.getText();
			String surname = firstNameInput.getText();
			int number = Integer.parseInt(idInput.getText());
			String tel = phoneNumberInput.getText();
			String mail = EmailText.getText();
			int total = Integer.parseInt(totalFamilyInput.getText());
			card = "";//default value
			if((!creditCard.getText().equals("")) && creditCard.getText().matches("[0-9]+{2}")) {
				 card = creditCard.getText();
			}
			else if (!creditCard.getText().equals("")){
			 alert = new Alert(AlertType.ERROR,
						"Unvalid card number.");
				alert.getButtonTypes().removeAll(ButtonType.CANCEL);
				alert.showAndWait();
				return;
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
				alert = new Alert(AlertType.CONFIRMATION,
						"Traveler Registered successfully.");
				alert.getButtonTypes().removeAll(ButtonType.CANCEL);
				alert.showAndWait();
				idInput.setText("");
				allowEditting(false);
                
			}else {//register failed
				alert = new Alert(AlertType.ERROR,
						"Traveler Registered Failed.");
				alert.showAndWait();	
			}
        }
    	else if(signUpType.getSelectionModel().getSelectedItem().contentEquals("Guide")) {
    		traveler.setTravelerType(TravelerType.Guide);
    		firstNameInput.setText(traveler.getFirstName());
    		lastNameInput.setText(traveler.getSurname());
    		phoneNumberInput.setText(traveler.getTel());
    		EmailText.setText(traveler.getEmail());
    		boolean isUpdated = WorkerControllerClient.updateTravelerType(traveler);
    		if (!isUpdated) {
    			alert = new Alert(AlertType.ERROR, "Failed to register new guide.");
    			alert.showAndWait();
    			return;
    		}
    		else {
    		alert = new Alert(AlertType.CONFIRMATION, "Register new Guide succeed.");
    		alert.getButtonTypes().removeAll(ButtonType.CANCEL);
    		alert.showAndWait();
			idInput.setText("");
    		allowEditting(false);
    		}
    	}
    	
	}
    /**
     * this method promote new traveler.
     */
    private void promoteNewTraveler() {
    	String name = firstNameInput.getText();
		String surname = firstNameInput.getText();
		int number = Integer.parseInt(idInput.getText());
		String tel = phoneNumberInput.getText();
		String mail = EmailText.getText();
		if(!markEmptyFields()) return;
		
       	if(signUpType.getSelectionModel().getSelectedItem().contentEquals("Subscription")) {
			int total = Integer.parseInt(totalFamilyInput.getText());

			//now we wiil update the traveler.
			SubscriptionTraveler subscriptionTraveler = null;
			Traveler createTraveler =null;
			
			if(total == 1) {//if single subscription
				createTraveler = new Traveler(number, name, surname, mail, tel,TravelerType.SingleSubscription );
				subscriptionTraveler = new SubscriptionTraveler( number, name, surname, mail, tel, TravelerType.SingleSubscription, card, total);
			}else {//if family subscription.
				createTraveler = new Traveler(number, name, surname, mail, tel, TravelerType.FamilySubscription);
				subscriptionTraveler = new SubscriptionTraveler( number, name, surname, mail, tel, TravelerType.FamilySubscription, card, total);
			}
			
			boolean isCreated = WorkerControllerClient.createNewTraveler(createTraveler);//update existing traveler.
			boolean isAdded = WorkerControllerClient.addNewFamilySubscription(subscriptionTraveler);//create new subscription traveler.
			
			if(isCreated && isAdded) {//register succeed.
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"New subscription registered successfully.");
				alert.getButtonTypes().removeAll(ButtonType.CANCEL);
				alert.showAndWait();
				idInput.setText("");
				allowEditting(false);
                
			}else {//register failed
				Alert alert = new Alert(AlertType.ERROR,
						"Traveler Registered Failed.");
				alert.showAndWait();	
			}
       	}
       	else if(signUpType.getSelectionModel().getSelectedItem().contentEquals("Guide")) {
		
			Traveler createTraveler =null;
       		createTraveler = new Traveler(number, name, surname, mail, tel,TravelerType.Guide );

			boolean isCreated = WorkerControllerClient.createNewTraveler(createTraveler);//update existing traveler.

			if(isCreated) {//register succeed.
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"New guide registered successfully.");
				alert.getButtonTypes().removeAll(ButtonType.CANCEL);
				alert.showAndWait();
				idInput.setText("");
				allowEditting(false);
                
			}else {//register failed
				Alert alert = new Alert(AlertType.ERROR,
						"Traveler Registered Failed.");
				alert.showAndWait();	
			}
       	}
     }

    		
    	
/**
 * this method display mainpage.fxml page.
 * @param event the mouse event.
 * @throws IOException
 */
	@FXML
    void displayMainPage(MouseEvent event) throws IOException {
		boolean isRemoved = WorkerControllerClient.removeOnlineWorker(worker.getWorkerID()+"");
		if(isRemoved) {
			System.out.println(worker.getWorkerID()+ " Successfully removed from online table.");
		} else {
			System.out.println("Failed to remove from online table.");
		}
		Parent root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		Stage stage = (Stage) logoutButton.getScene().getWindow();
		stage.setTitle("GoNature System");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}
    /**
     * this method submit new registration.
     * @param event
     */
    @FXML
    void submitRegistration(MouseEvent event) {
    	if(!markEmptyFields()) {
    		return;
    	}
    	Alert alert = new Alert(AlertType.CONFIRMATION,"Are you sure you want to complete registration?");
    	alert.setHeaderText("Registeration");
    	alert.showAndWait();
    	if(alert.getResult() == ButtonType.CANCEL) {
    		return;
    	}
    	if(existTraveler) {
    		promoteExistingTraveler();
    		return;
    	}
    	else {
    		promoteNewTraveler();
    	}

    }
    


/**
 * this method set text and visibility.
 * @param trueForEdit the boolean parameter.
 */
 private void allowEditting(boolean trueForEdit) {
    	checkExist.setDisable(trueForEdit);
		idInput.setEditable(!trueForEdit);
		if(trueForEdit) {
		borderPane.setStyle(greenSquare);
		idInput.setOpacity(0.30);
		}
		else {
			borderPane.setStyle(redSquare);
			idInput.setOpacity(1);
		}
		cancelRegistration.setVisible(trueForEdit);
		proceedRegistration.setVisible(trueForEdit);
    	 firstNameInput.setText("");
    	 lastNameInput.setText("");
    	 phoneNumberInput.setText("");
    	 EmailText.setText("");
    	 totalFamilyInput.setText("");
    	 creditCard.setText("");
    	 sheet.setVisible(!trueForEdit);
    }
   /**
    * this method cancel the Registration process.
    * @param event
    */
    @FXML
    void cancelRegistration(MouseEvent event) {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Are you sure you want to cancel?");
		alert.setContentText("All the information you entered will be lost");
		alert.getButtonTypes().remove(ButtonType.OK);
		ButtonType cancel = new ButtonType("Yes");
		ButtonType goBack = new ButtonType("No");
		
		alert.getButtonTypes().add(cancel);
		alert.getButtonTypes().add(goBack);
		alert.showAndWait();
		
		if(alert.getResult() == cancel) {
			idInput.setText("");
			allowEditting(false);
		}			
    }
    /**
     * this method clear all registration fields.
     * @param event
     */
    @FXML
    void clearAllFields(MouseEvent event) {
   	 firstNameInput.setText("");
   	 lastNameInput.setText("");
   	 phoneNumberInput.setText("");
   	 EmailText.setText("");
   	 totalFamilyInput.setText("");
   	 creditCard.setText("");
    }
/**
 * this method initialize ServiceRepresentativeHomePageController.
 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		proceed = false;
		worker = WorkerLoginPageController.getWorker();
		workerName.setText(worker.getWorkerName());
		park.setText(worker.getPark());
		signUpType.getItems().add("Guide");
		signUpType.getItems().add("Subscription");
		signUpType.setValue("Subscription");
		
		signUpType.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
			//System.out.println(oldValue + " / " + newValue );
			handelListener(oldValue, newValue);
			//System.out.println(newValue);
	    }
	    ); 

		allowEditting(false);
	}
	
	private void handelListener(String oldValue, String newValue) {
		if(!oldValue.contentEquals(newValue)) {
			creditCardText.setVisible(newValue.contentEquals("Subscription"));
			creditCard.setVisible(newValue.contentEquals("Subscription"));
			allowEditting(false);
		if(newValue.contentEquals("Subscription")) {
			familySheet.setVisible(false);
			}
		if(newValue.contentEquals("Guide")) {
			familySheet.setVisible(true);
        }
	  }
	}
/**
 * this method make validation to the user input fields.
 * @return
 */
	private boolean markEmptyFields() {
		
		card = "";
		
		String tmpTxt = firstNameInput.getText();

		if(tmpTxt.contentEquals("") || (!tmpTxt.matches("^[a-zA-Z]*$"))) {
				errorAlert("Error input in field 'First Name'.\nPlease enter valid First Name(chars only).");
				return false;
		}
		
		tmpTxt = lastNameInput.getText();
				
		if(tmpTxt.contentEquals("") || (!tmpTxt.matches("^[a-zA-Z]*$"))) {
			errorAlert("Error input in field 'Last Name'.\nPlease enter valid Last Name(chars only).");
			return false;
	}
		
		tmpTxt = phoneNumberInput.getText();
		
		if(tmpTxt.contentEquals("") || (!tmpTxt.matches("[0-9]+"))
				|| tmpTxt.length() > 10) {
			errorAlert("Error input in field 'Phone number'.\nPlease enter valid Phone number(positive numbers only).");
			return false;
		}

		
		tmpTxt = EmailText.getText();
		
		if(tmpTxt.contentEquals("") || (!isValid(tmpTxt)) ) {
			errorAlert("Error input in field 'Email'.\nPlease enter valid Email.");
			return false;
		}
		
		if(signUpType.getSelectionModel().getSelectedItem().contentEquals("Subscription")) {
			
			tmpTxt = totalFamilyInput.getText();
			
			if(tmpTxt.contentEquals("") || (!tmpTxt.matches("[0-9]+"))
					|| Integer.parseInt(tmpTxt) < 1 
					|| Integer.parseInt(tmpTxt) > 15 ) {
				errorAlert("Error input in field 'Total family members'\n" + 
					"Enter number between 1 to 15");
				return false;
			}
			
			// initiate card number
			if((!creditCard.getText().equals("")) 
					&& creditCard.getText().matches("[0-9]+")) {
				 card = creditCard.getText();
			} 
			// card field contians unvalid input
			else if (!creditCard.getText().equals("")){
				errorAlert("Error input in field 'Credit Card");
				return false;
			} 
		}
		
		return true;
	}
	
    private void errorAlert( String content) {
    	Alert alert = new Alert(AlertType.ERROR, content);
		alert.showAndWait();
    }
}

