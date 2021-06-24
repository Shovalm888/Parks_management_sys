package boundary;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import entity.Order;
import enums.OrderStatus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
/**
 * 
 * @author group 20
 * @version 1.0
 
 *
 */
public class TravelerMyOrdersController implements Initializable {
/**
 *  @param YES this is a ButtonData component for YES button data in the system.
 * @param ordersTable this is a TableView component for ordersTable in the system.
 * @param orderNumberColumn this is a TableColumn component for orderNumberColumn in the system.
 * @param parkColumn this is a TableColumn component for parkColumn in the system.
 * @param dateColumn this is a TableColumn component for dateColumn in the system.
 * @param timeColumn this is a TableColumn component for timeColumn in the system.
 * @param statusColumn this is a TableColumn component for statusColumn in the system.
 * @param cancelOrderButton this is a Button component for cancelOrderButton in the system.
 * @param goBackButton this is a ImageView component for goBackButton in the system.
 * @param travelerID this is a int object component for saving travelerID in the system.
 * @param ordersIDList this is a ArrayList<Integer> component for saving ordersIDList in the system.
 * @param orders this is a ArrayList<Order> component for saving orders in the system.
 */
	private static final ButtonData YES = null;
	@FXML
	private TableView<Order> ordersTable;
	@FXML
	private TableColumn<Order, String> orderNumberColumn;
	@FXML
	private TableColumn<Order, String> parkColumn;
	@FXML
	private TableColumn<Order, String> dateColumn;
	@FXML
	private TableColumn<Order, String> timeColumn;
	@FXML
	private TableColumn<Order, String> statusColumn;
	@FXML
	private Button cancelOrderButton;
	@FXML
	private ImageView goBackButton;

	private static int travelerID;
	private ArrayList<Integer> ordersIDList = new ArrayList<>();
	ArrayList<Order> orders = new ArrayList<>();

	/**
	 * this method hides the goBackButton component screen.
	 * @param event
	 */
	@FXML
	void goBack(MouseEvent event) {
		goBackButton.getScene().getWindow().hide();
	}

	public static void setTravelerID(int id) {
		travelerID = id;
	}
/**
 * this method cancel the selected order from the tableView component.
 * @param event the mouse event.
 */
	@FXML
	void removeTravelerOrder(MouseEvent event) {
		if (ordersTable.getSelectionModel().getSelectedItem() != null) {// if there is existing order.
			int tableOrderIDNumber = ordersTable.getSelectionModel().getSelectedItem()
					.getOrderID();/* get table order id number to// cancel the selected order. */
			//check if the user sure about cancelling the order.
			Alert alert = new Alert(AlertType.NONE,"Are you sure you want to cancel order no. "+tableOrderIDNumber + "?");
			alert.setTitle("Order Cancellation");
			ButtonType btn1 = ButtonType.YES;
			ButtonType btn2 = ButtonType.NO;
			alert.getButtonTypes().addAll(btn1,btn2);
			alert.showAndWait();
			if((alert.getResult()==btn2)) {
				return;
			}
			int realOrderIDNumber = ordersIDList.get(tableOrderIDNumber - 1);
			/**
			 * get the real order id number(thats saved in DB) to cancel the selected order.
			 * now we change order status to 'Cancelled' in DB.
			 */
			orders.get(tableOrderIDNumber - 1).setOrderID(realOrderIDNumber);/* update the order id. */
			if (orders.get(tableOrderIDNumber - 1).getOrderStatus() == OrderStatus.Pending
					|| orders.get(tableOrderIDNumber - 1).getOrderStatus() == OrderStatus.Approved) {
				orders.get(tableOrderIDNumber - 1).setOrderStatus(OrderStatus.Cancelled);// set the order status.
				boolean orderIsCancelled = TravelerControllerClient
						.changeOrderStatusByID(orders.get(tableOrderIDNumber - 1));
				if (orderIsCancelled) {// if cancel order succeed, then update table.
					updateOrdersTableView();
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Cancel Order Failed.");
					alert.setContentText("Cancel Failed.");
					alert.showAndWait();
				}
			} else if (orders.get(tableOrderIDNumber - 1).getOrderStatus() == OrderStatus.AtWaitingList) {
				//orders.get(tableOrderIDNumber - 1).setOrderStatus(OrderStatus.Cancelled);// set the order status.
				boolean orderIsCancelled = TravelerControllerClient
						.changeOrderStatusByID(orders.get(tableOrderIDNumber - 1));
				if (orderIsCancelled) {// if cancel order succeed, then update table.
					updateOrdersTableView();
				} else {
					alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("Cancel Order Failed.");
					alert.setContentText("Cancel Failed.");
					alert.showAndWait();
				}
			} else {
				alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText("Cancel Order Failed.");
				switch (orders.get(tableOrderIDNumber - 1).getOrderStatus()) {
				case Cancelled:
					alert.setContentText("Order is already Cancelled.");
					alert.showAndWait();
					break;
				case Expired: case Done: 
					alert.setContentText("Can't Cancel Order because it is already Expired");
					alert.showAndWait();
					break;
				case CurrentlyAtThePark:
					alert.setContentText("Can't Cancel Order because order is already started.");
					alert.showAndWait();
					break;
				case WaitingForConfirmation:
					alert.setContentText("Messages with 'WaitingForConfirmation' status,\n can be removed only from 'My Messages'");
					alert.setHeaderText("Order can't be cancelled from here");
					alert.showAndWait();
					break;
				default:
					alert.setContentText("Only orders with status 'Pending' or 'AtWaitingList' can be cancelled");
					alert.setHeaderText("Can't Cancel Order");
					alert.showAndWait();
					break;
				}
			}
		}
		else {
			Alert alert = new Alert(AlertType.ERROR, "Please select which order you want to cancel");
			alert.setHeaderText("Order had not selected");
			alert.showAndWait();
		}
	}
/**
 * this method updates the traveler order tableView if exists.
 */
	public void updateOrdersTableView() {// function that update the table orders list.
		ordersTable.getItems().removeAll(orders);// remove the existing orders, so we can update the list.
		orders = TravelerControllerClient.GetTravelerOrders(travelerID + "");// gets traveler orders.

		// Set rows colors:
		// #0bc91ec4 - green
		// #e8613cd9 - red
		// #e4ea3bcf - yellow
		ordersTable.setRowFactory(tv -> {
			TableRow<Order> row = new TableRow<>();
		    row.itemProperty().addListener((obs, previousOrder, currentOrder) -> {
   		        if(currentOrder != null) {
		          if(currentOrder.getOrderStatus() == OrderStatus.Cancelled) {
		        	row.setStyle("-fx-background-color : #e8613cd9 ");
		          }
		          else if(currentOrder.getOrderStatus() == OrderStatus.Approved) {
		        	row.setStyle("-fx-background-color : #0bc91ec4 ");
		          }
		          else if(row.getIndex() % 2 == 0) 
			        			row.setStyle("-fx-background-color : #e0ebe8 ");
		          else 
			           row.setStyle("-fx-background-color : #b9e7da ");
		      } 
		          		    });
		    return row ;
		});
		
		if (orders != null) {// if there is orders then display them on the ordersTable.
			boolean pendingOrderExist = false;
			int i;
			
			for ( i = 0; i < orders.size(); i++) {
				ordersIDList.add(orders.get(i).getOrderID());/* save the real order id as presented in the database */
				orders.get(i).setOrderID(i + 1); //setting the order id to display by auto increment instead of the real order id
				ordersTable.getItems().add(orders.get(i));
				if(orders.get(i).getOrderStatus() == OrderStatus.Pending
						|| orders.get(i).getOrderStatus() == OrderStatus.AtWaitingList  )
					pendingOrderExist = true;      
			}
			int height = 40*i + 15;
			if(height > 335)height = 335;
			ordersTable.setPrefHeight(height);
			cancelOrderButton.setVisible(pendingOrderExist);
		} else {
			ordersTable.setPlaceholder(new Label("Currently there is no orders to display."));
		}
		ordersTable.refresh();
	}
	
	/**
	 * this method initialize TravelerMyOrderController class.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
//		orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
		parkColumn.setCellValueFactory(new PropertyValueFactory<>("park"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
		updateOrdersTableView();// update the orders table.
	}

}
