/*
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
*/
/* ----------------------------------------------------------------------------
 * $Header: /src/voyager/com/volantis/mcs/cli/MarinerSocketServer.java,v 1.2 2002/03/18 12:41:13 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Jan-02    Adrian          VBM:2002010201 - Created this class to enable
 *                              remote viewing of volantis log over a socket.
 *                              It should be run using simpleSocket.sh with the
 *                              parameters of port and configuration file.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.cli;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.util.StringTokenizer;

import org.apache.log4j.Category;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.log4j.net.SocketNode;

/**
 * A simple org.apache.log4j.set.SocketNode based server for remotely viewing
 * the volantis log file.
 * Usage: java com.volantis.mcs.cli.MarinerSocketServer port configFile
 * where port is a port number where the server listens and
 * configFile is an xml log4j configuration file.
 * @author Adrian Bosworth
 */
public class MarinerSocketServer  {

  private static String mark = "(c) Volantis Systems Ltd 2000.";

  /**
   * The log4j object to log to.
   */
  static Category cat = Category.
    getInstance(MarinerSocketServer.class);

  /**
   * The port to listen for logging. Defaults to 4445.
   */
  static int port = 4445;
  
  
  public static void main(String args[]) {
    if (args.length > 2) {
      usage("Wrong number of arguments.");
    }
    
    // get configFile and port from args
    String configFile = null;
    String portStr = null;
    StringTokenizer st = null;
    for(int i=0;i<args.length;i++) {
      String arg = args[i].toLowerCase();
      if(arg.startsWith("config")) {
	st = new StringTokenizer(args[i],":");
	st.nextToken();
	if(st.hasMoreTokens()) {
	  configFile = st.nextToken();
	}
      }
      if(arg.startsWith("port")) {
	st = new StringTokenizer(args[i],":");
	st.nextToken();
	if(st.hasMoreTokens()) {
	  try {
	    portStr = st.nextToken();
	    port = Integer.parseInt(portStr);      
	  }
	  catch(java.lang.NumberFormatException e) {
	    e.printStackTrace();
	    usage("Could not interpret port number ["+ portStr +"].");
	  }
	}
      }
    }
      
    loadConfiguration(configFile);
    
    try {
      cat.info("Listening on port " + port);
      ServerSocket serverSocket = new ServerSocket(port);
      while(true) {
	cat.info("Waiting to accept a new client.");
	Socket socket = serverSocket.accept();
	cat.info("Connected to client at " + socket.getInetAddress());
	cat.info("Starting new socket node.");	
	new Thread(new SocketNode(socket, 
				  Category.getDefaultHierarchy())).start();
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Print a usage message to System.out and exit.
   * @param msg The error message.
   */
  protected static void usage(String msg) {
    System.out.println(msg);
    System.out.println(
      "Usage: java " +MarinerSocketServer.class.getName()
      + " port configFile");
    System.exit(1);
  }

  /**
   * Print a error message to System.out and exit.
   * @param msg The error message.
   */
  protected static void doError(String msg) {
    System.out.println("\n" + msg + "\n");
    System.exit(1);
  }
  
  /**
   * Load the log4j xml configuration file.  If the user does not supply a
   * configuration file load the sample configuration.
   * @param configFile The configuration filename.
   */
  private static void loadConfiguration(String configFile) {
    DOMConfigurator domConfigurator = new DOMConfigurator();
    if (configFile == null) {
      ClassLoader loader = ClassLoader.getSystemClassLoader ();    
      String resourceName = "com/volantis/mcs/cli/" 
	+ "marinerSocketServer-log4j.xml";
      try {
	InputStream inputStream = loader.getResourceAsStream (resourceName);
	if (inputStream != null) {
	  domConfigurator.doConfigure(inputStream, cat.getDefaultHierarchy());
	} else {
	  doError("An error occured loading the default configuration");
	}
      }
      catch (Exception e) {
	doError("An error occured loading the default configuration");
      }
    } else {
      try {
	FileInputStream fis = new FileInputStream(configFile);
	domConfigurator.doConfigure(fis, cat.getDefaultHierarchy());
      }
      catch (FileNotFoundException e) {
	doError("Configuration File not found: " + configFile);
      }
      catch (Exception e) {
	doError("An error occured loading config file: " + configFile);
      }
    }
  }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
