package boundary;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import entity.Order;
import entity.Traveler;
import enums.OrderStatus;
import enums.OrderType;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * this class is in charge of all the orders for guests in the system 
 * @author group 20
 * @version 1.0
 */
public class GuestNewOrderSingleController implements Initializable{

/**
 * @param parkOptions this is a choise box componet for park options in the system
 * @param selectedDate this is a date picker componet for selected date in the system
 * @param timeOptions this is a ChoiceBox componet for time options in the system
 * @param totalVisitorsText this is a text field componet for total visitors text in the system
 * @param paymentOption this is a choise box componet for payment option in the system
 * @param IdInput this is a text field componet for id input in the system
 * @param firstNameInput this is a text field componet for first name input in the system
 * @param lastNameInput this is a text field componet for the last name input in the system
 * @param phoneNumberInput this is a text field componet for the phone number input in the system
 * @param EmailText this is a text field componet for the email text in the system
 * @param checkButton this is a button componet for check button in the system
 * @param cancelButton this is a image view componet for the cancel button in the system
 * @param parkNameError this is a text componet for park name error in the system
 * @param dateError this is a text componet for date error in the system
 * @param timeError this is a text componet for time error in the system
 * @param visitorError this is a text componet for visitors error in the system.
 * @param emailError this is a text componet for email error in the system
 * @param phoneError this is a text componet for phoneError in the system
 * @param lastNameError this is a text componet for lastNameError in the system
 * @param firstNameError this is a text componet for firstNameError in the system
 * @param paymentError this is a text componet for payment error in the system
 * @param idError this is a text componet for id error in the system
 * @param paymentTextonly this is a text componet for payment text in the system
 * @param paymentRedError this is a text componet for payment red error in the system
 * @param travelerID this is the travelerID field 
 * @param paymentIsVisible this is a boolean parameter to determine whater payment window will be displayed.
 * @param order this is an order in the system.
 * @param personalData this is personalData in the system in the form of a string
 */
	@FXML
	private ChoiceBox<String> parkOptions;

	@FXML
	private DatePicker selectedDate;

	@FXML
	private ChoiceBox<String> timeOptions;

	@FXML
	private TextField totalVisitorsText;
	
    @FXML
	private ChoiceBox<String> paymentOption;

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
	private Button checkButton;

	@FXML
	private ImageView cancelButton;

	@FXML
	private Text parkNameError;

	@FXML
	private Text dateError;

	@FXML
	private Text timeError;

	@FXML
	private Text visitorsError;

	@FXML
	private Text emailError;

	@FXML
	private Text phoneError;

	@FXML
	private Text lastNameError;

	@FXML
	private Text firstNameError;

    @FXML
    private Text paymentError;

	@FXML
	private Text idError;

	@FXML
	private Text paymentTextOnly;

	@FXML
	private Text paymentRedError;

	private int travelerID;

	private static Order order=null;
	boolean paymentIsVisible = false;

	static String[] personalData;

	/**
	 * this method is incharge of checking the validation of email adress in the system.
	 * 
	 * @param email this is the email we wish to check
	 * @return true in the case of valid email, false otherwise.
	 */
	//  ---- EMAIL ADDRESS VALIDATION ----   \\
	static boolean isValid(String email) 
	{
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		return email.matches(regex);
	}
	/**
	 * this method is a getter for single traveler order.
	 * @return single traveler order
	 */
	//  ---- EXPORTING METHODS START ----   \\
	public static Order getSingleTravelerOrder() {
		return order;
	}
	/**
	 * this method is in charge of displaying the report page link in the form of a string
	 * @return the report page link in the form of a string
	 */
	public static String submittionReportPage() {
		return "GuestNewOrderSubmition.fxml";
	}
	/**
	 * getter for personal data of the class
	 * @return the personal data field in the class
	 */
	public static String[] getPersonalData() {
		return personalData;
	}
	//  ---- EXPORTING METHODS END ----   \\

/**
 * this method checks new order in the system
 * @param event this is the event that causes the system to check for the order.
 * @throws IOException
 */
	@FXML
	void checkNewOrder(MouseEvent event) throws IOException {

		String time;
		String[] temp;
		int hour;

		if(timeOptions.getSelectionModel().getSelectedItem() == null) 
			hour = -1;   // will always make "markEmptyFields" to return false
		else 
		{
			time = timeOptions.getSelectionModel().getSelectedItem();
			temp = time.split(":");// temp[0] will contain the required hour.
			hour = Integer.parseInt(temp[0]);// get hour to int variable.
		}

		LocalDate orderDate = selectedDate.getValue();//get date
		LocalDateTime localTime = null;
		localTime = localTime.now();// get local time. so we can compare with user input. check parameters:
		// (year,month,day,hour)


		if(!markEmpyFields(localTime, orderDate,hour)) return;  // INPUT VALIDATION


		//now check if can create the order.
		int timeOfStay = TravelerControllerClient.getParkTimeOfStay((parkOptions.getSelectionModel().getSelectedIndex()+1));//get time of stay
		int totalVisitors = Integer.parseInt(totalVisitorsText.getText());
		String parkName = parkOptions.getSelectionModel().getSelectedItem();
		String email = EmailText.getText();
		String id = idInput.getText();
		String firstName = firstNameInput.getText();
		String lastName = lastNameInput.getText();
		String phone = phoneNumberInput.getText();


		Time orderTime = new Time(hour, 0, 0);// set time.
		switch (totalVisitors) {
		case 1:// single
			order = new Order(Integer.parseInt(id), OrderType.Single, parkName, java.sql.Date.valueOf(orderDate),
					orderTime, totalVisitors, email, OrderStatus.Pending, timeOfStay);// create order.
			break;

		default:// group
			if (paymentOption.getSelectionModel().getSelectedItem() == "Pre Payment") {
				order = new Order(Integer.parseInt(id), OrderType.PrePaymentGroup, parkName,
						java.sql.Date.valueOf(orderDate), orderTime, totalVisitors, email, OrderStatus.Pending,
						timeOfStay);// create order.
			} else {
				order = new Order(Integer.parseInt(id), OrderType.PrivateGroup, parkName,
						java.sql.Date.valueOf(orderDate), orderTime, totalVisitors, email, OrderStatus.Pending,
						timeOfStay);// create order.
			}
			break;
		}

		if(TravelerControllerClient.checkNewOrder(order)) {// check if we can create the new order.

			personalData = new String[] 
					{
							firstNameInput.getText(), 
							lastNameInput.getText(), 
							phoneNumberInput.getText()
					};
			Parent root = FXMLLoader.load(getClass().getResource("GuestNewOrderSubmition.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Order Submition");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			if(GuestNewOrderSubmitionController.getOrderStatus()) 
				changeStage("Traveler HomePage","TravelerHomePage.fxml", true );
			
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Order Failed");
			alert.setContentText("We are sorry the park is fully booked at this date.\nPlease choose another option.");
			alert.getButtonTypes().remove(ButtonType.OK);
			ButtonType waitingList = new ButtonType("Enter Waiting List");
			ButtonType tableView = new ButtonType("See Available Dates");
			ButtonType cancel = new ButtonType("Cancel");
			alert.getButtonTypes().add(cancel);
			alert.getButtonTypes().add(waitingList);
			alert.getButtonTypes().add(tableView);
			alert.showAndWait();

			if (alert.getResult() == waitingList) {
				System.out.println("waiting list is chosen");
				// now adding the order to the queue
				order.setOrderStatus(OrderStatus.AtWaitingList);
				double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);// get ticket price.
				order.setTotalSum(ticketPrice);
				boolean isAddOrder = TravelerControllerClient.createNewOrder(order);
				if (!isAddOrder) {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Order Failed");
					alert.setContentText("Failed to save your order in our DB.");
					alert.showAndWait();
				}
				else {
					Traveler traveler = new Traveler
							(
									Integer.parseInt(id),
									firstName,
									lastName,
									email,
									phone,
									TravelerType.Single
									);

					boolean isCreated = TravelerControllerClient.createNewTraveler(traveler);//create new traveler.
					if(!isCreated) {
						System.out.println("failed to create new traveler.");
						Alert creationFailed = new Alert(AlertType.ERROR);
						creationFailed.setHeaderText("System ERROR!");
						creationFailed.setContentText("System can not create user.");
						creationFailed.showAndWait();
					}
					else {
						boolean isAddedToQueue = TravelerControllerClient.addToQueue(order);
						if (isAddedToQueue) {
							Alert waitinglistAlert = new Alert(AlertType.CONFIRMATION);
							waitinglistAlert.getButtonTypes().remove(ButtonType.CANCEL);
							waitinglistAlert.setHeaderText("Confirmation message");
							waitinglistAlert.setContentText("You have been added successfully to the waiting list.");
							waitinglistAlert.showAndWait();
							TravelerLoginPageController.setTravelerID(traveler.getID());
							TravelerLoginPageController.setTravelerName(traveler.getFirstName());
							TravelerLoginPageController.setType(traveler.getTravelerType());
						
							changeStage("Traveler HomePage", "TravelerHomePage.fxml", true);
						} else {
							Alert waitinglistAlert = new Alert(AlertType.ERROR);
							waitinglistAlert.getButtonTypes().remove(ButtonType.CANCEL);
							waitinglistAlert.setHeaderText("Failed");
							waitinglistAlert.setContentText("Failed to enter to the waiting list.");
							waitinglistAlert.showAndWait();
						}}}
			} else if (alert.getResult() == tableView) {// show fxml
				order.setOrderStatus(OrderStatus.Pending);//set order status.
				double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);// get ticket price.
				order.setTotalSum(ticketPrice);
				TravelerOrderTableListController.setOrder(order);//set order for next fxml page.
				Traveler traveler = new Traveler 
						(
								Integer.parseInt(id),
								firstName,
								lastName,
								email,
								phone,
								TravelerType.Single
								);

				//TravelerOrderTableListController.setTraveler(traveler);
				//TravelerOrderTableListController.setStage(stage);
				personalData = new String[] 
						{
								firstNameInput.getText(), 
								lastNameInput.getText(), 
								phoneNumberInput.getText()
						};
				System.out.println("tableView is chosen");
				//checkButton.getScene().getWindow().hide();
				TravelerOrderTableListController.setFxmlSubmissionPage("GuestNewOrderSubmition.fxml");
				Parent root = FXMLLoader.load(getClass().getResource("TravelerOrderTableList.fxml"));
				Stage stage = new Stage();//(Stage) cancelButton.getScene().getWindow();
				stage.setTitle("Traveler Order Table List Page");
				stage.setScene(new Scene(root));
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();
				

				TravelerLoginPageController.setTravelerID(traveler.getID());
				TravelerLoginPageController.setTravelerName(traveler.getFirstName());
				TravelerLoginPageController.setType(traveler.getTravelerType());
				
				if(TravelerOrderTableListController.dateChosed()
						&& GuestNewOrderSubmitionController.getOrderStatus()) 
					changeStage("Traveler HomePage", "TravelerHomePage.fxml", true);
//					else {
//						System.out.println("failed to create new traveler.");
//						Alert creationFailed = new Alert(AlertType.ERROR);
//						creationFailed.setHeaderText("System ERROR!");
//						creationFailed.setContentText("System can not create user.");
//						creationFailed.showAndWait();
//					}
				


			} else if (alert.getResult() == cancel) {
				System.out.println("cancel is chosen");
			}
		}
	}

/**
 * this method closes the current displayed window in the gui
 * @param event this is the event that triggers the cancel and close window action.
 * @throws IOException
 */
	@FXML
	void cancelAndCloseWindow(MouseEvent event) throws IOException {
		// show alert message if the user sure he wants to cancel.
		Alert alert = new Alert(AlertType.NONE);
		alert.setHeaderText("Warning!");
		alert.setContentText("Are you sure you want to cancel ? (all information will be lost!)");
		ButtonType yesButton = ButtonType.YES;
		ButtonType noButton = ButtonType.NO;
		alert.getButtonTypes().remove(ButtonType.OK);
		alert.getButtonTypes().add(yesButton);
		alert.getButtonTypes().add(noButton);
		alert.showAndWait();
		if (alert.getResult() == yesButton) {
			TravelerControllerClient.removeOnlineTraveler(travelerID + "");
			changeStage("Traveler Page", "TravelerLoginPage.fxml", false);
		}
	}
	/**
	 * this method is in charge of shifting the stage in the gui
	 * @param heading the heading string
	 * @param fxmlFile the fxml file we need
	 * @param setOnClose boolean parameter
	 * @throws IOException
	 */
	private void changeStage(String heading, String fxmlFile, boolean setOnClose) throws IOException
	{
		Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.setTitle(heading);
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
		if(setOnClose)
		stage.setOnCloseRequest(e -> {
			TravelerControllerClient.removeOnlineTraveler(travelerID + "");
		});
	}
	/**
	 * this method is in charge of initallizing to the class 
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		parkOptions.getItems().add("Park A");
		parkOptions.getItems().add("Park B");
		parkOptions.getItems().add("Park C");
		timeOptions.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00");
		travelerID = TravelerLoginPageController.getID();// Initialize traveler id
		idInput.setText(travelerID+"");
		idInput.setEditable(false);
		paymentOption.getItems().addAll("Pre Payment", "Pay At The Park");
		paymentOption.setVisible(false);
		paymentRedError.setVisible(false);
		paymentTextOnly.setVisible(false);
		paymentError.setVisible(false);
		//payment option listener.
		totalVisitorsText.textProperty().addListener((observable, oldValue, newValue) -> {
			if((!newValue.contentEquals("")) && newValue.matches(("[0-9]+"))) {
			if(Integer.parseInt(newValue) > 1) {
				paymentIsVisible = true;
				paymentOption.setVisible(true);
				paymentRedError.setVisible(true);
				paymentTextOnly.setVisible(true);
//				paymentError.setVisible(true);
			} else {
				paymentOption.setVisible(false);
				paymentRedError.setVisible(false);
				paymentTextOnly.setVisible(false);
//				paymentError.setVisible(false);
				paymentIsVisible = false;
			}
			}
		});
	}
	/**
	 * this method is in charge of marking empty fields in the gui 
	 * @param localTime gets the local time
	 * @param orderDate gets the order date
	 * @param hour gets the hour
	 * @return true if successeed to mark empty fields false otherwise.
	 */
	//  ---- INPUT VALIDATION ----   \\
	private boolean markEmpyFields(LocalDateTime localTime, LocalDate orderDate, int hour) {
		boolean returnValue = true;

		String alertedComponent = "-fx-border-color : #ff0000; -fx-background-color :  #ffffff";
		String validComponent = "-fx-background-color :  #ffffff";

		if( parkOptions.getSelectionModel().getSelectedItem() == null) {
			parkNameError.setVisible(true);
			parkOptions.setStyle(alertedComponent);
			returnValue = false;
		}
		else {
			parkNameError.setVisible(false);
			parkOptions.setStyle(validComponent);
		}

		if(selectedDate.getValue() == null) {
			dateError.setText("* Required field");
			dateError.setVisible(true);
			selectedDate.setStyle(alertedComponent);
			returnValue = false;
		}
		else if(localTime.getYear() > orderDate.getYear() || 
				(localTime.getDayOfYear() > orderDate.getDayOfYear() && localTime.getYear() == orderDate.getYear())) {
			dateError.setText("* Choose valid date");
			dateError.setVisible(true);
			selectedDate.setStyle(alertedComponent);
			returnValue = false; 		 
		}
		else {
			dateError.setVisible(false);
			selectedDate.setStyle(validComponent);
		}
		if(timeOptions.getSelectionModel().getSelectedItem() == null) {
			timeError.setText("* Required field");
			timeError.setVisible(true);
			timeOptions.setStyle(alertedComponent);
			returnValue = false;
		}
		else if(selectedDate.getValue()!=null && 
				        (
						localTime.getDayOfYear() == orderDate.getDayOfYear() && 
						localTime.getYear() == orderDate.getYear() && 
						localTime.getHour() >= hour
						)
				){
			timeError.setText("* Choose valid time");
			timeError.setVisible(true);
			timeOptions.setStyle(alertedComponent);
			returnValue = false;
		}
		else {
			timeError.setVisible(false);
			timeOptions.setStyle(validComponent);
		}

		if(EmailText.getText().equals("")) {
			emailError.setText("* Required field");
			emailError.setVisible(true);
			EmailText.setStyle(alertedComponent);
			returnValue = false;
		}
		else if(!isValid(EmailText.getText())){
			emailError.setText("* Unvalid email address");
			emailError.setVisible(true);
			EmailText.setStyle(alertedComponent);
			returnValue = false;
		} 
		else{
			emailError.setVisible(false);
			EmailText.setStyle(validComponent);
		}

		String tmpText = totalVisitorsText.getText();

		if(tmpText.contentEquals("")) {
			visitorsError.setText("* Required field");
			visitorsError.setVisible(true);
			totalVisitorsText.setStyle(alertedComponent);
			returnValue = false;
		} 
		else if(!tmpText.matches("[0-9]+")) {//check if it is a number
			visitorsError.setText("* Enter numbers only");
			visitorsError.setVisible(true);
			totalVisitorsText.setStyle(alertedComponent);
			returnValue = false;
		}
		else if(Integer.parseInt(tmpText) <=0 
				|| Integer.parseInt(tmpText) >15) {//check if it is a number
			visitorsError.setText("* Enter numbers between 1 to 15");
			visitorsError.setVisible(true);
			totalVisitorsText.setStyle(alertedComponent);
			returnValue = false;
		}
		else {
			visitorsError.setVisible(false);
			totalVisitorsText.setStyle(validComponent);
		}

		tmpText = idInput.getText();

		if(tmpText.contentEquals("")) {
			idError.setText("* Required field");
			idError.setVisible(true);
			idInput.setStyle(alertedComponent);
			returnValue = false;
		} 		
		else if (!tmpText.matches("[0-9]+")) {
			idError.setText("* Enter numbers only");
			idError.setVisible(true);
			idInput.setStyle(alertedComponent);
			returnValue = false;
		}
		else {
			idError.setVisible(false);
			idInput.setStyle(validComponent);
		}

		tmpText = firstNameInput.getText();

		if(tmpText.contentEquals("")) {
			firstNameError.setText("* Required field");
			firstNameError.setVisible(true);
			firstNameInput.setStyle(alertedComponent);
			returnValue = false;
		} 		
		else if (!tmpText.matches("^[a-zA-Z]*$")) {
			firstNameError.setText("* Enter letters only");
			firstNameError.setVisible(true);
			firstNameInput.setStyle(alertedComponent);
			returnValue = false;
		}
		else {
			firstNameError.setVisible(false);
			firstNameInput.setStyle(validComponent);
		}

		tmpText = lastNameInput.getText();

		if(tmpText.contentEquals("")) {
			lastNameError.setText("* Required field");
			lastNameError.setVisible(true);
			lastNameInput.setStyle(alertedComponent);
			returnValue = false;
		} 		
		else if (!tmpText.matches("^[a-zA-Z]*$")) {
			lastNameError.setText("* Enter letters only");
			lastNameError.setVisible(true);
			lastNameInput.setStyle(alertedComponent);
			returnValue = false;
		}
		else {
			lastNameError.setVisible(false);
			lastNameInput.setStyle(validComponent);
		}
		//^[a-z]{,50}$
		tmpText = phoneNumberInput.getText();

		if(tmpText.contentEquals("")) {
			phoneError.setText("* Required field");
			phoneError.setVisible(true);
			phoneNumberInput.setStyle(alertedComponent);
			returnValue = false;
		} 		
		else if ((!tmpText.matches("[0-9]+")) || tmpText.length() > 10) {
			phoneError.setText("* Unvalid phone number");
			phoneError.setVisible(true);
			phoneNumberInput.setStyle(alertedComponent);
			returnValue = false;
		}
		else {
			phoneError.setVisible(false);
			phoneNumberInput.setStyle(validComponent);
		}

		if( paymentIsVisible && paymentOption.getSelectionModel().getSelectedItem() == null ) {
			paymentError.setText("* Required field");
			paymentError.setVisible(true);
			paymentOption.setStyle(alertedComponent);
			returnValue = false;
		} else {
			paymentError.setVisible(false);
			paymentOption.setStyle("-fx-background-color :  #ffffff");
		}
		return returnValue;
	}
}
