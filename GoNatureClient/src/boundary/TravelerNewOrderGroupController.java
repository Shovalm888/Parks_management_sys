package boundary;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * This class is the Controller for the New Group Order Page in the park.
 * @version 1.0
 * @author Group 20

 */
public class TravelerNewOrderGroupController implements Initializable {
/**
 * @param parkOptions the available parks to choose from.
 * @param selectedDate the available datess to choose from.
 * @param totalVisitorsText the num of visitors in the group.
 * @param emailText the TextField where the user needs to put his email.
 * @param timeOptions the available times to choose from.
 * @param paymentOption what type of payment the group choose.
 * @param checkButton button to check if the order is available.
 */
	private static Order tableList;

    @FXML
    private ChoiceBox<String> parkOptions;
    @FXML
    private DatePicker selectedDate;
    @FXML
    private TextField totalVisitorsText;
    @FXML
    private TextField EmailText;
    @FXML
    private ChoiceBox<String> timeOptions;
    @FXML
    private ChoiceBox<String> paymentOption;
    @FXML
    private Button checkButton;
    @FXML
    private ImageView cancelButton;
    @FXML
    private Text nameError;
    @FXML
    private Text dateError;
    @FXML
    private Text timeError;
    @FXML
    private Text visitorsError;
    @FXML
    private Text emailError;
    @FXML
    private Text paymentError;

	private int travelerID;
	private static Order order=null;

	// ---- Email address validator ----
	   static boolean isValid(String email) {
		      String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		      return email.matches(regex);
	   }
	   
	   public static Order getSingleTravelerOrder() {
		   return order;
	   }
	 
	   /**
	    * Method that cancel the out-going order and returns to the previous page.
	    */
	@FXML
	void cancelAndCloseWindow(MouseEvent event) {
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
			cancelButton.getScene().getWindow().hide();
		}

	}
	/**
	 * Method that checks if the order is available and shows the relevant message.
	 */
	@FXML
	void checkNewOrder(MouseEvent event) throws IOException {
		String parkName = parkOptions.getSelectionModel().getSelectedItem();// get park
		LocalDate orderDate = selectedDate.getValue();// get date
		String time;
		String[] temp;
		int hour;
		Time orderTime;
		String orderEmail = EmailText.getText();// get email

		// if(Integer.parseInt(totalVisitorsText.gette))

		LocalDateTime localTime = null;
		localTime = localTime.now();// get local time. so we can compare with user input. check parameters:
									// (year,month,day,hour)

		if (timeOptions.getSelectionModel().getSelectedItem() == null)
			hour = -1;
		else {
			time = timeOptions.getSelectionModel().getSelectedItem();
			temp = time.split(":");// temp[0] will contain the required hour.
			hour = Integer.parseInt(temp[0]);// get hour to int variable.
		}

		if (!markEmpyFields(localTime, orderDate, hour))
			return;

		orderTime = new Time(hour, 0, 0);// set time.

		Traveler traveler = TravelerControllerClient.GetTravelerInfoByID(travelerID + "");// get traveler info.
		order = null;
		switch (paymentOption.getSelectionModel().getSelectedItem()) {
		case "Pre Payment":// private group that pay before arriving.
			int timeOfStay = (int) TravelerControllerClient
					.getParkTimeOfStay(parkOptions.getSelectionModel().getSelectedIndex() + 1);
			order = new Order(travelerID, OrderType.PrePaymentGroup, parkName, java.sql.Date.valueOf(orderDate),
					orderTime, Integer.parseInt(totalVisitorsText.getText()), orderEmail, OrderStatus.Pending,
					timeOfStay);
			if (traveler.getTravelerType() == TravelerType.Guide) {// if its guide who is making the order.
				order.setOrderType(OrderType.PrePaymentOrganizedGroup);
			}
			break;
		case "Pay At The Park":// private group that pay at the park..
			timeOfStay = (int) TravelerControllerClient
					.getParkTimeOfStay(parkOptions.getSelectionModel().getSelectedIndex() + 1);
			order = new Order(travelerID, OrderType.PrivateGroup, parkName, java.sql.Date.valueOf(orderDate), orderTime,
					Integer.parseInt(totalVisitorsText.getText()), orderEmail, OrderStatus.Pending, timeOfStay);
			if (traveler.getTravelerType() == TravelerType.Guide) {// if its guide who is making the order.
				order.setOrderType(OrderType.OrganizedGroup);
			}
			break;
		default:
			System.out.println("error in getting payment option.");
			break;
		}
		if (TravelerControllerClient.checkNewOrder(order)) {// check if we can create the new order.

			double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);// get ticket price.
			order.setTotalSum(ticketPrice);
			Parent root = FXMLLoader.load(getClass().getResource("TravelerNewOrderGroupSubmition.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Order Submition");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();

			if (TravelerNewOrderGroupSubmitionController.getOrderStatus()) {
				cancelButton.getScene().getWindow().hide();
			}
		} else {

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
					return;
				}
				ArrayList<Order> list = TravelerControllerClient.GetTravelerOrders(travelerID + ""); // create hash-code
																										// and equals in
																										// Order and use
																										// it here
				for (Order array : list) {
					if (array.getOrderDate().equals(order.getOrderDate())
							&& array.getOrderTime().equals(order.getOrderTime())
							&& array.getPark().equals(order.getPark())) {
						order.setOrderID(array.getOrderID());// set the real orderid as saved in db in the queue.
					}
				}
				boolean isAddedToQueue = TravelerControllerClient.addToQueue(order);
				if (isAddedToQueue) {
					Alert waitinglistAlert = new Alert(AlertType.CONFIRMATION);
					waitinglistAlert.getButtonTypes().remove(ButtonType.CANCEL);
					waitinglistAlert.setHeaderText("Confirmation message");
					waitinglistAlert.setContentText("You have been added successfully to the waiting list.");
					waitinglistAlert.showAndWait();
					checkButton.getScene().getWindow().hide();
				} else {
					Alert waitinglistAlert = new Alert(AlertType.ERROR);
					waitinglistAlert.getButtonTypes().remove(ButtonType.CANCEL);
					waitinglistAlert.setHeaderText("Failed");
					waitinglistAlert.setContentText("Failed to enter to the waiting list.");
					waitinglistAlert.showAndWait();
				}
			} else if (alert.getResult() == tableView) {// show fxml
				if (order != null) {
					TravelerOrderTableListController.setOrder(order);
				} else {
					System.out.println("order is null");
				}
				System.out.println("tableView is chosen");
				TravelerOrderTableListController.setFxmlSubmissionPage("TravelerNewOrderGroupSubmition.fxml");
				Parent root = FXMLLoader.load(getClass().getResource("TravelerOrderTableList.fxml"));
				Stage stage = new Stage();// (Stage) cancelButton.getScene().getWindow();
				stage.setTitle("Traveler Order Table List Page");
				stage.setScene(new Scene(root));
				stage.setResizable(false);
				stage.initModality(Modality.APPLICATION_MODAL);
				stage.showAndWait();

				if (TravelerOrderTableListController.dateChosed()
						&& TravelerNewOrderGroupSubmitionController.getOrderStatus())
					cancelButton.getScene().getWindow().hide();

			} else if (alert.getResult() == cancel) {
				System.out.println("cancel is chosen");
				// submitButton.getScene().getWindow().hide();
			}
		}
	}
	/**
	 * Method that helps the user to put correct input to make the order.
	 */
	private boolean markEmpyFields(LocalDateTime localTime, LocalDate orderDate, int hour) {
		boolean returnValue = true;
		
		String alertedComponent = "-fx-border-color : #ff0000; -fx-background-color :  #ffffff";
		
		if( parkOptions.getSelectionModel().getSelectedItem() == null) {
			nameError.setVisible(true);
			parkOptions.setStyle(alertedComponent);
			returnValue = false;
		}
		else {
		    nameError.setVisible(false);
			parkOptions.setStyle("-fx-background-color :  #ffffff");
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
			selectedDate.setStyle("-fx-border-color : #ff0000; -fx-background-color :  #ffffff");
			returnValue = false; 		 //LocalDateTime.now();

		}
		else {
			dateError.setVisible(false);
			selectedDate.setStyle("-fx-background-color :  #ffffff");
		}
		if(timeOptions.getSelectionModel().getSelectedItem() == null) {
			timeError.setText("* Required field");
			timeError.setVisible(true);
			timeOptions.setStyle(alertedComponent);
			returnValue = false;
		}
		else if(selectedDate.getValue()!=null && 
				(localTime.getDayOfYear() == orderDate.getDayOfYear() && localTime.getYear() == orderDate.getYear() && localTime.getHour() >= hour)){
		    timeError.setText("* Choose valid time");
		    timeError.setVisible(true);
		    timeOptions.setStyle(alertedComponent);
		    returnValue = false;
		}
		else {
			timeError.setVisible(false);
			timeOptions.setStyle("-fx-background-color :  #ffffff");
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
			EmailText.setStyle("-fx-background-color :  #ffffff");
		}
		
		String textID = totalVisitorsText.getText();
		
		if(textID.contentEquals("")) {
			visitorsError.setText("* Required field");
			visitorsError.setVisible(true);
			totalVisitorsText.setStyle(alertedComponent);
			returnValue = false;
		} 
		else if(!textID.matches("[0-9]+")) {//check if it is a number
			visitorsError.setText("* Enter numbers only");
			visitorsError.setVisible(true);
			totalVisitorsText.setStyle(alertedComponent);
			returnValue = false;
		}
		else if(Integer.parseInt(textID) <=1 
				|| Integer.parseInt(textID) >15) {//check if it is a number
			visitorsError.setText("* Enter numbers between 2 to 15");
			visitorsError.setVisible(true);
			totalVisitorsText.setStyle(alertedComponent);
			returnValue = false;
		}
		else {
			visitorsError.setVisible(false);
			totalVisitorsText.setStyle("-fx-background-color :  #ffffff");
		}
		
		if(paymentOption.getSelectionModel().getSelectedItem() == null) {
			paymentError.setText("* Required field");
			paymentError.setVisible(true);
			paymentOption.setStyle(alertedComponent);
			returnValue = false;
		}
		else {
			paymentError.setVisible(false);
			paymentOption.setStyle("-fx-background-color :  #ffffff");
		}
		
		return returnValue;
	}

	public static Order getOrder() {
		return tableList;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		travelerID = TravelerLoginPageController.getID();// Initialize traveler id
		Traveler traveler = TravelerControllerClient.GetTravelerInfoByID(travelerID+"");
        EmailText.setText(traveler.getEmail());
		parkOptions.getItems().add("Park A");
		parkOptions.getItems().add("Park B");
		parkOptions.getItems().add("Park C");
		timeOptions.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
				"17:00");

		paymentOption.getItems().addAll("Pre Payment", "Pay At The Park");
	}

}
