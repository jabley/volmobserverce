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
 * $Header: /src/voyager/com/volantis/mcs/cli/MarinerManager.java,v 1.10 2002/03/18 12:41:13 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Nov-01    Pether          VBM:2001110902 - Renamed LogManager to
 *                              MarinerManager. Added command options to flush
 *                              the cache.
 * 12-Nov-01    Pether          VBM:2001110902 - In printUsage() renamed the
 *                              commands to have prefix flush insted of
 *                              refresh.
 * 03-Dec-01    Doug            VBM:2001112901 - Added the ability to flush
 *                              the external repository plugin cache
 * 02-Jan-02    Paul            VBM:2002010201 - Made text more general.
 * 10-Jan-02    Paul            VBM:2002010403 - Commented out usage of
 *                              unsupported commands and configured logging
 *                              off.
 * 29-Jan-02    Adrian          VBM:2002010201 - Redundant commented out code
 *                              removed.
 * 08-Mar-02    Paul            VBM:2002030607 - Added flush-generated-styles.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.cli;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.management.agent.MarinerAgent;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.DefaultConfigurator;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import org.apache.log4j.LogManager;

/**
 * MarinerManager is a simple standalone command line utility for managing
 * Volantis settings remotely via the MarinerAgent.
 *
 * @author Allan Boyd
 */
public class MarinerManager {

    private static String mark = "(c) Volantis Systems Ltd 2000. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(MarinerManager.class);

    /**
     * Used to obtain localized messages
     */
    private static final MessageLocalizer messageLocalizer =
        LocalizationFactory.createMessageLocalizer(MarinerManager.class);

    
    /** 
     * General command error
     */
    private static final int COMMAND_GENERIC_ERROR = 1;
        
    /**
     * Command succeed
     */
    private static final int COMMAND_OK = 0;

    /**
     * Host name read from arguments.
     */
    static private String host = null;

    /**
     * Port number taken from arguments.
     */
    static private int port;

    /**
     * Socket used to connect to server process.
     */
    static private Socket socket = null;

    /**
     * Writer used to send commands to server process.
     */
    static private BufferedWriter socketWriter = null;

    /**
     * Reader used to recieve responce from server.
     */
    static private BufferedReader socketReader = null;

    protected static void printUsage() {

        final String lineSeparator = System.getProperty("line.separator");
        // load the usage message
        String usageMessage = messageLocalizer.format("server-manager-usage",
                                               MarinerAgent.PASSWORD);

        usageMessage += lineSeparator + messageLocalizer.format(
            "logging-level-details",
            MarinerAgent.LEVEL);

        usageMessage += lineSeparator + messageLocalizer.format(
            "jvm-properties",
            MarinerAgent.PROPS);

        usageMessage += lineSeparator + messageLocalizer.format(
            "flush-all-cache-content",
            MarinerAgent.ALL_CACHE_FLUSH);

        usageMessage += lineSeparator + messageLocalizer.format(
            "flush-component-cahce-content",
            MarinerAgent.COMPONENT_CACHE_FLUSH);

        usageMessage += lineSeparator + messageLocalizer.format(
            "flush-devices-cached-content",
            MarinerAgent.DEVICE_CACHE_FLUSH);

        usageMessage += lineSeparator + messageLocalizer.format(
            "flush-layout-content",
            MarinerAgent.LAYOUT_CACHE_FLUSH);

        usageMessage += lineSeparator + messageLocalizer.format(
            "flush-themes",
            MarinerAgent.THEME_CACHE_FLUSH);

        usageMessage += lineSeparator + messageLocalizer.format(
            "flush-rendered-page",
            MarinerAgent.RENDERED_PAGE_CACHE);

        usageMessage += lineSeparator + messageLocalizer.format(
            "flush-all-pipeline-caches",
            MarinerAgent.PIPELINE_ALL_CACHE_FLUSH);

        usageMessage += lineSeparator + messageLocalizer.format(
            "flush-pipeline-cache",
            MarinerAgent.PIPELINE_CACHE_FLUSH);

        System.out.println(usageMessage);
        System.out.flush();

    }

    public static void main(String args[]) {
        // default return code value - generic error
        int errno = COMMAND_GENERIC_ERROR;

        DefaultConfigurator.configure(false);
        getHostAndPortFromArgs(args);

        if (host == null || port == -1) {
            printUsage();
        } else {
            try {
                connectionToServer(host, port);
                String commandString = buildCommandString(args);
                sendCommandToServer(commandString);
                waitForResponseFromServer();
                printResponse();
                // opeartion succeed - change return value
                errno = COMMAND_OK;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                // Need this or log4j may cause us to hang at this point.
                // Well it will if it has an AsyncAppender anyway.
                LogManager.shutdown();
                try {
                    if (socketReader != null) {
                        socketReader.close();
                    }
                    if (socketWriter != null) {
                        socketWriter.close();
                    }
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.exit(errno);
    }

    /**
     * Wait until there is something to read from the server.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    private static void waitForResponseFromServer() throws IOException,
        InterruptedException {

        System.out.print("Awaiting response...");
        Object lock = new Object();

        synchronized (lock) {
            while (!socketReader.ready()) {
                lock.wait(500);
            }
        }

        System.out.println("OK");
    }

    /**
     * Send supplied command string to server via socket.
     *
     * @param commandString Command string to be sent to server.
     * @throws IOException
     */
    private static void sendCommandToServer(String commandString)
        throws IOException {

        System.out.print("Sending request...");
        if (logger.isDebugEnabled()) {
            logger.debug("Request:" + commandString);
        }
        socketWriter.write(commandString);
        socketWriter.newLine();
        socketWriter.flush();

        System.out.println("OK");
    }

    /**
     * Print to standard out the response from the server.
     *
     * @throws IOException
     */
    private static void printResponse() throws IOException {

        while (socketReader.ready()) {
            String line = socketReader.readLine();
            if (line != null) {
                System.out.println(line);
            }
        }
    }

    /**
     * Build a command string from the supplied arguments and
     * system properties. The host and port arguments are discarded as
     * they are only used to connect to the server.
     *
     * @param args command line arguments containing the command to be
     *             sent to the server.
     * @return String containing the command ready to be sent to the server
     */
    private static String buildCommandString(String[] args) {

        StringBuffer argsLine = new StringBuffer();

        for (int i = 0; i < args.length; i++) {
            if (!args[i].toLowerCase().startsWith("host") &&
                !args[i].toLowerCase().startsWith("port")) {
                argsLine.append(' ').append(args[i]);
            }
        }

        argsLine.append(" user:").append(System.getProperty("user.name"));

        return argsLine.toString();
    }

    /**
     * Extract the host name and port number from the supplied
     * arguments.
     *
     * @param args command line argumets holding
     */
    private static void getHostAndPortFromArgs(String[] args) {

        host = null;
        port = -1;

        StringTokenizer st = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].toLowerCase();
            if (arg.startsWith("host")) {
                st = new StringTokenizer(arg, ":");
                st.nextToken();
                if (st.hasMoreTokens()) {
                    host = st.nextToken();
                }
            }
            if (arg.startsWith("port")) {
                st = new StringTokenizer(arg, ":");
                st.nextToken();
                if (st.hasMoreTokens()) {
                    port = Integer.parseInt(st.nextToken());
                }
            }
        }

    }

    /**
     * Given a host, port and socket try to connect to the server,
     * setting up a reader and a writer for communication.
     *
     * @param host target host
     * @param port port on host to connect to
     * @throws IOException
     */
    private static void connectionToServer(String host, int port)
        throws IOException {

        System.out.print(
            "\nConnecting to " + host + " on port " + port + "...");
        socket = new Socket(host, port);
        System.out.println("OK");

        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();
        socketWriter =
            new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        socketReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Nov-05	10387/1	pabbott	VBM:2005110815 Fix code page problems with MarinerAgent and MarinerManager

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 25-May-05	7890/8	pduffin	VBM:2005042705 Committing supermerge changes

 24-May-05	7890/5	pduffin	VBM:2005042705 Committing extensive restructuring changes

 05-May-05	7890/2	pduffin	VBM:2005042705 Committing results of supermerge

 28-Apr-05	7914/1	pduffin	VBM:2005042714 Removing ExternalPluginDefinitionsManager, AssetGroup#repositoryName and related classes

 20-May-05	7762/2	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 19-May-05	8158/1	emma	VBM:2005041508 Merge from 330: Moving MarinerAgent management from Volantis to a servlet

 18-May-05	8156/1	emma	VBM:2005041508 Commit to allow RelMCS to build (product dependency problem)

 26-Apr-05	7759/4	pcameron	VBM:2005040505 Logging initialisation changed

 19-Apr-05	7665/3	pcameron	VBM:2005040505 Logging initialisation changed

 02-Mar-05	7197/1	emma	VBM:2005021519 mergevbm from MCS 3.3

 02-Mar-05	7175/5	emma	VBM:2005021519 Modifications after review

 02-Mar-05	7175/3	emma	VBM:2005021519 Modifications after review

 02-Mar-05	7175/1	emma	VBM:2005021519 Reformatting MarinerManager usage message and removing port from sent commands

 21-Dec-04	6531/1	doug	VBM:2004122005 Enhancements to the MessageLocalizer interface

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 26-Feb-04	3225/2	tony	VBM:2004022409 usage message debranding and externalisation

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 06-Aug-03	970/1	geoff	VBM:2003071607 merged from metis

 06-Aug-03	967/1	geoff	VBM:2003071607 merge from metis, mostly manual

 06-Aug-03	951/1	geoff	VBM:2003071607 fix up the agent and manager

 ===========================================================================
*/
