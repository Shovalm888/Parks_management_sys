package entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * this class helps us define an event in the park, with this class we can
 * save/get information regarding events held in the park.
 * 
 * @author Group 20
 * @author version 1.0
 *
 */
public class Event implements Serializable {
	/**
	 * @param eventNumber this is the number of the event
	 * 
	 * @param discountNumber this is the discount number given during the event
	 * @param start          this is the starting date of the event
	 * @param end            this is the ending date of the event
	 * @param park           this is the park where the event takes place.
	 */
	private static final long serialVersionUID = -6608928202156391594L;
	private int eventNumber;
	private int discountNumber;
	private Date start;
	private Date end;
	private String park;

	/**
	 * this is the Event constructor, it initiallizes an event with a given event
	 * number, discount number, start date, end date and designated park.
	 * 
	 * @param eventNumber    this is the number of the event
	 * @param discountNumber this is the discount given during the event
	 * @param start          this is the starting date of the event
	 * @param end            this is the end date of the event
	 * @param park           this is the designated park
	 */
	public Event(int eventNumber, int discountNumber, Date start, Date end, String park) {
		super();
		this.eventNumber = eventNumber;
		this.discountNumber = discountNumber;
		this.start = start;
		this.end = end;
		this.park = park;
	}

	/**
	 * Another Event constructor using only discount number, start date , end date
	 * and designated park
	 * 
	 * @param discountNumber this is the discount number given during the event
	 * @param start          this is the start date of the event
	 * @param end            this is the end date of the event
	 * @param park           this is the deisgnated park of the event
	 */
	public Event(int discountNumber, Date start, Date end, String park) {
		super();
		this.discountNumber = discountNumber;
		this.start = start;
		this.end = end;
		this.park = park;
	}

	public int getEventNumber() {
		return eventNumber;
	}

	public void setEventNumber(int eventNumber) {
		this.eventNumber = eventNumber;
	}

	public int getDiscountNumber() {
		return discountNumber;
	}

	public void setDiscountNumber(int discountNumber) {
		this.discountNumber = discountNumber;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getPark() {
		return park;
	}

	public void setPark(String park) {
		this.park = park;
	}

	@Override
	public String toString() {
		return "Event [eventNumber=" + eventNumber + ", discountNumber=" + discountNumber + ", start=" + start
				+ ", end=" + end + ", park=" + park + "]";
	}
}