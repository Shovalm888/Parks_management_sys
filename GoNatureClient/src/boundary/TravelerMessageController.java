package boundary;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import entity.Order;
import entity.TravelerMessage;
import enums.OrderStatus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableRow;
import enums.TravelerMessageType;
/**
 * 
 * @author group 20
 * @version 1.0

 * 
 *
 */
public class TravelerMessageController implements Initializable {
/**
 * @param goBackButton this is a ImageView component for goBackButton in the system.
 * @param messageTable this is a TableView component for messageTable in the system.
 * @param messageNumberColumn this is a TableColumn component for messageNumberColumn in the system.
 * @param messageInfoColumn this is a TableColumn component for messageInfoColumn in the system.
 * @param messageDateColumn this is a TableColumn component for messageDateColumn in the system.
 * @param messageTimeColumn this is a TableColumn component for messageTimeColumn in the system.
 * @param orderIDColumn this is a TableColumn component for orderIDColumn in the system.
 * @param approveButton this is a Button component for approveButton in the system.
 * @param disapproveButton this is a Button component for disapproveButton in the system.
 * @param travelerID this is a int component for travelerID in the system.
 * @param messages this is a ArrayList<TravelerMessage>  component for messages in the system.
 * @param ordersID this is a ArrayList<Integer> component for ordersID in the system.
 */
	@FXML
	private ImageView goBackButton;
	@FXML
	private TableView<TravelerMessage> messageTable;
	@FXML
	private TableColumn<TravelerMessage, String> messageNumberColumn;
	@FXML
	private TableColumn<TravelerMessage, String> messageInfoColumn;
	@FXML
	private TableColumn<TravelerMessage, String> messageDateColumn;
	@FXML
	private TableColumn<TravelerMessage, String> messageTimeColumn;
	@FXML
	private TableColumn<TravelerMessage, String> orderIDColumn;
	@FXML
	private Button approveButton;
	@FXML
	private Button disapproveButton;

	private static int travelerID;
	private ArrayList<TravelerMessage> messages = new ArrayList<>();
	private ArrayList<Integer> ordersID = new ArrayList<>();
	
	public static void setTravelerID(int id) {
		travelerID = id;
	}
/**
 * this method hide the traveler message page.
 * @param event
 */
	@FXML
	void goBack(MouseEvent event) {
		goBackButton.getScene().getWindow().hide();
	}
// #0bc91ec4 - green
// #e8613cd9 - red
// #e4ea3bcf - yellow
	
	/**
	 * this method display the traveler messages in a the javaFX tableView if exists..
	 */
	public void showMessages() {
		
		
		messageTable.getItems().removeAll(messages);//remove the entire table.
		messages = TravelerControllerClient.GetTravelerMessages(travelerID + "");// gets traveler orders.
		if (messages != null) {// if there is orders then display them on the ordersTable.
			boolean waitingForConfirmationMessageExist = false;
			int i, height = 0;
			for (i = 0; i < messages.size(); i++) {
				messages.get(i).setMessageID(i + 1);// setting the order id to display by auto increment instead of the real order id.
				ordersID.add(messages.get(i).getOrderID());//get the real order id before we change it.
				messages.get(i).setOrderID(i+1);// setting the order id to display by auto increment instead of the real order id.
				messageTable.getItems().add(messages.get(i));
				if(messages.get(i).getMessageType() == TravelerMessageType.ApproveArrival
						||messages.get(i).getMessageType() == TravelerMessageType.WaitingList)
					waitingForConfirmationMessageExist = true;                            //responsible if the buttons will appear or not 
			}
			 height = 30*i + 45;
				if (height > 420)
					height = 420;
				messageTable.setPrefHeight(height);

				approveButton.setVisible(waitingForConfirmationMessageExist);
				disapproveButton.setVisible(waitingForConfirmationMessageExist);

			} else {
				messageTable.setPlaceholder(new Label("Currently there is no messages."));
			}
		}
/**
 * this method approves the selected order from the tableView.
 * @param event the mouse event.
 */
	@FXML
	void approveOrder(MouseEvent event) {// need to approve order.
		// we will change order status and then we will delete the message.
		if (messageTable.getSelectionModel().getSelectedItem() != null) {// if the user select table row.
			int index = messageTable.getSelectionModel().getSelectedIndex();//get index of the select item in the table.
			int orderID = ordersID.get(index);//get order id of the select item in the table.
			//check if order status is 'WaitingForConfirmation' and order in queue table.
			Order checkOrder = TravelerControllerClient.GetTravelerInfoByInvitation(orderID + "");
			if (checkOrder.getOrderStatus() == OrderStatus.WaitingForConfirmation) {
				TravelerControllerClient.checkIfOrderInQueue(orderID);
			}

			Order approveOrder = new Order(orderID, OrderStatus.Approved);//create Order entity with order id and status only.
			boolean isStatusChanged = TravelerControllerClient.changeOrderStatusByID(approveOrder);//change the status of the order in the DB to Approved.
			messages = TravelerControllerClient.GetTravelerMessages(travelerID + "");// gets traveler orders.
			System.out.println("Message test ///////////  "+messages.toString() + "///////////////");
			boolean isRemoved = TravelerControllerClient.removeTravelerMessages(messages.get(index).getMessageID()+"");//remove message from DB by id.
			if(isStatusChanged && isRemoved) {
				Alert alert = new Alert(AlertType.CONFIRMATION,"Message Approved successfully.");
				alert.showAndWait();
			}else {
				Alert alert = new Alert(AlertType.ERROR,"Message Approval failed.");
				alert.showAndWait();
			}
			showMessages();
			messageTable.refresh();
		}
		else {
			Alert alert = new Alert(AlertType.ERROR, "Please select which message you want to Approve");
			alert.setHeaderText("message had not selected");
			alert.showAndWait();
		}
	}
	/**
	 * this method disapproves the selected order from the tableView.
	 * @param event the mouse event.
	 */
	@FXML
	void disapproveOrder(MouseEvent event) {// need to disapprove order.
		// we will change order status and then we will delete the message.
		if (messageTable.getSelectionModel().getSelectedItem() != null) {// if the user select table row.
			int index = messageTable.getSelectionModel().getSelectedIndex();//get index of the select item in the table.
			int orderID = ordersID.get(index);//get order id of the select item in the table.
			//check if order status is 'WaitingForConfirmation' and order in queue table.
			Order checkOrder = TravelerControllerClient.GetTravelerInfoByInvitation(orderID + "");
			if (checkOrder.getOrderStatus() == OrderStatus.WaitingForConfirmation) {
				TravelerControllerClient.checkIfOrderInQueue(orderID);
			}
			
			Order cancelOrder = TravelerControllerClient.GetTravelerInfoByInvitation(orderID+"");
			cancelOrder.setOrderStatus(OrderStatus.Cancelled);
			boolean isStatusChanged = TravelerControllerClient.changeOrderStatusByID(cancelOrder);
			messages = TravelerControllerClient.GetTravelerMessages(travelerID + "");// gets traveler orders.
			System.out.println("Message test ///////////  "+messages.toString() + "///////////////");
			boolean isRemoved = TravelerControllerClient.removeTravelerMessages(messages.get(index).getMessageID()+"");//remove message from DB by id.
			if(isStatusChanged && isRemoved) {
				Alert alert = new Alert(AlertType.CONFIRMATION,"Message Cancelled successfully.");
				alert.showAndWait();
			}else {
				Alert alert = new Alert(AlertType.ERROR,"Message Cancellation failed.");
				alert.showAndWait();
			}
			showMessages();
			messageTable.refresh();
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Please select which message you want to disapprove");
			alert.setHeaderText("message had not selected");
			alert.showAndWait();
		}
	}
	
/**
 * thie method initialize TravelerMessageController class.
 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/*
		 * initialize table with message Entity Parameters.
		 */
		messageNumberColumn.setCellValueFactory(new PropertyValueFactory<>("messageID"));
		messageInfoColumn.setCellValueFactory(new PropertyValueFactory<>("messageInfo"));
		orderIDColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		messageDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		messageTimeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
		// Set rows colors:
		// #0bc91ec4 - green
		// #e8613cd9 - red
		// #e4ea3bcf - yellow
		// #d9534f - dark red
		messageTable.setRowFactory(tv -> {
			TableRow<TravelerMessage> row = new TableRow<>();
		    row.itemProperty().addListener((obs, previousMessage, currentMessage) -> {
		        if(currentMessage != null) {
		        if(currentMessage.getMessageType() == TravelerMessageType.ApproveArrival
		        		|| currentMessage.getMessageType() == TravelerMessageType.WaitingList) {
		        	row.setStyle("-fx-background-color : #e4ea3bcf ");
		        	}
		        else if(currentMessage.getMessageType() == TravelerMessageType.Cancelled) {
		        	row.setStyle("-fx-background-color : #e8613cd9 ");
		        }
		        else if(currentMessage.getMessageType() == TravelerMessageType.Approved) {
		        	row.setStyle("-fx-background-color : #0bc91ec4 ");
		        }
		        else
		        	row.setStyle("-fx-background-color : #e0ebe8 ");		        }
		          		    });
		    return row ;
		});
		showMessages();//show messages if there is any.
	}

}
