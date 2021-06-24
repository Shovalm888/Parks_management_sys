package server;
 
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import entity.Event;
import entity.Order;
import entity.Park;
import entity.ParkWorker;
import entity.Report;
import entity.Request;
import entity.SubscriptionTraveler;
import entity.Traveler;
import enums.OrderStatus;
import enums.OrderType;
import enums.RequestStatus;
import enums.RequestType;
import enums.TravelerType;
import enums.WorkerType;
/**
 * Server controller server is responssible for various actions done in the system that communicates with the DB, to name a few would be adding order to the DB, get time of stay inside the park change order status delete orders from DB, add travelers to DB and more.
 * in general, it runs SQL queries releated to the worker, this class mainly handles in connecting between the worker and the DB.
 * @author group 20
 * @version 1.0
 *
 */
public class WorkerControllerServer {


	/**
	 * this method is in charge of making sure a worker is only logged into the system once and not multiple logins in the same time.
	 * @param obj this is the worker we wish to check if hes logged on to the system, from this parameter we can retreieve the ID we wish to check if hes online.
	 * @return true in case the worker is online, false otherwise.
	 */
	public static synchronized boolean addOnlineWorker(Object obj) {
		try {
			PreparedStatement workerOnlineID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM onlineworkers WHERE workerID =?;");// set query
			workerOnlineID.setInt(1, Integer.parseInt((String) obj));// set traveler id number.
			ResultSet r = workerOnlineID.executeQuery();// get result from DB.
			if (!r.next()) {// if traveler id is not exists, add the traveler to the list.
				PreparedStatement addWorkerToOnlineList = ConnectionToDB.conn
						.prepareStatement("INSERT INTO onlineworkers(workerID,ip)" + "VALUES(?,?);");// delete message from DB.
				addWorkerToOnlineList.setInt(1, Integer.parseInt((String) obj));
				addWorkerToOnlineList.setString(2, null);
				addWorkerToOnlineList.executeUpdate();
				return true;// return traveler added successfully.
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * this method is the corrosponding to the add online worker method, in this case if the traveler has logged off from the system we remove hes online status from the DB to allow him to log in again.
	 * @param obj this is the worker we wish to check online status about, from this parameter we can get the worker ID and check it in the DB with SQL query.
	 * @return true in case the operation was made successfully, false in case of failure.
	 */
	public static synchronized boolean removeOnlineWorker(Object obj) {
		try {
			PreparedStatement workerOnlineID = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM onlineworkers WHERE workerID =?;");// set query
			workerOnlineID.setInt(1, Integer.parseInt((String) obj));// set traveler id number.
			ResultSet r = workerOnlineID.executeQuery();// get result from DB.
			if (r.next()) {// if traveler id exists, remove the traveler from the list.
				PreparedStatement removeWorkerFromOnlineList = ConnectionToDB.conn
						.prepareStatement("DELETE FROM onlineworkers WHERE workerID =?;");// delete onlien traveler from DB.
				removeWorkerFromOnlineList.setInt(1, Integer.parseInt((String) obj));
				removeWorkerFromOnlineList.executeUpdate();
				return true;// return traveler added successfully.
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	/**
	 * this method checks the log in details for a worker, it uses a worker ID and password to determine if the worker exists in the DB or not.
	 * @param obj ArrayList that contains the worker ID and Password we want to
	 *            check
	 * @return The worker that contains this ID and password, if ID or Password
	 *         Incorrect return null.
	 * @throws SQLException sql exception
	 */
	public static synchronized ParkWorker checkWorkerLogIn(Object obj) throws SQLException {
		ArrayList<Integer> workerLogInDetails = (ArrayList<Integer>) obj;
		int workerID = workerLogInDetails.get(0);
		int workerPassword = workerLogInDetails.get(1); 
		ParkWorker returnedWorker = null;
		try {
			PreparedStatement checkWorker = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM worker WHERE workerID=?");
			checkWorker.setInt(1, workerID);
			ResultSet r = checkWorker.executeQuery();
			if (r.next()) {// if worker id exists.
				returnedWorker = new ParkWorker(r.getString(3), r.getString(4), r.getInt(1), r.getInt(2),
						r.getString(5), WorkerType.valueOf(r.getString(6)), r.getString(7));
			}
			if (!(returnedWorker.getWorkerPassword() == workerPassword))// check if the password is the same.
				return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnedWorker;
	}
	/**
	 * this method is in charge of retreiving park worker info from the DB, it runs an SQL query with a worker ID, and in the case that the worker exists in the DB, it finds and returns his information
	 * @param obj this is the worker we wish to check on, with this parameter we can retrieve the worker ID, and scan the DB accoridnly.
	 * @return the worker that corrosponds to the checked ID, if he doesn't exist in the DB return null.
	 * @throws SQLException sql exception
	 */
	public static synchronized ParkWorker getWorkerInfo(Object obj) throws SQLException {
		int workerID = (int) obj;
		ParkWorker returnedWorker = null;
		try {
			PreparedStatement checkWorker = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM worker WHERE workerID=?");
			checkWorker.setInt(1, workerID);
			ResultSet r = checkWorker.executeQuery();
			if (r.next()) {// if worker id exists.
				returnedWorker = new ParkWorker(r.getString(3), r.getString(4), r.getInt(1), r.getInt(2),
						r.getString(5), WorkerType.valueOf(r.getString(6)), r.getString(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnedWorker;
	}
	/**
	 * this method gets the park capacity of a given park from the DB using SQL query on a park name
	 * @param obj with this parameter we can obtain the park name , and scan the DB accoridnly to obtain the capacity of that park
	 * @return the park capacity of the corrosponding park, null in case of error.
	 */
	public static synchronized ArrayList<Integer> getParkCapacity(Object obj) {
		String parkName = (String) obj;//get park name.
		try {
			PreparedStatement getCapacity = ConnectionToDB.conn.prepareStatement(
					"SELECT * FROM park WHERE parkName =?;");
			getCapacity.setString(1, parkName);
			ResultSet r = getCapacity.executeQuery();
			ArrayList<Integer> list = new ArrayList<>();
			if (r.next()) {
				int number = r.getInt(3);
				list.add(number);//add maxParkCapacity
				number = r.getInt(5);
				list.add(number);//add currentParkTravelers
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * this method retreieves traveler information from the DB using traveler ID, it uses SQL query to scan the DB for that id and returns the information that corrosponds to that id.
	 * @param obj this is the traveler we wish to check for in the DB, from this parameter we can retreieve the ID and scan for information on that given traveler.
	 * @return the traveler that corrosponds to the ID we checked in the DB, null if it doesn't exist in the DB.
	 * @throws SQLException sql exception
	 */
	public static synchronized Traveler getTravelerInfoByID(Object obj) throws SQLException {
		Traveler returnedTraveler = null;
		try {
			PreparedStatement dataID = ConnectionToDB.conn.prepareStatement("SELECT * FROM traveler WHERE ID =?");
			dataID.setInt(1, Integer.parseInt((String) obj));
			ResultSet r = dataID.executeQuery();
			if (r.next()) {
				returnedTraveler = new Traveler(r.getInt(1), r.getString(2), r.getString(3), r.getString(4),
						r.getString(5), TravelerType.valueOf(r.getString(6)));
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return returnedTraveler;
	}
	/**
	 * this method is in charge of updating traveler types in the DB, it receives a traveler and the type to change to, and with SQL query updates the type in the DB.
	 * @param obj this is the traveler we wish to update in the DB, with this parameter we can receive the ID and retreieve data corrosponding to it from the DB.s
	 * @return true in case the traveler was udpated successfuly, false otherwise
	 * @throws SQLException sql exception
	 */
	public static synchronized boolean updateTravelerType(Object obj) throws SQLException {
		Traveler traveler = (Traveler) obj;
		try {
			PreparedStatement dataID = ConnectionToDB.conn.prepareStatement("SELECT * FROM traveler WHERE ID =?");
			dataID.setInt(1, traveler.getID());
			ResultSet r = dataID.executeQuery();
			if (r.next()) {// now we will update traveler type.
				PreparedStatement updateTraveler = ConnectionToDB.conn.prepareStatement("UPDATE traveler SET travelerType=? WHERE ID =?;");
				String type = traveler.getTravelerType().name();// get type
				updateTraveler.setString(1, type);
				updateTraveler.setInt(2, traveler.getID());
				updateTraveler.executeUpdate();
				return true;
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	/**
	 * this method is responssible of updating a traveler using SQL query to update the DB
	 * @param obj this is the traveler we wish to update info for, with this we can get the traveler ID and use SQL query to gather information from the DB.
	 * @return true if the traveler was updated successfuly, false otherwise.
	 * @throws SQLException sql exception
	 */
	public static synchronized boolean updateTravelerInfo(Object obj) throws SQLException {
		Traveler traveler = (Traveler) obj;
		try {
			PreparedStatement updateTraveler = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM traveler WHERE ID =?");
			updateTraveler.setInt(1, traveler.getID());
			ResultSet r = updateTraveler.executeQuery();
			if (r.next()) {// if id exists. then update fields and type of traveler.
				PreparedStatement update = ConnectionToDB.conn.prepareStatement(
						"UPDATE traveler SET firstName =? , lastName =? , email =? , phoneNumber =? , travelerType =? WHERE ID =?;");
				update.setString(1, traveler.getFirstName());
				update.setString(2, traveler.getSurname());
				update.setString(3, traveler.getEmail());
				update.setString(4, traveler.getTel());
				update.setString(5, traveler.getTravelerType().name());
				update.setInt(6, traveler.getID());
				update.executeUpdate();
				return true;
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	/**
	 * this method is in charge of creating a new subscription in the DB, it receives a subscriber type traveler, and inserts it into the DB using SQL query.
	 * @param obj this is the traveler of type subscriber we wish to insert into the DB.
	 * @return true if the subscriber was inserted to the DB, false otherwise.
	 */
	public static synchronized boolean createNewSubscription(Object obj) {
		SubscriptionTraveler traveler = (SubscriptionTraveler) obj;
		try {
			PreparedStatement updateTraveler = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM subscriptiontraveler WHERE travelerID =?");
			updateTraveler.setInt(1, traveler.getID());
			ResultSet r = updateTraveler.executeQuery();
			if (!r.next()) {// if the id isn't exist in DB. then create new subscription.
				PreparedStatement addSubscriptionTraveler = ConnectionToDB.conn.prepareStatement(
						"INSERT INTO subscriptiontraveler(travelerID,firstName,lastName,email,phoneNumber,travelerType,creditCardNumber,totalFamilyMember)"
								+ "VALUES(?,?,?,?,?,?,?,?);");
				addSubscriptionTraveler.setInt(1, traveler.getID());
				addSubscriptionTraveler.setString(2, traveler.getFirstName());
				addSubscriptionTraveler.setString(3, traveler.getSurname());
				addSubscriptionTraveler.setString(4, traveler.getEmail());
				addSubscriptionTraveler.setString(5, traveler.getTel());
				addSubscriptionTraveler.setString(6, traveler.getTravelerType().name());
				addSubscriptionTraveler.setString(7, traveler.getCreditCardNumber());
				addSubscriptionTraveler.setInt(8, traveler.getTotalFamilyMembers());
				addSubscriptionTraveler.executeUpdate();
				return true;
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	/**
	 * this method is in charge of inserting a new traveler into the DB, it receives traveler info, and inserts it into the DB.
	 * @param obj this is the traveler we wish to insert, with this parameter we have all the information needed to store the traveler inside the DB.
	 * @return true if the traveler was added to the DB, false otherwise.
	 */
	public static synchronized boolean createNewTraveler(Object obj) {
		Traveler traveler = (Traveler) obj;
		try {
			PreparedStatement addTraveler = ConnectionToDB.conn
					.prepareStatement("INSERT INTO traveler(ID,firstName,lastName,email,phoneNumber,travelerType)"
							+ "VALUES(?,?,?,?,?,?);");
			addTraveler.setInt(1, traveler.getID());
			addTraveler.setString(2, traveler.getFirstName());
			addTraveler.setString(3, traveler.getSurname());
			addTraveler.setString(4, traveler.getEmail());
			addTraveler.setString(5, traveler.getTel());
			addTraveler.setString(6, traveler.getTravelerType().name());
			addTraveler.executeUpdate();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * this method gets the time of stay of a given park.
	 * @param obj this is the park name we wish to get the time of stay for.
	 * @return the time of stay of the corrosponding park.
	 */
	public static synchronized int getTimeOfStay(Object obj) {
		String parkName = (String) obj;
		try {
			PreparedStatement timeOfStay = ConnectionToDB.conn.prepareStatement("SELECT * FROM park WHERE parkName =?;");
			timeOfStay.setString(1, parkName);
			ResultSet r = timeOfStay.executeQuery();
			if (r.next()) {
				return r.getInt(4);// return gap number.
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}
	/**
	 * this method is in charge of adding a casual traveler to the DB, it receives a traveler of type casual, and using an SQL query, it inserts the traveler into the DB
	 * @param obj this is the traveler of type Casual we wish to insert into the DB.
	 * @return true if the traveler was added into the DB, false otherwise.
	 */
	public static synchronized boolean addCasualTraveler(Object obj) {
		ArrayList<Object> order = (ArrayList<Object>) obj;
		String type = (String) order.get(0);
		int total = (int) order.get(1);
		String parkName = (String) order.get(2);
		String date = (String) order.get(3);
		String time = (String) order.get(4);
		int price = (int) order.get(5);
		/*
		 * array list with park,date,time to get time of stay.
		 */
		ArrayList<Object> getTimeOfStay = (ArrayList<Object>) obj;
		getTimeOfStay.add(parkName);
		getTimeOfStay.add(date);
		getTimeOfStay.add(time);
		int timeOfStay = getTimeOfStay(getTimeOfStay);// get time of stay.

		try {
			PreparedStatement addOrder = ConnectionToDB.conn.prepareStatement(
					"INSERT INTO gonature.order(travelerID,orderType,park,orderDate,orderTime,numOfTravelers,travelerEmail,orderStatus,totalSum,timeOfStay)"
							+ "VALUES(?,?,?,?,?,?,?,?,?,?);");
			addOrder.setInt(1, (Integer) null);
			addOrder.setString(2, OrderType.valueOf(type).name());
			addOrder.setString(3, parkName);
			addOrder.setString(4, date);
			addOrder.setString(5, time);
			addOrder.setInt(6, total);
			addOrder.setString(7, (String) null);
			addOrder.setString(8, OrderStatus.valueOf("Approved").name());
			addOrder.setInt(9, price);
			addOrder.setInt(10, timeOfStay);
			addOrder.executeUpdate();

			String tmpTime = time.substring(0, 2);
			int intTime = Integer.parseInt(tmpTime);
			// now update next date. loop on time of stay.
			for (int i = 0; i < timeOfStay && intTime + i < 16; i++, intTime++) {
				// update current travelers.
				PreparedStatement updateDates = ConnectionToDB.conn.prepareStatement(
						"UPDATE parkactivityhours SET currentTravelers= currentTravelers+? WHERE park=? AND parkActivityDate=? AND parkActivityTime=?;");
				updateDates.setInt(1, total);
				updateDates.setString(2, parkName);
				updateDates.setString(3, date);
				tmpTime = intTime + ":00:00";
				updateDates.setString(4, tmpTime);
				updateDates.executeUpdate();

				PreparedStatement updateIsFull = ConnectionToDB.conn.prepareStatement(
						"UPDATE parkactivityhours SET isFull =? WHERE park=? AND parkActivityDate=? AND parkActivityTime=? AND maximumTravelers - gapNumber = currentTravelers;");
				updateIsFull.setInt(1, 1);
				updateIsFull.setString(2, parkName);
				updateIsFull.setString(3, date);
				updateIsFull.setString(4, tmpTime);
				updateIsFull.executeUpdate();
			}
			return true;
		} catch (Exception e) {
			return false;// exception default false.
		}

	}
	/**
	 * this method checks if we can change parameters releated to to the park such as gap, capacity and time of stay.
	 * in general, it receives a request and checks if we can fulfill it in the system.
	 * @param obj this is the request that we receive, based on this parameter we check different scenarios in the DB.
	 * @return true if we can update the parameter, false otherwise.
	 */
	public static synchronized boolean checkParkParameterUpdate(Object obj) {
		ArrayList<Object> list = (ArrayList<Object>) obj;// list = ( type, value, park-name, time,date)
		// cast list to parameters.
		RequestType type = (RequestType) list.get(0);
		int value = (int) list.get(1);
		String parkName = (String) list.get(2);
		Time time = (Time) list.get(3);
		Date date = (Date) list.get(4);

		switch (type) {// switch on request type.
		// option-1
		case ParkCapacity:
			// check if we can change park capacity.
			// first make query that gets all the upcoming dates. then check if
			// max-current-gap > the update parameter value.
			try {
				PreparedStatement parkCapacityCheck = ConnectionToDB.conn.prepareStatement(
						"SELECT * FROM parkactivityhours WHERE park=? AND ((parkActivityDate =? AND parkActivityTime >?) OR parkActivityDate >?) ORDER BY parkActivityDate, parkActivityTime;");
				parkCapacityCheck.setString(1, parkName);
				parkCapacityCheck.setDate(2, (java.sql.Date) date);
				parkCapacityCheck.setTime(3, (java.sql.Time) time);
				parkCapacityCheck.setDate(4, (java.sql.Date) date);
				ResultSet r = parkCapacityCheck.executeQuery();// execute query

				ArrayList<Park> parks = new ArrayList<>();// list to save all parks we need to check them.
				while (r.next()) {// add all park from result set
					Park park = new Park(r.getString(1), r.getInt(4), r.getInt(5), r.getInt((7)));
					parks.add(park);
				}
				// now we got all the parks we need to check.
				// now we check if we can change the parameter value.
				for (Park p : parks) {// new value - current - gap >= 0 ?
					if (!(value - p.getCurrentParkCapacity() - p.getGap() >= 0)) {
						return false;
					}
				}
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
		// option-2
		case GapChange:
			// check if we can change park gap number.
			// first make query that gets all the upcoming dates.
			try {
				PreparedStatement parkCapacityCheck = ConnectionToDB.conn.prepareStatement(
						"SELECT * FROM parkactivityhours WHERE park=? AND ((parkActivityDate =? AND parkActivityTime >?) OR parkActivityDate >?) ORDER BY parkActivityDate, parkActivityTime;");
				parkCapacityCheck.setString(1, parkName);
				parkCapacityCheck.setDate(2, (java.sql.Date) date);
				parkCapacityCheck.setTime(3, (java.sql.Time) time);
				parkCapacityCheck.setDate(4, (java.sql.Date) date);
				ResultSet r = parkCapacityCheck.executeQuery();// execute query

				ArrayList<Park> parks = new ArrayList<>();// list to save all parks we need to check them.
				while (r.next()) {// add all park from result set
					Park park = new Park(r.getString(1), r.getInt(4), r.getInt(5), r.getInt((7)));
					parks.add(park);
				}
				// now we got all the parks we need to check.
				// now we check if we can change the parameter value.
				for (Park p : parks) {// max - current - new gap >= 0 ?
					if (!(p.getMaxParkCapacity() - p.getCurrentParkCapacity() - value >= 0)) {
						return false;
					}
				}
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
		// option-3
		case TimeOfStay:
			return true;

		default:
			System.out.println("error in request type-checkParkParameterUpdate.");
			return false;
		}
	}
	/**
	 * this method is in charge of inserting new request into our database it receives a request and insert it to the DB with SQL query.
	 * @param obj this is the request we wish to add to the DB
	 * @return true if we added the request, false otherwise
	 */
	public static synchronized boolean createNewRequest(Object obj) {
		Request request = (Request) obj;
		try {
			if (request.getType() != RequestType.Event) {// type = gap,capacity or time of stay.
				PreparedStatement createRequest = ConnectionToDB.conn.prepareStatement(
						"INSERT INTO request(parkName,data,type,status,date,time,workerID,value)VALUES(?,?,?,?,?,?,?,?);");
				createRequest.setString(1, request.getPark());
				createRequest.setString(2, request.getRequestData());
				createRequest.setString(3, request.getType().name());
				createRequest.setString(4, request.getStatus().name());
				createRequest.setDate(5, (java.sql.Date) request.getDate());
				createRequest.setTime(6, (java.sql.Time) request.getTime());
				createRequest.setInt(7, request.getWorkerID());
				createRequest.setInt(8, request.getValue());
				createRequest.executeUpdate();// execute query
				return true;
			} else {// type = event
				PreparedStatement createRequest = ConnectionToDB.conn.prepareStatement(
						"INSERT INTO request(parkName,data,type,status,date,time,workerID,value,startDate,endDate)VALUES(?,?,?,?,?,?,?,?,?,?);");
				createRequest.setString(1, request.getPark());
				createRequest.setString(2, request.getRequestData());
				createRequest.setString(3, request.getType().name());
				createRequest.setString(4, request.getStatus().name());
				createRequest.setDate(5, request.getDate());
				createRequest.setTime(6, request.getTime());
				createRequest.setInt(7, request.getWorkerID());
				createRequest.setInt(8, request.getValue());
				createRequest.setDate(9, request.getStartDate());
				createRequest.setDate(10, request.getEndDate());
				createRequest.executeUpdate();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * this method is in charge of retreiving workers requests, it uses SQL query with the worker ID to get the corrosponding request to that worker.
	 * @param obj this is the worker we wish to retrieve requests for, with this entity we can get the worker ID and use it with SQL query to get the requests for that worker
	 * @return the corrosponding requests for that worker in the form  of an ArrayList.
	 */
	public static synchronized ArrayList<Request> getWorkerRequests(Object obj) {
		int workerID = (int) obj;
		ArrayList<Request> requests = new ArrayList<>();
		try {
			ParkWorker worker = getWorkerInfo(obj);
			switch (worker.getWorkerType()) {
			case ParkManager:
				PreparedStatement request = ConnectionToDB.conn.prepareStatement("SELECT * FROM  request WHERE workerID =?;");
				request.setInt(1, workerID);
				ResultSet r = request.executeQuery();
				int i = 1;// to set request id as ascending way. 1,2,3,4,...
				while (r.next()) {
					Request tmp = new Request(i++, r.getInt(8), r.getString(2), r.getString(3),r.getInt(9),RequestType.valueOf(r.getString(4)),
							RequestStatus.valueOf(r.getString(5)),r.getDate(6), r.getTime(7));
					requests.add(tmp);
				}
				return requests;

			case DepartmentManager:
				String parkName = worker.getPark();
				request = ConnectionToDB.conn.prepareStatement("SELECT * FROM  request WHERE parkName =? AND status=?;");
				request.setString(1, parkName);
				request.setString(2, RequestStatus.Pending.name());
				r = request.executeQuery();
				while (r.next()) {
					Request tmp = new Request(r.getInt(1), r.getInt(8), r.getString(2), r.getString(3),r.getInt(9),RequestType.valueOf(r.getString(4)),
							RequestStatus.valueOf(r.getString(5)),r.getDate(6), r.getTime(7));
					requests.add(tmp);
				}
				return requests;

			default:
				System.out.println("error in getting worker requests.");
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * this method is in charge of checking if we can add a new event, if theres an existing event during the period of the desired new event, we cannot add it.
	 * @param obj this is the event we wish to add to the DB
	 * @return true if we can add the event, false otherwise.
	 */
	public static synchronized boolean checkEvent(Object obj) {
		entity.Event tmp = (entity.Event) obj;
		try {
			PreparedStatement eventPreparedStatement = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM  events WHERE park =? AND ((startingDate>=? AND startingDate <=?)"
							+ " OR (endingDate>=? AND endingDate <=?));");
			eventPreparedStatement.setString(1, tmp.getPark());
			eventPreparedStatement.setDate(2, tmp.getStart());
			eventPreparedStatement.setDate(3, tmp.getEnd());
			eventPreparedStatement.setDate(4, tmp.getStart());
			eventPreparedStatement.setDate(5, tmp.getEnd());
			System.out.println(eventPreparedStatement.toString());
			ResultSet r = eventPreparedStatement.executeQuery();
			if (r.next()) {// if there is we return false. means we can't add this event.
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * this method is the corrosponding to the checkparkmeter method, if the check went through okay, we can then proceed to make the update needed in the DB, which is what  does method does.
	 * @param obj receives the updated park parameter that needs to be inserted into the DB.
	 * @return true if the update succeeded, false otherwise.
	 */
	public static synchronized boolean updateParkParameter(Object obj) {
		Request request = (Request) obj;
		//check if we need to approve or cancelled
		if(request.getStatus() == RequestStatus.Cancelled) {
			PreparedStatement updateParameter;
			try {
				updateParameter = ConnectionToDB.conn.prepareStatement("UPDATE request SET status=? WHERE requestID =?;");
				updateParameter.setString(1, request.getStatus().name());
				updateParameter.setInt(2, request.getRequestID());
				updateParameter.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		switch (request.getType()) {
		case ParkCapacity:
			try {// we need to update 3 tables. park, parkactivityhours and change request to approved status. update parkactivityhours table.
				PreparedStatement updateParameter = ConnectionToDB.conn.prepareStatement(
						"UPDATE parkactivityhours SET maximumTravelers=? WHERE park =? AND ((parkActivityDate =? And parkActivityTime >?) OR parkActivityDate >?);");
				updateParameter.setInt(1, request.getValue());
				updateParameter.setString(2, request.getPark());
				updateParameter.setDate(3, request.getDate());
				updateParameter.setTime(4, request.getTime());
				updateParameter.setDate(5, request.getDate());
				updateParameter.executeUpdate();
				// update park table
				updateParameter = ConnectionToDB.conn
						.prepareStatement("UPDATE park SET maxParkCapacity=? WHERE parkName =?;");
				updateParameter.setInt(1, request.getValue());
				updateParameter.setString(2, request.getPark());
				updateParameter.executeUpdate();
				// update request status
				updateParameter = ConnectionToDB.conn
						.prepareStatement("UPDATE request SET status=? WHERE requestID =?;");
				updateParameter.setString(1, request.getStatus().name());
				updateParameter.setInt(2, request.getRequestID());
				updateParameter.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
			
		case GapChange:
			try {
				PreparedStatement updateParameter = ConnectionToDB.conn.prepareStatement(
						"UPDATE parkactivityhours SET gapNumber=? WHERE park =? AND ((parkActivityDate =? And parkActivityTime >?) OR parkActivityDate >?) ;");
				updateParameter.setInt(1, request.getValue());
				updateParameter.setString(2, request.getPark());
				updateParameter.setDate(3, request.getDate());
				updateParameter.setTime(4, request.getTime());
				updateParameter.setDate(5, request.getDate());
				updateParameter.executeUpdate();
				// update request status
				updateParameter = ConnectionToDB.conn
						.prepareStatement("UPDATE request SET status=? WHERE requestID =?;");
				updateParameter.setString(1, RequestStatus.Approved.name());
				updateParameter.setInt(2, request.getRequestID());
				updateParameter.executeUpdate();
				return true;

			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
			
		case TimeOfStay:
			try {
				PreparedStatement updateParameter = ConnectionToDB.conn
						.prepareStatement("UPDATE park SET travelerTimeOfStayInHours=? WHERE parkName =? ;");
				updateParameter.setInt(1, request.getValue());
				updateParameter.setString(2, request.getPark());
				updateParameter.executeUpdate();
				// update request status
				updateParameter = ConnectionToDB.conn
						.prepareStatement("UPDATE request SET status=? WHERE requestID =?;");
				updateParameter.setString(1, RequestStatus.Approved.name());
				updateParameter.setInt(2, request.getRequestID());
				updateParameter.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
			
		case Event:
			try {
				PreparedStatement addNewEvent = ConnectionToDB.conn.prepareStatement(
						"INSERT INTO events(percentage,startingDate,endingDate,park) VALUES(?,?,?,?);");
				addNewEvent.setInt(1, request.getValue());
				addNewEvent.setDate(2, request.getStartDate());
				addNewEvent.setDate(3, request.getEndDate());
				addNewEvent.setString(4, request.getPark());
				addNewEvent.executeUpdate();
				// update event status
				addNewEvent = ConnectionToDB.conn
						.prepareStatement("UPDATE request SET status=? WHERE requestID =?;");
				addNewEvent.setString(1, RequestStatus.Approved.name());
				addNewEvent.setInt(2, request.getRequestID());
				addNewEvent.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		default:
			System.out.println("error in trying to update park parameters (event,gap,capacity,timeofstay).");
			break;
		}
		return false;
	}
	/**
	 * this method retrieves the corrosponding event to the request ID this method receives.
	 * @param obj this is the request ID , with this parameter we turn to the DB to bring the corrosponding event.
	 * @return the corrosponding event.
	 */
	public static synchronized Event getEventByRequestID(Object obj) {
		int requestID = (int) obj;
		try {
			PreparedStatement getEvent = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM request WHERE requestID=?;");
			getEvent.setInt(1, requestID);
			ResultSet r = getEvent.executeQuery();
			if (r.next()) {
				entity.Event tmp = new Event(r.getInt(9), r.getDate(10), r.getDate(11), r.getString(2));
				return tmp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	/**
	 * this method generates an array list of orders that corrospond to the report we wish to generate by using SQL query to retrieve data from the DB.
	 * @param obj this is the report that we are basing our orders upon.
	 * @return an array list of orders that corrospond to the report were going by.
	 */
		
	public static synchronized ArrayList<Order> getReportOrders(Object obj) {
		ArrayList<Order> orders = new ArrayList<>();
		Report report = (Report) obj;
		
		switch (report.getType()) {
		case TotalVisitors:
			try {
				PreparedStatement getOrders = ConnectionToDB.conn
						.prepareStatement("SELECT * FROM gonature.order WHERE park=? AND (orderStatus =? OR orderStatus =?) AND orderDate >=? And orderDate <=?;");
				getOrders.setString(1, report.getPark());
				getOrders.setString(2, OrderStatus.Done.name());
				getOrders.setString(3, OrderStatus.CurrentlyAtThePark.name());
				getOrders.setString(4, report.getMonth());
				getOrders.setString(5, report.getEndMonth());
				ResultSet r = getOrders.executeQuery();
				while(r.next()) {
					Order order = new Order(OrderType.valueOf(r.getString(3)), r.getString(4), r.getDate(5),r.getTime(6) ,r.getInt(7), OrderStatus.valueOf(r.getString(9)), r.getDouble(10));
					orders.add(order);// order type , park , numof traveler, order status , total sum
				}
				return orders;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null; 
		case OccupancyUse:
			try {
				PreparedStatement getOrders = ConnectionToDB.conn
						.prepareStatement("SELECT * FROM parkactivityhours WHERE park =? AND occupancy =0 AND parkActivityDate >=? And parkActivityDate <=? ORDER BY parkActivityDate, parkActivityTime;");
				getOrders.setString(1, report.getPark());
				getOrders.setString(2, report.getMonth());
				getOrders.setString(3, report.getEndMonth());
				ResultSet r = getOrders.executeQuery();
				while(r.next()) {
					Order order = new Order(r.getString(1),r.getDate(2), r.getTime(3));
					orders.add(order);
				}
				return orders;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
			
		case IncomeStatement:
			try {
				PreparedStatement getOrders = ConnectionToDB.conn
						.prepareStatement("SELECT * FROM gonature.order WHERE park=? AND (orderStatus =? OR orderStatus =?) AND orderDate >=? And orderDate <=?;");
				getOrders.setString(1, report.getPark());
				getOrders.setString(2, OrderStatus.Done.name());
				getOrders.setString(3, OrderStatus.CurrentlyAtThePark.name());
				getOrders.setString(4, report.getMonth());
				getOrders.setString(5, report.getEndMonth());
				ResultSet r = getOrders.executeQuery();
				while(r.next()) {
					Order order = new Order(OrderType.valueOf(r.getString(3)), r.getString(4),r.getDate(5),r.getTime(6), r.getInt(7), OrderStatus.valueOf(r.getString(9)), r.getDouble(10));
					orders.add(order);
				}
				return orders;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		case Cancelation:
			try {
				PreparedStatement getOrders = ConnectionToDB.conn
						.prepareStatement("SELECT * FROM gonature.order WHERE park=? AND (orderStatus =? OR orderStatus =?) AND orderDate >=? And orderDate <=?;");
				getOrders.setString(1, report.getPark());
				getOrders.setString(2, OrderStatus.Expired.name());
				getOrders.setString(3, OrderStatus.Cancelled.name());
				getOrders.setString(4, report.getMonth());
				getOrders.setString(5, report.getEndMonth());
				ResultSet r = getOrders.executeQuery();
				while(r.next()) {
					Order order = new Order(OrderType.valueOf(r.getString(3)), r.getString(4), r.getDate(5),r.getTime(6),r.getInt(7), OrderStatus.valueOf(r.getString(9)), r.getDouble(10));
					orders.add(order);
				}
				return orders;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
			
		case Visit://return all 'Done','CurrentlyAtThePark' orders. within the input dates,
			try {
				PreparedStatement getOrders = ConnectionToDB.conn
						.prepareStatement("SELECT * FROM gonature.order WHERE park=? AND (orderStatus =? OR orderStatus =?) AND orderDate >=? And orderDate <=? ORDER BY orderDate, orderTime;");
				getOrders.setString(1, report.getPark());
				getOrders.setString(2, OrderStatus.Done.name());
				getOrders.setString(3, OrderStatus.CurrentlyAtThePark.name());
				getOrders.setString(4, report.getMonth());
				getOrders.setString(5, report.getEndMonth());
				ResultSet r = getOrders.executeQuery();
				while(r.next()) {
					Order order = new Order(OrderType.valueOf(r.getString(3)), r.getString(4),r.getDate(5),r.getTime(6), r.getInt(7), OrderStatus.valueOf(r.getString(9)), r.getInt(11));
					//Order order = new Order(OrderType.valueOf(r.getString(3)), r.getString(4), r.getInt(7), OrderStatus.valueOf(r.getString(9)), r.getDouble(10));
					orders.add(order);
				}
				return orders;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		default:
			System.out.println("Error in get report orders.");
			break;
		}
		return null;
	}
	/**
	 * this method receives a park ID, and gets the time of stay for that corrosponding park.
	 * @param obj this is the park we wish to get the time of stay for.
	 * @return the time of stay for the park we received.
	 * @throws SQLException sql exception
	 */
	public static synchronized int getParkTimeOfStay(Object obj) throws SQLException {
		int parkID = (int) obj;
		try {
			PreparedStatement timeOfStay = ConnectionToDB.conn
					.prepareStatement("SELECT travelerTimeOfStayInHours FROM park WHERE parkID=? ;");
			timeOfStay.setInt(1, parkID);
			ResultSet r = timeOfStay.executeQuery();
			if (r.next()) {
				int number = r.getInt(1);
				return number;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;// exception default false.
		}
		return 0;
	}
	/**
	 * this method checks an order date to see if theres an event during that date, and in which case it will return the discount for the order according to the event discount number.
	 * @param orderDate this is the date of the order we wish to check event for.
	 * @return discount number 1 if no discount, 1- discount if there is an event.
	 * @throws SQLException sql exception
	 */
	public static synchronized double checkForEvent(Object orderDate) throws SQLException {
		Date checkDate = (Date) orderDate;
		int discount = 0;
		double res = 1;
		try {
			PreparedStatement eventCheck = ConnectionToDB.conn
					.prepareStatement("SELECT * FROM events WHERE startingDate <= ? AND endingDate >= ?");
			eventCheck.setDate(1, checkDate);
			eventCheck.setDate(2, checkDate);
			ResultSet r = eventCheck.executeQuery();
			if (r.next()) {
				discount = r.getInt(2);// get the discount.
				return res - ((double) discount / 100);// return 1 - discount.
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 1;
		}
		return 1;// return 1- discount.
	}
	/**
	 * this method receives an order, and updates the park current travelers number with the num of travelers in the given order.
	 * @param obj this is the order, with this we can tell how many travelers will be visiting the park, and with SQL query we can update the DB accordinly.
	 * @return true if we updated the amount of travelers currently in the park, false otherwise.
	 */
	public static boolean updateCurrentParkTravelersNumber(Object obj) {
		Order order = (Order) obj;
		try {
			PreparedStatement updatePark = ConnectionToDB.conn.prepareStatement("SELECT * FROM park WHERE parkName =?");
			updatePark.setString(1, order.getPark());
			ResultSet r = updatePark.executeQuery();
			if (r.next()) {// now we will update traveler type.
				PreparedStatement updateCurrentNumber = ConnectionToDB.conn
						.prepareStatement("UPDATE park SET currentParkTravelers = currentParkTravelers + ? WHERE parkName =?;");
				updateCurrentNumber.setInt(1, order.getNumOfTravelers());
				updateCurrentNumber.setString(2, order.getPark());
				updateCurrentNumber.executeUpdate();
				return true;
			}
			r.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * this method receives an order, and checks to see the data in that order is valid, for instance if the park exists in the DB or the time/date is correct and more.
	 * @param obj this is the order we wish to check the data for.
	 * @return true if the order is valid, false otherwise.
	 */
	public static boolean checkNewOrder(Object obj) {
		Order checkOrder = (Order) obj;
		try {
			PreparedStatement getPark = ConnectionToDB.conn.prepareStatement("SELECT * FROM park WHERE parkName=?;");
			getPark.setString(1, checkOrder.getPark());
			System.out.println(getPark.toString());
			ResultSet park = getPark.executeQuery();
			if ((park.next() && (park.getInt(3)-park.getInt(4)>=0))) {//if the park capacity available to receive new visitors.
				int timeOfStay = park.getInt(4);
				for (int i = 1; i < timeOfStay &&(checkOrder.getOrderTime().toLocalTime().getHour()+i<18); i++) {
					PreparedStatement checkOrderhours = ConnectionToDB.conn.prepareStatement(
							"SELECT * FROM parkactivityhours WHERE park=? And parkActivityDate=? AND parkActivityTime=?;");
					checkOrderhours.setString(1, checkOrder.getPark());
					checkOrderhours.setDate(2, checkOrder.getOrderDate());
					Time tmpTime = new Time(checkOrder.getOrderTime().getHours() + i, 0, 0);
					checkOrderhours.setString(3, tmpTime.toString());
					System.out.println(checkOrderhours.toString());
					ResultSet r = checkOrderhours.executeQuery();
					if (r.next()) {
						int remainingPlace = r.getInt(5) - r.getInt(4);// max - current 
						if (remainingPlace < checkOrder.getNumOfTravelers())
							return false;// return false if remaining is less then traveler number.
					} else
						return false;// return false if order date and time is not exists in my sql table.
				}
				return true;// return true if we finish the time of stay for loop and everything is ok.
			} else
				return false;// return false if park is not exists in my sql table.
		} catch (Exception e) {
			e.printStackTrace();
			return false;// exception default false.
		}
	}
	/**
	 * this method is in charge of checking if the parks we have in the DB are currently full, if true then it updates the occupancy parameter.
	 * @param obj empty string
	 * @return true if the check worked, false otherwise.
	 */
	public static boolean checkOccupancy(Object obj) {
		try {
			PreparedStatement checkParkOccupancy = ConnectionToDB.conn.prepareStatement("SELECT * FROM park ;");
			ResultSet r = checkParkOccupancy.executeQuery();
			while (r.next()) {// check 3 parks and check if current = max.
				int max = r.getInt(3);
				int current = r.getInt(5);
				if (max == current) { // update occpancy to true.
					PreparedStatement updateOccupancy = ConnectionToDB.conn.prepareStatement(
							"UPDATE gonature.parkactivityhours SET occupancy = ? WHERE park =? AND parkActivityDate =? AND parkActivityTime =?;");
					updateOccupancy.setBoolean(1, true);
					updateOccupancy.setString(2, r.getString(2));
					Date date = Date.valueOf(LocalDate.now());
					Time time = Time.valueOf(LocalTime.of(LocalTime.now().getHour(),0,0));
					updateOccupancy.setDate(3, date);
					updateOccupancy.setTime(4, time);
					updateOccupancy.executeUpdate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}