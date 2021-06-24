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
 * 
 * @author group 20
 * @version 1.0

 * 
 *
 */
public class DepartmentManagerOrderSubmitionController implements Initializable {
/**
 *  @param submitButton this is a Button component for submitButton in the system.
 * @param orderPrice this is a Text component for orderPrice in the system.
 * @param goBackButton this is a ImageView component for goBackButton in the system.
 * @param id this is a Text component for id text in the system.
 * @param parkName is a Text component for parkName in the system.
 * @param date is a Text component for date in the system.
 * @param time is a Text component for time in the system.
 * @param visitorsNumber is a Text component for visitorsNumber in the system. 
 * @param orderType is a Text component for orderType in the system.
 * @param order this is Order object for saving order data in the system.
 * @param orderSucceed this is boolean object for saving true,false data in the system.
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

    
    public static boolean getOrderStatus() {
    	return orderSucceed;
    }
   /**
    * this method display department manager home page stage. 
    * @param event the mouse event.
    */
    @FXML
    void goBackToWorkerPage(MouseEvent event) {
    	goBackButton.getScene().getWindow().hide();
    }
/**
 * this method creates new order in the DB if available.
 * @param event the mouse event.
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
     * this method initialize DepartmentManagerOrderSubmitionController class.
     */
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	orderSucceed = false;
    	order = DepartmentManagerHomePageController.getCasualTravelerOrder();
    	double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);// get ticket price.
    	id.setText(DepartmentManagerHomePageController.getTravlerID()+"");
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
