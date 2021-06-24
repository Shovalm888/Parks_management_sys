package boundary;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.WorkerControllerClient;
import entity.ParkWorker;
import entity.Request;
import enums.RequestStatus;
import enums.RequestType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
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
 * this class is in charge of the different features we have in the request page for the department manager in the system
 * @author group 20
 * @version 1.0
 * 
 *
 */
public class DepartmentManagerRequestPageController implements Initializable {

/**
 * @param park this is a text componet for park in the system
 * @param requestsTable this is a table view componet for requests table in the system
 * @param noColumn this is a TableColumn componet for no column in the system
 * @param dataColumn this is a TableColumn componet for data column in the system
 * @param valueColumn this is a TableColumn componet for value column in the system
 * @param fromColumn this is a TableColumn componet for from column in the system
 * @param dateColumn statusColumn this is a TableColumn componet for date column in the system
 * @param timeColumn this is a TableColumn componet for time column in the system 
 * @param statusColumn this is a TableColumn componet for status column in the system
 * @param goBackButton this is a imagebiew componet of go back in the system
 * @param approveButton this is a button componet for approve in the system
 * @param disapproveButton this is Hyperlink componet for disaspporve in the system
 * @param requests this is an array list of requests in the system
 * @param requestIDList this is an array list of request IDS in the system
 * @param worker this is a park worker entity in the system
 */
    @FXML
    private Text park;
	@FXML
	private TableView<Request> requestsTable;
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
	@FXML
	private Button approveButton;
    @FXML
    private Hyperlink disapproveButton;

	private ArrayList<Request> requests;
	private ArrayList<Request> requestIDList;
	private ParkWorker worker;
	/**
	 * this method is in charge of approving requests in the system
	 * @param event this is the event that triggers approving requests in the system
	 */
	@FXML
	void approveRequest(MouseEvent event) {
		if(requestsTable.getSelectionModel().getSelectedItem()==null) {//if user didn't select request to approve.
			Alert alert = new Alert(AlertType.ERROR,"Please choose request to approve.");
			alert.showAndWait();
			return;
		}
		int requestIndex = requestsTable.getSelectionModel().getSelectedIndex();//table index
		int realRequestID = requestIDList.get(requestIndex).getRequestID();//real request id in table index place.
		requests.get(requestIndex).setRequestID(realRequestID);
		//now check agian if we can approve the request and then delete the request and make changes.
		//check request details.
		Request request = requests.get(requestIndex);
		RequestType type = request.getType();
		if(type == RequestType.Event) {
			entity.Event tmp = WorkerControllerClient.getEventByRequestID(request.getRequestID());
			boolean canUpdateEvent = WorkerControllerClient.checkEvent(tmp);
	    	if(canUpdateEvent) {//if true, we will update the event.
	    		request.setStatus(RequestStatus.Approved);
	    		request.setStartDate(tmp.getStart());//set request start date *only for events
	    		request.setEndDate(tmp.getEnd());//set request end date *only for events
	    		System.out.println(request.toString());
	    		boolean isUpdated = WorkerControllerClient.updateParkParameter(request);
	    		if(isUpdated) {//updates succeed.
	    			Alert alert = new Alert(AlertType.CONFIRMATION,"Request approved successfully.");
	        		alert.showAndWait();
	        		updateRequestsTable();
	        		return;
	    		}else {
	    			Alert alert = new Alert(AlertType.ERROR,"Failed to update event.");
	        		alert.showAndWait();
	        		return;
				}
	    	}else {
	    		Alert alert = new Alert(AlertType.ERROR,"Sorry Request can't be approved.\nPlease choose disapprove button to disapper this request.");
	    		alert.showAndWait();
	    		return;
			}
		}else {//the request type != event.
			requestIndex = requestsTable.getSelectionModel().getSelectedIndex();//table index
			realRequestID = requestIDList.get(requestIndex).getRequestID();//real request id in table index place.
			requests.get(requestIndex).setRequestID(realRequestID);
			request = requests.get(requestIndex);
			//now check agian if we can approve the request and then delete the request and make changes.
			//check request details.
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(request.getType());
			list.add(request.getValue());
			list.add(request.getPark());
			list.add(request.getTime());
			list.add(request.getDate());
			boolean canUpdateParkCapacity = WorkerControllerClient.checkParkParameterUpdate(list);//list = ( type, value, park-name, time,date)
	    	if(canUpdateParkCapacity) {//now update.
	    		request.setStatus(RequestStatus.Approved);
	    		boolean isUpdated = WorkerControllerClient.updateParkParameter(request);
	    		if(isUpdated) {//updates succeed.
	    			Alert alert = new Alert(AlertType.CONFIRMATION,"Request approved successfully.");
	        		alert.showAndWait();
	        		updateRequestsTable();
	        		return;
	    		}
	    		else {
	    			Alert alert = new Alert(AlertType.ERROR,"Sorry Request can't be approved.");
	        		alert.showAndWait();
	        		return;
				}
	    	}
	    	else {
	    		Alert alert = new Alert(AlertType.ERROR,"Sorry Request can't be approved.");
	    		alert.showAndWait();
	    		return;
			}
		}
	}
	/**
	 * this method is in charge of disapproving request in the system
	 * @param event this is the event that triggers disapproving request.
	 */
	@FXML
	void disapproveRequest(MouseEvent event) {
		if (requestsTable.getSelectionModel().getSelectedItem() == null) {
			Alert alert = new Alert(AlertType.ERROR, "Please choose request to disapprove.");
			alert.showAndWait();
			return;
		}else {
			//here we will change request status to cancelled.
			int requestIndex = requestsTable.getSelectionModel().getSelectedIndex();//table index
			int realRequestID = requestIDList.get(requestIndex).getRequestID();//real request id in table index place.
			requests.get(requestIndex).setRequestID(realRequestID);
			//now check agian if we can approve the request and then delete the request and make changes.
			//check request details.
			Request request = requests.get(requestIndex);
			request.setStatus(RequestStatus.Cancelled);
			boolean isUpdated = WorkerControllerClient.updateParkParameter(request);
    		if(isUpdated) {//updates succeed.
    			Alert alert = new Alert(AlertType.CONFIRMATION,"Request disapproved successfully.");
        		alert.showAndWait();
        		updateRequestsTable();
        		return;
    		}
    		else {
    			Alert alert = new Alert(AlertType.ERROR,"Sorry Request can't be disapproved.");
        		alert.showAndWait();
        		return;
			}
		}
	}
	/**
	 * this method is in charge of updating the requests table in the system
	 */
	private void updateRequestsTable() {
		approveButton.setVisible(false);
		disapproveButton.setVisible(false);
		requestsTable.setPlaceholder(new Label("Request table is currently empty."));
		if (requests != null) {
			requestsTable.getItems().removeAll(requests);// remove the existing requests, so we can update the list.
		}
		int workerID = worker.getWorkerID();
		requests = WorkerControllerClient.getWorkerRequests(workerID);// get worker requests.
		if (requests != null && !requests.isEmpty()) {// if there is requests.
			approveButton.setVisible(true);
			disapproveButton.setVisible(true);
			requestIDList = new ArrayList<>();
			for (int i = 0; i < requests.size(); i++) {
				Request request = new Request(requests.get(i));
				requestIDList.add(request);
				requests.get(i).setRequestID(i + 1);
			}
			requestsTable.getItems().addAll(requests);// add all of the requests.
		}
	}
	/**
	 * this method is the go back feature in the system
	 * @param event this is the event that triggers going back in the system
	 */
	@FXML
	void goBack(MouseEvent event) {
		goBackButton.getScene().getWindow().hide();
	}
	/**
	 * this is the initallization of the class
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		worker = WorkerLoginPageController.getWorker();//set worker
		noColumn.setCellValueFactory(new PropertyValueFactory<>("requestID"));
		dataColumn.setCellValueFactory(new PropertyValueFactory<>("requestData"));
		valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
		fromColumn.setCellValueFactory(new PropertyValueFactory<>("workerID"));
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
		timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
		park.setText(worker.getPark());
		requestsTable.setRowFactory(tv -> {
			TableRow<Request> row = new TableRow<>();
		    row.itemProperty().addListener((obs, previousRequest, currentRequest) -> {
		    	if(currentRequest != null) {
		    		switch(currentRequest.getStatus()) {
		    		case Cancelled: 
				    	row.setStyle("-fx-background-color : #e8613cd9 ");
                        break;
		    		case Approved: 
				    	row.setStyle("-fx-background-color : #0bc91ec4 ");
                        break;
		    		case Pending: 
				    	row.setStyle("-fx-background-color : #e4ea3bcf ");
                        break;  
                    default:
                    	row.setStyle("-fx-background-color : #ffffff ");
		    		}
		    	}
		          		    });
		    return row ;
		});

		updateRequestsTable();// update the orders table.
		//set tool tip
		approveButton.setTooltip(new Tooltip("Approves your selected Request."));
		disapproveButton.setTooltip(new Tooltip("Disapproves your selected Request."));
	}

}
