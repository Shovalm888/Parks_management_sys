package boundary;

import java.io.IOException;
import java.util.ArrayList;

import client.TravelerControllerClient;
import client.WorkerControllerClient;
import entity.ParkWorker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
/**
 * This class is the Controller for the Login Page of the workers in the park.
 * @version 1.0
 * @author Group 20
 */
public class WorkerLoginPageController {
/**
 *  @param workerIDText is the TextField where the user types in his workerID.
 * @param signinButton the button that the user needs to click on to log-in to the system.
 * @param workerPassword is the PasswordField where the user types in his workerPassword.
 * @param goBackButton is an ImageView that the user can click on and go back to the system main page.
 * @param parkWorker is an entity of ParkWorker in which we store the relevant data of the worker that wants to log-in.
 */
	@FXML
	private TextField workerIDText;
	@FXML
	private Button signinButton;
	@FXML
    private PasswordField workerPassword;
	@FXML
	private ImageView goBackButton;
	
	final String ParkWorkerHomePagePath ="ParkWorkerHomePage.fxml";
	final String ServiceRepresentativeHomePagePath ="ServiceRepresentativeHomePage.fxml";
	final String ParkManagerHomePagePath ="ParkManagerHomePage.fxml";
	final String DeparmentManagerHomePagePath ="DeparmentManagerHomePage.fxml";
	final String MainPagePath ="MainPage.fxml";
	
	private static ParkWorker parkWorker=null;
	/**
	 * method that returns the specific data of the worker in the system.
	 * @return the entity of the ParkWorker.
	 */
	public static ParkWorker getWorker() {
		return parkWorker;
	}

    /**
     * this method checks if the user input is correct and true,if yes show the user the right page.
     * else show the user where is the problem.
     */
	@FXML
	void checkWorkerDetails(MouseEvent event) throws IOException {
		if(workerIDText.getText().equals("") || workerPassword.getText().equals("")) {
			Alert alert= new Alert(AlertType.ERROR,"Please enter both id and password.");
			alert.showAndWait();
			return;
		}
		String textID = workerIDText.getText();
		if(!textID.matches("[0-9]+")) {
			Alert alert = new Alert(AlertType.ERROR,"Please enter numbers only in field ID.");
			alert.showAndWait();
			return;
		}
		ArrayList<Integer> workerDetails = new ArrayList<>();
		String workerID = workerIDText.getText();//get id
		String workerPass = workerPassword.getText();//get pass
		workerDetails.add(Integer.parseInt(workerID));//add id to array
		workerDetails.add(Integer.parseInt(workerPass));//add pass to array
		ParkWorker worker = WorkerControllerClient.getWorkerInfo(workerDetails);//check worker details.
		if(worker==null){//wrong details entered. or worker doesn't exists.
			Alert alert= new Alert(AlertType.ERROR,"Please enter valid id and password.");
			alert.showAndWait();
			return;
		}
		WorkerLoginPageController.parkWorker = new ParkWorker(worker);//set worker type,name,id so we can use it to other fxml.
		
		if (!(WorkerControllerClient.addOnlineWorker(worker.getWorkerID() + ""))) {
			Alert alert = new Alert(AlertType.ERROR,"Worker " + worker.getWorkerID()+ " is already online.");
			alert.showAndWait();
			return;
		}
		switch (worker.getWorkerType()) {
		case ParkWorker:// park worker
			System.out.println("Park Worker");
			Parent root = FXMLLoader.load(getClass().getResource(ParkWorkerHomePagePath));
			Stage stage = (Stage) signinButton.getScene().getWindow();
			stage.setTitle("Park Worker Page");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
			stage.setOnCloseRequest(e -> {
				WorkerControllerClient.removeOnlineWorker(worker.getWorkerID() + "");
			});
			break;
		case ServiceRepresentative:// representative
			root = FXMLLoader.load(getClass().getResource(ServiceRepresentativeHomePagePath));
			System.out.println("Park representative Worker");
			stage = (Stage) signinButton.getScene().getWindow();
			stage.setTitle("Service Representative Page");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
			stage.setOnCloseRequest(e -> {
				WorkerControllerClient.removeOnlineWorker(worker.getWorkerID() + "");
			});
			break;
		case ParkManager:// park manager
			root = FXMLLoader.load(getClass().getResource(ParkManagerHomePagePath));
			System.out.println("Park Manager Worker Page");
			stage = (Stage) signinButton.getScene().getWindow();
			stage.setTitle("Park Manager Page");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
			stage.setOnCloseRequest(e -> {
				WorkerControllerClient.removeOnlineWorker(worker.getWorkerID() + "");
			});
			break;
		case DepartmentManager:// department manager
			root = FXMLLoader.load(getClass().getResource(DeparmentManagerHomePagePath));
			System.out.println("Department Manager Park Worker Page");
			stage = (Stage) signinButton.getScene().getWindow();
			stage.setTitle("Department Manager Page");
			stage.setScene(new Scene(root));
			stage.setResizable(false);
			stage.show();
			stage.setOnCloseRequest(e -> {
				WorkerControllerClient.removeOnlineWorker(worker.getWorkerID() + "");
			});
			break;

		default:
			System.out.println("Error in identify worker.");
			break;
		}
	}
    /**
     * This method works when the user clicks on the Go Back button and it let him go back to the last Page.
     */
	@FXML
	void goBackToMainPage(MouseEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(MainPagePath));
		Stage stage = (Stage) goBackButton.getScene().getWindow();
		stage.setTitle("Traveler Page");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}

}
