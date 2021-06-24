
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * The Class ConnectionToDB connects the java to database (mySql database).
 */
public class ConnectionToDB {

	/** The conn. */
	public static Connection conn;

	/**
	 * Connect to DB.
	 *
	 * @param schema of user
	 * @param passWord of user
	 */
	public static void connectToDB(String schema, String passWord) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			//ServerController.writeToServerConsole("Driver definition succeed");
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			//ServerController.writeToServerConsole("Driver definition failed");
			System.out.println("Driver definition failed");
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/" + schema + "?serverTimezone=CAT", "root",
					passWord);

			//ServerController.writeToServerConsole("SQL connection succeed");
			System.out.println("SQL connection succeed");
		} catch (SQLException e) {/* handle any errors */
			e.printStackTrace();
		}

	}

}

