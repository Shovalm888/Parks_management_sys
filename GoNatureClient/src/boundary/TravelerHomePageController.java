package boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import enums.TravelerType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * 
 * @author group 20
 * @version 1.0

 * 
 */
public class TravelerHomePageController implements Initializable {
/**
 * @param newOrderButton this is a Button component for newOrderButton in the system.
 * @param myOrdersButton this is a Button component for myOrdersButton in the system.
 * @param myMessagesButton this is a Button component for myMessagesButton in the system.
 * @param travelerFirstNameOutput this is a Text component for travelerFirstNameOutput in the system.
 * @param travelerType this is a TravelerType component for travelerType in the system.
 * @param travelerID this is a int component for travelerID in the system.
 * @param MainPagePath this is a String component for MainPagePath in the system.
 * @param TravelerMessagePath this is a String component for TravelerMessagePath in the system.
 * @param TravelerMyOrdersPath this is a String component for TravelerMyOrdersPath in the system.
 * @param TravelerNewOrderOptionsPagePath this is a String component for TravelerNewOrderOptionsPagePath in the system.
 * @param TravelerNewOrderFamilyPath this is a String component for TravelerNewOrderFamilyPath in the system.
 * @param TravelerNewOrderGroupPath this is a String component for TravelerNewOrderGroupPath in the system.
 */
	@FXML
	private Button newOrderButton;
	@FXML
	private Button myOrdersButton;
	@FXML
	private Button myMessagesButton;
	@FXML
	private Text travelerFirstNameOutput;

	private TravelerType travelerType;
	private int travelerID;
	private final String MainPagePath = "MainPage.fxml";
	private final String TravelerMessagePath = "TravelerMessage.fxml";
	private final String TravelerMyOrdersPath = "TravelerMyOrders.fxml";
	private final String TravelerNewOrderOptionsPagePath = "TravelerNewOrderOptionsPage.fxml";
	private final String TravelerNewOrderFamilyPath = "TravelerNewOrderFamily.fxml";
	private final String TravelerNewOrderGroupPath = "TravelerNewOrderGroup.fxml";
	
/**
 * this method displays the main page of GoNature.
 * @param event the mouse event.
 * @throws IOException
 */
	@FXML
	void displayMainPage(MouseEvent event) throws IOException {
		boolean isRemoved = TravelerControllerClient.removeOnlineTraveler(travelerID + "");
		if(isRemoved) {
			System.out.println(travelerFirstNameOutput.getText()+ " Successfully removed from online table.");
		} else {
			System.out.println("Failed to remove from online table.");
		}
		Parent root = FXMLLoader.load(getClass().getResource(MainPagePath));
		Stage stage = (Stage) newOrderButton.getScene().getWindow();
		stage.setTitle("GoNature System");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}
/**
 * this method displays traveler messages in javaFX tableView.
 * @param event the mouse event.
 * @throws IOException
 */
	@FXML
	void displayMyMessagesPage(MouseEvent event) throws IOException {
		TravelerMessageController.setTravelerID(travelerID);// set traveler id for messages controller
		Parent root = FXMLLoader.load(getClass().getResource(TravelerMessagePath));
		Stage stage = new Stage();
		stage.setTitle("My Messages");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
	}
	/**
	 * this method displays traveler orders in javaFX tableView.
	 * @param event the mouse event.
	 * @throws IOException
	 */
	@FXML
	void displayMyOrdersTravelerPage(MouseEvent event) throws IOException {
		TravelerMyOrdersController.setTravelerID(travelerID);// set traveler id for orders controller
		Parent root = FXMLLoader.load(getClass().getResource(TravelerMyOrdersPath));
		Stage stage = new Stage();
		stage.setTitle("My Orders");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
	}
/**
 * this method display traveler new order form page according to traveler type.
 * @param event the mouse event.
 * @throws IOException
 */
	@FXML
	void displayNewOrderTravelerPage(MouseEvent event) throws IOException {
		switch (travelerType) {
		case Single: // 1 = single traveler
			TravelerNewOrderOptionsPageController.setOrderPage("TravelerNewOrderSingle.fxml");
			Parent root = FXMLLoader.load(getClass().getResource(TravelerNewOrderOptionsPagePath));
			Stage stage = new Stage();
			stage.setTitle("Order options");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			break;
			
		case FamilySubscription:// 2 = family traveler
			TravelerNewOrderOptionsPageController.setOrderPage(TravelerNewOrderFamilyPath);
			root = FXMLLoader.load(getClass().getResource(TravelerNewOrderOptionsPagePath));
			stage = new Stage();
			stage.setTitle("Order options");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			break;

		case SingleSubscription:// 3 = single traveler
			TravelerNewOrderOptionsPageController.setOrderPage(TravelerNewOrderFamilyPath);
			root = FXMLLoader.load(getClass().getResource(TravelerNewOrderOptionsPagePath));
			stage = new Stage();
			stage.setTitle("New Order");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			break;

		case Guide:// 4 = group traveler
			root = FXMLLoader.load(getClass().getResource(TravelerNewOrderGroupPath));
			stage = new Stage();
			stage.setTitle("New Order");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			break;
		default:
			System.out.println("error in defining traveler type.");
			break;
		}
	}
	/**
	 * this method initialize TravelerHomePageController class.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		travelerType = TravelerLoginPageController.getType();// Initialize traveler type
		travelerID = TravelerLoginPageController.getID();// Initialize traveler id
		String name = TravelerLoginPageController.getName();
		travelerFirstNameOutput.setText(name.substring(0, 1).toUpperCase() + name.substring(1));// initialize traveler nameOutput.

		newOrderButton.setTooltip(new Tooltip("Creates new order."));
		myOrdersButton.setTooltip(new Tooltip("Display your orders."));
		myMessagesButton.setTooltip(new Tooltip("Display your messages."));
		newOrderButton.setCursor(Cursor.HAND);
		myOrdersButton.setCursor(Cursor.HAND);
		myMessagesButton.setCursor(Cursor.HAND);
	}
}
