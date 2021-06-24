package client;

import java.sql.Date;
import java.util.ArrayList;

import entity.Event;
import entity.Message;
import entity.Order;
import entity.Park;
import entity.ParkWorker;
import entity.Report;
import entity.Request;
import entity.SubscriptionTraveler;
import entity.Traveler;
import enums.Commands;
/**
 * this class sends requests from workers to the server (commands) and receives data accordinly, it is essinatlly a client-server commmuncation class.
 * @author group 20
 * @version 1.0
 *
 */
public class WorkerControllerClient {
/**
 * this method receives worker details and returns the data stored in the DB that corrosponds to that worker 
 * @param workerDetails this is the ID and PW of the worker we get the data of.
 * @return the park worker object with the corrosponding data.
 */
	public static ParkWorker getWorkerInfo(ArrayList<Integer> workerDetails) {
		ClientUI.client.accept(new Message(workerDetails, Commands.GetWorkerInfo));
		return (ParkWorker) GoNatureClient.ServerRetObj;
	}
/**
 * this method receives park name and returns parameters regarding the park capacity from the DB
 * @param parkName this is the park name, with this parameter we can aquire the relevant data from the DB.
 * @return the park parameters corrosponding to the park name given.
 */
	public static ArrayList<Integer> getParkCapacity(String parkName) {
		ClientUI.client.accept(new Message(parkName, Commands.GetParkCapacityNumber));
		return (ArrayList<Integer>) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to receieve the traveler ID and returns the corrosponding traveler from the DB
 * @param travelerID this is the traveler ID 
 * @return the traveler that corrosponds to the traveler ID, null if it doesn't exist in the DB
 */
	public static Traveler getTravelerInfoByID(String travelerID) {
		ClientUI.client.accept(new Message(travelerID, Commands.GetTravelerInfo));
		return (Traveler) GoNatureClient.ServerRetObj;
	}
/**
 * this method send a request to update the type of traveler in the DB
 * @param traveler this is the traveler we wish to update the type for
 * @return true if the type was updated successfuly, false otherwise
 */
	public static boolean updateTravelerType(Traveler traveler) {
		ClientUI.client.accept(new Message(traveler, Commands.UpdateTravelerType));
		return (boolean) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to update traveler info in the DB
 * @param toUpdateTraveler this is the traveler we wish to update info for
 * @return true if the update went through false otherwise.
 */
	public static boolean updateTravelerInfo(Traveler toUpdateTraveler) {
		ClientUI.client.accept(new Message(toUpdateTraveler, Commands.UpdateTravelerInfo));
		return (boolean) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to add a subscription traveler to the DB.
 * @param subscriptionTraveler this is the subscriberTraveler entity we wish to insert into the DB
 * @return true if the traveler was added sucessfuly false other wise.
 */
	public static boolean addNewFamilySubscription(SubscriptionTraveler subscriptionTraveler) {
		ClientUI.client.accept(new Message(subscriptionTraveler, Commands.CreateNewSubscription));
		return (boolean) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to insert a new traveler into the DB
 * @param createTraveler this is the traveler we wish to insert
 * @return true if the insertion went through, false otherwise.
 */
	public static boolean createNewTraveler(Traveler createTraveler) {
		ClientUI.client.accept(new Message(createTraveler, Commands.CreateNewTraveler));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	
	/**
	 * this method sends a request to add a casual traveler into the DB
	 * @param list this is the data we wish to insert into the DB
	 * @return true if the insertion was successful, false otherwise.
	 */
	public static boolean addCasualTraveler(ArrayList<Object> list) {
		ClientUI.client.accept(new Message(list, Commands.CreateNewOrderCasual));
		return (boolean) GoNatureClient.ServerRetObj;
	}

	/**
	 * this method sends a request to check if we can update the park parameter in the DB
	 * @param list this is the data we wish to check for
	 * @return true if its okay to update the parameter in the DB, false otherwise.
	 */
	public static boolean checkParkParameterUpdate(ArrayList<Object> list) {
		ClientUI.client.accept(new Message(list, Commands.CheckParkParameterUpdate));
		return (boolean) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to create a new request in the DB
 * @param request this is the data we wish to request.
 * @return true if we created a new request, false otherwise.
 */
	public static boolean createNewRequest(Request request) {
		ClientUI.client.accept(new Message(request, Commands.CreateNewRequest));
		return (boolean) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to the server to get the requests that corrospond to a given worker from the DB
 * @param workerID this is the worker we wish to gather requests for from the DB
 * @return the Requests of the corrosponding worker in the form of an array list.
 */
	public static ArrayList<Request> getWorkerRequests(int workerID) {
		ClientUI.client.accept(new Message(workerID, Commands.GetRequests));
		return (ArrayList<Request>) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to check for event in the park.
 * @param tmp this is the event we wish to check for.
 * @return true if there is an event, false otherwise.
 */
	public static boolean checkEvent(entity.Event tmp) {
		ClientUI.client.accept(new Message(tmp, Commands.CheckEvent));
		return (boolean) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to update the park parameter in the DB from the server.
 * @param request this is the request we wish to fulfill.
 * @return true if we managed to update the park parameter in the DB, false otherwise.
 */
	public static boolean updateParkParameter(Request request) {
		ClientUI.client.accept(new Message(request, Commands.UpdateParkParameter));
		return (boolean) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to get the event that corrosponds to a given request ID from the DB.
 * @param requestID this is the request ID we check.
 * @return the event that corrospond to the request ID we received.
 */
	public static Event getEventByRequestID(int requestID) {
		ClientUI.client.accept(new Message(requestID, Commands.GetEventByRequestID));
		return (Event) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to the server to aquire orders we need for a given report.
 * @param report this is the report we want to generate.
 * @return the orders needed to make the report.
 */
	public static ArrayList<Order> getReportOrders(Report report) {
		ClientUI.client.accept(new Message(report, Commands.GetReportOrders));
		return (ArrayList<Order> ) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to the server to get a park info with the park name from the DB.
 * @param park this is the park name we wish to get info for.
 * @return the park entity that corrospond to the name received.
 */
	public static Park getParkInfoByParkName(String park) {
		ClientUI.client.accept(new Message(park, Commands.GetParkInfo));
		return (Park) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to the server to receive the time of stay of a given park from the DB
 * @param parkID this is the park ID, we check the ID in the DB to receive the time of stay in that park parameter.
 * @return the time of stay for the park parameter from the DB.
 */
	public static int getTimeOfStay(int parkID) {
		ClientUI.client.accept(new Message(parkID, Commands.GetParkTimeOfStay));
		return (int) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to the server to receive the discount (if it exists) for an order date, it checks the DB to see if there is an event in the same time of the order date and returns the appropriate discount.
 * @param orderDate this is the order date we wish to check to see if there is event.
 * @return the discount if there is one during that period of time.
 */
	public static double checkForEvent(Date orderDate) {
		ClientUI.client.accept(new Message(orderDate, Commands.CheckForEvent));
		return (double) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to the server to check a new order and all of its data such as availability and more..
 * @param order this is the order we wish to check the data for.
 * @return true if the order can go through, false otherwise.
 */
	public static boolean checkNewOrder(Order order) {
		ClientUI.client.accept(new Message(order, Commands.WorkerCheckNewOrder));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to the server to create a new order in the DB.
	 * @param order this is the order we wish to insert into the DB
	 * @return true if the order has been inserted to the DB, false otherwise.
	 */
	public static boolean createNewOrder(Order order) {
		ClientUI.client.accept(new Message(order, Commands.ParkWorkerCreateNewOrder));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	
	/**
	 * this method sends a request to the server to show that the worker is currently online so he could not log in simontuniously from different computers.
	 * @param WorkerID this is the workerID we wish to update the status for.
	 * @return true if the status was updated false otherwise.
	 */
	public static boolean addOnlineWorker(String WorkerID) {
		ClientUI.client.accept(new Message(WorkerID, Commands.CheckOnlineWorker));
		return (boolean) GoNatureClient.ServerRetObj;
	}
	/**
	 * this method sends a request to the server to show that the worker is currently offline, so he could log on again.
	 * @param WorkerID this is the worker ID we wish to update the status for in the DB
	 * @return true if the update went through, false otherwise.
	 */
	public static boolean removeOnlineWorker(String WorkerID) {
		ClientUI.client.accept(new Message(WorkerID, Commands.RemoveOnlineWorker));
		return (boolean) GoNatureClient.ServerRetObj;
	}
/**
 * this method sends a request to the server to check the occupancy of the parks 
 */
	public static void checkOccupancy() {
		ClientUI.client.accept(new Message("", Commands.CheckOccupancy));
	}

	

}