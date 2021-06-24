package boundary;

import java.net.URL;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import client.WorkerControllerClient;
import entity.Order;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
/**
 * this class is in charge of submiting Home Page for park manager
 * @author group 20
 * @version 1.0
 *
 */
public class ParkManagerHomePageOrderSubmitionController implements Initializable {
/**
 *  @param submitButton this is a button componet for submit in the system
 * @param orderPrice this is a text componet for order price in the system
 * @param goBackButton this is a imageview componet for go back in the system
 * @param id this is a text componet for id in the system
 * @param parkName this is a text componet for park name in the system
 * @param date this is a text componet for date in the system
 * @param time this is a text componet for time in the system
 * @param visitorsNumber this is a text componet for visitor number in the system
 * @param orderType this is a text componet for order type in the system
 * @param order this is an order in the system
 * @param orderSucceed this is a boolean parameter to determine if an order is successful in the system.
 */
    @FXML
    private Button submitButton;

    @FXML
    private Text orderPrice;

    @FXML
    private ImageView goBackButton;

    @FXML
    private Text id;

    @FXML
    private Text parkName;

    @FXML
    private Text date;

    @FXML
    private Text time;

    @FXML
    private Text visitorsNumber;

    @FXML
    private Text orderType;

    private Order order;
    private static boolean orderSucceed;

    /**
     * this method is a getter for orderStatus in the system
     * @return the orderStatus in the system
     */
    public static boolean getOrderStatus() {
    	return orderSucceed;
    }
    /**
     * this method goes back to the worker page display 
     * @param event this is the event that triggers going back to the worker page
     */
    @FXML
    void goBackToWorkerPage(MouseEvent event) {
    	goBackButton.getScene().getWindow().hide();
    }
    /**
     * this method is in charge of submitting new order in the system
     * @param event this is the event that triggered the submit button in the system
     */
    @FXML
    void submitNewOrder(MouseEvent event) {
			//everything is ok, now we will add casual traveler order.
			if ( order != null && WorkerControllerClient.createNewOrder(order) ) {
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
     * this method initallizes the class
     * @param arg0 arg0
     * @param arg1 arg1
     */
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	orderSucceed = false;
    	order = ParkManagerHomePageController.getCasualTravelerOrder();
    	double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);// get ticket price.
    	id.setText(ParkManagerHomePageController.getTravlerID()+"");
    	orderPrice.setText(ticketPrice + "¤");
		parkName.setText(order.getPark());
		date.setText(order.getOrderDate()+"");
		time.setText(order.getOrderTime()+"");
		visitorsNumber.setText(order.getNumOfTravelers()+"");
		switch(order.getOrderType()) {
		case CasualSingle:
			orderType.setText("Casual-Single");
			break;
		case CasualSingleSubscription:
			orderType.setText("Casual-Single-Subscription");
			break;
		case CasualFamily:
			orderType.setText("Casual-Family");
			break;
		case CasualGroup:
			orderType.setText("Casual-Group");
		break;
			default:
				orderType.setText("Unrecognized");
		}
    }
}
