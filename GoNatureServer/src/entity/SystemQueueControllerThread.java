package entity;

import java.time.LocalTime;
import server.GoNatureServer;
import server.SystemControllerServer;

/**
 * this class represents the thread that is constantly ran while the server is
 * online, to take care of the diffrent queues we have in our system
 * 
 * @author group 20
 * @version 1.0
 */
public class SystemQueueControllerThread implements Runnable {

	/**
	 * this is the method that operates the thread and makes it constantly work
	 * while the server is running, the thread here calls diffrent method that
	 * operates on queues and takes care of relevant issues such as updating orders
	 * etc.
	 * 
	 */
	public void run() {
		while (GoNatureServer.runThreads) {
			LocalTime localTime = LocalTime.now();
			try {
				SystemControllerServer.checkWaitingList();
				if (localTime.getHour() == 23 && localTime.getMinute() == 30) {
					SystemControllerServer.deleteUnnecessaryWaitingList();
				}
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
