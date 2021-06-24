package client;

import java.util.ArrayList;

import entity.*;
import enums.Commands;
/**
 * this class sends requests from travelers to the server (commands) and receives data accordinly, it is essinatlly a client-server commmuncation class.
 * @author group 20
 * @version 1.0
 *
 */
public class TravelerControllerClient {
/**
 * this method receives traveler details and returns the data stored in the DB that corresponding to that traveler 
 * @param travelerInfo this is the ID of the traveler we get the data of.
 * @return the Traveler object with the corresponding data.
 */
	public static Traveler GetTravelerInfoByID(String travelerInfo) {
		ClientUI.client.accept(new Message(travelerInfo, Commands.GetTravelerInfoByID));
		return (Traveler) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method receives traveler details and returns the data stored in the DB that corresponding to that traveler 
	 * @param travelerSubscriptionID this is the ID of the traveler subscription number we get the data of.
	 * @return the SubscriptionTraveler object with the corresponding data.wei
	 */
	public static SubscriptionTraveler GetTravelerInfoBySubscription(String travelerSubscriptionID) {
		ClientUI.client.accept(new Message(travelerSubscriptionID, Commands.GetTravelerInfoBySubscription));
		return (SubscriptionTraveler) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method receives traveler details and returns the data stored in the DB that corresponding to that traveler 
	 * @param inviteNumber this is the orderID of the order we get the data of.
	 * @return the Order object of the traveler.
	 */
	public static Order GetTravelerInfoByInvitation(String inviteNumber) {
		ClientUI.client.accept(new Message(inviteNumber, Commands.GetTravelerInfoByOrderID));
		return (Order) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method receives travelerID and returns the travelerID Orders stored in the DB that corresponding to that travelerID. 
	 * @param travelerID this is the travelerID that we search he's orders.
	 * @return list of Order objects of the corresponding travelerID.
	 */
	public static ArrayList<Order> GetTravelerOrders(String travelerID) {
		ClientUI.client.accept(new Message(travelerID, Commands.GetTravelerOrders));
		return (ArrayList<Order>) GoNatureClient.ServerRetObj;
	}
/**
 *  this method receives travelerID and returns the travelerID messages stored in the DB that corresponding to that travelerID. 
 * @param travelerID this is the travelerID that we search his messages.
 * @return list of Message objects of the corresponding travelerID.
 */
	public static ArrayList<TravelerMessage> GetTravelerMessages(String travelerID) {
		ClientUI.client.accept(new Message(travelerID, Commands.GetTravelerMessages));
		return (ArrayList<TravelerMessage>) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method receives messageID number and remove it from the DB.
	 * @param messageID this is the messageID that we want to remove from DB.
	 * @return true if the Message object was removed, false otherwise.
	 */
	public static boolean removeTravelerMessages(String messageID) {
		ClientUI.client.accept(new Message(messageID, Commands.RemoveTravelerMessage));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method receives Order and change the status of the this order accordingly to the order status.
	 * @param order this is the order we want to change his status.
	 * @return true if order status changed successfully, false otherwise.
	 */
	public static boolean changeOrderStatusByID(Order order) {
		ClientUI.client.accept(new Message(order, Commands.CancelTravelerOrder));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to the server to show that the traveler is currently online so he could not log in simontuniously from different computers.
	 * @param travelerID this is the travelerID we wish to update the status for.
	 * @return true if the status was updated, false otherwise.
	 */
	public static boolean addOnlineTraveler(String travelerID) {
		ClientUI.client.accept(new Message(travelerID, Commands.CheckOnlineTraveler));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to the server to show that the traveler is currently offline, so he could log on again.
	 * @param travelerID this is the worker ID we wish to update the status for in the DB
	 * @return true if the status was updated, false otherwise.
	 */
	public static boolean removeOnlineTraveler(String travelerID) {
		ClientUI.client.accept(new Message(travelerID, Commands.RemoveOnlineTraveler));
		return (boolean) GoNatureClient.ServerRetObj;
	}

	/**
	 * this method sends a request to the server to check a new order and all of its data such as availability and more..
	 * @param order this is the order we wish to check the data for.
	 * @return true if the order can go through, false otherwise.
	 */
	public static boolean checkNewOrder(Order order) {
		ClientUI.client.accept(new Message(order, Commands.CheckNewOrder));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to the server to create a new order in the DB.
	 * @param order this is the order we wish to insert into the DB
	 * @return  true if the order has been inserted to the DB, false otherwise.
	 */
	public static boolean createNewOrder(Order order) {
		ClientUI.client.accept(new Message(order, Commands.CreateNewOrder));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to the server to get the order total cost.
	 * @param order this is the order we want to calculate his total cost.
	 * @return the order total cost price.
	 */
	public static double getOrderTotalCost(Order order) {
		ClientUI.client.accept(new Message(order, Commands.GetOrderTotalCost));
		return (double) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to the server to get the park time of stay parameter.
	 * @param parkID this is the parkID we send to server.
	 * @return the corresponding park time of stay accordingly to parkID.
	 */
	public static int getParkTimeOfStay(int parkID) {
		ClientUI.client.accept(new Message(parkID, Commands.GetParkTimeOfStay));
		return (int) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to server to add new order to queue.
	 * @param order the order we want to add to queue.
	 * @return true if queue order added successfully to DB, false otherwise.
	 */
	public static boolean addToQueue(Order order) {
		ClientUI.client.accept(new Message(order, Commands.AddToQueue));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to server to get available order dates.
	 * @param order the order we use his data to get order available dates.
	 * @return list of Order objects that has available order dates.
	 */
	public static ArrayList<Order> getAvailableOrderDates(Order order) {
		ClientUI.client.accept(new Message(order, Commands.GetAvailableOrderDates));
		return (ArrayList<Order>) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to server to get subscription traveler data.
 * @param travelerID the travelerID we use to get his subscription data.
 * @return SubscriptionTraveler object if travelerID is signed as a SubscriptionTraveler in DB , null otherwise.
 */
	public static SubscriptionTraveler getSubscriptionTravelerByID(int travelerID) {
		ClientUI.client.accept(new Message(travelerID, Commands.GetSubscriptionTravelerByID));
		return (SubscriptionTraveler) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to server to create new traveler.
	 * @param traveler the traveler object we want to create.
	 * @return true if traveler created successfully, false otherwise.
	 */
	public static boolean createNewTraveler(Traveler traveler) {
		ClientUI.client.accept(new Message(traveler, Commands.AddNewTraveler));
		return (boolean) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to server to check if order in queue.
 * @param orderID the orderID that we want to check if he is in queue.
 * @return true if order in queue, false otherwise.
 */
	public static boolean checkIfOrderInQueue(int orderID) {
		ClientUI.client.accept(new Message(orderID, Commands.CheckIfOrderInQueue));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to server to get upcoming events.
	 * @return list of Event objects if exists, null otherwise.
	 */
	public static ArrayList<Event> getUpcomingEvents() {
		ClientUI.client.accept(new Message("", Commands.GetUpcomingEvents));
		return (ArrayList<Event>) GoNatureClient.ServerRetObj;
	}
}
