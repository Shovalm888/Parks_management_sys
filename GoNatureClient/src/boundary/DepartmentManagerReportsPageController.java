package boundary;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ResourceBundle;

import client.WorkerControllerClient;
import entity.Order;
import entity.ParkWorker;
import entity.Report;
import enums.OrderStatus;
import enums.ReportType;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * this class handles all the features we have in the Department manager reports page 
 * @author group 20
 * @version 1.0
 */
public class DepartmentManagerReportsPageController implements Initializable {
/**
 * @param goBackButton this is a ImageView componet for go back button in the system
 * @param submitButton this is a button componet for submit in the system
 * @param choiceBoxReports this is a choice box componet for choice box reports in the system
 * @param textArea this is a text area componet for text area in the system
 * @param choiceBoxMonths this is a choice box componet for choice box months in the system
 * @param downloadButton this is a button componet for download in the system
 * @param downloadAllButton this is a button componet for download all in the system
 * @param stackBarChart this is a StackedBarChart componet for stack bar chart in the system
 * @param x this is a CategoryAxis componet for x in the system
 * @param y this is a number axis componet for y in the system
 * @param park this is text componet for park in the system
 * @param worker this is a park worker entity in the system
 */
	@FXML
	private ImageView goBackButton;
	@FXML
	private Button submitButton;
	@FXML
	private ChoiceBox<String> choiceBoxReports;
	@FXML
	private TextArea textArea;
	@FXML
	private ChoiceBox<String> choiceBoxMonths;
	@FXML
	private Button downloadButton;
	@FXML
	private Button downloadAllButton;
	@FXML
	private StackedBarChart<String, Number> stackBarChart;
    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private Text park;
	
	private ParkWorker worker;
	/**
	 * this method is in charge of displaying the select report page in the system
	 * @param event this is the event that triggers displaying of the select report page.
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

		case "Cancelation":
			LocalDate localDate = LocalDate.now();// set current date.
			LocalTime localTime = LocalTime.now();//set current time.
			Time time = Time.valueOf(localTime);//cast local time to sql.time
			Date date = Date.valueOf(localDate);//cast local date to sql.date
			Report report = new Report( worker.getWorkerID(), worker.getPark(), "", ReportType.Cancelation, date, time ,eventStartString,eventEndString);
			ArrayList<Order> orders = WorkerControllerClient.getReportOrders(report);
			if(orders.isEmpty()) {//if there is no orders.
				textArea.setText("There is no Cancellations at "+worker.getPark()+ " in "+month);
				return;
			}else { // now we display the data.
				int cancellations =0;
				int expired = 0;
				for (Order o : orders) {
					if(o.getOrderStatus() == OrderStatus.Cancelled) {
						cancellations++;
					}else {
						expired++;
					}
				}
				String data = "";
				data = "Total Cancellation in " + worker.getPark() + " at " + month + " is = " + (expired+cancellations);
				data += "\nTotal Cancelled Orders is: "+cancellations;
				data += "\nTotal Unfulfilled Orders is: "+expired;
				textArea.setText(data);
				textArea.setVisible(true);
				break;
			}
			
			
			
			
		case "Visit":
			 localDate = LocalDate.now();// set current date.
			 localTime = LocalTime.now();//set current time.
				time = Time.valueOf(localTime);// cast local time to sql.time
				date = Date.valueOf(localDate);// cast local date to sql.date
				report = new Report(worker.getWorkerID(), worker.getPark(), "", ReportType.Visit, date, time,
						eventStartString, eventEndString);
				orders = WorkerControllerClient.getReportOrders(report);
				// now we display the data.
				String data = "";

				HashMap<Integer, Double> single = new HashMap<>();
				HashMap<Integer, Double> group = new HashMap<>();
				HashMap<Integer, Double> subscribers = new HashMap<>();
				for (int i = 0; i < 15; i++) {
					single.put(i, (double) 0);
					group.put(i, (double) 0);
					subscribers.put(i, (double) 0);
				}
				double totalSingle = 0;
				double totalGroup = 0;
				double totalSubscribers = 0;

				// ArrayList<XYChart<String,Number>> dataList = new ArrayList<>();
				for (Order o : orders) {
					switch (o.getOrderType()) {
					case Single:
					case CasualSingle:
						totalSingle += o.getTimeOfStay();// order time of stay .
//						single[o.getOrderTime().toLocalTime().getHour()-8]++; // order time - 8(to start from index 0) counter.
						single.put(o.getOrderTime().toLocalTime().getHour() - 8,
								(single.get(o.getOrderTime().toLocalTime().getHour() - 8) + 1));
						break;
					case CasualFamily:
					case PrivateGroup:
					case OrganizedGroup:
					case PrePaymentGroup:
					case PrePaymentOrganizedGroup:
					case CasualGroup:
					case CasualOrganizedGroup:
						totalGroup += o.getTimeOfStay();// order time of stay .
						group.put(o.getOrderTime().toLocalTime().getHour() - 8,
								(group.get(o.getOrderTime().toLocalTime().getHour() - 8) + 1));
						break;
					case SingleSubscription:
					case CasualSingleSubscription:
					case FamilySubscription:
					case CasualFamilyNoSubscription:
						totalSubscribers += o.getTimeOfStay();// order time of stay .
						subscribers.put(o.getOrderTime().toLocalTime().getHour() - 8,
								(subscribers.get(o.getOrderTime().toLocalTime().getHour() - 8) + 1));
						break;
					default:
						System.out.println("error in report query, need to check order types.");
						break;
					}
					textArea.setText(data);
					textArea.setVisible(true);
				}
				// avg timeofstay by month
				double[] monthTimeOfStayAvgCounter = new double[3]; // 0 - single, 1- group , 2-subscribers
				double[] monthTimeOfStayAvg = new double[3]; // 0 - single, 1- group , 2-subscribers
				for (int j = 0; j < 3; j++) {
					switch (j) {
					case 0:// calculate counter
						Collection<Double> total = single.values();
						Iterator<Double> it = total.iterator();
						monthTimeOfStayAvgCounter[0] = 0;
						while (it.hasNext()) {
							monthTimeOfStayAvgCounter[0] += it.next();
						}
						// calculate month avg.
						monthTimeOfStayAvg[0] = totalSingle/monthTimeOfStayAvgCounter[0];

						break;
					case 1:// calculate counter
						total = group.values();
						it = total.iterator();
						monthTimeOfStayAvgCounter[1] = 0;
						while (it.hasNext()) {
							monthTimeOfStayAvgCounter[1] += it.next();
						}
						// calculate month avg.
						monthTimeOfStayAvg[1] = totalGroup/monthTimeOfStayAvgCounter[1];
						break;
					case 2:// calculate counter
						total = subscribers.values();
						it = total.iterator();
						monthTimeOfStayAvgCounter[2] = 0;
						while (it.hasNext()) {
							monthTimeOfStayAvgCounter[2] += it.next();
						}
						// calculate month avg.
						monthTimeOfStayAvg[2] = totalSubscribers/monthTimeOfStayAvgCounter[2];
						break;

					default:
						System.out.println("error in month time of stay number array.");
						break;
					}
				}
				CategoryAxis xAxis = new CategoryAxis();// x
				NumberAxis yAxis = new NumberAxis();//y
				xAxis.setCategories(
						FXCollections.<String>observableArrayList(Arrays.asList("Single", "Group", "Subscribers")));
				xAxis.setLabel("Average Visit Time");
				yAxis.setLabel("Time Of Stay");
				final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
				bc.setTitle(month+" Visit Time Of Stay Report");

				// now the data.
				XYChart.Series<String, Number> series1 = new XYChart.Series<>();
				XYChart.Series<String, Number> series2 = new XYChart.Series<>();
				XYChart.Series<String, Number> series3 = new XYChart.Series<>();
				series1.setName("Single");
				series2.setName("Group");
				series3.setName("Subscribers");

				series1.getData().add(new XYChart.Data<>("Single", monthTimeOfStayAvg[0]));
				series2.getData().add(new XYChart.Data<>("Group", monthTimeOfStayAvg[1]));
				series3.getData().add(new XYChart.Data<>("Subscribers", monthTimeOfStayAvg[2]));

				

				//avg by time
				CategoryAxis x = new CategoryAxis();// x
				NumberAxis y = new NumberAxis();//y
				x.setCategories(
						FXCollections.<String>observableArrayList(Arrays.asList("08:00", "09:00", "10:00",
								"11:00", "12:00", "13:00", "14:00", "15:00", "16:00","17:00")));
				x.setLabel("Time");
				y.setLabel("Time Of Stay");
				final BarChart<String, Number> bc1 = new BarChart<String, Number>(x, y); 
				bc1.setTitle(month+" " + LocalDate.now().getYear() + " Visit Report");

				// now the data.
				XYChart.Series<String, Number> series4 = new XYChart.Series<>();
				XYChart.Series<String, Number> series5 = new XYChart.Series<>();
				XYChart.Series<String, Number> series6 = new XYChart.Series<>();
				series4.setName("Single");
				series5.setName("Group");
				series6.setName("Subscribers");
				//calculate time of stay by hours formula.
				double[][] counter = new double[3][10];
				double[][] timeOfStay = new double[3][10];
				for(int k=0;k<orders.size();k++) {
					switch (orders.get(k).getOrderTime().toLocalTime().getHour()) {
					case 8://if time is 08:00
						switch (orders.get(k).getOrderType()) {//add to order type.
						case Single:
						case CasualSingle:
							counter[0][0]++;
							timeOfStay[0][0] += orders.get(k).getTimeOfStay();
							break;
						case CasualFamily:
						case PrivateGroup:
						case OrganizedGroup:
						case PrePaymentGroup:
						case PrePaymentOrganizedGroup:
						case CasualGroup:
						case CasualOrganizedGroup:
							counter[1][0]++;
							timeOfStay[1][0] += orders.get(k).getTimeOfStay();
							break;
						case SingleSubscription:
						case CasualSingleSubscription:
						case FamilySubscription:
						case CasualFamilyNoSubscription:
							counter[2][0]++;
							timeOfStay[2][0] += orders.get(k).getTimeOfStay();
							break;

						default:
							System.out.println("error in order type in reports.");
							break;
						}
						break;
					case 9:
						switch (orders.get(k).getOrderType()) {//add to order type.
						case Single:
						case CasualSingle:
							counter[0][1]++;
							timeOfStay[0][1] += orders.get(k).getTimeOfStay();
							break;
						case CasualFamily:
						case PrivateGroup:
						case OrganizedGroup:
						case PrePaymentGroup:
						case PrePaymentOrganizedGroup:
						case CasualGroup:
						case CasualOrganizedGroup:
							counter[1][1]++;
							timeOfStay[1][1] += orders.get(k).getTimeOfStay();
							break;
						case SingleSubscription:
						case CasualSingleSubscription:
						case FamilySubscription:
						case CasualFamilyNoSubscription:
							counter[2][1]++;
							timeOfStay[2][1] += orders.get(k).getTimeOfStay();
							break;

						default:
							System.out.println("error in order type in reports.");
							break;
						}
						break;
					case 10:
						switch (orders.get(k).getOrderType()) {//add to order type.
						case Single:
						case CasualSingle:
							counter[0][2]++;
							timeOfStay[0][2] += orders.get(k).getTimeOfStay();
							break;
						case CasualFamily:
						case PrivateGroup:
						case OrganizedGroup:
						case PrePaymentGroup:
						case PrePaymentOrganizedGroup:
						case CasualGroup:
						case CasualOrganizedGroup:
							counter[1][2]++;
							timeOfStay[1][2] += orders.get(k).getTimeOfStay();
							break;
						case SingleSubscription:
						case CasualSingleSubscription:
						case FamilySubscription:
						case CasualFamilyNoSubscription:
							counter[2][2]++;
							timeOfStay[2][2] += orders.get(k).getTimeOfStay();
							break;

						default:
							System.out.println("error in order type in reports.");
							break;
						}
						break;
					case 11:
						switch (orders.get(k).getOrderType()) {//add to order type.
						case Single:
						case CasualSingle:
							counter[0][3]++;
							timeOfStay[0][3] += orders.get(k).getTimeOfStay();
							break;
						case CasualFamily:
						case PrivateGroup:
						case OrganizedGroup:
						case PrePaymentGroup:
						case PrePaymentOrganizedGroup:
						case CasualGroup:
						case CasualOrganizedGroup:
							counter[1][3]++;
							timeOfStay[1][3] += orders.get(k).getTimeOfStay();
							break;
						case SingleSubscription:
						case CasualSingleSubscription:
						case FamilySubscription:
						case CasualFamilyNoSubscription:
							counter[2][3]++;
							timeOfStay[2][3] += orders.get(k).getTimeOfStay();
							break;

						default:
							System.out.println("error in order type in reports.");
							break;
						}
						break;
					case 12:
						switch (orders.get(k).getOrderType()) {//add to order type.
						case Single:
						case CasualSingle:
							counter[0][4]++;
							timeOfStay[0][4] += orders.get(k).getTimeOfStay();
							break;
						case CasualFamily:
						case PrivateGroup:
						case OrganizedGroup:
						case PrePaymentGroup:
						case PrePaymentOrganizedGroup:
						case CasualGroup:
						case CasualOrganizedGroup:
							counter[1][4]++;
							timeOfStay[1][4] += orders.get(k).getTimeOfStay();
							break;
						case SingleSubscription:
						case CasualSingleSubscription:
						case FamilySubscription:
						case CasualFamilyNoSubscription:
							counter[2][4]++;
							timeOfStay[2][4] += orders.get(k).getTimeOfStay();
							break;

						default:
							System.out.println("error in order type in reports.");
							break;
						}
						break;
					case 13:
						switch (orders.get(k).getOrderType()) {//add to order type.
						case Single:
						case CasualSingle:
							counter[0][5]++;
							timeOfStay[0][5] += orders.get(k).getTimeOfStay();
							break;
						case CasualFamily:
						case PrivateGroup:
						case OrganizedGroup:
						case PrePaymentGroup:
						case PrePaymentOrganizedGroup:
						case CasualGroup:
						case CasualOrganizedGroup:
							counter[1][5]++;
							timeOfStay[1][5] += orders.get(k).getTimeOfStay();
							break;
						case SingleSubscription:
						case CasualSingleSubscription:
						case FamilySubscription:
						case CasualFamilyNoSubscription:
							counter[2][5]++;
							timeOfStay[2][5] += orders.get(k).getTimeOfStay();
							break;

						default:
							System.out.println("error in order type in reports.");
							break;
						}
						break;
					case 14:
						switch (orders.get(k).getOrderType()) {//add to order type.
						case Single:
						case CasualSingle:
							counter[0][6]++;
							timeOfStay[0][6] += orders.get(k).getTimeOfStay();
							break;
						case CasualFamily:
						case PrivateGroup:
						case OrganizedGroup:
						case PrePaymentGroup:
						case PrePaymentOrganizedGroup:
						case CasualGroup:
						case CasualOrganizedGroup:
							counter[1][6]++;
							timeOfStay[1][6] += orders.get(k).getTimeOfStay();
							break;
						case SingleSubscription:
						case CasualSingleSubscription:
						case FamilySubscription:
						case CasualFamilyNoSubscription:
							counter[2][6]++;
							timeOfStay[2][6] += orders.get(k).getTimeOfStay();
							break;

						default:
							System.out.println("error in order type in reports.");
							break;
						}
						break;
					case 15:
						switch (orders.get(k).getOrderType()) {//add to order type.
						case Single:
						case CasualSingle:
							counter[0][7]++;
							timeOfStay[0][7] += orders.get(k).getTimeOfStay();
							break;
						case CasualFamily:
						case PrivateGroup:
						case OrganizedGroup:
						case PrePaymentGroup:
						case PrePaymentOrganizedGroup:
						case CasualGroup:
						case CasualOrganizedGroup:
							counter[1][7]++;
							timeOfStay[1][7] += orders.get(k).getTimeOfStay();
							break;
						case SingleSubscription:
						case CasualSingleSubscription:
						case FamilySubscription:
						case CasualFamilyNoSubscription:
							counter[2][7]++;
							timeOfStay[2][7] += orders.get(k).getTimeOfStay();
							break;

						default:
							System.out.println("error in order type in reports.");
							break;
						}
						break;
					case 16:
						switch (orders.get(k).getOrderType()) {//add to order type.
						case Single:
						case CasualSingle:
							counter[0][8]++;
							timeOfStay[0][8] += orders.get(k).getTimeOfStay();
							break;
						case CasualFamily:
						case PrivateGroup:
						case OrganizedGroup:
						case PrePaymentGroup:
						case PrePaymentOrganizedGroup:
						case CasualGroup:
						case CasualOrganizedGroup:
							counter[1][8]++;
							timeOfStay[1][8] += orders.get(k).getTimeOfStay();
							break;
						case SingleSubscription:
						case CasualSingleSubscription:
						case FamilySubscription:
						case CasualFamilyNoSubscription:
							counter[2][8]++;
							timeOfStay[2][8] += orders.get(k).getTimeOfStay();
						}
							break;
							
						case 17:
							switch (orders.get(k).getOrderType()) {//add to order type.
							case Single:
							case CasualSingle:
								counter[0][9]++;
								timeOfStay[0][9] += orders.get(k).getTimeOfStay();
								break;
							case CasualFamily:
							case PrivateGroup:
							case OrganizedGroup:
							case PrePaymentGroup:
							case PrePaymentOrganizedGroup:
							case CasualGroup:
							case CasualOrganizedGroup:
								counter[1][9]++;
								timeOfStay[1][9] += orders.get(k).getTimeOfStay();
								break;
							case SingleSubscription:
							case CasualSingleSubscription:
							case FamilySubscription:
							case CasualFamilyNoSubscription:
								counter[2][9]++;
								timeOfStay[2][9] += orders.get(k).getTimeOfStay();
								break;

						default:
							System.out.println("error in order type in reports.");
							break;
						}
						break;

					default:
						System.out.println("error in order visual time report.");
						break;
					}
				}

				ArrayList<Double> list = new ArrayList<>();
				for(int i=0;i<10;i++) {
					for (int j = 0; j < 3; j++) {
						if (counter[j][i] == 0) {
							list.add((double) 0);
						} else {
							double temp = timeOfStay[j][i] / counter[j][i];
							list.add((double) temp);
						}
					}
				}
				series4.getData().add(new XYChart.Data<>("08:00",list.get(0)));
				series5.getData().add(new XYChart.Data<>("08:00",list.get(1)));
				series6.getData().add(new XYChart.Data<>("08:00",list.get(2)));
				
				series4.getData().add(new XYChart.Data<>("09:00",list.get(3)));
				series5.getData().add(new XYChart.Data<>("09:00",list.get(4)));
				series6.getData().add(new XYChart.Data<>("09:00",list.get(5)));
				
				series4.getData().add(new XYChart.Data<>("10:00",list.get(6)));
				series5.getData().add(new XYChart.Data<>("10:00",list.get(7)));
				series6.getData().add(new XYChart.Data<>("10:00",list.get(8)));

				series4.getData().add(new XYChart.Data<>("11:00",list.get(9)));
				series5.getData().add(new XYChart.Data<>("11:00",list.get(10)));
				series6.getData().add(new XYChart.Data<>("11:00",list.get(11)));
				
				series4.getData().add(new XYChart.Data<>("12:00",list.get(12)));
				series5.getData().add(new XYChart.Data<>("12:00",list.get(13)));
				series6.getData().add(new XYChart.Data<>("12:00",list.get(14)));
				
				series4.getData().add(new XYChart.Data<>("13:00",list.get(15)));
				series5.getData().add(new XYChart.Data<>("13:00",list.get(16)));
				series6.getData().add(new XYChart.Data<>("13:00",list.get(17)));

				series4.getData().add(new XYChart.Data<>("14:00",list.get(18)));
				series5.getData().add(new XYChart.Data<>("14:00",list.get(19)));
				series6.getData().add(new XYChart.Data<>("14:00",list.get(20)));
				
				series4.getData().add(new XYChart.Data<>("15:00",list.get(21)));
				series5.getData().add(new XYChart.Data<>("15:00",list.get(22)));
				series6.getData().add(new XYChart.Data<>("15:00",list.get(23)));
				
				series4.getData().add(new XYChart.Data<>("16:00",list.get(24)));
				series5.getData().add(new XYChart.Data<>("16:00",list.get(25)));
				series6.getData().add(new XYChart.Data<>("16:00",list.get(26)));
				
				series4.getData().add(new XYChart.Data<>("17:00",list.get(27)));
				series5.getData().add(new XYChart.Data<>("17:00",list.get(28)));
				series6.getData().add(new XYChart.Data<>("17:00",list.get(29)));
		

				
				
				bc.getData().addAll(series1, series2, series3);
				bc1.getData().addAll(series4, series5, series6);
System.out.println(bc1.getWidth());
                bc1.setLayoutX(40);
                bc.setLayoutX(bc1.getLayoutX());
				bc.setLayoutY(400);
				bc.setTitle(month+ " " + LocalDate.now().getYear() + " Visit Report");
				Button exit = new Button("Go Back");
				exit.setOnMouseClicked(e -> {
					exit.getScene().getWindow().hide();
				});
				exit.setLayoutX(12);
				exit.setLayoutY(750);
				Stage stage = (Stage) goBackButton.getScene().getWindow();
				Group g1 = new Group(bc1,bc,exit);
				Scene scene = new Scene(g1, 620, 800);
				stage.setScene(scene);
				stage.setTitle("Visit Report Page");
				stage.show();
				

				break;
			default:
				System.out.println("error in getting report type.");
				break;
			}

		}
	/**
	 * this method takes you back to the previous screen in the system
	 * @param event this is the event that triggers going back to the previous screen
	 */
	@FXML
	void goBack(MouseEvent event) {
		goBackButton.getScene().getWindow().hide();
	}
	/**
	 * this method is in charge of initallizing the class 
	 * @param arg0 arg0
	 * @param arg1 arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		choiceBoxReports.getItems().add("Visit");
		choiceBoxReports.getItems().add("Cancelation");
		choiceBoxMonths.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August",
				"September", "October", "November", "December");
		worker = WorkerLoginPageController.getWorker();
		//set tool tip
		submitButton.setTooltip(new Tooltip("Display Report with your selected Type, Month."));
		choiceBoxReports.setTooltip(new Tooltip("The Report type options."));
		choiceBoxMonths.setTooltip(new Tooltip("The Months options."));
		park.setText(worker.getPark());
	}
}
