package boundary;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.WorkerControllerClient;
import entity.ParkWorker;
import entity.Request;
import enums.RequestStatus;
import enums.RequestType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;
/**
 * this method is in charge of features of parameters in the system
 * @version 1.0
 * @author group 20

 *
 */
public class ParkManagerParameterOptionsController implements Initializable {
/**
 * @param parameterOptions this is a toggleGroup componet for parameter options in the system
 * @param valueText this is a text field componet for value text in the system
 * @param submitButton this is a button componet for submit in the system
 * @param park this is a text componet for park in the system
 * @param worker this is a worker entity in the system
 * @param value this is an integer value
 * @param r this is a radioButton componet in the system.
 */

    @FXML
    private ToggleGroup parameterOptions;

    @FXML
    private TextField valueText;

    @FXML
    private Button submitButton;

    @FXML
    private Text park;
    
    private ParkWorker worker;
    private int value;
    private RadioButton r ;

    /**
	 * this method checks if the user entered parameter value
	 * @return true if he chose parameter, false otherwise.
	 */
	private boolean checkParameterValue() {
		
		if((RadioButton) parameterOptions.getSelectedToggle() == null) {
			Alert alert = new Alert(AlertType.ERROR, "Please choose parameter.");
			alert.showAndWait();
			return false;
		}
		
		if(valueText.getText().equals("")) {
			Alert alert = new Alert(AlertType.ERROR,"Please enter value(Positive number) first.");
			alert.setTitle("Input Error");
			alert.showAndWait();
			return false;
		}
		
		if(!valueText.getText().matches("[0-9]+")) {//check if it is a number
			Alert alert = new Alert(AlertType.ERROR,"Please enter positive number only.");
			alert.setTitle("Input Error");
			alert.showAndWait();
			return false;
		}
		
		RadioButton rb = (RadioButton) parameterOptions.getSelectedToggle();// cast selected radio button.
		r = rb;
		
		switch (rb.getText()) {
		
		case "Capacity Number":

			if (Integer.parseInt(valueText.getText()) < 0) { // check if number is positive
				Alert alert = new Alert(AlertType.ERROR, "Please enter positive number and try again.");
				valueText.setText("");
				alert.showAndWait();
				return false;
			}
			// check current time and date and see if we can change park capacity.
			ArrayList<Object> list = new ArrayList<>(); // parameter type, parameter value, park name, date,time.
			RequestType type = RequestType.ParkCapacity;// set parameter
			int parameterValue = Integer.parseInt(valueText.getText());// get the parameter value.
			LocalDate localDate = LocalDate.now();// set current date.
			LocalTime localTime = LocalTime.now();// set current time.
			Time time = Time.valueOf(localTime);// cast local time to sql.time
			Date date = Date.valueOf(localDate);// cast local date to sql.date
			list.add(type);
			list.add(parameterValue);
			list.add(worker.getPark());
			list.add(time);
			list.add(date);
			boolean canUpdateParkCapacity = WorkerControllerClient.checkParkParameterUpdate(list);// check the park
																									// parameter.
			if (canUpdateParkCapacity) {// if true, then check finished with success.
				return true;
			} else 
			{// check failed.
				Alert alert = new Alert(AlertType.ERROR,
						"Sorry the value you entered can't be update.\nPlease choose another value and try again.");
				alert.showAndWait();
				return false;
			}

		case "Gap Number":
			submitButton.setVisible(false);
			if (Integer.parseInt(valueText.getText()) < 0) {// check if the number is positive.
				Alert alert = new Alert(AlertType.ERROR, "Please enter positive number and try again.");
				valueText.setText("");
				alert.showAndWait();
				return false;
			}
			list = new ArrayList<>();// create arraylist of object to send to server. list = ( type, value,
										// park-name, time,date)
			type = RequestType.GapChange;// set parameter
			parameterValue = Integer.parseInt(valueText.getText());// get the parameter value.
			localDate = LocalDate.now();// set current date.
			localTime = LocalTime.now();// set current time.
			time = Time.valueOf(localTime);// cast local time to sql.time
			date = Date.valueOf(localDate);// cast local date to sql.date
			list.add(type);
			list.add(parameterValue);
			list.add(worker.getPark());
			list.add(time);
			list.add(date);
			canUpdateParkCapacity = WorkerControllerClient.checkParkParameterUpdate(list);// check the park parameter.
			if (canUpdateParkCapacity) {// if true, then check finished with success.
				return true;
			} else {// check failed.
				Alert alert = new Alert(AlertType.ERROR,
						"Sorry the value you entered can't be update.\nPlease choose another value and try again.");
				alert.showAndWait();
				return false;
			}
		case "Time Of Stay":
			parameterValue = Integer.parseInt(valueText.getText());
			value = parameterValue;
			if (parameterValue <= 0 || parameterValue > 9) {// if it is bigger then park working hours.
				Alert alert = new Alert(AlertType.ERROR, "Please enter a number between 0-9 only.");
				alert.showAndWait();
				return false;
			}

		default:
			System.out.println("error in park parameters options.");
			Alert alert = new Alert(AlertType.ERROR, "Unrecognized error happened");
			alert.showAndWait();
			return false;
		}
	}
	/**
	 * this method updates the parameter in the sysetm
	 * @param event this is the event that triggers the update in the system
	 */
	@FXML
	void setParameterUpdate(MouseEvent event) {
		
		if(!checkParameterValue()) return;
		
		switch (r.getText()) {
		case "Capacity Number":
			LocalDate localDate = LocalDate.now();// set current date.
			LocalTime localTime = LocalTime.now();// set current time.
			Time time = Time.valueOf(localTime);// cast local time to sql.time
			Date date = Date.valueOf(localDate);// cast local date to sql.date
			String data = "Please approve my request to change Capacity number to " + value + ".";
			Request request = new Request(worker.getWorkerID(), worker.getPark(), data, value, RequestType.ParkCapacity,
					RequestStatus.Pending, date, time);
			boolean isCreated = WorkerControllerClient.createNewRequest(request);
			if (isCreated) {// if request created successfully then display confirmation msg.
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"Request sent successfully to Department Manager for approval.");
				alert.showAndWait();
				submitButton.getScene().getWindow().hide();
			} else {// create failed, display error msg.
				Alert alert = new Alert(AlertType.ERROR, "Failed to create new request.");
				alert.showAndWait();
				return;
			}
			break;
		case "Gap Number":
			localDate = LocalDate.now();// set current date.
			localTime = LocalTime.now();// set current time.
			time = Time.valueOf(localTime);// cast local time to sql.time
			date = Date.valueOf(localDate);// cast local date to sql.date
			data = "Please approve my request to change gap number to " + value + ".";
			request = new Request(worker.getWorkerID(), worker.getPark(), data, value, RequestType.GapChange,
					RequestStatus.Pending, date, time);
			isCreated = WorkerControllerClient.createNewRequest(request);// create new request in DB, and now Department
																			// Manager can see the request.
			if (isCreated) {// if request created successfully then display confirmation msg.
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"Request sent successfully to Department Manager for approval.");
				alert.showAndWait();
				submitButton.getScene().getWindow().hide();
			} else {// create failed, display error msg.
				Alert alert = new Alert(AlertType.ERROR, "Failed to create new request.");
				alert.showAndWait();
				return;
			}
			break;
		case "Time Of Stay":
			localDate = LocalDate.now();// set current date.
			localTime = LocalTime.now();// set current time.
			time = Time.valueOf(localTime);// cast local time to sql.time
			date = Date.valueOf(localDate);// cast local date to sql.date
			data = "Hello,\nPlease approve my request for changing Park Time Of Stay value to " + value + ".";
			request = new Request(worker.getWorkerID(), worker.getPark(), data, value, RequestType.TimeOfStay,
					RequestStatus.Pending, date, time);
			isCreated = WorkerControllerClient.createNewRequest(request);
			if (isCreated) {// if request created successfully then display confirmation msg.
				Alert alert = new Alert(AlertType.CONFIRMATION,
						"Request sent successfully to Department Manager for approval.");
				alert.showAndWait();
				submitButton.getScene().getWindow().hide();
			} else {// create failed, display error msg.
				Alert alert = new Alert(AlertType.ERROR, "Failed to create new request.");
				alert.showAndWait();
				return;
			}
			break;

		default:
			System.out.println("Error in setting new parameter update.");
			break;
		}
	}
	/**
	 * this method takes u back to the previous screen in the system
	 * @param event this is the event that triggers the previous page to be displayed.
	 * @throws IOException
	 */
	@FXML
	void goBack(MouseEvent event) throws IOException {
		submitButton.getScene().getWindow().hide();
	}
	/**
	 * this method intiallizes the class.
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		submitButton.setTooltip(new Tooltip("Display the selected Parameter page."));
		submitButton.setTooltip(new Tooltip("Returns to Home page."));
		worker = WorkerLoginPageController.getWorker();
		park.setText(worker.getPark());
	}

}
