package boundary;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import client.TravelerControllerClient;
import client.WorkerControllerClient;
import entity.Order;
import entity.ParkWorker;
import entity.SubscriptionTraveler;
import entity.TicketPrice;
import entity.Traveler;
import enums.OrderStatus;
import enums.OrderType;
import enums.TravelerType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
/**
 * this class is in charge of features we have in our system in regards to park manager
 * @author group 20
 * @version 1.0
 *
 */
public class ParkManagerHomePageController implements Initializable{
/**
 * * @param workerName this is a text componet for worker name in the system
 * @param park this is a text componet for park in the system
 * @param checkNewOrder this is a button componet for checking new order in the system
 * @param travelerIDInput this is a text field for traveler ID 
 * @param totalVisitors this is a text field for total visitors in the system
 * @param visitorTypeOptions this is a choiseBox componet for the visitorTypeOptions in the system
 * @param lastRefresh this is a text componet for the last refresh in the system
 * @param parkCurrentCapacity this is a text componet for the park current capacity parameter in the system
 * @param capacitySubmitButton this is a button componet for the capacity submit feature in the system
 * @param logoutButton this is a button componet for the log out feature in the system
 * @param refreshError this is a text componet for refresh error feature in the system
 * @param TicketPrice this is the ticket price in the system
 * @param ParkOrignalTicketPrice this is the orignal ticket price in the system
 * @param order this is an order in the system
 * @param id this is a string representation of ID in the system
 * @param traveler this is a traveler entity in the system
 * @param orderSubmissionPage this is a link in the form of a string to orderSubmissionPage
 * @param ParkManagerEventPagePath this is a link in the form of a string to ParkManagerEventPage
 * @param ParkManagerReportsPagePath this is a link in the form of a string to ParkManagerReportsPage
 * @param ParkManagerRequestsPagePath this is a link in the form of a string to ParkManagerRequestsPage
 * @param ParkManagerParameterOptionsPath this is a link in the form of a string to ParkManagerParameterOptions
 * @param MainPagePath this is a link in the form of a string to MainPage
 * @param worker this is a worker entity in the system
 */

    @FXML
    private Text workerName;
    
    @FXML
    private Text park;

    @FXML
    private Button checkNewOrder;

    @FXML
    private TextField travelerIDInput;

    @FXML
    private TextField totalVisitors;

    @FXML
    private ChoiceBox<String> visitorTypeOptions;

    @FXML
    private Text lastRefresh;

    @FXML
    private Text parkCurrentCapacity;

    @FXML
    private Button capacitySubmitButton;

    @FXML
    private Button logoutButton;
    
    @FXML
    private Text refreshError;
    

	@SuppressWarnings("unused")
	private TicketPrice ticketPrice=null;
	private final int ParkOrginalTicketPrice = 100;
	private static Order order = null;
	private static String id;
	private Traveler traveler;
	final private String orderSubmissionPage = "ParkManagerHomePageOrderSubmition.fxml";
    private final String ParkManagerEventPagePath = "ParkManagerEventPage.fxml";
    private final String ParkManagerReportsPagePath = "ParkManagerReportsPage.fxml";
    private final String ParkManagerRequestsPagePath = "ParkManagerRequestsPage.fxml";
    private final String ParkManagerParameterOptionsPath ="ParkManagerParameterOptions.fxml";
	final private String MainPagePath = "MainPage.fxml";

	
	//private LocalTime localTime;
    private static ParkWorker worker;
    /**
     * this is a getter for park worker info in the system
     * @return the parkWorker entity
     */
    public static ParkWorker getParkWorkerInfo() {
		return worker;
	}
    /**
     * this is a getter for Casual Traveler Order
     * @return CasualTravelerOrder
     */
    public static Order getCasualTravelerOrder() {
		   return order;
	   }
    /**
     * this is a getter for travelerID
     * @return travelerID
     */
    public static String getTravlerID() {
    	return id;
    }
    /**
     * this method is in charge of displaying the event page in the system
     * @param event this is the event that triggered the display of the event page
     * @throws IOException
     */
    @FXML
    void displayEventPage(MouseEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource(ParkManagerEventPagePath));
    	Stage stage = new Stage();
		stage.setTitle("Event Page");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
    }
    
    /**
     * this method is in charge of displaying the UpdateParameters Page in the system
     * @param event this is the event that triggered the display of the UpdateParameters Page in the system
     * @throws IOException
     */
    @FXML
    void displayUpdateParametersPage(MouseEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource(ParkManagerParameterOptionsPath));
		Stage stage = new Stage();
		stage.setTitle("Parameters Page");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
    }
    /**
     * this method is in charge of displaying ApprovalRequest Page in the system
     * @param event this is the event that triggered the display of the approval request page
     * @throws IOException
     */
    @FXML
    void displayApprovalRequestsPage(MouseEvent event)throws IOException  {    	
    	Parent root = FXMLLoader.load(getClass().getResource(ParkManagerRequestsPagePath));
    	Stage stage = new Stage();
		stage.setTitle("Requests Page");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();	
    }
    /**
     * this method is in charge of displaying the reports page in the system
     * @param event this is the event that triggered the the display of the reports page in the system.
     * @throws IOException
     */
    @FXML
    void displayReportsPage(MouseEvent event)throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource(ParkManagerReportsPagePath));
    	Stage stage = new Stage();
		stage.setTitle("Reports Page");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.show();
    }
    /**
     * this method displays the main page in the system
     * @param event this is the event that triggered the display of the main page in the system
     * @throws IOException
     */
    @FXML
    void goToMainPage(MouseEvent event) throws IOException {
    	boolean isRemoved = WorkerControllerClient.removeOnlineWorker(worker.getWorkerID()+"");
		if(isRemoved) {
			System.out.println(worker.getWorkerID()+ " Successfully removed from online table.");
		} else {
			System.out.println("Failed to remove from online table.");
		}
    	Parent root = FXMLLoader.load(getClass().getResource(MainPagePath));
		Stage stage = (Stage) logoutButton.getScene().getWindow();
		stage.setTitle("GoNature System");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
    }
    /**
     * this method refreshes the current visitors amount in the system
     * @param event this is the event that causes the refresh of the current visitors amount in the system
     */
    @FXML
    void refreshCurrentlyVisitorsAmount(MouseEvent event) {
    	CurrentlyVisitorsAmount();
    }
    /**
     * this method updates the current visitors amount in the system
     */
    private void CurrentlyVisitorsAmount() {
    	//lastRefresh.setText(localTime.getHour() + ":" + localTime.getMinute());
		ArrayList<Integer> capacity = WorkerControllerClient.getParkCapacity(worker.getPark());//get park max and current numbers.
		LocalTime currentTime = LocalTime.now();
		LocalTime hourAfter17 = LocalTime.of(17, 0);
		LocalTime hourBefore08 = LocalTime.of(8, 0);
		
		lastRefresh.setText(currentTime.toString().substring(0, 8));
		int max = capacity.get(0);//max
		int current = capacity.get(1);//current
		if(currentTime.isBefore(hourBefore08) || currentTime.isAfter(hourAfter17)){ // check if 08:00 > current time , or 17:00 < current time (working park hours 08:00-17:00)
			parkCurrentCapacity.setText("0 / " + max);
			refreshError.setVisible(true);
		} else {//means current time is , 08:00 < currentTime < 17:00
			if(!capacity.isEmpty()) {
				refreshError.setVisible(false);
				parkCurrentCapacity.setText(current + " / " + max);
			}
		}
    }
    /**
     * this method is in charge of initallizing the class
     * @param arg0 arg0
     * @param arg1 arg1
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		worker = WorkerLoginPageController.getWorker();
		String name = worker.getWorkerName();
		workerName.setText(name.substring(0, 1).toUpperCase() 
				+ name.substring(1).toLowerCase());
		park.setText(worker.getPark());
		//workerName.setFont(Font.font("System", FontWeight.BOLD, 16));
		CurrentlyVisitorsAmount();
		visitorTypeOptions.getItems().addAll("Single", "Family", "Group");

		//set tool tip
	//	createNewOrderButton.setTooltip(new Tooltip("Creates a new Order"));
	//	parkCapacityButton.setTooltip(new Tooltip("Shows the number of Visitors at the park"));
	//	logoutButton.setTooltip(new Tooltip("Takes you back to the main page"));
	}
	/**
	 * this method is in charge of checking if a new order is valid.
	 * @param event this is the event that causes the checking of the order.
	 * @throws IOException
	 */
    @SuppressWarnings("unused")
	@FXML
    void checkNewOrder(MouseEvent event) throws IOException {
	//	visitorTypeOptions.setSelectionModel(null);

    	if(travelerIDInput.getText().equals("")) {
    		Alert alert = new Alert(AlertType.ERROR,"Please enter valid ID number.");
    		alert.showAndWait();
    		return;
    	}
		traveler = WorkerControllerClient.getTravelerInfoByID(travelerIDInput.getText());

		id = travelerIDInput.getText();
		
		if(!markEmpyFields()) return;
//		
		String parkName = worker.getPark();
		ArrayList<Integer> list = WorkerControllerClient.getParkCapacity(parkName);//get park details.
		int max = 0;
		@SuppressWarnings("unused")
		int current = 0;
		int parkID = 0;
		
		switch (worker.getPark()) {//cast park string to park int for queries
		case "Park A":
			parkID = 1;
			break;
		case "Park B":
			parkID = 2;
			break;
		case "Park C":
			parkID = 3;
			break;
		default:
			System.out.println("error in switch with park names.");
			break;
		}
		if (!list.isEmpty()) {
			max = list.get(0);// max
			current = list.get(1);// current
		}
		// show ticket price for the traveler.
		ticketPrice = TicketPrice.getInstance(ParkOrginalTicketPrice);//ticket price instance.
		LocalTime localTime = LocalTime.now();
		@SuppressWarnings("deprecation")
		Time time = new Time(localTime.getHour(), 0, 0);
		int timeOfStay = WorkerControllerClient.getTimeOfStay(parkID);
		//get traveler type by traveler id
		int travelerIDNumber = Integer.parseInt(id);
		// switch on options and update total number of visitors and type.
		switch (visitorTypeOptions.getSelectionModel().getSelectedItem()) {// get order option.
		case "Single":
			if(traveler == null || traveler.getTravelerType() == TravelerType.Single) {//id isn't exists means casual single type., or id found with single type.
				order = new Order(OrderType.CasualSingle, worker.getPark(), Date.valueOf(LocalDate.now()), time, 1, OrderStatus.CurrentlyAtThePark, timeOfStay);
				order.setTravlerID(travelerIDNumber);
			} else if (traveler.getTravelerType() == TravelerType.SingleSubscription || traveler.getTravelerType() == TravelerType.FamilySubscription ){
				order = new Order(OrderType.CasualSingleSubscription, worker.getPark(), Date.valueOf(LocalDate.now()), time, 1, OrderStatus.CurrentlyAtThePark, timeOfStay);
				order.setTravlerID(travelerIDNumber);
			} 
			break;
		case "Family":
			// id isn't exists means casual casual family type
				order = new Order(OrderType.CasualFamily, worker.getPark(), Date.valueOf(LocalDate.now()), time,
						Integer.parseInt(totalVisitors.getText()), OrderStatus.CurrentlyAtThePark, timeOfStay);
				order.setTravlerID(travelerIDNumber);
			
			break;
		case "Group":
				order = new Order(OrderType.CasualGroup, worker.getPark(), Date.valueOf(LocalDate.now()), time,
						Integer.parseInt(totalVisitors.getText()), OrderStatus.CurrentlyAtThePark, timeOfStay); 
				order.setTravlerID(travelerIDNumber);
			break;
		default:
			errorAlert("Error defining traveler type", "");
			return;
			
		}
		
		if(WorkerControllerClient.checkNewOrder(order)) {
			
			double ticketPrice = TravelerControllerClient.getOrderTotalCost(order);// get ticket price.
			order.setTotalSum(ticketPrice);
			Parent root = FXMLLoader.load(getClass().getResource(orderSubmissionPage));
			Stage stage = new Stage();
			stage.setTitle("Casual order Submition");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.showAndWait();
			
			if(ParkManagerHomePageOrderSubmitionController.getOrderStatus()) {
				travelerIDInput.setText("");
				totalVisitors.setText("1");
			}
		}
		else {
			errorAlert("Park Is Full","Sorry The Park Is full at the moment." );
			return;
		}
    }
    /**
     * this method sends an error alert in the system
     * @param heading this is the header of the error 
     * @param content this is the body of the error
     */
    private void errorAlert(String heading, String content) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(heading);
		alert.setContentText(content);
		alert.showAndWait();
    }
    /**
     * this method highlights empty fields in the system
     * @return true if found empty fields, false otherwise.
     */
	private boolean markEmpyFields() {
		
		
		LocalTime currentTime = LocalTime.now();
		LocalTime hourAfter17 = LocalTime.of(17, 0);
		LocalTime hourBefore08 = LocalTime.of(8, 0);
		
		if(currentTime.isBefore(hourBefore08) || currentTime.isAfter(hourAfter17)) {
			errorAlert("Failed!", "Park is not working now");
			return false;
		}
		
		else if(!travelerIDInput.getText().matches("[0-9]+")) {//check if it is a number
			errorAlert("Field ID", "Enter numbers only");
			return false;
			}
		
		else if(!totalVisitors.getText().matches("[0-9]+")) {//check if it is a number
			errorAlert("Field Visitors", "Enter number between 1 to 15");
			return false;
		}
		
		else if( visitorTypeOptions.getSelectionModel().getSelectedItem() == null) {
			errorAlert("Required field!", "Please select visitor type");
			return false;
		}
		
		int visitors = Integer.parseInt(totalVisitors.getText());
		
		switch(visitorTypeOptions.getSelectionModel().getSelectedItem()){
			case "Single":
					if (visitors != 1) {
						errorAlert("Error in visitors number field", 
								"Total visitors number must be 1.\nWe have changed it to 1 for you.\nPlease press Check Button agian.");
						totalVisitors.setText("1");
						return false;
					}
				break;
			case "Family":
				SubscriptionTraveler travelerWithSubscription = TravelerControllerClient.getSubscriptionTravelerByID(Integer.parseInt(id));
				if (travelerWithSubscription != null) {
						if(visitors < 1 || visitors > travelerWithSubscription.getTotalFamilyMembers()) {
							errorAlert("Error in visitors number field", 
									"number should be between 1 to " + travelerWithSubscription.getTotalFamilyMembers());
							return false;
						}
				}
				else {
					errorAlert("Error in visitor type field", 
							"The ID was not found as subscription traveler");
					return false;
				}
				break;
				
			case "Group":
                         if(visitors < 1 || visitors > 15)     {
							errorAlert("Error in visitors number field", 
									"number should be between 1 to 15");
							return false;
						}                          
				break;
			default:
				return false;
		}

		return true;
	}
   

}
