package boundary;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.WorkerControllerClient;
import entity.Order;
import entity.ParkWorker;
import entity.Report;
import enums.ReportType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

/**
 * this class is in charge of of reports for park manager in the system
 * 
 * @version 1.0
 * @author group 20
 *
 */
public class ParkManagerReportsPageController implements Initializable {
	/**
	 *
	 * @param choiseBoxParks   htis is a choisebox componet for choise box parks in
	 *                         the system
	 * @param choiceBoxMonths  this is a choiseBox componet for the choise box
	 *                         months in the system
	 * @param submitButton     this is a button componet for submit in the system
	 * @param choiseBoxReports this is a choiceBox componet for choise box reports
	 *                         in the system
	 * @param textArea         this is a textarea componet in the system
	 * @param park             this is a text componet for park in the system
	 * @param worker           this is a park worker entity in the system
	 *
	 */
	@FXML
	private ChoiceBox<String> choiceBoxParks;
	@FXML
	private ChoiceBox<String> choiceBoxMonths;
	@FXML
	private Button submitButton;
	@FXML
	private ChoiceBox<String> choiceBoxReports;
	@FXML
	private TextArea textArea;
    @FXML
    private Text park;
	
	private ParkWorker worker;
	/**
	 * this method displays the selected report page in the system
	 * @param event this is the event that triggers the display of the report page.
	 */
	@FXML
	void displaySelectedReportPage(MouseEvent event) {
		if(choiceBoxMonths.getSelectionModel().getSelectedItem() == null  || choiceBoxReports.getSelectionModel().getSelectedItem() == null ) {//check if both fields != null
			Alert alert = new Alert(AlertType.ERROR,"Please select both Report type and Month.");
			alert.showAndWait();
			return;
		}
		String month = choiceBoxMonths.getSelectionModel().getSelectedItem();
		LocalDate tmp = LocalDate.now();
		String eventStartString ="";
		String eventEndString ="";
		switch (month) {
		case "January":
			eventStartString= tmp.getYear() + "-" + "01-01"; // 2020-01-01 present January.
			eventEndString = tmp.getYear() + "-" + "01-31"; // 2020-01-31 present January.
			break;
		case "February":
			eventStartString= tmp.getYear() + "-" + "02-01"; 
			eventEndString = tmp.getYear() + "-" + "02-28";
			break;
		case "March":
			eventStartString= tmp.getYear() + "-" + "03-01"; 
			eventEndString = tmp.getYear() + "-" + "03-31";
			break;
		case "April":
			eventStartString= tmp.getYear() + "-" + "04-01"; 
			eventEndString = tmp.getYear() + "-" + "04-30";
			break;
		case "May":
			eventStartString= tmp.getYear() + "-" + "05-01"; 
			eventEndString = tmp.getYear() + "-" + "05-31";
			break;
		case "June":
			eventStartString= tmp.getYear() + "-" + "06-01"; 
			eventEndString = tmp.getYear() + "-" + "06-30";
			break;
		case "July":
			eventStartString= tmp.getYear() + "-" + "07-01";
			eventEndString = tmp.getYear() + "-" + "07-31";
			break;
		case "August":
			eventStartString= tmp.getYear() + "-" + "08-01"; 
			eventEndString = tmp.getYear() + "-" + "08-31";
			break;
		case "September":
			eventStartString= tmp.getYear() + "-" + "09-01"; 
			eventEndString = tmp.getYear() + "-" + "09-30";
			break;
		case "October":
			eventStartString= tmp.getYear() + "-" + "10-01"; 
			eventEndString = tmp.getYear() + "-" + "10-31";
			break;
		case "November":
			eventStartString= tmp.getYear() + "-" + "11-01"; 
			eventEndString = tmp.getYear() + "-" + "11-30";
			break;
		case "December":
			eventStartString= tmp.getYear() + "-" + "12-01"; 
			eventEndString = tmp.getYear() + "-" + "12-31";
			break;
		default:
			System.out.println("Error in getting the month.");
			break;
		}
		switch (choiceBoxReports.getSelectionModel().getSelectedItem()) {
		case "Total Visitors":
			LocalDate localDate = LocalDate.now();// set current date.
			LocalTime localTime = LocalTime.now();//set current time.
			Time time = Time.valueOf(localTime);//cast local time to sql.time
			Date date = Date.valueOf(localDate);//cast local date to sql.date
			Report report = new Report( worker.getWorkerID(), worker.getPark(), "", ReportType.TotalVisitors, date, time ,eventStartString,eventEndString);
			ArrayList<Order> orders = WorkerControllerClient.getReportOrders(report);
			if(orders.isEmpty()) {//if there is no orders.
				textArea.setText("There is no visitors at "+worker.getPark()+ " in "+month);
				return;
			}else { //now we display the data.
				String data = "";
				int totalSingle = 0;
				int totalGroup = 0;
				int totalSubscribers = 0;
				for (Order o : orders) {
					switch (o.getOrderType()) {
					case Single:
					case CasualSingle:
						totalSingle += o.getNumOfTravelers();
						break;
					case CasualFamily:
					case PrivateGroup:
					case OrganizedGroup:
					case PrePaymentGroup:
					case PrePaymentOrganizedGroup:
					case CasualGroup:
					case CasualOrganizedGroup:
						totalGroup += o.getNumOfTravelers();
						break;
					case SingleSubscription:
					case CasualSingleSubscription:
					case FamilySubscription:
					case CasualFamilyNoSubscription:
						totalSubscribers += o.getNumOfTravelers();
						break;
					default:
						System.out.println("error in report query, need to check order types.");
						break;
					}
				}
				int total = totalGroup + totalSingle + totalSubscribers;
				data += "Total visitors at " + orders.get(0).getPark() + " in " +choiceBoxMonths.getSelectionModel().getSelectedItem() + " = " + total + "\n";
				data += "Single visitors = "+totalSingle +"\n";
				data += "group visitors = "+totalGroup +"\n";
				data += "subscribers visitors = "+totalSubscribers +"\n";
				textArea.setText(data);
				textArea.setVisible(true);
				break;
			}
		case "Occupancy Use":
			localDate = LocalDate.now();// set current date.
			localTime = LocalTime.now();//set current time.
			time = Time.valueOf(localTime);//cast local time to sql.time
			date = Date.valueOf(localDate);//cast local date to sql.date
			month = choiceBoxMonths.getSelectionModel().getSelectedItem();
			report = new Report(worker.getWorkerID(), worker.getPark(), "", ReportType.OccupancyUse, date, time,
					eventStartString, eventEndString);
			orders = WorkerControllerClient.getReportOrders(report);//get orders.
			if (orders.isEmpty()) {// if there is no orders.
				textArea.setText(worker.getPark() + " was not in full occupancy at " + month + "\n" );//every day and time park was full.
				return;
			} else { // now we display the data.
				String data = "";
				for (Order o : orders) {
					data +="Date " +  o.getOrderDate()+", Time = " + o.getOrderTime() +".\n";
				}
				textArea.setText("OccupancyUse in " + worker.getPark() + " in " + month + " is = \n" + data);
				textArea.setVisible(true);
				break;
			}	
		case "Income Statement":
			localDate = LocalDate.now();// set current date.
			localTime = LocalTime.now();// set current time.
			time = Time.valueOf(localTime);// cast local time to sql.time
			date = Date.valueOf(localDate);// cast local date to sql.date
			month = choiceBoxMonths.getSelectionModel().getSelectedItem();
			report = new Report(worker.getWorkerID(), worker.getPark(), "", ReportType.IncomeStatement, date, time,
					eventStartString, eventEndString);
			orders = WorkerControllerClient.getReportOrders(report);
			if (orders.isEmpty()) {// if there is no orders.
				textArea.setText("Total Income in " + worker.getPark() + " in " + month + " is = 0.");
				return;
			} else { // now we display the data.
				int income = 0;
				for (Order o : orders) {
					income += o.getTotalSum();
				}
				textArea.setText("Total Income in " + worker.getPark() + " in " + month + " is = " + income + ".");
				textArea.setVisible(true);
				break;
			}
		}
	}
	/**
	 * this method takes you back to the previous screen in the system
	 * @param event this is the event that triggers going back to the previous screen feature
	 */
	@FXML
	void goBack(MouseEvent event) {
		submitButton.getScene().getWindow().hide();
	}
	/**
	 * this method initallizes the class 
	 * @param arg0 arg0 
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		worker = WorkerLoginPageController.getWorker();//set worker
		choiceBoxReports.getItems().add("Total Visitors");//set report type.
		choiceBoxReports.getItems().add("Occupancy Use");
		choiceBoxReports.getItems().add("Income Statement");
		choiceBoxMonths.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August",
				"September", "October", "November", "December");//set months.
		// set tool tip
		submitButton.setTooltip(new Tooltip("Returns to Home page."));
		submitButton.setTooltip(new Tooltip("Display Report with your selected Type, Month."));
		choiceBoxReports.setTooltip(new Tooltip("The Report type options."));
		choiceBoxMonths.setTooltip(new Tooltip("The Months options."));
        park.setText(worker.getPark());
	}

}
