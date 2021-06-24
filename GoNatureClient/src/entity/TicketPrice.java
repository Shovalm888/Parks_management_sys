package entity;

import java.io.Serializable;

import enums.OrderType;

/**
 * we use this class to describe and save information in regards to a tickets
 * price.
 * 
 * @version 1.0
 * @author group 20
 *
 */
public class TicketPrice implements Serializable {
	/**
	 * @param ticketPrice                this is the ticket price
	 * @param totalTicketPrice           this is the total sum of price needed to
	 *                                   pay for ticket.
	 * @param single                     this is a single ticket price
	 * @param singleSubscriber           this is a single subscriber ticket price
	 * @param casualSingle               this is a ticket price for a Casual Single
	 *                                   type of travaler
	 * @param casualSingleSubscriber     this is a ticket price for a Casual single
	 *                                   travaler who is also a subscriber in the
	 *                                   system.
	 * @param family                     this is a ticket price for a family type
	 *                                   traveler
	 * @param casualFamily               this is a ticket price for a casual family
	 *                                   type traveler
	 * @param privateGroup               this is a ticket price for a private group
	 * @param OrganizedGroup             this is a ticket price for an organized
	 *                                   Group
	 * @param prePaymentgroup            this is a ticket price for a prepaid group
	 * @param prePaymentOrganizedGroup   this is a ticket price for a pre-paid
	 *                                   organized group
	 * @param casualGroup                this is a ticket price for a casual group
	 * @param casualOrganizedGroup       this is a ticket price for a casual
	 *                                   organized group.
	 * @param casualFamilyNoSubscription this is a ticket price for a casual family
	 *                                   with no subscription.
	 */
	private static final long serialVersionUID = -4556782636399686738L;
	private static TicketPrice ticketPrice = null;
	private double totalTicketPrice;
	private static double single;
	private static double singleSubscriber;
	private static double casualSingle;
	private static double casualSingleSubscriber;
	private static double family;
	private static double casualFamily;
	private static double privateGroup;
	private static double Organizedgroup;
	private static double PrePaymentgroup;
	private static double PrePaymentOrganizedGroup;
	private static double casualGroup;
	private static double casualOrganizedGroup;
	private static double casualFamilyNoSubscription;

	/**
	 * this constructor initiates the amount that needs to be paid for a given
	 * ticket according to our system.
	 * 
	 * @param totalTicketPrice this is how much the ticket originally costs.
	 */
	private TicketPrice(double totalTicketPrice) {
		this.totalTicketPrice = totalTicketPrice;
		single = totalTicketPrice * 0.85;
		singleSubscriber = totalTicketPrice * 0.85 * 0.8;
		casualSingle = totalTicketPrice;
		casualSingleSubscriber = totalTicketPrice * 0.8;
		family = totalTicketPrice * 0.85 * 0.8;
		casualFamily = totalTicketPrice * 0.8;
		privateGroup = totalTicketPrice * 0.75; // pay at the park.
		Organizedgroup = privateGroup;
		PrePaymentgroup = privateGroup * 0.88; // 0.75 * 0.88
		PrePaymentOrganizedGroup = PrePaymentgroup;
		casualGroup = totalTicketPrice * 0.9;
		casualOrganizedGroup = casualGroup;
		casualFamilyNoSubscription = casualSingle;
	}

	/**
	 * this method returns an instance of a given tickets price.
	 * 
	 * @param price this is the ticket we want to get the price for.
	 * @return the givens ticket price.
	 */
	public static TicketPrice getInstance(double price) {
		if (TicketPrice.ticketPrice == null) {
			ticketPrice = new TicketPrice(price);
			return ticketPrice;
		} else
			return ticketPrice;
	}

	/**
	 * this method calculates total price for a given order.
	 * 
	 * @param tempOrder this is the order we wish to calculate the price on.
	 * @return total price for the given order.
	 */
	public static double calculateTotalPrice(Order tempOrder) {
		double realPrice = 0;
		OrderType orderType = tempOrder.getOrderType();
		switch (orderType) {

		case Single:
			realPrice = single * tempOrder.getNumOfTravelers();
			break;

		case SingleSubscription:
			realPrice = singleSubscriber * tempOrder.getNumOfTravelers();
			break;

		case CasualSingle:
			realPrice = casualSingle * tempOrder.getNumOfTravelers();
			break;

		case FamilySubscription:

			realPrice = family * tempOrder.getNumOfTravelers();
			break;

		case CasualFamily:
			realPrice = casualFamily * tempOrder.getNumOfTravelers();
			break;

		case PrivateGroup:
			realPrice = privateGroup * tempOrder.getNumOfTravelers();
			break;

		case OrganizedGroup:
			if (tempOrder.getNumOfTravelers() == 1) {
				realPrice = Organizedgroup;
				return realPrice;
			}
			realPrice = Organizedgroup * (tempOrder.getNumOfTravelers() - 1);
			break;

		case PrePaymentGroup:
			realPrice = PrePaymentgroup * tempOrder.getNumOfTravelers();
			break;

		case PrePaymentOrganizedGroup:
			if (tempOrder.getNumOfTravelers() == 1) {
				realPrice = PrePaymentOrganizedGroup;
				return realPrice;
			}
			realPrice = PrePaymentOrganizedGroup * (tempOrder.getNumOfTravelers() - 1);
			break;

		case CasualGroup:
			realPrice = casualGroup * tempOrder.getNumOfTravelers();
			break;

		case CasualOrganizedGroup:
			realPrice = casualOrganizedGroup * tempOrder.getNumOfTravelers();
			break;

		case CasualSingleSubscription:
			realPrice = casualSingleSubscriber * tempOrder.getNumOfTravelers();
			break;

		case CasualFamilyNoSubscription:
			realPrice = casualFamilyNoSubscription * tempOrder.getNumOfTravelers();
			break;
		default:
			System.out.println("error in getting ticket price correctly.");
			break;
		}
		return realPrice;
	}

	public static double getCasualFamilyNoSubscription() {
		return casualFamilyNoSubscription;
	}

	public static void setCasualFamilyNoSubscription(double casualFamilyNoSubscription) {
		TicketPrice.casualFamilyNoSubscription = casualFamilyNoSubscription;
	}

	public double getTotalTicketPrice() {
		return totalTicketPrice;
	}

	public double getSingle() {
		return single;
	}

	public double getSingleSubscriber() {
		return singleSubscriber;
	}

	public double getCasualSingle() {
		return casualSingle;
	}

	public double getCasualSingleSubscriber() {
		return casualSingleSubscriber;
	}

	public double getFamily() {
		return family;
	}

	public double getCasualFamily() {
		return casualFamily;
	}

	public double getPrivateGroup() {
		return privateGroup;
	}

	public double getOrganizedgroup() {
		return Organizedgroup;
	}

	public double getPrePaymentgroup() {
		return PrePaymentgroup;
	}

	public double getPrePaymentOrganizedGroup() {
		return PrePaymentOrganizedGroup;
	}

	public double getCasualGroup() {
		return casualGroup;
	}

	public double getCasualOrganizedGroup() {
		return casualOrganizedGroup;
	}

}
