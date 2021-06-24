package boundary;

import java.net.URL;
import java.nio.channels.NetworkChannel;
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
 * Class that is the Controller of the Casual Submission FXMl.
 * @version 1.0
 * @author Group 20

 * 
 */
public class ParkWorkerNewOrderCasualSubmissionController implements Initializable {

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
    
    @FXML
    void goBackToWorkerPage(MouseEvent event) {
    	goBackButton.getScene().getWindow().hide();
    }

    
    /**
     * Method that submits the casual order.
     */
    @FXML
    void submitNewOrder(MouseEvent event) {
			//everything is ok, now we will add casual traveler order.
			if ( order != null && WorkerControllerClient.createNewOrder(order)) {
				WorkerControllerClient.checkOccupancy();
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
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	
    	orderSucceed = false;
    	order = ParkWorkerHomePageController.getCasualTravelerOrder();
    	double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);// get ticket price.
    	id.setText(ParkWorkerHomePageController.getTravlerID()+"");
    	orderPrice.setText(ticketPrice + "�");
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
		case CasualFamily: case CasualFamilyNoSubscription:
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
