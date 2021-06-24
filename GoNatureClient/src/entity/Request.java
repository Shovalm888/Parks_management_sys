package entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import enums.RequestStatus;
import enums.RequestType;

/**
 * Request class - this class is used to define a request in our system, here we
 * save all the information we need to process a given request.
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class Request implements Serializable {
	/**
	 * @param requestID this is the ID of the request so we can identify it
	 * easily.
	 * 
	 * @param workerID    this is the workers ID that requests something from the
	 *                    system.
	 * @param park        this is the park we request from
	 * @param requestData this is the data we wish to request
	 * @param value       this is the value we need
	 * @param type        this is the type of request we wish to make to the system
	 * @param status      this is the status of the request in our system
	 * @param date        this is the date the request was received.
	 * @param time        this is the time the request was received
	 * @param startDate   this is the start date for a specific request
	 * @param endDate     this is the ened date for a specific request.
	 */
	private static final long serialVersionUID = -1949846487072436699L;
	private int requestID;
	private int workerID;
	private String park;
	private String requestData;
	private int value;
	private RequestType type;
	private RequestStatus status;
	private Date date;
	private Time time;
	private Date startDate;// for event only
	private Date endDate;// for event only

	/**
	 * Request constructor, here we initallize a request with the relevant
	 * information we receive from the user.
	 * 
	 * @param requestID   this is the requestID
	 * @param workerID    this is the worker to requested the request ID
	 * @param park        this is the park to request information from
	 * @param requestData this is the type of data we wish to receive
	 * @param value       this is the value
	 * @param type        this is the type of request we wish to make
	 * @param status      this is the request status
	 * @param date        this is the date of the request
	 * @param time        this is the time of the request
	 * 
	 */
	public Request(int requestID, int workerID, String park, String requestData, int value, RequestType type,
			RequestStatus status, Date date, Time time) {
		super();
		this.requestID = requestID;
		this.workerID = workerID;
		this.park = park;
		this.requestData = requestData;
		this.value = value;
		this.type = type;
		this.status = status;
		this.date = date;
		this.time = time;
	}

	/**
	 * Request constructor, same as above but uses diffrent information of the
	 * order.
	 * 
	 * @param workerID    this is the worker to requested the request ID
	 * @param park        this is the park to request information from
	 * @param requestData this is the type of data we wish to receive
	 * @param value       this is the value
	 * @param type        this is the type of request we wish to make
	 * @param status      this is the request status
	 * @param date        this is the date of the request
	 * @param time        this is the time of the request
	 */
	public Request(int workerID, String park, String requestData, int value, RequestType type, RequestStatus status,
			Date date, Time time) {
		super();
		this.workerID = workerID;
		this.park = park;
		this.requestData = requestData;
		this.value = value;
		this.type = type;
		this.status = status;
		this.date = date;
		this.time = time;
	}

	/**
	 * request constructor we use this constructor with a given request to
	 * initiallize a new request.
	 * 
	 * @param request this is the request we receive data from.
	 */
	public Request(Request request) {
		this.requestID = request.getRequestID();
		this.workerID = request.getWorkerID();
		this.park = request.getPark();
		this.requestData = request.getRequestData();
		this.value = request.getValue();
		this.type = request.getType();
		this.status = request.getStatus();
		this.date = request.getDate();
		this.time = request.getTime();
	}

	/**
	 * Request constructor, same as above but uses diffrent information of the
	 * order.
	 * 
	 * @param workerID    this is the worker to requested the request ID
	 * @param park        this is the park to request information from
	 * @param requestData this is the type of data we wish to receive
	 * @param value       this is the value
	 * @param type        this is the type of request we wish to make
	 * @param status      this is the request status
	 * @param date        this is the date of the request
	 * @param time        this is the time of the request
	 * @param startDate   this is the startDate of the request
	 * @param endDate     this is the endDate of the request
	 */
	public Request(int workerID, String park, String requestData, int value, RequestType type, RequestStatus status,
			Date date, Time time, Date startDate, Date endDate) {
		super();
		this.workerID = workerID;
		this.park = park;
		this.requestData = requestData;
		this.value = value;
		this.type = type;
		this.status = status;
		this.date = date;
		this.time = time;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPark() {
		return park;
	}

	public void setPark(String park) {
		this.park = park;
	}

	public String getRequestData() {
		return requestData;
	}

	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}

	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public int getWorkerID() {
		return workerID;
	}

	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "Request [requestID=" + requestID + ", date=" + date + ", time=" + time + ", park=" + park
				+ ", requestData=" + requestData + ", type=" + type + ", status=" + status + ", workerID=" + workerID
				+ ", value=" + value + "]";
	}

}
