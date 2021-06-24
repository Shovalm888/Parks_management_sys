package entity;

import java.io.Serializable;

import enums.TravelerType;

/**
 * this class describes an employee of the park which has all the details of a
 * traveler, but also features specific to the employee which in this case is a
 * guide.
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class ParkGuide extends Traveler implements Serializable {

	private static final long serialVersionUID = 1L;


	private int guideID;

	/**
	 * 
	 * @param guideID      this is a unique id given to the employee.
	 * @param ID           is the ID of the employee
	 * @param firstName    is the Name of the employee
	 * @param surname      is the surname of the employee
	 * @param email        is the email of the employee
	 * @param tel          is the phone number of the employee
	 * @param travelerType is the travelerType of the employee
	 */
	public ParkGuide(int ID, String firstName, String surname, String email, String tel, TravelerType travelerType,
			int guideID) {
		super(ID, firstName, surname, email, tel, travelerType);
		this.guideID = guideID;
	}

	public int getGuideID() {
		return guideID;
	}

	public void setGuideID(int guideID) {
		this.guideID = guideID;
	}

}
