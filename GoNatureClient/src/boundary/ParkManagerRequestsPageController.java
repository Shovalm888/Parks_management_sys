package boundary;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.WorkerControllerClient;
import entity.Order;
import entity.ParkWorker;
import entity.Request;
import enums.OrderStatus;
import enums.RequestStatus;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
/**
 * this class is in charge of the request page features we have in the system
 * @version 1.0
 * @author group 20
 
 */
public class ParkManagerRequestsPageController implements Initializable {
/**
 * @param park this is a text componet for park in the system
 * @param requestTable this is a tableview componet for request table in the system
 * @param typecolumn this is a tablecolumn componet for type column in the system
 * @param noColumn this is a tablecolumn componet for no column in the system
 * @param DataColumn this is a tablecolumn componet for for data column in the system
 * @param valueColumn this is a tablecolumn componet for value column in the system
 * @param fromColumn this is a tablecolumn componet for fromColumn in the system
 * @param statusColumn this is a tablecolumn componet for status column in the system
 * @param dateColumn this is a tablecolumn componet for for date column in the system
 * @param timeColumn this is a tablecolumn componet for time column in the system
 * @param goBackButton this is a imageView componet for go back button in the system
 * @param requests this is array list of requests in the system
 * @param worker this is a park worker entity in the system
 */
    @FXML
    private Text park;
	@FXML
	private TableView<Request> requestsTable;
	@FXML
	private TableColumn<Request, String> typeColumn;
	@FXML
	private TableColumn<Request, String> noColumn;
	@FXML
	private TableColumn<Request, String> dataColumn;
	@FXML
	private TableColumn<Request, String> valueColumn;
	@FXML
	private TableColumn<Request, String> fromColumn;
	@FXML
	private TableColumn<Request, String> statusColumn;
	@FXML
	private TableColumn<Request, String> dateColumn;
	@FXML
	private TableColumn<Request, String> timeColumn;
    @FXML
    private ImageView goBackButton;

	private ArrayList<Request> requests;
	private ParkWorker worker;
	/**
	 * this method is in charge of updating the requests table in the system
	 */
	private void updateRequestsTable() {
		requestsTable.setPlaceholder(new Label("Request table is currently empty."));
		if (requests != null) {
			requestsTable.getItems().removeAll(requests);// remove the existing requests, so we can update the list.
		}
		int workerID = worker.getWorkerID();
		requests = WorkerControllerClient.getWorkerRequests(workerID);// get worker requests.
		if (requests != null) {// if there is requests.
			requestsTable.getItems().addAll(requests);// add all of the requests.
		}
	}
	/**
	 * this method is in charge of taking you to the previous screen in the sytem
	 * @param event this is the event that triggers going back to the previous screen
	 */
	@FXML
	void goBack(MouseEvent event) {
		goBackButton.getScene().getWindow().hide();
	}
	/**
	 * this method initallizes the class
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		//set all table view cells.
		noColumn.setCellValueFactory(new PropertyValueFactory<>("requestID"));
		dataColumn.setCellValueFactory(new PropertyValueFactory<>("requestData"));
		valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		fromColumn.setCellValueFactory(new PropertyValueFactory<>("workerID"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		worker = WorkerLoginPageController.getWorker();//set worker
		park.setText(worker.getPark());
		
		// #0bc91ec4 - green
		// #e8613cd9 - red
		// #e4ea3bcf - yellow
		requestsTable.setRowFactory(tv -> {
			TableRow<Request> row = new TableRow<>();
		    row.itemProperty().addListener((obs, previousOrder, currentOrder) -> {
		    	if(currentOrder != null) {
		    		switch(currentOrder.getStatus()) {
		    		case Cancelled: 
				    	row.setStyle("-fx-background-color : #e8613cd9 ");
                        break;
		    		case Approved: 
				    	row.setStyle("-fx-background-color : #0bc91ec4 ");
                        break;
		    		case Pending: 
				    	row.setStyle("-fx-background-color : #e4ea3bcf ");
                        break;                    	
		    		}
		    	}
		          		    });
		    return row ;
		});
		updateRequestsTable();// update the orders table.
		//set tool tip
	}

}
