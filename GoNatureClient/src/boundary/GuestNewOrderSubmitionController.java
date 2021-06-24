package boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import entity.Order;
import entity.Traveler;
import enums.TravelerType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * this class is the stage for the submission of the order for guests
 * @version 1.0
 * @author group 20
 * 
 *
 */
public class GuestNewOrderSubmitionController implements Initializable{
/**
 * @param submitButton this is a button componet for submit button in the system
 * @param parkName this is a text componet for parkName in the system
 * @param payment this is a text componet for the payment in the system
 * @param orderDate this is a text componet for the order date in the system
 * @param orderTime this is a text componet for order time in the system
 * @param orderEmail this is a text componet for the order email system
 * @param orderPrice this is a text componet for the order price in the system
 * @param backToNewOrder this is a image view componet for back to new order in the system
 * @param visitors this is text componet for the visitors in the system
 * @param phoneNumber this is text componet for the phone number field in the system
 * @param lastName this is a text componet for the last name in the system
 * @param firstName this is a text componet for the first name in the system
 * @param idNumber this is a text componet for the id number in the system
 * @param Order this is an order for guest in the system
 * @param orderSucceed this is a boolean parameter to determine if the order is successful.
 */
    @FXML
    private Button submitButton;

    @FXML
    private Text parkName;
    
    @FXML
    private Text payment;

    @FXML
    private Text orderDate;

    @FXML
    private Text orderTime;

    @FXML
    private Text orderEmail;

    @FXML
    private Text orderPrice;

    @FXML
    private ImageView backToNewOrder;

    @FXML
    private Text visitors;

    @FXML
    private Text phoneNumber;

    @FXML
    private Text lastName;

    @FXML
    private Text firstName;

    @FXML
    private Text idNumber;
    
    private Order order;
    
    static boolean orderSucceed;
    
    public static boolean getOrderStatus() {
    	return orderSucceed;
    }
/**
 * this method is in charge of displaying the guest new order fxml page
 * @param event this is the event that triggers the display 
 */
    @FXML
    void displayGuestNewOrderPage(MouseEvent event) {
    	backToNewOrder.getScene().getWindow().hide();
    }
   /**
    * this method  is in charge of submitting new order display
    * @param event this is the event that triggers the display
    * @throws IOException
    */
    @FXML
    void submitNewOrder(MouseEvent event) throws IOException {
		// add new order, then display new fxml with my orders and my messages.
		if (order != null) {//the order from the check button.
			boolean isAdded = TravelerControllerClient.createNewOrder(order);//if create new order succeed.
			if(isAdded) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setHeaderText("succeed!");
				alert.setContentText("order created successfully");
				alert.showAndWait();
				//now create new traveler.
				String firstName = this.firstName.getText();
	        	String lastName = this.lastName.getText();
	        	String phone = phoneNumber.getText();
	        	String email = orderEmail.getText();
				String id = idNumber.getText();
				Traveler traveler = new Traveler(Integer.parseInt(id), firstName, lastName, email, phone,
						TravelerType.Single);

				 //create new traveler.
				if(TravelerControllerClient.createNewTraveler(traveler)) {
					//add him to online list.  -- He logged in the login page
				//boolean isOnline = TravelerControllerClient.addOnlineTraveler(id);
						
//						if (!isOnline) {// check if traveler id is in the online travelers table.
//							alert = new Alert(AlertType.ERROR);
//							alert.setHeaderText("Login Failed.");
//							alert.setContentText("Traveler is already online.");
//							alert.showAndWait();
//						}
					//now update home page.
					TravelerLoginPageController.setTravelerID(traveler.getID());
					TravelerLoginPageController.setTravelerName(traveler.getFirstName());
					TravelerLoginPageController.setType(traveler.getTravelerType());
					//stage= GuestTravelerHomePageController.getStage();
					//stage.hide();
					orderSucceed = true;
					submitButton.getScene().getWindow().hide();
			
				}else {
					System.out.println("failed to create new traveler.");
					Alert creationFailed = new Alert(AlertType.ERROR);
					creationFailed.setHeaderText("System ERROR!");
					creationFailed.setContentText("System can not create user.");
					creationFailed.showAndWait();
					return;
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Failed!");
				alert.setContentText("Order Failed");
				alert.showAndWait();
				return;
			}
		}
	}

    /**
     * this is the initallization of the class.
     * @param arg0 arg0
     * @param arg1 arg1
     */
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	orderSucceed = false;
    	order = GuestNewOrderSingleController.getSingleTravelerOrder();
    	double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);// get ticket price.
    	order.setTotalSum(ticketPrice);//set total cost
    	orderPrice.setText(ticketPrice + "¤");
		idNumber.setText(order.getTravlerID()+"");
    	parkName.setText(order.getPark());
		orderDate.setText(order.getOrderDate()+"");
		orderTime.setText(order.getOrderTime()+"");
		orderEmail.setText(order.getTravelerEmail());
		visitors.setText(order.getNumOfTravelers()+"");
		// importing F.name , L.name and phone number :
		String[] personalData = GuestNewOrderSingleController.getPersonalData();
		firstName.setText(rewriteNameCorrectly(personalData[0]));
		lastName.setText(rewriteNameCorrectly(personalData[1]));
		phoneNumber.setText(personalData[2]);
		switch(order.getOrderType()) {
		case Single:
			payment.setText("single");
			break;
		case PrePaymentGroup: case PrePaymentOrganizedGroup:
			payment.setText("Pre Payment");
			break;
		case PrivateGroup: case OrganizedGroup:
			payment.setText("Pay at the Park");
			break;
			default:
				payment.setText("Unrecognized");
		}
    }
    /**
     * this method is in charge of rewriting strings correctly
     * @param name this is the name we want to rewrite
     * @return the new written name
     */
    String rewriteNameCorrectly(String name) {
    	return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

}
