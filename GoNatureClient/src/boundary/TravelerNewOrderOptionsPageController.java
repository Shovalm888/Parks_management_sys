package boundary;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


/**
 * This class is the Controller for the Page that helps the Traveler to choose his desirable order.
 * @version 1.0
 * @author Group 20
 */
public class TravelerNewOrderOptionsPageController implements Initializable{
/**
 * @param backToTravelerHomePage let the user go back to the previous page.
 */
    @FXML
    private ImageView backToTravelerHomePage;

    @FXML
    private Button singleInvitation;

    @FXML
    private Button groupInvitation;

    private static String orderPage ="";
    
    public static void setOrderPage(String page) {
    	orderPage = page;
    }
    /**
     * Method that shows the right order page, The order page that the user choose.
     */
	@FXML
	void displayNewOrderPage(MouseEvent event) throws IOException {
		
		String resource = "", title = "Order page";	
		if(((Button)event.getSource()).getId().contentEquals("singleInvitation")) 
			resource = orderPage;
		else if(((Button)event.getSource()).getId().contentEquals("groupInvitation"))
			resource = "TravelerNewOrderGroup.fxml";
		else {
			System.out.println("Error");
			return;
		}
		Parent root = FXMLLoader.load(getClass().getResource(resource));
		Stage stage = (Stage) groupInvitation.getScene().getWindow();
		stage.setTitle(title);
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}

    @FXML
    void displayHomePage(MouseEvent event) throws IOException{
//		Parent root = FXMLLoader.load(getClass().getResource("TravelerHomePage.fxml"));
//		Stage stage = (Stage) singleInvitation.getScene().getWindow();
//		stage.setTitle("Traveler HomePage");
//		stage.setScene(new Scene(root));
//		stage.setResizable(false);
//		stage.show();
    	singleInvitation.getScene().getWindow().hide();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (orderPage.contentEquals("TravelerNewOrderFamily.fxml"))
		   singleInvitation.setText("Subscriber invitation");
		else
			singleInvitation.setText("Single invitation");
	}
}
