package boundary;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import server.ConnectionToDB;
import server.GoNatureServer;
import server.ServerUI;

/**
 * @author group 20
 * @version 1.0
 *
 */
public class ServerController implements Initializable {
	/**
	 * @param txt this is a text fill where the user can enter his text.
	 * 
	 * @param textArena           this is the area in where the user will enter his
	 *                            text
	 * @param btn                 this is a button componet
	 * @param serverPane          this is the servers Pane
	 * @param serverPortPane      this is the server port Pane
	 * @param portNumber          this is the text field for the port number
	 * @param setPortNumberBtn    this is port number button componet
	 * @param schemaName          this is a text field for schena name
	 * @param dbPassword          this is text field componet for the data base
	 *                            password
	 * @param serverPane1         this is a Pane for the server
	 * @param serverScreen        this is a text field for the server screen
	 * @param onOffbtn            this is an on/off button componet
	 * @param serverConsole       this is a text field for the server console
	 * @param UsersTable          this is a table field componet for the user table
	 * @param userID              this is a table column for the user ID
	 * @param userType            this is a table column for the userType
	 * @param cardReaderButton    this is the card reader button componet.
	 * @param serverConsoleStatic this is the text area for the server console
	 *                            static
	 */
	private boolean serverStatus = false;
	private String serverPortNumber;

	@FXML
	private TextField txt;
	@FXML
	private TextArea textArena;
	@FXML
	private Button btn;
	@FXML
	private Pane serverPane;
	@FXML
	private Pane serverPortPane;
	@FXML
	private TextField portNumber;
	@FXML
	private Button setPortNumberBtn;
	@FXML
	private TextField schemaName;
	@FXML
	private TextField dbPassword;
	@FXML
	private Pane serverPane1;
	@FXML
	private TextField serverScreen;
	@FXML
	private Button OnOffBtn;
	@FXML
	private TextArea serverConsole;
	@FXML
	private TableView<?> UsersTable;
	@FXML
	private TableColumn<?, ?> userID;
	@FXML
	private TableColumn<?, ?> userType;
	@FXML
	private Button cardReaderButton;

	private static TextArea serverConsoleStatic;
	/**
	 * this method triggers an event that displays the card reader window that we created.
	 * @param event this is an event that will trigger the card reader window to appear, in our case it is a mouse click event.
	 * @throws IOException exception
	 */
    @FXML
    void displayCardReaderWindow(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("CardReader.fxml"));
		Stage stage = new Stage();
		stage.setTitle("Card Reader Service");
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
    }
    /**
     * this method shows when the server is running or server has stopped running 
     * @param event this is the trigger of the event, when we run our server then the event occurs.
     */
	@FXML
	void OnOff(ActionEvent event) {
		if (!serverStatus) {
			// run server
			ServerUI.runServer(serverPortNumber);
			serverScreen.setText("Server is running");
			writeToServerConsole("Server is running");
			serverStatus = true;
			cardReaderButton.setVisible(true);
		} else {
			// stop server
			serverScreen.setText("Server Stop");
			writeToServerConsole("Server Stoped");
			ServerUI.stopServer();
			serverStatus = false;
			cardReaderButton.setVisible(false);
		}
	}
	/**
	 * this method boots the server with the correct port and get the schena name and password
	 * 
	 * @param event this is the event that triggers the server to boot.
	 */
	@FXML
	void setServerData(ActionEvent event) {// gets the mysql data from user.
		serverPortNumber = portNumber.getText();
		GoNatureServer.schemaName = schemaName.getText();
		GoNatureServer.dbPassword = dbPassword.getText();

		ConnectionToDB.connectToDB(schemaName.getText(), dbPassword.getText());

		if (serverPortNumber.isEmpty() || GoNatureServer.schemaName.isEmpty() || GoNatureServer.dbPassword.isEmpty())
			JOptionPane.showMessageDialog(null, "Please fill all the server details");
		else {
			serverPortPane.setVisible(false);
			serverPane.setVisible(true);
		}
	}
	/**
	 * this method is in charge of writing to the server console.
	 * @param str this is the string we wish to write to the server console.
	 */
	public static void writeToServerConsole(String str) {
		if (serverConsoleStatic == null) {
			System.out.println("error console = null line 115");
		} else {
			StringBuilder sb = new StringBuilder(serverConsoleStatic.getText());
			sb.append(String.format("<%s><server> %s\n", LocalTime.now(), str));
			serverConsoleStatic.setText(sb.toString());

			System.out.println(str);
		}
	}
	/**
	 * this method starts the primary stage, it loads the server gui.
	 * @param primaryStage this is the stage we load to the gui.
	 * @throws Exception incase of error.
	 */
	public void start(Stage primaryStage) throws Exception {
		Pane mainPane;
		Scene s;

		FXMLLoader loader = new FXMLLoader();
		System.out.println("starting server gui");
		loader.setLocation(getClass().getResource("/boundary/Server.fxml"));
		mainPane = loader.load();

		s = new Scene(mainPane);

		primaryStage.setTitle("Prototype");
		primaryStage.setScene(s);
		primaryStage.show();

	}
	/**
	 * initallize the server console.
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		serverConsoleStatic = serverConsole;
	}

}
