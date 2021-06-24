package entity;

import java.io.Serializable;

/**
 * this class describes our park.
 * 
 * @author group 20
 * @version 1.0
 * 
 */
public class Park implements Serializable {
	/**
	 * * @param parkID This is our unique park ID.
	 * 
	 * @param parkName                  this is our park name
	 * @param currentParkCapacity       this parameter holds the current amount of
	 *                                  visitors in the park.
	 * @param maxParkCapacity           this parameter holds how many visitors are
	 *                                  allowed in the park.
	 * @param travelerTimeOfStayInHours this is the time that holds the amount of
	 *                                  hours a visitor is scheduled to have in the
	 *                                  park.
	 * @param isFull                    is a boolean parameter to determine if the
	 *                                  park is currently full or not.
	 * @param gap                       the unique parameter gap as described in the
	 *                                  user story.
	 */
	private static final long serialVersionUID = 7592916600484009121L;

	private int parkID;
	private String parkName;
	private int currentParkCapacity;
	private int maxParkCapacity;
	private int travelerTimeOfStayInHours;
	private boolean isFull;
	private int gap;

	/**
	 * this is a constructor for a park, it initiates the park fields so our system
	 * will be able to work with the given park.
	 * 
	 * @param parkID                    this is the parkID that will be given to the
	 *                                  park
	 * @param parkName                  this is the park name that will be given to
	 *                                  the park
	 * @param currentParkCapacity       this is the current amount of people in the
	 *                                  park
	 * @param maxParkCapacity           this is the limit of allowed people inside
	 *                                  the park
	 * @param travelerTimeOfStayInHours this determines the travelers time inside
	 *                                  the park
	 * @param isFull                    this is to determine if the park is
	 *                                  currently full.
	 * @param gap                       the parameter gap.
	 */
	public Park(int parkID, String parkName, int currentParkCapacity, int maxParkCapacity,
			int travelerTimeOfStayInHours, boolean isFull, int gap) {
		super();
		this.parkID = parkID;
		this.parkName = parkName;
		this.currentParkCapacity = currentParkCapacity;
		this.maxParkCapacity = maxParkCapacity;
		this.travelerTimeOfStayInHours = travelerTimeOfStayInHours;
		this.isFull = isFull;
		this.gap = gap;
	}

	/**
	 * park constructor using a park name, current capacity, max capacity and gap.
	 * 
	 * @param parkName            the given park name
	 * @param currentParkCapacity the current park capacity
	 * @param maxParkCapacity     the max park capacity
	 * @param gap                 the gap.
	 */
	public Park(String parkName, int currentParkCapacity, int maxParkCapacity, int gap) {
		this.parkName = parkName;
		this.currentParkCapacity = currentParkCapacity;
		this.maxParkCapacity = maxParkCapacity;
		this.gap = gap;
	}

	/**
	 * park constructor using park name current capacity and max capacity
	 * 
	 * @param parkName            the park name
	 * @param currentParkCapacity the current park capacity
	 * @param maxParkCapacity     the max park capacity
	 */
	public Park(String parkName, int currentParkCapacity, int maxParkCapacity) {
		this.parkName = parkName;
		this.currentParkCapacity = currentParkCapacity;
		this.maxParkCapacity = maxParkCapacity;
	}

	public int getGap() {
		return gap;
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

	public int getParkID() {
		return parkID;
	}

	public void setParkID(int parkID) {
		this.parkID = parkID;
	}

	public String getParkName() {
		return parkName;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public int getCurrentParkCapacity() {
		return currentParkCapacity;
	}

	public void setCurrentParkCapacity(int currentParkCapacity) {
		this.currentParkCapacity = currentParkCapacity;
	}

	public int getMaxParkCapacity() {
		return maxParkCapacity;
	}

	public void setMaxParkCapacity(int maxParkCapacity) {
		this.maxParkCapacity = maxParkCapacity;
	}

	public int getTravelerTimeOfStayInHours() {
		return travelerTimeOfStayInHours;
	}

	public void setTravelerTimeOfStayInHours(int travelerTimeOfStayInHours) {
		this.travelerTimeOfStayInHours = travelerTimeOfStayInHours;
	}

	public boolean isFull() {
		return isFull;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	@Override
	public String toString() {
		return "Park [parkID=" + parkID + ", parkName=" + parkName + ", currentParkCapacity=" + currentParkCapacity
				+ ", maxParkCapacity=" + maxParkCapacity + ", travelerTimeOfStayInHours=" + travelerTimeOfStayInHours
				+ ", isFull=" + isFull + ", gap=" + gap + "]";
	}

}
