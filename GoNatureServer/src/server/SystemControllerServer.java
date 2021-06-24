package server;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


import entity.*;
import enums.*;
/**
 * this is a controller class, used to do operations on our server, this specific class sends messages to clients/delete messages, checks waiting list, updates current amount of travelers in the park.
 * @version 1.0
 * @author group 20
 *
 */
public class SystemControllerServer {
	/**
	 * this method checks for the dates of park orders that need to confirm their order.
	 * @param obj with this object we receive the date.
	 * @throws SQLException sql exception
	 */
      public static synchronized void getRelevantDatesToMessage (Object obj) throws SQLException{
    	  int changeOrder;
    	  LocalDate localDate = (LocalDate)obj;
    	  Date sqlDate = Date.valueOf(localDate);
    	  try {
    		  PreparedStatement relevantOrders = ConnectionToDB.conn
  					.prepareStatement("SELECT * FROM gonature.order WHERE orderDate =? AND orderStatus = 'Pending';");
    		  relevantOrders.setString(1,sqlDate.toString());
    		  ResultSet r = relevantOrders.executeQuery();
    		  while(r.next()) {
    			//  addMessageToTraveler(r.getInt(1),r.getInt(2),"Please approve your order number " + r.getInt(1),"ApproveArrival");
    			  addMessageToTraveler(r.getInt(1),r.getInt(2),"Please approve your order for " +r.getString(4)+" Date = "+r.getDate(5)+ " Time = "+r.getTime(6)+".","ApproveArrival");
    			  changeOrder = r.getInt(1);
    			  PreparedStatement changeStatus = ConnectionToDB.conn
    	  					.prepareStatement("UPDATE gonature.order SET orderStatus=? WHERE orderID =?;");
    			  changeStatus.setString(1,OrderStatus.WaitingForConfirmation.name());
    			  changeStatus.setInt(2,changeOrder);
    			  changeStatus.executeUpdate();  
    			  System.out.println("Sent message to approve order "+r.getInt(1));
    		  } 		  
    	  } catch (Exception e) {
  			e.printStackTrace();
  		}

      }
      /**
       * this methods sends messages to the systems clients to confirm their order.
       * @param orderID this is the orderID we send the message to.
       * @param travelerID this is the travelerID we send the message to.
       * @param messageInfo this is the message info that we send
       * @param messageType this is the message type we send.
       * @throws SQLException sql exception
       */
     public static synchronized void addMessageToTraveler(int orderID,int travelerID,String messageInfo,String messageType) throws SQLException{
    	 try {
    		 PreparedStatement addMessage = ConnectionToDB.conn.prepareStatement(
 					"INSERT INTO gonature.travelermessage (travelerID, orderID, messageInfo, messageType, messageDate, messageTime)"
 							+ "VALUES(?,?,?,?,?,?);");
    		 LocalDate localDate = LocalDate.now();
    		 LocalTime localTime = LocalTime.now();
       	     Date sqlDate = Date.valueOf(localDate);
       	     Time sqlTime = Time.valueOf(localTime);
    		 addMessage.setInt(1,travelerID);
    		 addMessage.setInt(2,orderID);
    		 addMessage.setString(3,messageInfo);
    		 addMessage.setString(4,messageType);
    		 addMessage.setDate(5,sqlDate);
    		 addMessage.setTime(6,sqlTime);
    		 addMessage.executeUpdate();
    	 } catch (Exception e) {
   			e.printStackTrace();
   		}
     }
     /**
      * this  method deletes the messages that have been unapproved by the clients so they are no longer necceccery.
      * @param obj with this parameter we can receive the date of the message we wish to delete.
      * @throws SQLException sql exception
      */
     public static synchronized void deleteUnAprrovedMessages(Object obj) throws SQLException{
    	 Order returnedOrder = null;
    	 LocalTime localTime = LocalTime.now();
    	 LocalDate localDate = (LocalDate)obj;
   	     Date sqlDate = Date.valueOf(localDate);
   	  try {
		  PreparedStatement dates = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM gonature.travelermessage WHERE messageDate =? AND messageType=?;");
		  dates.setString(1,sqlDate.toString());
		  dates.setString(2,TravelerMessageType.ApproveArrival.name());
		  ResultSet r = dates.executeQuery();
		  while(r.next()) {
			  returnedOrder = TravelerControllerServer.getTravelerInfoByOrderID(r.getInt(3) + "");
			  LocalTime compareTime = r.getTime(7).toLocalTime().plusHours(2);
			  if(localTime.isAfter(compareTime) && returnedOrder.getOrderStatus().equals(OrderStatus.WaitingForConfirmation)) {		  
				  addMessageToTraveler(r.getInt(3),r.getInt(2),"Your order number" + r.getInt(3) + "has been CANCELLED Because you did not confirm it in time.","Cancelled");
				  //now remove message from DB.
				  PreparedStatement deleteMessage = ConnectionToDB.conn
							.prepareStatement("DELETE FROM gonature.travelermessage WHERE messageID =?");
				  deleteMessage.setInt(1, r.getInt(1));
				  deleteMessage.executeUpdate();
				  returnedOrder.setOrderStatus(OrderStatus.Cancelled);
				  TravelerControllerServer.ChangeOrderStatusByID(returnedOrder);  
			   }				  
		   } 		  
	  } catch (Exception e) {
			e.printStackTrace();
		}
     }
     
     
     /**
      * this method checks the waiting list , and deletes unapproved/past their due messages.
      * @throws SQLException sql exception
      */
     public static void checkWaitingList() throws SQLException{ 
    	 Order checkOrder = null;
    	 LocalTime localTime = LocalTime.now().plusHours(1);
    	 LocalDate localDate = LocalDate.now();
    	 try {
    		 PreparedStatement waitingList = ConnectionToDB.conn
 					.prepareStatement("SELECT * FROM gonature.parkactivitydaysqueue WHERE queueStatus =?");
    		 waitingList.setString(1,QueueStatus.WaitingForConfirmation.name());
    		 ResultSet r = waitingList.executeQuery();
    		 while(r.next()) {
    			 checkOrder = TravelerControllerServer.getTravelerInfoByOrderID(r.getInt(6) + "");
    			 PreparedStatement messages = ConnectionToDB.conn
    						.prepareStatement("SELECT * FROM gonature.travelermessage WHERE orderID =? AND messageType=?;");
    			 messages.setInt(1,checkOrder.getOrderID());
    			 messages.setString(2,TravelerMessageType.WaitingList.name());
    			 ResultSet r2 = messages.executeQuery();
    			 if(r2.next()) {
    				 LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
    				 LocalDateTime orderLocalDateTime = LocalDateTime.of(r2.getDate(6).toLocalDate(), r2.getTime(7).toLocalTime());
    			 if((checkOrder.getOrderStatus() == OrderStatus.WaitingForConfirmation)  && localDateTime.isBefore(orderLocalDateTime)) {
    				 checkOrder.setOrderStatus(OrderStatus.Cancelled);
    				 System.out.println("Cancel Order -> "+checkOrder);
    				 TravelerControllerServer.ChangeOrderStatusByID(checkOrder);
    				 PreparedStatement deleteMessage = ConnectionToDB.conn
    							.prepareStatement("DELETE FROM gonature.travelermessage WHERE messageID =?");
    				 deleteMessage.setInt(1,r2.getInt(1)); 
    				 deleteMessage.executeUpdate();
    				 addMessageToTraveler(r2.getInt(3),r2.getInt(2),"Sorry,You missed your Chance to accept the order number " + r2.getInt(3),"Cancelled");
    				 ////we need to check if we need to delete the temporary order from the DB
    				 TravelerControllerServer.deleteOrderFromDB(checkOrder);
    				 //delete queue from queue table.
    				 PreparedStatement removeQueue = ConnectionToDB.conn
     						.prepareStatement("DELETE FROM parkactivitydaysqueue WHERE queueID =?;");
    				 removeQueue.setInt(1,r.getInt(1));//delete queueID
    				 removeQueue.executeUpdate();
    			 }
    		   }
    		 }
    	 }catch (Exception e) {
 			e.printStackTrace();
 		}
    	 
     }
     /**
      * this method is in charge of removing clients from the waiting list in the case which we cannot send confirmation message to the client due to a certain circumstance (too late cant confirm in time)
      * @throws SQLException sql exception
      */
     //every day at 23:30 deletes every queue for the next day that didn't get already a free space.
     public static void deleteUnnecessaryWaitingList() throws SQLException{ 
     LocalDate localDate = LocalDate.now().plusDays(1);   
	 Date sqlDate = Date.valueOf(localDate);
	 try {
	 PreparedStatement unnecessaryWaitingList = ConnectionToDB.conn
				.prepareStatement("SELECT * FROM gonature.parkactivitydaysqueue WHERE queueStatus = 'Waiting' AND parkActivityDate =?;");
	 unnecessaryWaitingList.setString(1,sqlDate.toString());
	 System.out.println(unnecessaryWaitingList.toString());
	 ResultSet r = unnecessaryWaitingList.executeQuery();
	 while(r.next()) {
		     PreparedStatement deleteFromQueue = ConnectionToDB.conn
					.prepareStatement("DELETE FROM gonature.parkactivitydaysqueue WHERE queueID=?");
			 deleteFromQueue.setInt(1,r.getInt(1));
			 deleteFromQueue.executeUpdate();
	    }		 
	   }catch (Exception e) {
			e.printStackTrace();
		} 
     }
     
     /**
      * this method retrieves orders from the DB that are currently executed checks if their time of stay in the park is over, and changes their status to done, and updates the park capacity parameter accordinly.
      * @throws SQLException sql exception.
      */
     public static void UpdateCurrentlyTravelersAtThePark()throws SQLException{
    	 try {
				LocalTime localTime = LocalTime.now();
				LocalDate localDate = LocalDate.now();
				Date date = Date.valueOf(localDate);
				Time time = Time.valueOf(localTime);

				PreparedStatement getOrders = ConnectionToDB.conn
    					.prepareStatement("SELECT * FROM gonature.order WHERE orderDate =? AND orderStatus =?;");
				getOrders.setDate(1, date);
				getOrders.setString(2, OrderStatus.CurrentlyAtThePark.name());
				ResultSet r = getOrders.executeQuery();
				while (r.next()) {
					Time comparedTime = r.getTime(6);
					int timeOfStay = r.getInt(11);
					if(comparedTime.toLocalTime().plusHours(timeOfStay).isBefore(time.toLocalTime()) ) {
						PreparedStatement updateOrderStatus = ConnectionToDB.conn
		    					.prepareStatement("Update gonature.order SET orderStatus =? WHERE orderID =?;");
						updateOrderStatus.setString(1, OrderStatus.Done.name());
						updateOrderStatus.setInt(2,r.getInt(1));
						updateOrderStatus.executeUpdate();
						PreparedStatement updateParkCapacity = ConnectionToDB.conn
		    					.prepareStatement("Update park SET currentParkTravelers = currentParkTravelers - ? WHERE parkName =?;");
						updateParkCapacity.setInt(1, r.getInt(7));
						updateParkCapacity.setString(2, r.getString(4));
						updateParkCapacity.executeUpdate();
					}
				}
    	 }catch (Exception e) {
 			e.printStackTrace();
 		}    	 
     }
     /**
      * this method runs SQL query to find unexecuted orders in the DB and changes their status to expired.
      * @throws SQLException sql exception
      */
	// find orders that didn't arrived to the park and their order status was
	// approved and changed it to Expired.
 	public static void findExpiredOrders() throws SQLException {
 		LocalTime localTime = LocalTime.now();
 		LocalDate localDate = LocalDate.now();
 		Date date = Date.valueOf(localDate);
 		Time time = Time.valueOf(localTime);

 		PreparedStatement getOrders = ConnectionToDB.conn
 				.prepareStatement("SELECT * FROM gonature.order WHERE orderDate =? AND orderStatus =?;");
 		getOrders.setDate(1, date);
 		getOrders.setString(2, OrderStatus.Approved.name());
 		ResultSet r = getOrders.executeQuery();
 		while (r.next()) {
 			Time comparedTime = r.getTime(6);
 			int timeOfStay = r.getInt(11);
 			if (comparedTime.toLocalTime().plusHours(timeOfStay).isBefore(time.toLocalTime())) { // if (o.time + o.time of stay < current time)
 				// we need to update status from 'approved' to 'expired'.
 				PreparedStatement updateOrderStatus = ConnectionToDB.conn
 						.prepareStatement("Update gonature.order SET orderStatus =? WHERE orderID =?;");
 				updateOrderStatus.setString(1, OrderStatus.Expired.name());
 				updateOrderStatus.setInt(2, r.getInt(1));
 				updateOrderStatus.executeUpdate();
 			}
 		}
 	}
}
