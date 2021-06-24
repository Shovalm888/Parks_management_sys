package boundary;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import entity.Order;
import entity.SubscriptionTraveler;
import enums.OrderStatus;
import enums.OrderType;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import server.ConnectionToDB;
import server.TravelerControllerServer;

/**
 * this class is responsibble of entry and exit of travelers in the park,
 * meaning all travelers go through the cardReader, it updates parameters in the
 * DB accordinly.
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class CardReaderController {
	/**
	 * @param IDOption this a componet to enter ID
	 * 
	 * @param IDtg               this is a componet to group the radio button
	 * @param SubscriptionOption this is a componet for subscription option.
	 * @param OrderIDOption      this is a componet for order ID option
	 * @param IDTextInPut        this is a text field for the user to enter his ID.
	 * @param enterOption        this is a componet to press enter.
	 * @param statusTG           this is a componet to group the optins of the
	 *                           status
	 * @param exitOption         this is a componet to allow the user to exit
	 * @param submitButton       this is a componet to allow the user to submit
	 * @param goBackButton       this is a componet to allow the user to go back.
	 */

    @FXML
    private RadioButton IDOption;
    @FXML
    private ToggleGroup IDtg;
    @FXML
    private RadioButton SusbcriptionOption;
    @FXML
    private RadioButton OrderIDOption;
    @FXML
    private TextField IDTextInput;
    @FXML
    private RadioButton enterOption;
    @FXML
    private ToggleGroup statusTG;
    @FXML
    private RadioButton exitOption;
    @FXML
    private Button submitButton;
    @FXML
    private Button goBackButton;
    /**
     * this method exits the card reader stage
     * @param event this is the event that triggers go back feature.
     */
    @FXML
    void goBack(MouseEvent event) {
    	goBackButton.getScene().getWindow().hide();
    }
    /**
     * this method is the event of submitting entry/exit
     * @param event event this is the event that triggers submitSelectedParkStatus feature.(enter/exit)
     * @throws SQLException exception
     */
    @FXML
    void submitSelectedParkStatus(MouseEvent event) throws SQLException {
    	if(IDTextInput.getText().equals("")) {
    		Alert alert = new Alert(AlertType.ERROR,"Please enter valid number.");
    		alert.showAndWait();
    		return;
    	}
    	//get selected options.
    	RadioButton rb = (RadioButton) IDtg.getSelectedToggle();
    	RadioButton rb2 = (RadioButton) statusTG.getSelectedToggle();
    	String selectedID = rb.getText();
    	String selectedStatus = rb2.getText();
    	
    	String id = IDTextInput.getText();
    	if(!id.matches("[0-9]+")) {//check if it is a number
		Alert alert = new Alert(AlertType.ERROR,"Please enter numbers only.");
		alert.showAndWait();
		return;
		}
    	switch (selectedID) {
		case "ID":
			if(selectedStatus.equals("Enter")) {//check if we can enter id.
				ArrayList<Order> travelerOrders = TravelerControllerServer.getTravelerOrders(id);//get travelers orders by id.
				LocalDate currentDate = LocalDate.now();
				LocalTime currentTime = LocalTime.now();
				if(travelerOrders.isEmpty()) {
					Alert alert = new Alert(AlertType.ERROR,"There is no orders for " + id + ".\nPlease enter valid ID nubmer.");
					alert.showAndWait();
					return;
				}
				for(Order o: travelerOrders){
					if( o.getOrderDate().toLocalDate().isEqual(currentDate) // if its the same date. 
							&& o.getOrderTime().toLocalTime().isBefore(currentTime) // order time is < the current time.
							&& (o.getOrderTime().toLocalTime().plusHours(o.getTimeOfStay())).isAfter(currentTime)  //check time (o.time <= current <= o.time+timeOfStay)
							&& o.getOrderStatus() == OrderStatus.Approved) { // check if order is approved.
						//change order status to currently at the park
						changeParkOrderStatus(o.getOrderID());// changed status to 'currentAtThePark'.
						// update park capacity.
						updateParkCapacity(o);
						checkOccupancy();
						Alert alert = new Alert(AlertType.CONFIRMATION,
								"Enter Order " + o.getOrderID() + " finished with success.\n" + o.getNumOfTravelers()
										+ " more travelers entered " + o.getPark());
						alert.showAndWait();
						return;
					}
				}
				 // if loop is finished without success, means no order is available for entering the park.
						Alert alert = new Alert(AlertType.ERROR,
								"There is no available orders to process for Traveler ID "
										+ IDTextInput.getText());
						alert.showAndWait();
						return;
			} else { //"exit" is selected.
				ArrayList<Order> travelerOrders = TravelerControllerServer.getTravelerOrders(id);//get travelers orders by id.
						for (Order order : travelerOrders) {
							if (order.getOrderStatus() == OrderStatus.CurrentlyAtThePark) { // get traveler order with status that is currentlyAtThePark.
								boolean isExited = exitTravelerFromPark(order);//change status to 'Done'.
								//change park capacity.
								if(isExited) {
									order.setNumOfTravelers(order.getNumOfTravelers() * (-1)); // set numOfTravelers to be minus numOfTravelers.
									updateParkCapacity(order);
									Alert alert = new Alert(AlertType.CONFIRMATION,
											"Traveler " + order.getTravlerID()
													+ " Finished his Park visit and left the park.\n"
													+ (order.getNumOfTravelers()*(-1)) + " more travelers left "
													+ order.getPark()+"\n" + "Order " + order.getOrderID()
													+ " finished with success.\nOrder status changed to 'Done'.");
									alert.showAndWait();
									return;
								}
							}
						}
						Alert alert = new Alert(AlertType.ERROR,
								"Can't process Exit for traveler " + IDTextInput.getText() + ".\nBecause Traveler "
										+ IDTextInput.getText() + " isn't at the park right now.");
						alert.showAndWait();
						return;
					}
		case "Subscription ID":
			if(selectedStatus.equals("Enter")) {//check if we can enter id.
				SubscriptionTraveler subscriptionTraveler = TravelerControllerServer.getTravelerInfoBySubscription(IDTextInput.getText());//get subscription traveler by subscription id number.
				if(subscriptionTraveler == null) {
					Alert alert = new Alert(AlertType.ERROR,"There is no such Subscription ID.\nPlease enter valid ID.");
					alert.showAndWait();
					return;
				}
				ArrayList<Order> travelerOrders = TravelerControllerServer.getTravelerOrders(subscriptionTraveler.getID()+"");//get travelers orders by id.
				LocalDate currentDate = LocalDate.now();
				LocalTime currentTime = LocalTime.now();
				if(travelerOrders.isEmpty()) {
					Alert alert = new Alert(AlertType.ERROR,"There is no orders for " + id + ".\nPlease enter valid Subscription number..");
					alert.showAndWait();
					return;
				}
				for(Order o: travelerOrders){
					if( o.getOrderDate().toLocalDate().isEqual(currentDate) // if its the same date. 
							&& o.getOrderTime().toLocalTime().isBefore(currentTime) // order time is < the current time.
							&& (o.getOrderTime().toLocalTime().plusHours(o.getTimeOfStay())).isAfter(currentTime)  //check time (o.time <= current <= o.time+timeOfStay)
							&& o.getOrderStatus() == OrderStatus.Approved) { // check if order is approved.
						//change order status to currently at the park
						changeParkOrderStatus(o.getOrderID());// changed status to 'currentAtThePark'.
						// update park capacity.
						updateParkCapacity(o);
						checkOccupancy();
						Alert alert = new Alert(AlertType.CONFIRMATION,"Enter Order " + o.getOrderID() + " finished with success.\n" + o.getNumOfTravelers()
						+ " more travelers entered " + o.getPark());
						alert.showAndWait();
						return;
					}
				}//if loop is finished without success, means no order is available for entering the park.
						Alert alert = new Alert(AlertType.ERROR,"There is no available orders to process for Subscription ID " + subscriptionTraveler.getSubscriptionID());
						alert.showAndWait();
						return;
			} else { //"exit" is selected.
					SubscriptionTraveler subscriptionTraveler = TravelerControllerServer.getTravelerInfoBySubscription(IDTextInput.getText());//get subscription traveler by subscription id number.
					if(subscriptionTraveler == null) {
						Alert alert = new Alert(AlertType.ERROR,"There is no such Subscription ID.\nPlease enter valid ID.");
						alert.showAndWait();
						return;
					}
					ArrayList<Order> travelerOrders = TravelerControllerServer.getTravelerOrders(subscriptionTraveler.getID()+"");//get travelers orders by id.
						for (Order order : travelerOrders) {
							if (order.getOrderStatus() == OrderStatus.CurrentlyAtThePark) { // get traveler order with status that is currentlyAtThePark.
								boolean isExited = exitTravelerFromPark(order);//change status to 'Done'.
								//change park capacity.
								if(isExited) {
									order.setNumOfTravelers(order.getNumOfTravelers()*(-1)); // set numOfTravelers to be minus numOfTravelers.
									updateParkCapacity(order);
									Alert alert = new Alert(AlertType.CONFIRMATION,"Traveler " + order.getTravlerID()
									+ " Finished his Park visit and left the park.\n"
									+ (order.getNumOfTravelers()*(-1)) + " more travelers left "
									+ order.getPark()+"\n" + "Order " + order.getOrderID()
									+ " finished with success.\nOrder status changed to 'Done'.");
									alert.showAndWait();
									return;
								} 
							} 
						}
								Alert alert = new Alert(AlertType.ERROR,
										"Can't process Exit for traveler " + subscriptionTraveler.getID()+ ".\nBecause Traveler " + subscriptionTraveler.getID() + " isn't at the park right now.");
								alert.showAndWait();
								return;
						}
		case "Order ID":
			if(selectedStatus.equals("Enter")) {//check if we can enter id.
			// check if the order id is available.
			LocalDate currentDate = LocalDate.now();
			LocalTime currentTime = LocalTime.now();
			Order order = getOrderInfo(Integer.parseInt(IDTextInput.getText()));
			if (order == null) {
				Alert alert = new Alert(AlertType.ERROR, "There is no such Order with number " + id + ".\nPlease enter valid ID number.");
				alert.showAndWait();
				return;
			}
			if (order.getOrderStatus() != OrderStatus.Approved) {// check order status.
				Alert alert = new Alert(AlertType.ERROR, "Order status is: " + order.getOrderStatus()
						+ ".\nOnly Orders with status: 'Approved' can enter the Park.");
				alert.showAndWait();
				return;
			}
			if (order.getOrderDate().toLocalDate().isEqual(currentDate) // if its the same date.
					&& order.getOrderTime().toLocalTime().isBefore(currentTime) // order time is < the current time.
					&& (order.getOrderTime().toLocalTime().plusHours(order.getTimeOfStay())).isAfter(currentTime) // check time(o.time<=current<=o.time+timeOfStay)
					&& order.getOrderStatus() == OrderStatus.Approved) {
				// change order status to currently at the park
				changeParkOrderStatus(order.getOrderID());// changed status to 'currentAtThePark'.
				// update park capacity.
				updateParkCapacity(order);
				checkOccupancy();
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"Enter Order " + order.getOrderID() + " finished with success.\n" + order.getNumOfTravelers()
						+ " more travelers entered " + order.getPark());
				alert.showAndWait();
				return;
			} else { // order date is not available for current date,time.
				if(order.getOrderDate().toLocalDate().isAfter(LocalDate.now()) // order date > current date.
						|| (order.getOrderDate().toLocalDate().isEqual(LocalDate.now())
								&& order.getOrderTime().toLocalTime().plusHours(order.getTimeOfStay()).isAfter(LocalTime.now()))){
					Alert alert = new Alert(AlertType.ERROR,
							"Order " + order.getOrderID()+ " Expired.");
					alert.showAndWait();
					return;
				}
				Alert alert = new Alert(AlertType.ERROR,
						"Order " + order.getOrderID()
								+ " is not available for current Date,Time.\nPlease wait for \nDate = "
								+ order.getOrderDate().toString() + ", \nTime = " + order.getOrderTime()+" - " +order.getOrderTime().toLocalTime().plusHours(order.getTimeOfStay()));
				alert.showAndWait();
				return;
			}
		} else { // exit option is selected.
			Order order = getOrderInfo(Integer.parseInt(IDTextInput.getText()));
			if(order == null) {
				Alert alert = new Alert(AlertType.ERROR,
						"There is no such Order id exists in out DB.");
				alert.showAndWait();
				return;
			}
			if(order.getOrderStatus() != OrderStatus.CurrentlyAtThePark) {
				Alert alert = new Alert(AlertType.ERROR,
						"Can't process exit for Order " + order.getOrderID() + ".\nBecause Traveler " + order.getTravlerID() + " isn't at the park right now.");
				alert.showAndWait();
				return;
			}
			if (order.getOrderStatus() == OrderStatus.CurrentlyAtThePark) { // get traveler order with status that is currentlyAtThePark.
					boolean isExited = exitTravelerFromPark(order);//change status to 'Done'.
					//change park capacity.
					if(isExited) {
						order.setNumOfTravelers(order.getNumOfTravelers() * (-1)); // set numOfTravelers to be minus numOfTravelers.
						updateParkCapacity(order);
						Alert alert = new Alert(AlertType.CONFIRMATION,"Traveler " + order.getTravlerID()
						+ " Finished his Park visit and left the park.\n"
						+ (order.getNumOfTravelers()*(-1)) + " more travelers left "
						+ order.getPark()+"\n" + "Order " + order.getOrderID()
						+ " finished with success.\nOrder status changed to 'Done'.");
						alert.showAndWait();
						return;
					}
				}
			}
			break;

		default:
			System.out.println("Error in getting selected card reader option.");
			break;
		}
    }
    
    /**
     * this method checks if the park is full using SQL query
     */
     public static void checkOccupancy() {
    	 try {
 			PreparedStatement checkParkOccupancy = ConnectionToDB.conn
 					.prepareStatement("SELECT * FROM park ;");
 			ResultSet r = checkParkOccupancy.executeQuery();
 			while (r.next()) {// check 3 parks and check if current = max.
				int max = r.getInt(3);
				int current = r.getInt(5);
				if (max == current) { // update occpancy to true.
					PreparedStatement updateOccupancy = ConnectionToDB.conn.prepareStatement(
							"UPDATE gonature.parkactivityhours SET occupancy = ? WHERE park =? AND parkActivityDate =? AND parkActivityTime =?;");
					updateOccupancy.setBoolean(1, true);
					updateOccupancy.setString(2, r.getString(2));
					Date date = Date.valueOf(LocalDate.now());
					Time time = Time.valueOf(LocalTime.of(LocalTime.now().getHour(),0,0));
					updateOccupancy.setDate(3, date);
					updateOccupancy.setTime(4, time);
					updateOccupancy.executeUpdate();
				}
 			}
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
	}
     /**
      * update order from 'currentlyAtThePark' to 'Done'
      * @param obj this is the order we change the status for
      * @return true in case we changed the status successfuly, false otherwise.
      */
    public static synchronized boolean exitTravelerFromPark(Object obj) {
		Order order = (Order) obj;
		try {
			PreparedStatement exitTravelerFromPark = ConnectionToDB.conn
					.prepareStatement("UPDATE gonature.order SET orderStatus =? WHERE orderID =?;");
			exitTravelerFromPark.setString(1, OrderStatus.Done.name());
			exitTravelerFromPark.setInt(2, order.getOrderID());
			exitTravelerFromPark.executeUpdate();
			//now update park activity hours
			for(int i = 0;i<order.getTimeOfStay();i++) {
				if (order.getOrderTime().getHours() + i > 17)// if order time + time of stay loop is getting bigger then 16 we need to stop. because park gates closed at 17:00.
					break;
				PreparedStatement currentTravelersNumber = ConnectionToDB.conn.prepareStatement(// get current travelers.
						"UPDATE parkactivityhours SET currentTravelers = currentTravelers -? WHERE park=? AND parkActivityDate=? AND parkActivityTime=?;");
				currentTravelersNumber.setInt(1, order.getNumOfTravelers());
				currentTravelersNumber.setString(2, order.getPark());
				currentTravelersNumber.setDate(3, order.getOrderDate());
				LocalTime time = order.getOrderTime().toLocalTime().plusHours(i);
				currentTravelersNumber.setTime(4, Time.valueOf(time));
				currentTravelersNumber.executeUpdate();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

    /**
     * this method is in charge of updating the  park current travelers.
     * @param obj this is the order, with this order we get the num of travelers so we know how to update the DB accoridnly.
     * @return true in case we updated successfuly, false otherwise.
     */
	public static synchronized boolean updateParkCapacity(Object obj) {
    	Order order = (Order) obj;
		try {
			PreparedStatement updateParkCapacity = ConnectionToDB.conn
					.prepareStatement("UPDATE park SET currentParkTravelers = currentParkTravelers +? WHERE parkName =?;");
			updateParkCapacity.setInt(1, order.getNumOfTravelers());
			updateParkCapacity.setString(2, order.getPark());
			updateParkCapacity.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * this method is in charge of updating the order status to 'CurrentlyAtThePark' for orders who have entered using the card reader.
	 * @param obj this is the order, with this parameter we can get the order ID, and change its status in the DB accoridnly.
	 * @return true in case we changed the status successfuly, false otherwise.
	 * @throws SQLException sql exception 
	 */
	public static synchronized boolean changeParkOrderStatus(Object obj) throws SQLException {
		int orderID = (int) obj;
		try {
			PreparedStatement changeOrderStatus = ConnectionToDB.conn
					.prepareStatement("UPDATE gonature.order SET orderStatus =? WHERE orderID =?;");
			changeOrderStatus.setString(1, OrderStatus.CurrentlyAtThePark.name());
			changeOrderStatus.setInt(2, orderID);
			changeOrderStatus.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * this method gets information from the DB regarding an order, this method is mainly used to check data like status of order and more.
	 * @param obj this is the order ID we want to gather data from
	 * @return the order data.
	 * @throws SQLException sql exception
	 */
	public static synchronized Order getOrderInfo(Object obj) throws SQLException {
		int orderID = (int) obj;
		try {
			PreparedStatement getOrder = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM gonature.order WHERE orderID =?;");
			getOrder.setInt(1, orderID);
			ResultSet r = getOrder.executeQuery();
			if(r.next()) {
				Order order = new Order(r.getInt(1), r.getInt(2), OrderType.valueOf(r.getString(3)), r.getString(4), r.getDate(5), r.getTime(6), r.getInt(7), r.getString(8),
						OrderStatus.valueOf(r.getString(9)), r.getInt(10), r.getInt(11));
				return order;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
}
