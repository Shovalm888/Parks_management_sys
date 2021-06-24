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
 * Class that is the Controller of the Park Worker FXMl.
 * @version 1.0
 * @author Group 20
 * 
 * 
 */
public class ParkWorkerHomePageController implements Initializable	 {


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

	private TicketPrice ticketPrice=null;
	private final int ParkOrginalTicketPrice = 100;
	final private String orderSubmissionPage = "ParkWorkerNewOrderCasualSubmission.fxml";
	final private String MainPagePath = "MainPage.fxml";
	private static Order order = null;
	private static String id;
	private Traveler traveler;

	//private LocalTime localTime;
    private static ParkWorker worker;
    
    public static ParkWorker getParkWorkerInfo() {
		return worker;
	}
    public static Order getCasualTravelerOrder() {
		   return order;
	   }
    
    public static String getTravlerID() {
    	return id;
    }

    /**
     * method that logs the worker out of the system and shows the SYSTEM main page
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
     * Method that refreshs the num of real travelers in the park.
     */
    @FXML
    void refreshCurrentlyVisitorsAmount(MouseEvent event) {
    	CurrentlyVisitorsAmount();
    }
    
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
	 * Method that checks if the Casual traveler can enter the park or not.
	 */
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
		//check if there is place in park for single casual traveler.
		String parkName = worker.getPark();
		ArrayList<Integer> list = WorkerControllerClient.getParkCapacity(parkName);//get park details.
		int max = 0;
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
			if(max-current == 0 ) {
				errorAlert("Park Capacity Error", "Sorry Can't proceed with order due to Park Capacity available place.");
				return;
			}
		}
		// show ticket price for the traveler.
		ticketPrice = TicketPrice.getInstance(ParkOrginalTicketPrice);//ticket price instance.
		LocalTime localTime = LocalTime.now();
		Time time = new Time(localTime.getHour(), 0, 0);
		int timeOfStay = WorkerControllerClient.getTimeOfStay(parkID);
		//get traveler type by traveler id
		int travelerIDNumber = Integer.parseInt(id);
		// switch on options and update total number of visitors and type.
		switch (visitorTypeOptions.getSelectionModel().getSelectedItem()) {// get order option.
		case "Single":
			if(traveler == null || traveler.getTravelerType() == TravelerType.Single || traveler.getTravelerType() == TravelerType.Guide) {//id isn't exists means casual single type., or id found with single type.
				order = new Order(OrderType.CasualSingle, worker.getPark(), Date.valueOf(LocalDate.now()), time, 1, OrderStatus.CurrentlyAtThePark, timeOfStay);
				order.setTravlerID(travelerIDNumber);
			} else if (traveler.getTravelerType() == TravelerType.SingleSubscription || traveler.getTravelerType() == TravelerType.FamilySubscription ){
				order = new Order(OrderType.CasualSingleSubscription, worker.getPark(), Date.valueOf(LocalDate.now()), time, 1, OrderStatus.CurrentlyAtThePark, timeOfStay);
				order.setTravlerID(travelerIDNumber);
			}
			break;
		case "Family":
			// id isn't exists means casual casual family type
			if (traveler == null) {
				order = new Order(OrderType.CasualFamilyNoSubscription, worker.getPark(), Date.valueOf(LocalDate.now()), time,
						Integer.parseInt(totalVisitors.getText()), OrderStatus.CurrentlyAtThePark, timeOfStay);
				order.setTravlerID(travelerIDNumber);
			} else if (traveler.getTravelerType() == TravelerType.SingleSubscription
					|| traveler.getTravelerType() == TravelerType.FamilySubscription) {
				order = new Order(OrderType.CasualFamily, worker.getPark(), Date.valueOf(LocalDate.now()), time,
						Integer.parseInt(totalVisitors.getText()), OrderStatus.CurrentlyAtThePark, timeOfStay);
				order.setTravlerID(travelerIDNumber);
			}
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
			
			if(ParkWorkerNewOrderCasualSubmissionController.getOrderStatus()) {
				travelerIDInput.setText("");
				totalVisitors.setText("1");
			}
		} else {
			errorAlert("Park Is Full","Sorry The Park Is full at the moment." );
			return;
		}
    }
    
    private void errorAlert(String heading, String content) {
    	Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(heading);
		alert.setContentText(content);
		alert.showAndWait();
    }
    /**
     * Method that helps the user put correct input to make the order.
     */
	private boolean markEmpyFields() {
		
		LocalTime currentTime = LocalTime.now();
		LocalTime hourAfter17 = LocalTime.of(17, 0);
		LocalTime hourBefore08 = LocalTime.of(8, 0);
		
		if(currentTime.isBefore(hourBefore08) || currentTime.isAfter(hourAfter17)) {
			errorAlert("Failed!", "Park is not working now");
			return false;
		}
		
		if(!travelerIDInput.getText().matches("[0-9]+")) {//check if it is a number
			errorAlert("Field ID", "Enter numbers only");
			return false;
		}
		
		if(!totalVisitors.getText().matches("[0-9]+")) {//check if it is a number
			errorAlert("Field Visitors", "Enter number between 1 to 15");
			return false;
		}
		
		if( visitorTypeOptions.getSelectionModel().getSelectedItem() == null) {
			errorAlert("Required field!", "Please select visitor type");
			return false;
		}
		
		int visitors = Integer.parseInt(totalVisitors.getText());

		switch (visitorTypeOptions.getSelectionModel().getSelectedItem()) {
		case "Single":
			if (visitors != 1) {
				errorAlert("Error in visitors number field",
						"Total visitors number must be 1.\nWe have changed it to 1 for you.\nPlease press Check Button agian.");
				totalVisitors.setText("1");
				return false;
			}
					break;
				case "Family":
					SubscriptionTraveler travelerWithSubscription = TravelerControllerClient
							.getSubscriptionTravelerByID(Integer.parseInt(id));
					if (travelerWithSubscription != null) {
						if (visitors < 1 || visitors > travelerWithSubscription.getTotalFamilyMembers()) {
							errorAlert("Error in visitors number field", "number should be between 1 to "
									+ travelerWithSubscription.getTotalFamilyMembers());
							return false;
						}
					}
//					} else {
//						errorAlert("Error in visitor type field", "The ID was not found as subscription traveler");
//						return false;
//					}
//					break;
				
			case "Group":
				if (visitors < 1 || visitors > 15) {
					errorAlert("Error in visitors number field", "number should be between 1 to 15");
					return false;
				}                  
				break;
			default:
				System.out.println("error in visitor type option, error method.");
				return false;
		}

		 return true;
	}
   

}
