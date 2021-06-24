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
 * 
 * This class is the Controller for the New Single Traveler in the park.
 * 
 * @version 1.0
 * @author Group 20
 *
 */
public class TravelerNewOrderSingleController implements Initializable {

	/**
	 * 
	 * @param parkOptions the available parks to choose from.
	 * @param selectedDate the available datess to choose from.
	 * @param emailTextInput the TextField where the user needs to put his email.
	 * @param timeOptions the available times to choose from.
	 * @param checkNewOrder button to check if the order is available.
	 */
    @FXML
    private ChoiceBox<String> parkOptions;
    @FXML
    private DatePicker selectedDate;
    @FXML
    private TextField emailTextInput;
    @FXML
    private ChoiceBox<String> timeOptions;
    @FXML
    private Button checkNewOrder;
    @FXML
    private ImageView cancelImage;
    @FXML
    private Text parkNameError;
    @FXML
    private Text dateError;
    @FXML
    private Text timeError;
    @FXML
    private Text emailError;
    
	private int travelerID;
	private static Order order = null;
	private static Order tableList= null;
	
	/**
	 * ---- Email address validator ----
	 */
	   static boolean isValid(String email) {
		      String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		      return email.matches(regex);
	   }
	   
		public static Order getOrder() {
			return tableList;
		}
	   
	   public static Order getSingleTravelerOrder() {
		   return order;
	   }
	/**
	 * Method that cancel the outgoing order and let the user go the previous page.
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
			cancelImage.getScene().getWindow().hide();
		}
	}

    /**
     * Method that checks if the wanted order from the user is available or not.
     */
	@FXML
	void checkNewOrder(MouseEvent event) throws IOException {

		String parkName = parkOptions.getSelectionModel().getSelectedItem();
		LocalDate orderDate = selectedDate.getValue();
		//String time = timeOptions.getSelectionModel().getSelectedItem();
		String time;
		String orderEmail = emailTextInput.getText();
		LocalDateTime localTime = null;
		localTime = localTime.now();// get local time. so we can compare with user input. check parameters:
									// (year,month,day,hour)		

		String[] temp;
		int hour;
		Time orderTime;
		
		if(timeOptions.getSelectionModel().getSelectedItem() == null) 
			hour = -1;
		
		else {
		time = timeOptions.getSelectionModel().getSelectedItem();
		temp = time.split(":");// temp[0] will contain the required hour.
	    hour = Integer.parseInt(temp[0]);// get hour to int variable.
		}


		if(!markEmpyFields(localTime, orderDate,hour)) return;


	Traveler traveler = TravelerControllerClient.GetTravelerInfoByID(travelerID + "");// get traveler info.
    orderTime = new Time(hour, 0, 0);// set time.
	order=null;

	switch(traveler.getTravelerType())
	{
		case Single:
			int timeOfStay = (int) TravelerControllerClient.getParkTimeOfStay(parkOptions.getSelectionModel().getSelectedIndex()+1);
			order = new Order(travelerID, OrderType.Single, parkName, java.sql.Date.valueOf(orderDate), orderTime, 1,
					orderEmail, OrderStatus.Pending,timeOfStay);
			break;
		case SingleSubscription:
			 timeOfStay = (int) TravelerControllerClient.getParkTimeOfStay(parkOptions.getSelectionModel().getSelectedIndex()+1);
			order = new Order(travelerID, OrderType.SingleSubscription, parkName, java.sql.Date.valueOf(orderDate),
					orderTime, 1, orderEmail, OrderStatus.Pending,timeOfStay);
			break;
		default:
			break;
			
		}
	if(TravelerControllerClient.checkNewOrder(order))
	{
		double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);// get ticket price.
		order.setTotalSum(ticketPrice);
		Parent root = FXMLLoader.load(getClass().getResource("TravelerSingleOrderSubmition.fxml"));
		Stage stage = new Stage();
		stage.setTitle("Order Submition");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();

		if (TravelerSingleOrderSubmitionController.getOrderStatus()) {
			cancelImage.getScene().getWindow().hide();
		}

	}else
	{

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
			ArrayList<Order> list = TravelerControllerClient.GetTravelerOrders(travelerID + "");  // create hash-code and equals in Order and use it here
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
				checkNewOrder.getScene().getWindow().hide();
			} else {
				Alert waitinglistAlert = new Alert(AlertType.ERROR);
				waitinglistAlert.getButtonTypes().remove(ButtonType.CANCEL);
				waitinglistAlert.setHeaderText("Failed");
				waitinglistAlert.setContentText("Failed to enter to the waiting list.");
				waitinglistAlert.showAndWait();
			}
		} else if (alert.getResult() == tableView) {// show fxml
			if(order!=null) {
				TravelerOrderTableListController.setOrder(order);
			}
			else {
				System.out.println("order is null");
				return;
			}
			System.out.println("tableView is chosen");
			//checkNewOrder.getScene().getWindow().hide();
			TravelerOrderTableListController.setFxmlSubmissionPage("TravelerSingleOrderSubmition.fxml");
			Parent root = FXMLLoader.load(getClass().getResource("TravelerOrderTableList.fxml"));
			Stage stage = new Stage();//(Stage) cancelButton.getScene().getWindow();
			stage.setTitle("Traveler Order Table List Page");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			
			if(TravelerOrderTableListController.dateChosed() &&
					TravelerSingleOrderSubmitionController.getOrderStatus()) {
				cancelImage.getScene().getWindow().hide();
			}
			
			
		} else if (alert.getResult() == cancel) {
			System.out.println("cancel is chosen");
			//submitButton.getScene().getWindow().hide();
		}
	}
	}
	/**
	 * Method that helps the user to put correct input to make the order.
	 */
	private boolean markEmpyFields(LocalDateTime localTime, LocalDate orderDate, int hour) {
		
		boolean returnValue = true;

		if( parkOptions.getSelectionModel().getSelectedItem() == null) {
			parkNameError.setVisible(true);
			parkOptions.setStyle("-fx-border-color : #ff0000; -fx-background-color :  #ffffff");
			returnValue = false;
		}
		else {
			parkNameError.setVisible(false);
			parkOptions.setStyle("-fx-background-color :  #ffffff");
		}
		
		if(selectedDate.getValue() == null) {
			dateError.setText("* Required field");
			dateError.setVisible(true);
			selectedDate.setStyle("-fx-border-color : #ff0000; -fx-background-color :  #ffffff");
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
			timeOptions.setStyle("-fx-border-color : #ff0000; -fx-background-color :  #ffffff");
			returnValue = false;
		}
		else if(selectedDate.getValue()!=null && 
				(localTime.getDayOfYear() == orderDate.getDayOfYear() && localTime.getYear() == orderDate.getYear() && localTime.getHour() >= hour)){
		    timeError.setText("* Choose valid time");
		    timeError.setVisible(true);
		    timeOptions.setStyle("-fx-border-color : #ff0000; -fx-background-color :  #ffffff");
		    returnValue = false;
		}
		else {
			timeError.setVisible(false);
			timeOptions.setStyle("-fx-background-color :  #ffffff");
		}
		
		if(emailTextInput.getText().equals("")) {
			emailError.setText("* Required field");
			emailError.setVisible(true);
			emailTextInput.setStyle("-fx-border-color : #ff0000; -fx-background-color :  #ffffff");
			returnValue = false;
		}
		else if(!isValid(emailTextInput.getText())){
			emailError.setText("* Unvalid email address");
			emailError.setVisible(true);
			emailTextInput.setStyle("-fx-border-color : #ff0000; -fx-background-color :  #ffffff");
			returnValue = false;
		} 
		else{
			emailError.setVisible(false);
			emailTextInput.setStyle("-fx-background-color :  #ffffff");
		}
		
		return returnValue;
	}
	/**
	 * Method that initialize the page with the right names and parameters.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		travelerID = TravelerLoginPageController.getID();// Initialize traveler id
		Traveler traveler = TravelerControllerClient.GetTravelerInfoByID(travelerID+"");
        emailTextInput.setText(traveler.getEmail());
		parkOptions.getItems().add("Park A");
		parkOptions.getItems().add("Park B");
		parkOptions.getItems().add("Park C");

		timeOptions.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
				"17:00");
		timeOptions.setValue(null);
		
	}

}
