package enums;

/**
 * * in this class, we list the different status in our system so we can
 * distinguish between different status of orders in the system.
 * 
 * @author group 20
 * @version 1.0
 * 
 *
 */
public enum OrderStatus {
	
	Pending, Approved, DisApproved, Expired, Cancelled, AtWaitingList, Done, // means traveler finished visiting
																				// successfully.
	CurrentlyAtThePark, WaitingForConfirmation;
}
