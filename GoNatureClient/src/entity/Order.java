package entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import enums.OrderStatus;
import enums.OrderType;

/**
 * The Entity that represents a given Order in our DB. we use this entity to
 * help us transfer the order information from the CLIENT to the SERVER and
 * back.
 * 
 * @author Group 20
 * @version 1.0
 */
public class Order implements Serializable {
	/**
	 * @param orderID represents the ID given to an order.
	 * 
	 * @param travlerID      represents the ID of the traveler that created the
	 *                       order.
	 * @param orderType      represents the type of order the client has chosen e.g
	 *                       (personal/family etc.)
	 * @param park           represents the parks desired park in the order. (its
	 *                       name)
	 * @param orderDate      specifies the given order desired date.
	 * @param orderTime      specifies the given order desired time.
	 * @param numOfTravelers specifies the amount of travelers in the order.
	 * @param travelerEmail  specifies the email address of the client that creates
	 *                       the order.
	 * @param orderStatus    specifies the current status of the order
	 *                       (pending/approved/expired/cancelled).
	 * @param totalSum       specifies the order total fee.
	 * @param timeOfStay     specifies the time of stay in the park.
	 */

	private static final long serialVersionUID = 5041858524549940544L;
	private int orderID;
	private int travlerID;
	private OrderType orderType;
	private String park;
	private Date orderDate;
	private Time orderTime;
	private int numOfTravelers;
	private String travelerEmail;
	private OrderStatus orderStatus;
	private double totalSum;
	private int timeOfStay;


	public Order(int orderID, int travlerID, OrderType orderType, String park, java.util.Date date, Time orderTime,
			int numOfTravelers, String travelerEmail, OrderStatus orderStatus, int totalSum, int timeOfStay) {
		super();
		this.orderID = orderID;
		this.travlerID = travlerID;
		this.orderType = orderType;
		this.park = park;
		this.orderDate = (Date) date;
		this.orderTime = orderTime;
		this.numOfTravelers = numOfTravelers;
		this.travelerEmail = travelerEmail;
		this.orderStatus = orderStatus;
		this.totalSum = totalSum;
		this.timeOfStay = timeOfStay;
	}


	public Order(int travlerID, OrderType orderType, String park, java.util.Date date, Time orderTime,
			int numOfTravelers, String travelerEmail, OrderStatus orderStatus, int timeOfStay) {
		this.travlerID = travlerID;
		this.orderType = orderType;
		this.park = park;
		this.orderDate = (Date) date;
		this.orderTime = orderTime;
		this.numOfTravelers = numOfTravelers;
		this.travelerEmail = travelerEmail;
		this.orderStatus = orderStatus;
		this.timeOfStay = timeOfStay;
	}

	
	public Order(int orderID, OrderStatus status) {
		this.orderID = orderID;
		this.orderStatus = status;
	}

	
	public Order(Order order) {
		this.orderID = order.getOrderID();
		this.travlerID = order.getTravlerID();
		this.orderType = order.getOrderType();
		this.park = order.getPark();
		this.orderDate = order.getOrderDate();
		this.orderTime = order.getOrderTime();
		this.numOfTravelers = order.getNumOfTravelers();
		this.travelerEmail = order.getTravelerEmail();
		this.orderStatus = order.getOrderStatus();
		this.totalSum = order.getTotalSum();
		this.timeOfStay = order.getTimeOfStay();
	}

	public Order(String parkName, java.util.Date date, Time orderTime) {
		this.park = parkName;
		this.orderDate = (Date) date;
		this.orderTime = orderTime;
	}

	
	public Order(OrderType orderType, String park, Date date, Time orderTime, int numOfTravelers,
			OrderStatus orderStatus, int timeOfStay) {
		this.orderType = orderType;
		this.park = park;
		this.orderDate = (Date) date;
		this.orderTime = orderTime;
		this.numOfTravelers = numOfTravelers;
		this.orderStatus = orderStatus;
		this.timeOfStay = timeOfStay;
	}

	public Order(OrderType orderType, String park, Date orderDate, Time orderTime, int numOfTravelers,
			OrderStatus orderStatus, double totalSum) {
		super();
		this.orderType = orderType;
		this.park = park;
		this.orderDate = orderDate;
		this.orderTime = orderTime;
		this.numOfTravelers = numOfTravelers;
		this.orderStatus = orderStatus;
		this.totalSum = totalSum;
	}

	public int getTimeOfStay() {
		return timeOfStay;
	}

	public void setTimeOfStay(int timeOfStay) {
		this.timeOfStay = timeOfStay;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getTravlerID() {
		return travlerID;
	}

	public void setTravlerID(int travlerID) {
		this.travlerID = travlerID;
	}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}

	public String getPark() {
		return park;
	}

	public void setPark(String park) {
		this.park = park;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Time getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Time orderTime) {
		this.orderTime = orderTime;
	}

	public int getNumOfTravelers() {
		return numOfTravelers;
	}

	public void setNumOfTravelers(int numOfTravelers) {
		this.numOfTravelers = numOfTravelers;
	}

	public String getTravelerEmail() {
		return travelerEmail;
	}

	public void setTravelerEmail(String travelerEmail) {
		this.travelerEmail = travelerEmail;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public double getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(double d) {
		this.totalSum = d;
	}

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", travlerID=" + travlerID + ", orderType=" + orderType + ", park=" + park
				+ ", orderDate=" + orderDate + ", orderTime=" + orderTime + ", numOfTravelers=" + numOfTravelers
				+ ", travelerEmail=" + travelerEmail + ", orderStatus=" + orderStatus + ", totalSum=" + totalSum
				+ ", timeOfStay=" + timeOfStay + "]";
	}

}
