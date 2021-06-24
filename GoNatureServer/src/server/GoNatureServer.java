package server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import boundary.ServerController;
import enums.*;
import entity.*;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * this class handles requests from clients (messages) and operates the
 * controllers and additonally sends back data to the client.
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class GoNatureServer extends AbstractServer {
	/**
	 * * @param schemaName this is our schema name for the server
	 * 
	 * @param dbPassword             this is our data base password we use to log on
	 *                               to the server
	 * @param ticketPrice            this is the price for a ticket
	 * @param ParkOrignalTicketPrice this is the orignal ticket price of the park.
	 * @param runThreads             a boolean parameter to let us know if the
	 *                               threads are currently running.
	 */

	/** The db password. */
	public static String schemaName, dbPassword;
	private final int ParkOrginalTicketPrice = 100;
	public TicketPrice ticketPrice = null;
	public static boolean runThreads = true;

	/**
	 * this method runs the server
	 * @param port this is the port we run
	 */
	public GoNatureServer(int port) {
		super(port);
		System.out.println(port);
	}
	/**
	 * this method handles receives requests (commands) from clients, and operates the controllers to fulfill those requests.
	 * @param msg this is the message we receive from the clients
	 * @param client this is the client were operating for.
	 */
	@Override 
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		Message message = (Message) msg;

		ServerController.writeToServerConsole(
				"Message from client --- received: cmd = " + message.getCmd() + " the object = " + message.getObj() + " from =" + client);

		switch (message.getCmd()) {
		case GetTravelerInfoByID:// get the traveler info by id
			try {
				Traveler traveler = TravelerControllerServer.getTravelerInfoByID(message.getObj());
				client.sendToClient((new Message((Object) traveler, Commands.RetTravelerInfoByID)));
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
			break;

		case GetTravelerInfoBySubscription:// get the traveler info by subscription
			try {
				Traveler traveler = TravelerControllerServer.getTravelerInfoBySubscription(message.getObj());
				client.sendToClient((new Message((Object) traveler, Commands.RetTravelerInfoBySubscription)));
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
			break;

		case GetSubscriptionTravelerByID:// get the traveler info by subscription
			try {
				SubscriptionTraveler traveler = TravelerControllerServer.getSubscriptionTravelerByID(message.getObj());
				client.sendToClient((new Message((Object) traveler, Commands.RetGetSubscriptionTravelerByID)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		case GetTravelerInfoByOrderID:// get the traveler info by order id
			try {
				Order order = TravelerControllerServer.getTravelerInfoByOrderID(message.getObj());
				client.sendToClient((new Message((Object) order, Commands.RetTravelerInfoByOrderID)));
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
			break;

		case CheckOnlineTraveler:// get the traveler info by id
			try {
				boolean isOnline = TravelerControllerServer.addOnlineTraveler(message.getObj());
				client.sendToClient((new Message((Object) isOnline, Commands.RetCheckOnlineTraveler)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case RemoveOnlineTraveler:// get the traveler info by id
			try {
				boolean isRemoved = TravelerControllerServer.removeOnlineTraveler(message.getObj());
				client.sendToClient((new Message((Object) isRemoved, Commands.RetRemoveOnlineTraveler)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GetTravelerOrders:// get the traveler orders.
			try {
				ArrayList<Order> orders = TravelerControllerServer.getTravelerOrders(message.getObj());
				client.sendToClient((new Message((Object) orders, Commands.RetTravelerOrders)));
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
			break;

		case CreateNewOrder:
			try {
			//	Order tempOrder = (Order) message.getObj();//the order we need to create.
			//	double ticketPrice = TicketPrice.calculateTotalPrice(tempOrder);//get ticket price from TicketPrice Class.
			//	double discountPercentage = TravelerControllerServer.checkForEvent(tempOrder.getOrderDate());//get discount percentage number, if there is any event, else return 1.
			//	double ticketPriceWithDiscount = ticketPrice * discountPercentage; // ticket price with discount.
			//	tempOrder.setTotalSum(ticketPriceWithDiscount);//set order total cost.
				boolean isOrderSucceed = TravelerControllerServer.addNewOrderToDB(message.getObj());//check if order succeed.
				client.sendToClient((new Message((Object) isOrderSucceed, Commands.RetCreateNewOrder)));
			} catch (IOException | SQLException e) { 
				e.printStackTrace();
			}
			break;
			
		case CheckNewOrder:
			try {
				boolean checkOrder = TravelerControllerServer.checkIfOrderAvailable(message.getObj());//check order.
				client.sendToClient((new Message((Object) checkOrder, Commands.RetCheckNewOrder)));
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
			break; 
			
		case GetOrderTotalCost:
			try {
				Order tempOrder = (Order) message.getObj();//the order we need to create.
				double ticketPrice = TicketPrice.calculateTotalPrice(tempOrder);//get ticket price from TicketPrice Class.
				double discountPercentage = TravelerControllerServer.checkForEvent(tempOrder.getOrderDate());//get discount percentage number, if there is any event, else return 1.
				double ticketPriceWithDiscount = ticketPrice * discountPercentage; // ticket price with discount.
				client.sendToClient((new Message((Object) ticketPriceWithDiscount, Commands.RetGetOrderTotalCost)));
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
			break;

		case CancelTravelerOrder:// change traveler order status.
			try {
				boolean isCancelled = TravelerControllerServer.ChangeOrderStatusByID(message.getObj());
				client.sendToClient((new Message((Object) isCancelled, Commands.RetCancelTravelerOrder)));
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
			break;

		case GetTravelerMessages:// get the traveler messages.
			try {
				ArrayList<TravelerMessage> messages = TravelerControllerServer.getTravelerMessages(message.getObj());
				client.sendToClient((new Message((Object) messages, Commands.RetTravelerMessages)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case RemoveTravelerMessage:// remove the traveler messages.
			try {
				boolean isRemove = TravelerControllerServer.removeTravelerMessages(message.getObj());
				client.sendToClient((new Message((Object) isRemove, Commands.RetRemoveTravelerMessage)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case AddNewTraveler:
			try {
				boolean updateStatus = TravelerControllerServer.addTravelerToDB(message.getObj());
				client.sendToClient((new Message((Object) updateStatus, Commands.RetNewTraveler)));
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case GetParkTimeOfStay:
			try {
				int timeOfStayNumber = TravelerControllerServer.getParkTimeOfStay((message.getObj()));
				client.sendToClient((new Message((Object) timeOfStayNumber, Commands.RetGetParkTimeOfStay)));
			} catch (IOException | SQLException e) {
				e.printStackTrace();
			}
			break;
			
		case AddToQueue:
			try {
				boolean isAdded = TravelerControllerServer.addToQueue((message.getObj()));
				client.sendToClient((new Message((Object) isAdded, Commands.RetAddToQueue)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case GetAvailableOrderDates:
			try {
				ArrayList<Order> orders = TravelerControllerServer.getAvailableOrderDates((message.getObj()));
				client.sendToClient((new Message((Object) orders, Commands.RetGetAvailableOrderDates)));
			} catch (IOException | SQLException e) {
				e.printStackTrace(); 
			}
			break;
			
		case CheckIfOrderInQueue:
			try {
				boolean inQueue = TravelerControllerServer.checkIfOrderInQueue((message.getObj()));
				client.sendToClient((new Message((Object) inQueue, Commands.RetCheckIfOrderInQueue)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case GetUpcomingEvents:
			try {
				ArrayList<Event> events = TravelerControllerServer.getUpComingEvents((message.getObj()));
				client.sendToClient((new Message((Object) events, Commands.RetGetUpcomingEvents)));
			} catch (IOException e) { 
				e.printStackTrace(); 
			}
			break;
			
			
			/*
			 * 
			 * 
			 * 
			 * this functions below is for workers.
			 * 
			 * 
			 * 
			 * 
			 */
			
		case GetWorkerInfo:
			try {
				ParkWorker worker = WorkerControllerServer.checkWorkerLogIn(message.getObj());
				client.sendToClient((new Message((Object) worker, Commands.RetWorkerInfo)));
			} catch (IOException | SQLException e) {
				e.printStackTrace(); 
			}
			break;

		case GetParkCapacityNumber:
			try {
				ArrayList<Integer> list = WorkerControllerServer.getParkCapacity(message.getObj());
				client.sendToClient((new Message((Object) list,Commands.RetGetParkCapacityNumber)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case GetTravelerInfo:
			try {
				Traveler traveler = WorkerControllerServer.getTravelerInfoByID(message.getObj());
				client.sendToClient((new Message((Object) traveler, Commands.RetGetTravelerInfo)));
			} catch (IOException | SQLException e) {
				e.printStackTrace(); 
			}
			break;
			
			
		case UpdateTravelerType:
			try {
				boolean isUpdated = WorkerControllerServer.updateTravelerType(message.getObj());
				client.sendToClient((new Message((Object) isUpdated, Commands.RetUpdateTravelerType)));
			} catch (IOException | SQLException e) {
				e.printStackTrace(); 
			}
			break;
			
		case UpdateTravelerInfo:
			try {
				boolean isUpdated = WorkerControllerServer.updateTravelerInfo(message.getObj());
				client.sendToClient((new Message((Object) isUpdated, Commands.RetUpdateTravelerInfo)));
			} catch (IOException | SQLException e) {
				e.printStackTrace(); 
			}
			break;
			
		case CreateNewSubscription:
			try {
				boolean isAdded = WorkerControllerServer.createNewSubscription(message.getObj());
				client.sendToClient((new Message((Object) isAdded, Commands.RetCreateNewSubscription)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case CreateNewTraveler:
			try {
				boolean isCreated = WorkerControllerServer.createNewTraveler(message.getObj());
				client.sendToClient((new Message((Object) isCreated, Commands.RetCreateNewTraveler)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case CreateNewOrderCasual:
			try {
				boolean isCreated = WorkerControllerServer.addCasualTraveler(message.getObj());
				client.sendToClient((new Message((Object) isCreated, Commands.RetCreateNewOrderCasual)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
		case CheckParkParameterUpdate:
			try {
				boolean canUpdate = WorkerControllerServer.checkParkParameterUpdate(message.getObj());
				client.sendToClient((new Message((Object) canUpdate, Commands.RetCheckParkParameterUpdate)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;

		case CreateNewRequest:
			try {
				boolean isCreated = WorkerControllerServer.createNewRequest(message.getObj());
				client.sendToClient((new Message((Object) isCreated, Commands.RetCreateNewRequest)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case GetRequests:
			try {
				ArrayList<Request> requests = WorkerControllerServer.getWorkerRequests(message.getObj());
				client.sendToClient((new Message((Object) requests, Commands.RetRequests)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case CheckEvent:
			try {
				boolean isAvailable = WorkerControllerServer.checkEvent(message.getObj());
				client.sendToClient((new Message((Object) isAvailable, Commands.RetCheckEvent)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case UpdateParkParameter:
			try {
				boolean isUpdated = WorkerControllerServer.updateParkParameter(message.getObj());
				client.sendToClient((new Message((Object) isUpdated, Commands.RetUpdateParkParameter)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case GetEventByRequestID:
			try {
				entity.Event tmp = WorkerControllerServer.getEventByRequestID(message.getObj());
				client.sendToClient((new Message((Object) tmp, Commands.RetGetEventByRequestID)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case GetReportOrders:
			try {
				ArrayList<Order> tmp = WorkerControllerServer.getReportOrders(message.getObj());
				client.sendToClient((new Message((Object) tmp, Commands.RetGetReportOrders)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case CheckForEvent:
			try {
				double tmp = WorkerControllerServer.checkForEvent(message.getObj());
				client.sendToClient((new Message((Object) tmp, Commands.RetCheckForEvent)));
			} catch (IOException | SQLException e) {
				e.printStackTrace(); 
			}
			break;
			
		case ParkWorkerCreateNewOrder:
			try {
				boolean isOrderSucceed = TravelerControllerServer.addNewOrderToDB(message.getObj());//check if order succeed.
				boolean isCurrentParkTravelerUpdated = WorkerControllerServer.updateCurrentParkTravelersNumber(message.getObj());
				client.sendToClient((new Message((Object) (isOrderSucceed && isCurrentParkTravelerUpdated), Commands.RetParkWorkerCreateNewOrder)));
			} catch (IOException | SQLException e) {
				e.printStackTrace(); 
			}
			break;
			
		case WorkerCheckNewOrder:
			try {
				boolean checkOrder = WorkerControllerServer.checkNewOrder(message.getObj());
				client.sendToClient((new Message((Object) checkOrder, Commands.RetWorkerCheckNewOrder)));
			} catch (IOException e) {
				e.printStackTrace(); 
			}
			break;
			
		case CheckOnlineWorker:// get the traveler info by id
			try {
				boolean isOnline = WorkerControllerServer.addOnlineWorker(message.getObj());
				client.sendToClient((new Message((Object) isOnline, Commands.RetCheckOnlineWorker)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;

		case RemoveOnlineWorker:// get the traveler info by id
			try {
				boolean isRemoved = WorkerControllerServer.removeOnlineWorker(message.getObj());
				client.sendToClient((new Message((Object) isRemoved, Commands.RetRemoveOnlineWorker)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		case CheckOccupancy:
			try {
				boolean isRemoved = WorkerControllerServer.checkOccupancy(message.getObj());
				client.sendToClient((new Message((Object) isRemoved, Commands.RetCheckOccupancy)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		
			
		default:
			break;
		}
	}
	/**
	 * this method is inherited method to signal us that the server has started and is listening to a given port.
	 */
	protected void serverStarted() {
		runThreads=true;
		ticketPrice = TicketPrice.getInstance(ParkOrginalTicketPrice);
		Thread messageThread = new Thread(new SystemMessageControllerThread());
		messageThread.start();
		Thread queueThread = new Thread(new SystemQueueControllerThread());
		queueThread.start();
		Thread managerThread = new Thread(new SystemManagerControllerThread());
		managerThread.start();
		System.out.println("Server listening for connections on port " + getPort());
	}
	/**
	 * this method is inherited method of java to let us know the server has stopped listening for connections.
	 */
	protected void serverStopped() {
		runThreads = false;
		System.out.println("Server has stopped listening for connections.");
	}

}
