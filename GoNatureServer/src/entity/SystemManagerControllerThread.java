package entity;

import server.GoNatureServer;
import server.SystemControllerServer;

/**
 * this class constantly runs and updates in real-time the amount of people
 * inside the park and finds cancelled orders and changes their status to
 * expired.
 * 
 * @author group 20
 * @version 1.0
 */

public class SystemManagerControllerThread implements Runnable {
	/**
	 * this method is constantly working to update the amount of people inside the
	 * park/update cancelled orders to expired.
	 */

	public void run() {
		while (GoNatureServer.runThreads) {
			try {
				SystemControllerServer.UpdateCurrentlyTravelersAtThePark();
				SystemControllerServer.findExpiredOrders();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
