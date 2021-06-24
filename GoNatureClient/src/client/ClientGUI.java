// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package client;
import java.io.*;

import common.AppTestIF;

/**
 * this class Initialize the GoNatureClient with the corresponding port,host
 * data, and establish a connection between client and server.
 * 
 * @author group 20
 * @version 1.0
 *
 */
public class ClientGUI implements AppTestIF // for display method.
{
	public static int DEFAULT_PORT;
	GoNatureClient client;// create our project client class.

  /**
   * this is the constructor.
   * this constructor Initialize the client with host,post data.
   * @param host host 
   * @param port port 
   */
  public ClientGUI(String host, int port) 
  {
    try 
    {
      client= new GoNatureClient(host, port, this);
    } 
    catch(IOException exception) 
    {
    	
      System.out.println("Error: Can't setup connection!"+ " Terminating client.");
      exception.printStackTrace();
      System.exit(1);
    }
  }

  /**
   * this method receive object from clientUI and sends the object to the server.
   * @param str str
   */
  public void accept(Object str) 
  {
	  client.handleMessageFromClientUI(str);
  }
  
 /**
  * this method print the message she receives.
  * @param message the message we receives.
  */
  public void display(String message) 
  {
    System.out.println("> " + message);
  }
}
