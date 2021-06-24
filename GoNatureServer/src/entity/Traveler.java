package entity;

import java.io.Serializable;
import enums.TravelerType;

/**
 * this class describes a traveler and all of its features saved in our system.
 * 
 * @version 1.0
 * @author group 20
 *
 */
public class Traveler implements Serializable {
	/**
	 * @param ID           this is the ID given to the traveler
	 * 
	 * @param firstName    this is the first name of the traveler
	 * @param surename     this is the Surname of the traveler
	 * @param TravelerType this holds the type of the Traveler(subscriber,employee
	 *                     etc.)
	 * @param email        this is the email of the traveler.
	 * @param tel          this is the phone number of the traveler.
	 */
	private static final long serialVersionUID = 1L;
	private int ID;
	private String firstName;
	private String surename;
	private TravelerType travelerType;
	private String email;
	private String tel;

	/**
	 * this is a constructor that saves the information our system holds for the
	 * traveler.
	 * 
	 * @param ID           this is the ID of the given traveler.
	 * @param firstName    this is the first name of the traveler
	 * @param surname      this is the surname of the traveler
	 * @param email        this is the email of the traveler
	 * @param tel          this is the phone number of the traveler
	 * @param travelerType this is the Type of traveler saved in our system.
	 */
	public Traveler(int ID, String firstName, String surname, String email, String tel, TravelerType travelerType) {
		super();
		this.ID = ID;
		this.firstName = firstName;
		this.surename = surname;
		this.email = email;
		this.tel = tel;
		this.travelerType = travelerType;
	}

	public void setTravelerType(TravelerType travelerType) {
		this.travelerType = travelerType;
	}

	public TravelerType getTravelerType() {
		return travelerType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surename;
	}

	public void setSurname(String surname) {
		surename = surname;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Override
	public String toString() {
		return "Traveler [ID=" + ID + ", firstName=" + firstName + ", surename=" + surename + ", travelerType="
				+ travelerType + ", email=" + email + ", tel=" + tel + "]";
	}

}
