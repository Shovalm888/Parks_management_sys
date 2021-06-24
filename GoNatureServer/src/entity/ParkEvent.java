package entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * this class describes a park event.
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class ParkEvent implements Serializable {
	/**
	 * @param eventNum     this is a number given to the event
	 * @param percentage   this is the amount of discount given during the event in
	 *                     percentages.
	 * @param startingDate this is the starting date for a given event
	 * @param endingDate   this is the ending date for a given event
	 *
	 */
	private static final long serialVersionUID = 1L;

	private int eventNum;
	private int percentage;
	private Date startingDate;
	private Date endingDate;

	/**
	 * a constructor to initiate a given event.
	 * 
	 * @param eventNum     this is the num given to the event
	 * @param percentage   this is the discount given to the event in percentages
	 * @param startingDate this is the starting date for the event
	 * @param endingDate   this is the ending date given to the event.
	 */
	public ParkEvent(int eventNum, int percentage, Date startingDate, Date endingDate) {
		super();
		this.eventNum = eventNum;
		this.percentage = percentage;
		this.startingDate = startingDate;
		this.endingDate = endingDate;
	}

	public int getEventNum() {
		return eventNum;
	}

	public void setEventNum(int eventNum) {
		this.eventNum = eventNum;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public Date getStartingDate() {
		return startingDate;
	}

	public void setStartingDate(Date startingDate) {
		this.startingDate = startingDate;
	}

	public Date getEndingDate() {
		return endingDate;
	}

	public void setEndingDate(Date endingDate) {
		this.endingDate = endingDate;
	}

}
