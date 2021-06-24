package boundary;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * this class is in charge of park manager event features such as discount and
 * more that we have in our system.
 * 
 * @version 1.0
 * @author group 20
 *
 */
public class ParkManagerEventPageController implements Initializable {
	/**
	 * * @param startDate this is a date picker componet for start date in the
	 * system
	 * 
	 * @param endDate        this is a date picker componet for the end date in the
	 *                       system
	 * @param discountNumber this is a choiseBox componet for the discount number in
	 *                       the system
	 * @param submitButton   this is a button componet for submit in the system
	 * @param park           this is a text componet for the park in the system
	 * @param worker         this is a Park Worker entity in the system.
	 */
	@FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private ChoiceBox<Integer> discountNumber;

    @FXML
    private Button submitButton;

    @FXML
    private Text park;


	private ParkWorker worker;
	/**
	 * this method is in charge of processing a discountEvent in our system
	 * @param event this is the event that triggers the processing to occour
	 */
	@FXML
	void ProcessDiscountEvent(MouseEvent event) {
		if(discountNumber.getSelectionModel().getSelectedItem() == null || startDate.getValue() == null || endDate.getValue() == null) {//check fields != null.
			Alert alert = new Alert(AlertType.ERROR,"Please fill all the fields.");
			alert.showAndWait();
			return;
		}

		int number = discountNumber.getSelectionModel().getSelectedItem();
		if(number<0 || number >25) {//check number between 1-25.
			Alert alert = new Alert(AlertType.ERROR,"Please enter numbers between 1-25.");
			alert.showAndWait();
			return;
		}
		LocalDate start = startDate.getValue();//user start time input
		LocalDate end = endDate.getValue();//user end time input
		LocalDate now = LocalDate.now();//current time.
		LocalTime time = LocalTime.now();
		Date startDate = Date.valueOf(start);//cast start
		Date endDate = Date.valueOf(end);//cast end
		if( now.isAfter( start) || now.isAfter(end) || start.isAfter(end)) {//if current time is after something thats means the start or end date expired already. or if start is after end.
			Alert alert = new Alert(AlertType.ERROR,"Please choose valid dates.");
			alert.showAndWait();
			return;
		}
		//now create new event and send it for approval at Department Manager.
		entity.Event tmp = new entity.Event( number, startDate, endDate, worker.getPark());
		boolean isAvailable = WorkerControllerClient.checkEvent(tmp);
		if(isAvailable) {
			String data = "Hello,\nPlease approve my request for creating new event. From "+start +" Till "+end;
			Request request = new Request(worker.getWorkerID(), worker.getPark(), data, number, RequestType.Event, RequestStatus.Pending,
					Date.valueOf(now), Time.valueOf(time), startDate, endDate);
			boolean isCreated = WorkerControllerClient.createNewRequest(request); ///create new request and return boolean and see if create succeed.
			if(isCreated) { //check if request create successfully.
				Alert alert = new Alert(AlertType.CONFIRMATION,"Event Request sent successfully for approval.");
				alert.showAndWait();
				submitButton.getScene().getWindow().hide();
				return;
			} else {
				Alert alert = new Alert(AlertType.ERROR,"Failed to create new Event.");
				alert.showAndWait();
				return;
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR,"Failed to create new Event.\nPlease choose different dates.");
			alert.showAndWait();
			return;
		}
	}
	/**
	 * this method takes u back to the previous displayed screen
	 * @param event this is the event that triggers the action to go back
	 */
	@FXML
	void goBack(MouseEvent event) {
		submitButton.getScene().getWindow().hide();
	}
	/**
	 * this is the initallization of the class
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		worker = WorkerLoginPageController.getWorker();
		//set tool tip
		discountNumber.setTooltip(new Tooltip("The Discount number."));
		startDate.setTooltip(new Tooltip("The Event start Date."));
		endDate.setTooltip(new Tooltip("The Event end Date."));
		submitButton.setTooltip(new Tooltip("Returns to Home page."));
		submitButton.setTooltip(new Tooltip("Submit new Event Request."));
		for(int i = 1 ; i <= 25 ; i++)
		   discountNumber.getItems().add(i);
		park.setText(worker.getPark());
	}

}
