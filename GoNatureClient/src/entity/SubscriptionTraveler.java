package entity;

import java.io.Serializable;

import enums.TravelerType;

/**
 * this class describes features of subscription to the park, this is a type of
 * traveler that has made a subscription to our system.
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class SubscriptionTraveler extends Traveler implements Serializable {

	/**
	 *
	 * @param subscriptionID     this is a unique ID given to a traveler once he
	 *                           makes a subscription to our system
	 * @param creditCardNumber   this is the credit card number used to pay for
	 *                           future park visits.
	 * @param totalFamilyMembers this is the amount of family members of the
	 *                           subscriber.
	 *
	 */
	private static final long serialVersionUID = 1L;

	private int subscriptionID;
	private String creditCardNumber;
	private int totalFamilyMembers;

	public int getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(int subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public int getTotalFamilyMembers() {
		return totalFamilyMembers;
	}

	public void setTotalFamilyMembers(int totalFamilyMembers) {
		this.totalFamilyMembers = totalFamilyMembers;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SubscriptionTraveler [subscriptionID=" + subscriptionID + ", creditCardNumber=" + creditCardNumber
				+ ", totalFamilyMembers=" + totalFamilyMembers + "]";
	}

	/**
	 * this constructor initiates a traveler that is of type subscriber.
	 * 
	 * @param subscriptionID     this is used to save the travelers subscriptionID
	 * @param ID                 this is used to save the travelers ID
	 * @param firstName          this is the travelers Name
	 * @param surname            this is the travelers surname
	 * @param email              this is the travelers email
	 * @param tel                this is the travelers phone number.
	 * @param travelerType       this is the type of traveler.
	 * @param creditCardNumber   this is used to save the travelers creditCard
	 *                           number.
	 * @param totalFamilyMembers this is used to save the amount of family members
	 *                           the traveler has.
	 */
	public SubscriptionTraveler(int subscriptionID, int ID, String firstName, String surname, String email, String tel,
			TravelerType travelerType, String creditCardNumber, int totalFamilyMembers) {
		super(ID, firstName, surname, email, tel, travelerType);
		this.subscriptionID = subscriptionID;
		this.creditCardNumber = creditCardNumber;
		this.totalFamilyMembers = totalFamilyMembers;
	}

	/**
	 * this constructor initiates a traveler that is of type subscriber.
	 * 
	 * @param ID                 this is used to save the travelers ID
	 * @param firstName          this is the travelers Name
	 * @param surname            this is the travelers surname
	 * @param email              this is the travelers email
	 * @param tel                this is the travelers phone number.
	 * @param travelerType       this is the type of traveler.
	 * @param creditCardNumber   this is used to save the travelers creditCard
	 *                           number.
	 * @param totalFamilyMembers this is used to save the amount of family members
	 *                           the traveler has.
	 */
	public SubscriptionTraveler(int ID, String firstName, String surname, String email, String tel,
			TravelerType travelerType, String creditCardNumber, int totalFamilyMembers) {

		super(ID, firstName, surname, email, tel, travelerType);
		this.creditCardNumber = creditCardNumber;
		this.totalFamilyMembers = totalFamilyMembers;
	}

}
