package entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import enums.TravelerMessageType;

/**
 * this class describes messages our traveler can receieve in the system
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class TravelerMessage implements Serializable {
	/**
	 * 
	 * 
	 * @param messageID           this is the ID of the message.
	 * @param travelerID          this is the ID of the traveler
	 * @param orderID             this is the ID of the given order
	 * @param messageInfo         this is the body of the message.
	 * @param TravelerMessageType this is the type of message the traveler received
	 *                            in the system.
	 * @param date                this is the date in which the message has been
	 *                            received.
	 * @param time                this is the time in which the message has been
	 *                            received.
	 */

	private static final long serialVersionUID = 409601982191142131L;
	private int messageID;
	private int travelerID;
	private int orderID;
	private String messageInfo;
	private TravelerMessageType messageType;
	private Date date;
	private Time time;

	public int getMessageID() {
		return messageID;
	}

	public void setMessageID(int messageID) {
		this.messageID = messageID;
	}

	public int getTravelerID() {
		return travelerID;
	}

	public void setTravelerID(int travelerID) {
		this.travelerID = travelerID;
	}

	public String getMessageInfo() {
		return messageInfo;
	}

	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}

	public TravelerMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(TravelerMessageType messageType) {
		this.messageType = messageType;
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
		return "TravelerMessage [messageID=" + messageID + ", travelerID=" + travelerID + ", orderID=" + orderID
				+ ", messageInfo=" + messageInfo + ", messageType=" + messageType + ", date=" + date + ", time=" + time
				+ "]";
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	/**
	 * this is a constructor to save the information on a given message in our
	 * system.
	 * 
	 * @param messageID   this is the ID given to the message
	 * @param travelerID  this is the ID of the traveler
	 * @param orderID     this is the orderID for the message
	 * @param messageInfo this is the body of the message
	 * @param messageType this is the type of the message
	 * @param date        this is the date of the message
	 * @param time        this is the time of the message
	 */
	public TravelerMessage(int messageID, int travelerID, int orderID, String messageInfo,
			TravelerMessageType messageType, Date date, Time time) {
		super();
		this.messageID = messageID;
		this.travelerID = travelerID;
		this.orderID = orderID;
		this.messageInfo = messageInfo;
		this.messageType = messageType;
		this.date = date;
		this.time = time;
	}

}
