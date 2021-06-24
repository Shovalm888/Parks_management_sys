package boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import entity.Order;
import entity.Traveler;
import enums.OrderType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
    /**
     * This class is the Controller for the MyOrders for a specific Traveler in the park.
     * @version 1.0
     * @author Group 20
     */
public class TravelerOrderTableListController implements Initializable	 {
	/**
	 * @param datesTable     the table that we store in the traveler orders
	 * @param parkNameColumn parkName column in the traveler orders.
	 * @param dateColumn     the Date column in the traveler orders.
	 * @param timeColumn     the Time column in the traveler orders.
	 * @param order          specific order that the traveler choose.
	 * @param dateChosed     specific orderDate that the traveler choose.
	 */
	@FXML
	private TableView<Order> datesTable;
    @FXML
    private TableColumn<Order, String> parkNameColumn;
    @FXML
    private TableColumn<Order, String> dateColumn;  
    @FXML
    private TableColumn<Order, String> timeColumn;  
    @FXML
    private ImageView exitButton;
	@FXML
	private Button enterButton;
	
	private static Order order = null;
	private static boolean dateChosed = false;
	private static String url;
	
	public static void setFxmlSubmissionPage(String origUrl) {
		url = origUrl;
	}
	
	public static void setOrder(Order order) {
		TravelerOrderTableListController.order = order;
	}
	
	public static boolean dateChosed() {
		return dateChosed;
	}

	/**
	 * Method that helps the user to choose his desirable order from the list.
	 */
	@FXML
	void addNewAvailableOrder(MouseEvent event) throws IOException {
		//after user chose the order date he want we need to update the date.
				Order selectedOrder = datesTable.getSelectionModel().getSelectedItem();
				
		if( selectedOrder != null) {
		order.setOrderDate(selectedOrder.getOrderDate());
		order.setOrderTime(selectedOrder.getOrderTime());
		//now we will calculate the price.
		double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);//get ticket price.
		order.setTotalSum(ticketPrice);
		//now we will create the order.
		dateChosed = true;
		Parent root = FXMLLoader.load(getClass().getResource(url));
		url = "";
    	Stage stage = (Stage)enterButton.getScene().getWindow();
		stage.setTitle("Order submission");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
//		boolean isOrderCreated = TravelerControllerClient.createNewOrder(order);//create new order.
//		if(isOrderCreated) {
//			
//			
//			
//			
//			addingSucceed = true;
//			Alert alert = new Alert(AlertType.CONFIRMATION);
//			alert.setHeaderText("Succeed");
//			alert.setContentText("Order Created Successfully");
//			alert.showAndWait();
//			exitButton.getScene().getWindow().hide();
//		}else {
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setHeaderText("Failed");
//			alert.setContentText("failed to create new order.");
//			alert.showAndWait();
//		}
		}
		else {
			Alert alert = new Alert(AlertType.ERROR, "Please select which order you want");
			alert.setHeaderText("Order had not selected");
			alert.showAndWait();
		}
	}

	@FXML
	void exitWindow(MouseEvent event) {
		exitButton.getScene().getWindow().hide();
	}
	/**
	 * Method that initialize the page with the right names.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		// Get the caller fxml page
		parkNameColumn.setCellValueFactory(new PropertyValueFactory<>("park"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
		ArrayList<Order> orders = TravelerControllerClient.getAvailableOrderDates(order);
		if(!orders.isEmpty()) {//if orders is not empty, add to table.
			datesTable.getItems().addAll(orders);//add order to list.
			enterButton.setVisible(true);
			int height = 30*orders.size() + 45;
			if(height > 350)height = 350;
			datesTable.setPrefHeight(height);
		}else {
			datesTable.setPlaceholder(new Label("Currently all park dates are full."));
			enterButton.setVisible(false);
		}
	}



}
