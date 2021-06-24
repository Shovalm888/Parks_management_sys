package boundary;

import java.net.URL;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import entity.Order;
import enums.OrderType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * This class is the Controller for the New Group order in the park.
 * @version 1.0
 * @author Group 20
 * 
 */
public class TravelerNewOrderGroupSubmitionController  implements Initializable{
/**
 * @param submitButton submit button
 * @param parkName is where we save the wanted park from the user.
 * @param orderDate is where we save the wanted date from the user.
 * @param orderTime is where we save the wanted time from the user.
 * @param orderEmail is where we save the wanted email from the user.
 * @param orderPrice is where we show the price of the specific order.
 */
    @FXML
    private Button submitButton;

    @FXML
    private Text parkName;

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
    private Text payment;

    @FXML
    private Text visitors;
    
    private Order order;
    
    static boolean orderSucceed;
    
    public static boolean getOrderStatus() {
    	return orderSucceed;
    }

    @FXML
    void displayGroupNewOrderPage(MouseEvent event) {
    	backToNewOrder.getScene().getWindow().hide();
    }
    
    /**
     * Method that submits the user order and shows him the relevant message,success or faild.
     */
    @FXML
    void submitNewOrder(MouseEvent event) {
		if (TravelerControllerClient.createNewOrder(order) && order != null) {
			// need to update my sql tables.
			orderSucceed = true;
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("succeed!");
			alert.setContentText("order created successfully");
			alert.showAndWait();
			submitButton.getScene().getWindow().hide();
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Failed!");
			alert.setContentText("Order Failed");
			alert.showAndWait();
			submitButton.getScene().getWindow().hide();
		}
    }
    /**
     * Method that initialize the page with the right names and parameters.
     */
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	orderSucceed = false;
    	order = TravelerNewOrderGroupController.getSingleTravelerOrder();
    	double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);// get ticket price.
    	orderPrice.setText(ticketPrice + "¤");
		parkName.setText(order.getPark());
		orderDate.setText(order.getOrderDate()+"");
		orderTime.setText(order.getOrderTime()+"");
		orderEmail.setText(order.getTravelerEmail());
		visitors.setText(order.getNumOfTravelers()+"");
		switch(order.getOrderType()) {
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

}
