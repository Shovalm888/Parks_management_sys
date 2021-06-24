package client;

import java.io.IOException;


import common.*;
import entity.Message;
import ocsf.client.AbstractClient;

/**
 
 * @author group-20
 * @version 1.0
 *
 */
public class GoNatureClient  extends AbstractClient {
/**
 * * @param clientUI this is the clientUI
 * @param ServerRetObj this is the return object from server.
 * @param awaitResponse this boolean object help us to understand if we are waiting for response from server or not.
 */
	AppTestIF clientUI;
	public static Object ServerRetObj;
	public static boolean awaitResponse = false;
/**
 * this constructor initialize the host,port,clientUI.
 * @param host host
 * @param port port 
 * @param clientUI client 
 * @throws IOException exception
 */
	public GoNatureClient(String host, int port, AppTestIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
	}
	
	/**
	 * this method handle message from server.
	 * @param msg this is the object we get from server.
	 */
	public void handleMessageFromServer(Object msg) {//the message from the server.
		System.out.println("Client recive from Server---");
		if (msg != null) {
			Message message = (Message) msg;

			switch (message.getCmd()) {
				case RetNewTraveler:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetTravelerInfoByID:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetTravelerInfoBySubscription:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetTravelerInfoByOrderID:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetRemoveOnlineTraveler:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCheckOnlineTraveler:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetTravelerOrders:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCancelTravelerOrder:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetTravelerMessages:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetRemoveTravelerMessage:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCheckNewOrder:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetGetOrderTotalCost:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCreateNewOrder:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetGetParkTimeOfStay:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetAddToQueue:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetGetAvailableOrderDates:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetGetSubscriptionTravelerByID:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
					
				/*
				 * 
				 * 
				 * 
				 * now the commands below is consider to workers methods.
				 * 
				 * 
				 */
					
				case RetWorkerInfo:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetGetParkCapacityNumber:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetGetTravelerInfo:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetUpdateTravelerType:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetUpdateTravelerInfo:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCreateNewSubscription:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCreateNewTraveler:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCreateNewOrderCasual:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCheckParkParameterUpdate:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCreateNewRequest:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetRequests:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCheckEvent:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetUpdateParkParameter:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetGetEventByRequestID:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetGetReportOrders:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetGetParkInfo:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCheckForEvent:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetParkWorkerCreateNewOrder:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetWorkerCheckNewOrder:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCheckOnlineWorker:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetRemoveOnlineWorker:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCheckOccupancy:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetCheckIfOrderInQueue:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
				case RetGetUpcomingEvents:
					ServerRetObj = message.getObj();
					System.out.println("Cmd= "+message.getCmd()+",obj= " + message.getObj());
					break;
					/*
					 * 
					 * 
					 */
			default:
				System.out.println("Error in getting ret cmd. go nature client,");
				break;
			}
		}
		else System.out.println("NULL RECIEVED FROM THE SERVER !!");
		awaitResponse = false;
	}

	/**
	 * this method handle message from Client-UI.
	 * @param str the object we get from the Client-UI.
	 */
	public void handleMessageFromClientUI(Object str) {//handle the message from clientUI then send the message to the server.
		try {
			awaitResponse = true;
			openConnection();
			sendToServer(str);//here we send the object to server.
			// wait for response
			while (awaitResponse) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			e.printStackTrace();
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}