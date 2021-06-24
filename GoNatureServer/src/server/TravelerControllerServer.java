package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

import entity.Event;
import entity.Order;
import entity.Park;
import entity.SubscriptionTraveler;
import entity.Traveler;
import entity.TravelerMessage;
import enums.OrderStatus;
import enums.OrderType;
import enums.QueueStatus;
import enums.TravelerMessageType;
import enums.TravelerType;
/**
 * Traveller controller server is responssible for various actions done in the system that communicates with the DB, to name a few would be adding order to the DB, get time of stay inside the park change order status delete orders from DB, add travelers to DB and more.
 * in general, it runs SQL queries releated to the traveler, this class mainly handles in connecting between the traveler and the DB.
 * @author group 20
 * @version 1.0
 *
 */
public class TravelerControllerServer {

	/**
	 * this method gets the relevant event for the upcoming week.
	 * @param obj the obj.
	 * @return Event the list of events of the upcoming week if there is , null otherwise.
	 */
	public static synchronized ArrayList<Event> getUpComingEvents(Object obj) {
		ArrayList<Event> eventList = new ArrayList<>();
		Event returnedEvent = null;
		try {
			for (int i = 0; i < 7; i++) {
				LocalDate localDate = LocalDate.now().plusDays(i);
				Date date = Date.valueOf(localDate);
				PreparedStatement releventEvents = ConnectionToDB.conn
						.prepareStatement("SELECT * FROM gonature.events WHERE startingDate =?;");
				releventEvents.setDate(1, date);
				ResultSet r = releventEvents.executeQuery();
				if (r.next()) {
					returnedEvent = new Event(r.getInt(1), r.getInt(2), r.getDate(3), r.getDate(4), r.getString(5));
					eventList.add(returnedEvent);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return eventList;
	}
	
	/**
	 * this method receives an order from the user, and with SQL query it adds it to the database, afterwards it also updates the park activity days, amount of travelers and other variables of the system that is updated when a new order is added to the DB.
	 * @param obj this is the order that our client makes.
	 * @return true if the order was added successfully to the DB, false in case of error.
	 * @throws SQLException sql exception
	 */
	public static synchronized boolean addNewOrderToDB(Object obj) throws SQLException { 
		Order newOrder = (Order) obj;
		LocalDate localDate = LocalDate.now();
		if(newOrder.getOrderStatus() != OrderStatus.CurrentlyAtThePark &&  localDate.equals(newOrder.getOrderDate().toLocalDate())) {
			newOrder.setOrderStatus(OrderStatus.Approved);
		}
		try {
			PreparedStatement addOrder = ConnectionToDB.conn.prepareStatement(
					"INSERT INTO gonature.order(orderID,travelerID,orderType,park,orderDate,orderTime,numOfTravelers,travelerEmail,orderStatus,totalSum,timeOfStay)"
							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?);");
			addOrder.setInt(1, newOrder.getOrderID());
			addOrder.setInt(2, newOrder.getTravlerID());
			addOrder.setString(3, newOrder.getOrderType().name());
			addOrder.setString(4, newOrder.getPark());
			addOrder.setDate(5, newOrder.getOrderDate());
			addOrder.setTime(6, newOrder.getOrderTime());
			addOrder.setInt(7, newOrder.getNumOfTravelers());
			addOrder.setString(8, newOrder.getTravelerEmail());
			addOrder.setString(9, newOrder.getOrderStatus().name());
			addOrder.setDouble(10, newOrder.getTotalSum());
			addOrder.setInt(11, newOrder.getTimeOfStay());
			addOrder.executeUpdate();

			// now we will update the park activities days.
			Date checkDate = newOrder.getOrderDate();
			StringBuilder tmpDate = new StringBuilder();
			tmpDate.append(checkDate.toString());
			tmpDate.deleteCharAt(4);
			tmpDate.deleteCharAt(6);
			for (int i = 0; i < newOrder.getTimeOfStay(); i++) {// loop for time of stay 
				// now get current travelers.
				if(newOrder.getOrderTime().getHours() + i >17)//if order time + time of stay loop is getting bigger then 16 we need to stop. because park gates closed at 17:00.
                 break;
				PreparedStatement currentTravelersNumber = ConnectionToDB.conn.prepareStatement(// get current travelers.
						"SELECT currentTravelers FROM parkactivityhours WHERE park=? AND parkActivityDate=? AND parkActivityTime=?;");
				currentTravelersNumber.setString(1, newOrder.getPark());
				currentTravelersNumber.setString(2, tmpDate.toString());
				Time tmpTime = new Time(newOrder.getOrderTime().getHours() + i, 0, 0);
				currentTravelersNumber.setString(3, tmpTime.toString());
				ResultSet number = currentTravelersNumber.executeQuery();
				int currentNumOfTravelers;
				if (number.next()) {
					currentNumOfTravelers = number.getInt(1);// the number of travelers.
				} else {
					return false;
				}
				// now update current travelres.
				if (newOrder.getOrderStatus() != OrderStatus.AtWaitingList) {// if its not order for queue then update parameters.
					PreparedStatement checkOrderhours = ConnectionToDB.conn.prepareStatement(// update current travelers.
							"UPDATE parkactivityhours SET currentTravelers=? WHERE park=? AND parkActivityDate=? AND parkActivityTime=?;");
					checkOrderhours.setInt(1, newOrder.getNumOfTravelers() + currentNumOfTravelers);
					checkOrderhours.setString(2, newOrder.getPark());
					checkOrderhours.setString(3, tmpDate.toString());
					checkOrderhours.setString(4, tmpTime.toString());
					checkOrderhours.executeUpdate();
					// now we need to update isFull varaible if needed.
					PreparedStatement isFullUpdate = ConnectionToDB.conn.prepareStatement(// update isFull variable.
							"UPDATE parkactivityhours SET isFull=? WHERE maximumTravelers - currentTravelers =0;");
					isFullUpdate.setInt(1, 1);// set true.
					isFullUpdate.executeUpdate();
				}
			}
			return true;
		} catch (

		Exception e) {
			e.printStackTrace();
			return false;// exception default false.
		}
	}

	/**
	 * this method receives a park ID and returns its time of stay parameter for that specific park.
	 * 
	 * @param obj = parkID number to get time of stay.
	 * @return the number of hours a client can stay in that park.
	 * @throws SQLException sql exception.
	 */
	public static synchronized int getParkTimeOfStay(Object obj) throws SQLException {
		int parkID = (int) obj;
		try {
			PreparedStatement timeOfStay = ConnectionToDB.conn
					.prepareStatement("SELECT travelerTimeOfStayInHours FROM park WHERE parkID=? ;");
			timeOfStay.setInt(1, parkID);
			ResultSet r = timeOfStay.executeQuery();
			if (r.next()) {
				int number = r.getInt(1);
				return number;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;// exception default false.
		}
		return 0;
	}

	/**
	 * This method is responssible of changing the order status of all the orders in our DB, depending on the status of the order that we change to, this method also does follow-up actions depending on the status
	 * for instance if an order status is changed to cancelled, then it also updates the activity days in the park depending on the time of stay in the park for that order and then since it was cancelled we also check the waiting list queue beacuse a spot opened up.
	 * so in general, this method main responsability is to change the status of an order, and make follow-up actions based on the status that it was changed to.
	 * @param obj this is the order received with the new status we wish to change to.
	 * @return true in case the status and follow-up actions was made successfully, false otherwise.
	 * @throws SQLException sql exception
	 */
	public static synchronized boolean ChangeOrderStatusByID(Object obj) throws SQLException {
		Order order = (Order) obj;
		try {
			PreparedStatement orderID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM gonature.order WHERE orderID =?");
			orderID.setInt(1, order.getOrderID());
			ResultSet r = orderID.executeQuery();
			if (r.next()) {
				PreparedStatement EmailUpdate = ConnectionToDB.conn
						.prepareStatement("UPDATE gonature.order SET orderStatus=? WHERE orderID=?");
				if (order.getOrderStatus() == OrderStatus.AtWaitingList) {
					EmailUpdate.setString(1, OrderStatus.Cancelled + "");// set order status.
					EmailUpdate.setInt(2, ((Order) obj).getOrderID());// set order id.
					EmailUpdate.executeUpdate();
					//now need to remove order from queue table.
					PreparedStatement removeAtWaitingListOrderFromQueue =ConnectionToDB.conn.prepareStatement("DELETE FROM parkactivitydaysqueue WHERE orderID =?");
					removeAtWaitingListOrderFromQueue.setInt(1,order.getOrderID());
					removeAtWaitingListOrderFromQueue.executeUpdate();//delete the queue order.
					return true;
				} else {
					EmailUpdate.setString(1, ((Order) obj).getOrderStatus().name());// set order status.
					EmailUpdate.setInt(2, ((Order) obj).getOrderID());// set order id.
					EmailUpdate.executeUpdate();
				}
				// now we wil check if the order status is 'cancelled' then we need to update
				// park activities days current travelers..
				OrderStatus status = order.getOrderStatus();
				if (status == OrderStatus.Cancelled) {
					// now we will update the park activities days.
					for (int i = 0; i < order.getTimeOfStay(); i++) {// loop for time of stay.
						PreparedStatement updateParkCurrentTravelers = ConnectionToDB.conn.prepareStatement(// update
								// isFull
								// variable.
								"UPDATE parkactivityhours SET currentTravelers = currentTravelers - ? WHERE park=? AND parkActivityDate=? AND parkActivityTime=? ;");
						updateParkCurrentTravelers.setInt(1, order.getNumOfTravelers());// set true.
						updateParkCurrentTravelers.setString(2, order.getPark());
						Date checkDate = order.getOrderDate();
						StringBuilder tmpDate = new StringBuilder();
						tmpDate.append(checkDate.toString());
						tmpDate.deleteCharAt(4);
						tmpDate.deleteCharAt(6);
						updateParkCurrentTravelers.setString(3, tmpDate.toString());
						Time tmpTime = new Time(order.getOrderTime().getHours() + i, 0, 0);
						if(tmpTime.toLocalTime().getHour() == 18){
							return true;
						}
						updateParkCurrentTravelers.setString(4, tmpTime.toString());
						updateParkCurrentTravelers.executeUpdate();
						// now we need to check if the park was full before we cancelled the order.
						// if it was full we need to change it.
						PreparedStatement isFullUpdate = ConnectionToDB.conn.prepareStatement(// update isFull
																								// variable.
								"UPDATE parkactivityhours SET isFull=? WHERE park=? AND parkActivityDate=? AND parkActivityTime=?;");
						isFullUpdate.setInt(1, 0);// set false = not full..
						isFullUpdate.setString(2, order.getPark());
						isFullUpdate.setString(3, tmpDate.toString());
						isFullUpdate.setString(4, tmpTime.toString());
						isFullUpdate.executeUpdate();
						//now add michel queue code.
						//we need to check what day and time cancelled order. //
						
					}
					
					// now we make query to see if there is any waiting list orders with this date
					// and time.
					for (int i = 0; i < order.getTimeOfStay(); i++) {
						java.sql.Date orderDate = order.getOrderDate();
						Time orderTime = Time.valueOf(order.getOrderTime().toLocalTime().plusHours(i));
						PreparedStatement checkWaitingList = ConnectionToDB.conn.prepareStatement(
								"SELECT * FROM parkactivitydaysqueue WHERE queueStatus=? AND parkActivityDate =? AND parkActivityTime =? ORDER BY queueID;");
						checkWaitingList.setString(1,QueueStatus.Waiting.name());
						checkWaitingList.setDate(2, orderDate);
						checkWaitingList.setTime(3, orderTime);
						System.out.println(checkWaitingList.toString());
						ResultSet r1 = checkWaitingList.executeQuery();
						int availableNumOfTravelers = order.getNumOfTravelers() ;
						while(r1.next()) {//have waiting list orders, and isFull for checking if we still got place depending on numoftravelers.
							int queueID = r1.getInt(1);
							int queueOrderID = r1.getInt(6);//the queue order id number.
							int numOfTravelers = r1.getInt(8);
							Order tempOrder = getTravelerInfoByOrderID(queueOrderID+"");
							System.out.println("queue order id = "+ queueOrderID);
//							availableNumOfTravelers - numOfTravelers >=0 &&
							int parkID = tempOrder.getPark() == "Park A" ? 1 : tempOrder.getPark() == "Park B" ? 2 : 3;
							int timeOfStay1 = getParkTimeOfStay(parkID);
							tempOrder.setTimeOfStay(timeOfStay1);
							System.out.println("time of stay = " +timeOfStay1);
							if(checkIfOrderAvailable(tempOrder)) {//if the numOfTravelers we cancelled - the waiting list number of travelers is >= 0 we can change the status.				
								PreparedStatement changeQueueStatus = ConnectionToDB.conn.prepareStatement(
										"UPDATE parkactivitydaysqueue SET queueStatus=? WHERE queueID =?;");
								changeQueueStatus.setString(1,QueueStatus.WaitingForConfirmation.name());//change waiting list order to QueueStatus.WaitingForConfirmation.
								changeQueueStatus.setInt(2,queueID);
								System.out.println(changeQueueStatus.toString());
								changeQueueStatus.executeUpdate();
								//now decrease the amount of available place.
								//now we Update order status, add order to parkActivityHours and create new message with status waitingList.
								PreparedStatement changeStatus = ConnectionToDB.conn
										.prepareStatement("UPDATE gonature.order SET orderStatus=? WHERE orderID =?;");
								changeStatus.setString(1, OrderStatus.WaitingForConfirmation.name());
								changeStatus.setInt(2, queueOrderID);
								System.out.println(changeStatus.toString());
								changeStatus.executeUpdate();
								temporaryUpdateParkActivityOrderDays(tempOrder);
								SystemControllerServer.addMessageToTraveler(queueOrderID, tempOrder.getTravlerID(),
										"Your order for " + tempOrder.getOrderDate() + "at" + tempOrder.getOrderTime()
												+ " has become available, Please approve your arrival",
										"WaitingList");
								availableNumOfTravelers -= numOfTravelers; // decreasing the num of travelers and checking the next queue order.
								//update parkactivityhours
								
								for (int j = 0; j < timeOfStay1; j++) {
									PreparedStatement updateHours = ConnectionToDB.conn
											.prepareStatement("UPDATE gonature.parkactivityhours SET currentTravelers = currentTravelers +? WHERE"
													+ " park =? AND parkActivityDate =? AND parkActivityTime =?;");
									updateHours.setInt(1, tempOrder.getNumOfTravelers());
									updateHours.setString(2, tempOrder.getPark());
									updateHours.setDate(3, tempOrder.getOrderDate());
									Time time = Time.valueOf(tempOrder.getOrderTime().toLocalTime().plusHours(j));
									updateHours.setTime(4, time);
									updateHours.executeUpdate();
								}
							}
						}
					}
				}
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * this method is responssible of updating the fields in the DB in regards to the park activity hours, it updates the amount of travelers that are at the park according to the time of stay of a given order.
	 * @param newOrder this is the order we receive from the user, from this order we can get the time of stay date, park and amount of travelers, and update the DB accordinly.
	 * @return true in case the DB was updated successfuly, false otherwise.
	 */
	public static synchronized boolean temporaryUpdateParkActivityOrderDays(Order newOrder) {
		// now we will update the park activities days.
		try {
		Date checkDate = newOrder.getOrderDate();
		StringBuilder tmpDate = new StringBuilder();
		tmpDate.append(checkDate.toString());
		tmpDate.deleteCharAt(4);
		tmpDate.deleteCharAt(6);
		for (int i = 0; i < newOrder.getTimeOfStay(); i++) {// loop for time of stay
			// now get current travelers.
			if (newOrder.getOrderTime().getHours() + i > 17)// if order time + time of stay loop is getting bigger then 16 we need to stop. because park gates closed at 17:00.
				break;
			PreparedStatement currentTravelersNumber = ConnectionToDB.conn.prepareStatement(// get current travelers.
					"SELECT currentTravelers FROM parkactivityhours WHERE park=? AND parkActivityDate=? AND parkActivityTime=?;");
			currentTravelersNumber.setString(1, newOrder.getPark());
			currentTravelersNumber.setString(2, tmpDate.toString());
			Time tmpTime = new Time(newOrder.getOrderTime().getHours() + i, 0, 0);
			currentTravelersNumber.setString(3, tmpTime.toString());
			ResultSet number = currentTravelersNumber.executeQuery();
			int currentNumOfTravelers;
			if (number.next()) {
				currentNumOfTravelers = number.getInt(1);// the number of travelers.
			} else {
				return false;
			}
			// now update current travelres.
			if (newOrder.getOrderStatus() != OrderStatus.AtWaitingList) {// if its not order for queue then update
																			// parameters.
				PreparedStatement checkOrderhours = ConnectionToDB.conn.prepareStatement(// update current travelers.
						"UPDATE parkactivityhours SET currentTravelers=? WHERE park=? AND parkActivityDate=? AND parkActivityTime=?;");
				checkOrderhours.setInt(1, newOrder.getNumOfTravelers() + currentNumOfTravelers);
				checkOrderhours.setString(2, newOrder.getPark());
				checkOrderhours.setString(3, tmpDate.toString());
				checkOrderhours.setString(4, tmpTime.toString());
				checkOrderhours.executeUpdate();
				// now we need to update isFull varaible if needed.
				PreparedStatement isFullUpdate = ConnectionToDB.conn.prepareStatement(// update isFull variable.
						"UPDATE parkactivityhours SET isFull=? WHERE maximumTravelers - currentTravelers =0;");
				isFullUpdate.setInt(1, 1);// set true.
				isFullUpdate.executeUpdate();
			}
		}
		return true;
	}catch(Exception e)
	 {
		e.printStackTrace();
		return false;// exception default false.
	 }
	}	
	/**
	 * this method removes an order from the DB, it receives an order runs an SQL query on the DB with that order ID to find it and remove it from the DB.
	 * @param obj this is the order we wish to remove, from this parameter we can receive the order ID we wish to remove from the DB.
	 * @return true in case the order was removed sucessfully, false otherwise.
	 * @throws SQLException sql exception
	 */
	public static synchronized boolean deleteOrderFromDB(Object obj) throws SQLException {
		Order order = (Order)obj;
		try {
			PreparedStatement deleteOrder = ConnectionToDB.conn
					.prepareStatement("DELETE FROM gonature.order WHERE orderID =?");
			deleteOrder.setInt(1,order.getOrderID());
			deleteOrder.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * this method is in charge of adding a traveler to the DB, it receives an entity of a traveler (meaning all of its data) and adds it to the DB with an SQL query.
	 * @param obj this is the entity of the traveler, from this parameter we can receive the data we need to save in our DB.
	 * @return true in case the traveler was added successfuly, false otherwise.
	 * @throws SQLException sql exception
	 */
	public static synchronized boolean addTravelerToDB(Object obj) throws SQLException {
		Traveler traveler = (Traveler) obj;
		try {
			PreparedStatement addTraveler = ConnectionToDB.conn
					.prepareStatement("INSERT INTO traveler(ID,firstName,lastName,email,phoneNumber,travelerType)"
							+ "VALUES(?,?,?,?,?,?);");
			addTraveler.setInt(1, traveler.getID());
			addTraveler.setString(2, traveler.getFirstName());
			addTraveler.setString(3, traveler.getSurname());
			addTraveler.setString(4, traveler.getEmail());
			addTraveler.setString(5, traveler.getTel());
			addTraveler.setString(6, traveler.getTravelerType().name());
			addTraveler.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * this method is in charge of retreiving a traveler info from the DB using its ID, this way we can check if the traveler exists in the DB.
	 * @param obj this is the traveler we wish to check upon, from this parameter we can retreieve the traveler ID.
	 * @return Traveler entity that corrosponds with the Traveler ID we received, and null if the traveler doesn't exist in the  DB.
	 * @throws SQLException sql exception
	 */
	public static synchronized Traveler getTravelerInfoByID(Object obj) throws SQLException {
		Traveler returnedTraveler = null;
		try {
			PreparedStatement dataID = ConnectionToDB.conn.prepareStatement("SELECT * FROM traveler WHERE ID =?");
			dataID.setInt(1, Integer.parseInt((String) obj));
			ResultSet r = dataID.executeQuery();
			if (r.next()) {
				returnedTraveler = new Traveler(r.getInt(1), r.getString(2), r.getString(3), r.getString(4),
						r.getString(5), TravelerType.valueOf(r.getString(6)));
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return returnedTraveler;
	}

	/**
	 * this method is in charge of retreiving a traveler info from the DB using its ID, this way we can check if the traveler exists in the DB.
	 * @param obj this is the traveler we wish to check upon from this parameter we can retreive the subscription ID.
	 * @return Traveler entity that corrosponds with the subscription ID we received, and null if the traveler doesn't exist in the  DB.
	 * @throws SQLException sql exception
	 */
	public static synchronized SubscriptionTraveler getTravelerInfoBySubscription(Object obj) throws SQLException {
		SubscriptionTraveler returnedTraveler = null;
		try {
			PreparedStatement dataID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM subscriptiontraveler WHERE subscriptionNumber =?");
			dataID.setInt(1, Integer.parseInt((String) obj));
			ResultSet r = dataID.executeQuery();
			if (r.next()) {
				returnedTraveler = new SubscriptionTraveler(r.getInt(1), r.getInt(2), r.getString(3), r.getString(4),
						r.getString(5), r.getString(6), TravelerType.valueOf(r.getString(7)), r.getString((8)),
						r.getInt(9));
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return returnedTraveler;
	}

	/**
	 * this method is in charge of retreiving a traveler info from the DB using its ID, this way we can check if the traveler exists in the DB.
	 * @param obj this is the traveler we wish to check upon, we can retreieve its information with order ID assosicated with that traveler in the case that it exists. 
	 * @return Order entity that corrosponds with the Traveler ID we received, and null if the traveler doesn't exist in the  DB.
	 * @throws SQLException sql exception
	 */
	public static synchronized Order getTravelerInfoByOrderID(Object obj) throws SQLException {
		Order returnedOrder = null;
		try {
			PreparedStatement dataID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM gonature.order WHERE orderID =?");
			dataID.setInt(1, Integer.parseInt((String) obj));
			ResultSet r = dataID.executeQuery();
			if (r.next()) {
				returnedOrder = new Order(r.getInt(1), r.getInt(2), OrderType.valueOf(r.getString(3)), r.getString(4),
						r.getDate(5), r.getTime(6), r.getInt(7), r.getString(8), OrderStatus.valueOf(r.getString(9)),
						r.getInt(10), r.getInt(11));
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return returnedOrder;
	}

	/**
	 * this method is in charge of giving orders assosicated to a given traveler in the DB, it receives a traveler and checks the DB with SQL query and returns the orders found.
	 * @param obj this is the traveler we check for in the DB.
	 * @return the orders of the corrosponding traveler in the form of an array list, null if no order was found or an error has occured.
	 * @throws SQLException sql exception
	 */
	public static synchronized ArrayList<Order> getTravelerOrders(Object obj) throws SQLException {
		ArrayList<Order> orders = new ArrayList<>();
		try {
			PreparedStatement dataID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM gonature.order WHERE travelerID =?;");
			dataID.setInt(1, Integer.parseInt((String) obj));
			ResultSet r = dataID.executeQuery();
			while (r.next()) {
				Order returnedOrder = new Order(r.getInt(1), r.getInt(2), OrderType.valueOf(r.getString(3)),
						r.getString(4), r.getDate(5), r.getTime(6), r.getInt(7), r.getString(8),
						OrderStatus.valueOf(r.getString(9)), r.getInt(10), r.getInt(11));
				orders.add(returnedOrder);
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return orders;
	}

	/**
	 * this method is responssible of retreiving traveler messages from the DB using an SQL Query.
	 * @param obj this is the traveler we wish to retreive messages for, from this parameter we can retreive the traveler ID and use it to search in the DB with SQL query.
	 * @return the messages corrosponding to the traveler we received, null in case of errors.
	 */
	public static synchronized ArrayList<TravelerMessage> getTravelerMessages(Object obj) {
		ArrayList<TravelerMessage> messages = new ArrayList<>();
		try {
			PreparedStatement dataID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM travelermessage WHERE travelerID =?");
			dataID.setInt(1, Integer.parseInt((String) obj));
			ResultSet r = dataID.executeQuery();
			while (r.next()) {
				TravelerMessage message = new TravelerMessage(r.getInt(1), r.getInt(2), r.getInt(3), r.getString(4),
						TravelerMessageType.valueOf(r.getString(5)), r.getDate(6), r.getTime(7));
				messages.add(message);
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return messages;
	}

	/**
	 * this method is in charge of removing traveler messages from the DB, it receives a message ID searches for that message in the DB and removes it.
	 * @param obj this is the message we wish to remove from the DB, we can retreieve the message ID from this parameter and run sql query on it to find it in the DB, then remove it.
	 * @return true in case the message was removed, false otherwise.
	 */
	public static synchronized boolean removeTravelerMessages(Object obj) {
		try {
			PreparedStatement dataID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM travelermessage WHERE messageID =?");// set query
			dataID.setInt(1, Integer.parseInt((String) obj));// set message id number.
			ResultSet r = dataID.executeQuery();// get result from DB.
			if (r.next()) {// if message id exists, remove the message from DB.
				PreparedStatement deleteMessage = ConnectionToDB.conn
						.prepareStatement("DELETE FROM travelermessage WHERE messageID =?");// delete message from DB.
				deleteMessage.setInt(1, Integer.parseInt((String) obj));
				deleteMessage.executeUpdate();
				return true;// return delete succeed.
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * this method is in charge of making sure a traveler is only logged into the system once and not multiple logins in the same time.
	 * @param obj this is the traveler we wish to check if hes logged on to the system, from this parameter we can retreieve the ID we wish to check if hes online.
	 * @return true in case the traveler is online, false otherwise.
	 */
	public static synchronized boolean addOnlineTraveler(Object obj) {
		try {
			PreparedStatement travelerOnlineID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM onlinetravelers WHERE travelerID =?");// set query
			travelerOnlineID.setInt(1, Integer.parseInt((String) obj));// set traveler id number.
			ResultSet r = travelerOnlineID.executeQuery();// get result from DB.
			if (!r.next()) {// if traveler id is not exists, add the traveler to the list.
				PreparedStatement addTravelerToOnlineList = ConnectionToDB.conn
						.prepareStatement("INSERT INTO onlinetravelers(travelerID,ip)" + "VALUES(?,?);");// delete message from DB.
				addTravelerToOnlineList.setInt(1, Integer.parseInt((String) obj));
				addTravelerToOnlineList.setString(2, null);
				addTravelerToOnlineList.executeUpdate();
				return true;// return traveler added successfully.
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * this method is the corrosponding to the add online traveler method, in this case if the traveler has logged off from the system we remove hes online status from the DB to allow him to log in again.
	 * @param obj this is the traveler we wish to check online status about, from this parameter we can get the traveler ID and check it in the DB with SQL query.
	 * @return true in case the operation was made successfully, false in case of failure.
	 */
	public static synchronized boolean removeOnlineTraveler(Object obj) {
		try {
			PreparedStatement travelerOnlineID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM onlinetravelers WHERE travelerID =?");// set query
			travelerOnlineID.setInt(1, Integer.parseInt((String) obj));// set traveler id number.
			ResultSet r = travelerOnlineID.executeQuery();// get result from DB.
			if (r.next()) {// if traveler id exists, remove the traveler from the list.
				PreparedStatement removeTravelerFromOnlineList = ConnectionToDB.conn
						.prepareStatement("DELETE FROM onlinetravelers WHERE travelerID =?");// delete onlien traveler
																								// from DB.
				removeTravelerFromOnlineList.setInt(1, Integer.parseInt((String) obj));
				removeTravelerFromOnlineList.executeUpdate();
				return true;// return traveler added successfully.
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

//	public static synchronized double checkForEvent(Object orderDate) throws SQLException {
//		Date checkDate = (Date) orderDate;
//		double sale = 0;
//		StringBuilder tmp = new StringBuilder();
//		tmp.append(checkDate.toString());
//		tmp.deleteCharAt(4);
//		tmp.deleteCharAt(6);
//		try {
//			PreparedStatement eventCheck = ConnectionToDB.conn
//					.prepareStatement("SELECT * FROM events WHERE startingDate <= ? AND endingDate >= ?");
//			eventCheck.setString(1, tmp.toString());
//			eventCheck.setString(2, tmp.toString());
//			ResultSet r = eventCheck.executeQuery();
//			while (r.next()) {
//				sale += r.getInt(2) / 100;// get the discount.
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return 1;
//		}
//		return 1 - sale;// return 1- discount.
//	}
	/**
	 * this method checks an order date, and in the case that during that date there is an event in the park, it gives discount to that order.
	 * @param orderDate this is the order we want to check, from this parameter we can receive the Date and check in the DB if there is an ongoing event during that date, and give discount accoridnly.
	 * @return 1 - the discount if the order is in the range of ongoing event, 1 otherwise ( no discount ) 
	 * @throws SQLException sql exception
	 */
	public static synchronized double checkForEvent(Object orderDate) throws SQLException {
		Date checkDate = (Date) orderDate;
		int discount = 0;
		double res = 1;
		try {
			PreparedStatement eventCheck = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM events WHERE startingDate <= ? AND endingDate >= ?");
			eventCheck.setDate(1, (java.sql.Date) checkDate);
			eventCheck.setDate(2, (java.sql.Date) checkDate);
			ResultSet r = eventCheck.executeQuery();
			if (r.next()) {
				discount = r.getInt(2);// get the discount.
				return res - ((double) discount / 100);// return 1 - discount.
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
		return 1;// return 1- discount.
	}
	/** 
	 * this method is in charge of checking if an order can be created in the DB, we get the relevant park, get its activity days and checks if theres room for that given order according to the time of stay in the park and amount of travelers in the order.
	 * @param order this is the order we receive from the user from this parameter we can get the releavent park and check accoridnly.
	 * @return true in case the order is available, false otherwise.
	 * @throws SQLException sql exception
	 */
	public static synchronized boolean checkIfOrderAvailable(Object order) throws SQLException {
		Order checkOrder = (Order) order;
		Date checkDate = checkOrder.getOrderDate();
		StringBuilder tmpDate = new StringBuilder();
		tmpDate.append(checkDate.toString());
		tmpDate.deleteCharAt(4);
		tmpDate.deleteCharAt(6);
		try {
			PreparedStatement getPark = ConnectionToDB.conn.prepareStatement("SELECT * FROM park WHERE parkName=?;");
			getPark.setString(1, checkOrder.getPark());
			ResultSet park = getPark.executeQuery();
			if (park.next()) {
				int timeOfStay = park.getInt(4);
				for (int i = 0; i < timeOfStay; i++) {
					PreparedStatement checkOrderhours = ConnectionToDB.conn.prepareStatement(
							"SELECT * FROM parkactivityhours WHERE park=? And parkActivityDate=? AND parkActivityTime=?;");
					checkOrderhours.setString(1, checkOrder.getPark());
					checkOrderhours.setString(2, tmpDate.toString());
					Time tmpTime = new Time(checkOrder.getOrderTime().getHours() + i, 0, 0);
					if(tmpTime.toLocalTime().getHour() == 18){
						return true;
					}
					checkOrderhours.setString(3, tmpTime.toString());
					ResultSet r = checkOrderhours.executeQuery();
					if (r.next()) {
						int remainingPlace = r.getInt(5) - r.getInt(4) - r.getInt(7);// max - current - gap
						if (remainingPlace < checkOrder.getNumOfTravelers())
							return false;// return false if remaining is less then traveler number.
					} else
						return false;// return false if order date and time is not exists in my sql table.
				}
				return true;// return true if we finish the time of stay for loop and everything is ok.
			} else
				return false;// return false if park is not exists in my sql table.
		} catch (Exception e) {
			e.printStackTrace();
			return false;// exception default false.
		}
	}
	/**
	 * this method is in charge of adding an order to a waiting list in the form of a queue for the relveant date .
	 * @param object this is the order, from this parameter we can retreieve the date of the order and add it to the corrosponding queue for that date.
	 * @return true in case the order was added to the waiting list, false otherwise.
	 */
	public static synchronized boolean addToQueue(Object object) {
		Order order = (Order) object;
		try {
			// create row with date and time if not exist.
			Date checkDate = order.getOrderDate();
			StringBuilder tmpDate = new StringBuilder();// date
			tmpDate.append(checkDate.toString());
			tmpDate.deleteCharAt(4);
			tmpDate.deleteCharAt(6);
			Time tmpTime = new Time(order.getOrderTime().getHours(), 0, 0);// time

			PreparedStatement addToQueue = ConnectionToDB.conn.prepareStatement(
					"INSERT INTO parkactivitydaysqueue (park, parkActivityDate, parkActivityTime, travelerID, orderID, queueStatus,numOfTravelers)"
							+ "VALUES(?,?,?,?,?,?,?);");
			addToQueue.setString(1, order.getPark());
			addToQueue.setString(2, tmpDate.toString());
			addToQueue.setString(3, tmpTime.toString());
			addToQueue.setInt(4, order.getTravlerID());
			addToQueue.setInt(5, order.getOrderID());
			addToQueue.setString(6, QueueStatus.Waiting.name());//queue status waiting.
			addToQueue.setInt(7, order.getNumOfTravelers());
			addToQueue.executeUpdate();
			return true;
		} catch (Exception e) {
		}
		return false;
	}
	/**
	 * this method is in charge of giving the available order dates, it checks the DB for a given park, and scans for information using SQL query to retreieve the available dates for that park if there are any available.
	 * @param object this is the order we receive from the client, from this parameter we can check what park he wishes to enter, and retreieve the available order dates to that corrosponding park.
	 * @return the available dates that are available to order for that park. 
	 * @throws SQLException sql exception
	 */
	public static synchronized ArrayList<Order> getAvailableOrderDates(Object object) throws SQLException {
		Order order = (Order) object;
		ArrayList<Order> orders = new ArrayList<Order>();
		ArrayList<Park> parkAvailableDates = new ArrayList<Park>();
		ArrayList<Order> returenedAvailableDates = new ArrayList<Order>();
		
		Date checkDate = order.getOrderDate();
		StringBuilder tmpDate = new StringBuilder();//date
		tmpDate.append(checkDate.toString());
		tmpDate.deleteCharAt(4);
		tmpDate.deleteCharAt(6);
		Time tmpTime = new Time(order.getOrderTime().getHours() , 0, 0);//time
		PreparedStatement tableListOrder = ConnectionToDB.conn.prepareStatement(
				"SELECT * FROM parkactivityhours WHERE park=? AND ((parkActivityDate >=? AND parkActivityTime >?) OR parkActivityDate >?)"
				+"AND maximumTravelers - currentTravelers - gapNumber >=? ORDER BY parkActivityDate, parkActivityTime;");// where park = park, date >= order.date , time > order.time
		tableListOrder.setString(1,order.getPark());
		tableListOrder.setString(2,tmpDate.toString());
		tableListOrder.setString(3,tmpTime.toString());
		tableListOrder.setString(4,tmpDate.toString());
		tableListOrder.setInt(5, order.getNumOfTravelers());
		ResultSet r = tableListOrder.executeQuery();
		
		while (r.next()) {//for each available date we need to check the next dates using time of stay variable.
			//so we will get all available dates, and using the main memory we will check available dates.
			Park park = new Park(r.getString(1), r.getInt(4), r.getInt(5) , r.getInt(7) ); // set new park with name , current, max, gap
			parkAvailableDates.add(park);
			Order listOrder = new Order(r.getString(1), r.getDate(2), r.getTime(3));
			orders.add(listOrder);
			//System.out.println(listOrder.toString());
		}
		
		//now we loop the dates to check the available ones.
		boolean flag = true;
		for(int i =0;i<parkAvailableDates.size();i++) {
			flag = true; // set flag to be true.
			/*
			 * get parameters to check.
			 */
			int year = orders.get(i).getOrderDate().getYear();
			int month = orders.get(i).getOrderDate().getMonth();                                                                                                          
			int day = orders.get(i).getOrderDate().getDay();
			int hour = orders.get(i).getOrderTime().getHours();
			
			for(int j=0;(j<order.getTimeOfStay() && hour+j <=17 && (j+i)< parkAvailableDates.size()) ;j++) {
				
				int comparedYear = orders.get(j+i).getOrderDate().getYear();
				int comparedMonth = orders.get(j+i).getOrderDate().getMonth();
				int comparedDay = orders.get(j+i).getOrderDate().getDay();
				int comparedHour = orders.get(j+i).getOrderTime().getHours();
				
				if( !(year == comparedYear) || !(month == comparedMonth) || !(day == comparedDay) ) {//check if it not the same year,month,day
					flag = false;//means not to add to available dates.
					break;
				}
				else {//same year,month,day. now check hours.
					if(!((comparedHour - hour) == (j))) { //check if the dates consistent. 
						flag = false;
						break;
					}
				}
			}
			if(flag) {//we can add the order date to array list.
				returenedAvailableDates.add(orders.get(i));
				//System.out.println(orders.get(i));
			}
		}
		//System.out.println("here");
		return returenedAvailableDates;
	}

	
	
	/**
	 *  this method checks that the given traveler is a subscription type traveler in the DB.
	 * @param obj this is the traveler we wish to check upon, from this parameter we can retreieve the traveler ID and use it to run SQL query, and check the DB to see if the traveler is indeed a subscriber or not.
	 * @return the SubscriptionTraveler entity in the case the traveler is indeed a subscriber, null otherwise.
	 */
	public static synchronized SubscriptionTraveler getSubscriptionTravelerByID(Object obj) {
		int travelerID = (int) obj;
		SubscriptionTraveler returnedTraveler = null;
		try {
			PreparedStatement dataID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM subscriptiontraveler WHERE travelerID =?;");
			dataID.setInt(1, travelerID);
			ResultSet r = dataID.executeQuery();
			if (r.next()) {
				returnedTraveler = new SubscriptionTraveler(r.getInt(1), r.getInt(2), r.getString(3), r.getString(4),
						r.getString(5), r.getString(6), TravelerType.valueOf(r.getString(7)), r.getString((8)),
						r.getInt(9));
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return returnedTraveler;
	}


	/**
	 * this method is responssible to check the DB and see if a given order is in a waiting list in the form of a queue or not.
	 * @param object this is the order we wish to check upon, with this parameter we can retreieve the order ID and scan the DB using a SQL query to check if that order is in a queue.
	 * @return true if the order is in the waiting list, false otherwise.
	 */
	public static boolean checkIfOrderInQueue(Object object) {
		int orderID = (int) object;
		try {
			PreparedStatement checkQueue = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM parkactivitydaysqueue WHERE orderID =?;");
			checkQueue.setInt(1, orderID);
			ResultSet r = checkQueue.executeQuery();
			if(r.next()) {
				//now delete it
				PreparedStatement removeQueueID = ConnectionToDB.conn
						.prepareStatement("DELETE FROM parkactivitydaysqueue WHERE queueID =?;");
				removeQueueID.setInt(1, r.getInt(1));
				removeQueueID.executeUpdate();
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}


}