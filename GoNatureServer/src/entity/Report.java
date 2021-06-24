package entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import enums.ReportType;

/**
 * Report class, this class helps us define a report in our system, we save here
 * all the information we need for a report.
 * 
 * @author group 20
 * @version 1.0
 *
 */

public class Report implements Serializable {
	/**
	 * @param reportID this is the given report id for easy identification.
	 * 
	 * @param workerID    this is the id of the worker
	 * @param park        this is the designated park
	 * @param requestData this is the the date we wish to receive from the system
	 * @param type        this is the type of report we wish to generate
	 * @param date        this is the specified date for the report
	 * @param time        this is the specified time for the report
	 * @param month       this is the specified month for the report
	 * @param endMonth    this is the specified month for the report.
	 */
	private static final long serialVersionUID = 2740425702925600604L;
	private int reportID;
	private int workerID;
	private String park;
	private String requestData;
	private ReportType type;
	private Date date;
	private Time time;
	private String month;
	private String endMonth;

	/**
	 * constructor to initiallize report using a worker ID, designated
	 * park,requested data, report type, time, month and end month.
	 * 
	 * @param workerID    this is the workerID used to generate the report with
	 * @param park        this is the park to generate data from
	 * @param requestData this is the type of data we wish to receive
	 * @param type        this is the type of report we wish to generate
	 * @param date        this is the date we wish to receive report from
	 * @param time        this is the time we wish to receive report from
	 * @param month       this is the month we wish to receive report from
	 * @param endMonth    this is the end month we wish to receive report from.
	 * 
	 */
	public Report(int workerID, String park, String requestData, ReportType type, Date date, Time time, String month,
			String endMonth) {
		super();
		this.workerID = workerID;
		this.park = park;
		this.requestData = requestData;
		this.type = type;
		this.date = date;
		this.time = time;
		this.month = month;
		this.endMonth = endMonth;
	}

	/**
	 * constructor to initiallize report using a worker ID, designated
	 * park,requested data, report type, time, month and end month.
	 * 
	 * @param reportID    this is generated reports ID.
	 * @param workerID    this is the workerID used to generate the report with
	 * @param park        this is the park to generate data from
	 * @param requestData this is the type of data we wish to receive
	 * @param type        this is the type of report we wish to generate
	 * @param date        this is the date we wish to receive report from
	 * @param time        this is the time we wish to receive report from
	 * 
	 */
	public Report(int reportID, int workerID, String park, String requestData, ReportType type, Date date, Time time) {
		super();
		this.reportID = reportID;
		this.workerID = workerID;
		this.park = park;
		this.requestData = requestData;
		this.type = type;
		this.date = date;
		this.time = time;
	}

	public int getReportID() {
		return reportID;
	}

	public void setReportID(int reportID) {
		this.reportID = reportID;
	}

	public int getWorkerID() {
		return workerID;
	}

	public void setWorkerID(int workerID) {
		this.workerID = workerID;
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

	public ReportType getType() {
		return type;
	}

	public void setType(ReportType type) {
		this.type = type;
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}
}
