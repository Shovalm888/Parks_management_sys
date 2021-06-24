package entity;

import java.io.Serializable;
import enums.Commands;

/**
 * 
 * The Entity Message - this class represents everything we need from a message
 * in our system.
 * 
 * @author Group 20
 * @version 1.0
 * 
 */
public class Message implements Serializable {
	/**
	 * @param obj describes what object were working with for our communication
	 * with the client-server, can be order etc.
	 * 
	 * @param cmd this is the command we wish to execute
	 */

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2104589928804663372L;

	/** The obj. */
	private Object obj;

	/** The cmd. */
	private Commands cmd;

	/**
	 * message constructor, here we initialize a new message.
	 *
	 * @param obj initiate the object were working with
	 * @param cmd initate the command we want to execute
	 */
	public Message(Object obj, Commands cmd) {
		this.obj = obj;
		this.cmd = cmd;
	}

	/**
	 * Gets the obj.
	 *
	 * @return the obj
	 */
	public Object getObj() {
		return obj;
	}

	/**
	 * Gets the cmd.
	 *
	 * @return the cmd
	 */
	public Commands getCmd() {
		return cmd;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "" + cmd + obj;
	}

}
