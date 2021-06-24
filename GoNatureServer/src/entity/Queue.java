package entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

/**
 * this is the Queue class, here we implement a queue system.
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class Queue implements Serializable {
	/**
	 * @param parkName this is the designated parkName that you wish to enter the
	 * queue for
	 * 
	 * @param date       this is the date you wish to enter the park at
	 * @param Time       this is the time you wish to enter the park at
	 * @param travelerID this is the travelerID that wishes to enter the queue
	 * @param orderID    this is the orderID used to enter the queue with.
	 */
	private static final long serialVersionUID = -3986962045888469053L;
	private String parkName;
	private Date date;
	private Time time;
	private int travelerID;
	private int orderID;

	/**
	 * this is the queue constructor, it uses a parkName, date, time, traveler id
	 * and orderID, to initallize a queue.
	 * 
	 * @param parkName   this is the parks name
	 * @param date       this is the desired date of the order
	 * @param time       this is the desired date of the time order
	 * @param travelerID this is the travelerID of
	 * @param orderID    this is the orders given ID.
	 */
	public Queue(String parkName, Date date, Time time, int travelerID, int orderID) {
		super();
		this.parkName = parkName;
		this.date = date;
		this.time = time;
		this.travelerID = travelerID;
		this.orderID = orderID;
	}

	@Override
	public String toString() {
		return "Queue [parkName=" + parkName + ", date=" + date + ", time=" + time + ", travelerID=" + travelerID
				+ ", orderID=" + orderID + "]";
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
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

	public int getTravelerID() {
		return travelerID;
	}

	public void setTravelerID(int travelerID) {
		this.travelerID = travelerID;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

}
