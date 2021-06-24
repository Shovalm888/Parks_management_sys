package entity;

import java.io.Serializable;

import enums.WorkerType;

/**
 * the class ParkWorker describes a worker in the park, here we save all of the
 * information we need on the worker. * @author Daniel
 * 
 * 
 * @version 1.0
 * @author group 20
 */
public class ParkWorker implements Serializable {

	/**
	 * 
	 * @param workerName     this is the name of the worker
	 * @param workerLastName this is the surname of the worker
	 * @param workerID       this is the given id to the worker.
	 * @param workerPassword this is the given password to the worker
	 * @param email          this is the workers email
	 * @param workerType     this is the type of worker
	 * @param park           this is the designated park of the worker.
	 */
	private static final long serialVersionUID = -7007937190806469577L;
	private String workerName;
	private String workerLastName;
	private int workerID;
	private int workerPassword;
	private String email;
	private WorkerType workerType;
	private String park;

	public String getPark() {
		return park;
	}

	public void setPark(String park) {
		this.park = park;
	}

	/**
	 * this is the park worker constructor, it uses a worker
	 * name/lastname/workerID/workerPassword/Email/Type/park to initiallize a
	 * worker.
	 * 
	 * @param workerName     this is the worker name
	 * @param workerLastName this is the worker surname
	 * @param workerID       this is the workerID
	 * @param workerPassword this is the workerPassword
	 * @param email          this is the workers email
	 * @param workerType     this is the workers Type
	 * @param park           this is the designated worker park
	 */
	public ParkWorker(String workerName, String workerLastName, int workerID, int workerPassword, String email,
			WorkerType workerType, String park) {
		super();
		this.workerName = workerName;
		this.workerLastName = workerLastName;
		this.workerID = workerID;
		this.workerPassword = workerPassword;
		this.email = email;
		this.workerType = workerType;
		this.park = park;
	}

	/**
	 * this constructor uses a given parkworker to initallize a new worker.
	 * 
	 * @param worker from this we can get all the relevant information we need to
	 *               create the worker.
	 */
	public ParkWorker(ParkWorker worker) {
		this.workerName = worker.getWorkerName();
		this.workerLastName = worker.getWorkerLastName();
		this.workerID = worker.getWorkerID();
		this.workerPassword = worker.getWorkerPassword();
		this.email = worker.getEmail();
		this.workerType = worker.getWorkerType();
		this.park = worker.getPark();
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public String getWorkerLastName() {
		return workerLastName;
	}

	public void setWorkerLastName(String workerLastName) {
		this.workerLastName = workerLastName;
	}

	public int getWorkerID() {
		return workerID;
	}

	public void setWorkerID(int workerID) {
		this.workerID = workerID;
	}

	public int getWorkerPassword() {
		return workerPassword;
	}

	public void setWorkerPassword(int workerPassword) {
		this.workerPassword = workerPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public WorkerType getWorkerType() {
		return workerType;
	}

	public void setWorkerType(WorkerType workerType) {
		this.workerType = workerType;
	}

	@Override
	public String toString() {
		return "ParkWorker [workerName=" + workerName + ", workerLastName=" + workerLastName + ", workerID=" + workerID
				+ ", workerPassword=" + workerPassword + ", email=" + email + ", workerType=" + workerType + ", park="
				+ park + "]";
	}

}
