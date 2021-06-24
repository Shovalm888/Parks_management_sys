package entity;

import java.time.LocalDate;

import server.GoNatureServer;
import server.SystemControllerServer;

/**
 * this class operates a thread that is constantly ran while the server is on,
 * and is taking care of messages in our system, update etc.
 * 
 * @version 1.0
 * @author group 20
 *
 */

public class SystemMessageControllerThread implements Runnable {

	/**
	 * this is the method that is constantly ran while the server is on, and it
	 * calls diffrent methods to take care of messages in our system.
	 * 
	 */
	public void run() {
		while (GoNatureServer.runThreads) {
			LocalDate localDate = LocalDate.now();
			System.out.println("Message THREAD started working");
			try {
				localDate = localDate.plusDays(1);
				SystemControllerServer.getRelevantDatesToMessage(localDate);
				localDate = localDate.minusDays(1);
				SystemControllerServer.deleteUnAprrovedMessages(localDate);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Message THREAD finished working");
			try {
				Thread.sleep(1000 * 1800); // original sleep time.

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
