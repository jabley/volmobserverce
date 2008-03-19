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
 * $Header: /src/voyager/com/volantis/mcs/utilities/MarinerAgent.java,v 1.14 2003/03/20 15:15:33 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 07-Sep-01    Allan           VBM:2001083118 - Renamed LogAgent to
 *                              MarinerAgent. Added ability to reset the
 *                              device and devicePattern caches (since these
 *                              are the only caches we need to reset at the
 *                              moment as the UpdateClient is the only thing
 *                              that will be using this facility). Added this
 *                              change history.
 * 09-Nov-01    Pether          VBM:2001110902 - Added abillity to recive
 *                              refresh calls to flush different caches.
 * 12-Nov-01    Pether          VBM:2001110902 - Changed all the commands
 *                              prefix from refresh to flush.
 * 03-Dec-01    Doug            VBM:2001112901 - Added the ability to flush
 *                              the external repository plugin cache.
 * 19-Dec-01    Paul            VBM:2001120506 - Removed unused import of
 *                              MarinerFacilities.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 09-Jan-02    Paul            VBM:2002010403 - Commented out all references
 *                              to the LogThread.
 * 29-Jan-02    Adrian          VBM:2002010201 - Reimplemented ability to
 *                              change the logging level.  Either integer or
 *                              string representation of level is received from
 *                              MarinerManager.  The log4j root category is
 *                              retrieved and the priority set on it.
 * 08-Mar-02    Paul            VBM:2002030607 - Added flush-generated-styles
 *                              command.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 *
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.management.agent;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.utilities.JDKProperties;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.MessageLocalizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * This class can be used to examine and configure a Volantis log remotely
 * without the need to restart the server. All logging categories that begin
 * with "com.volantis", "com.volantis." or "our." can have their logging level
 * changed at runtime. Note that logging categories for other Volantis products
 * running in the same JVM will also be updated.
 * <p>
 * The agent runs on a dedicated thread. This class also facilitates device
 * cache refresh of a running Mariner server.
 * </p>
 */
public class MarinerAgent extends Thread {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(MarinerAgent.class);

    /**
     * Used for message localization.
     */
    private final static MessageLocalizer MESSAGE_LOCALIZER =
        LocalizationFactory.
        createMessageLocalizer(MarinerAgent.class);

    public final static String PASSWORD = "password";

    public final static String LEVEL = "level";

    public final static String PROPS = "props";

    public final static String USER = "user";

    public final static String HOST = "host";

    /**
     * Constant identifier for the device pattern cache refresh command.
     */
    public static final String DEVICE_PATTERN_CACHE_FLUSH =
        "flush_device_patterns";

    /**
     * Constant identifier for the cache refresh command.
     */
    public final static String DEVICE_CACHE_FLUSH = "flush_devices";

    /**
     * Constant identifier for the cache refresh command.
     */
    public final static String THEME_CACHE_FLUSH = "flush_themes";

    /**
     * Constant identifier for the cache refresh command.
     */
    public final static String LAYOUT_CACHE_FLUSH = "flush_layouts";

    /**
     * Constant identifier for the cache refresh command.
     */
    public final static String EXTERNAL_REPOSITORY_CACHE_FLUSH =
        "flush_externalPlugins";

    /**
     * Constant identifier for the cache refresh command.
     */
    public final static String COMPONENT_CACHE_FLUSH = "flush_components";

    /**
     * Constant identifier for the rendered page cache refresh command.
     */
    public final static String RENDERED_PAGE_CACHE =
        "flush_page_cache";

    /**
     * Constant identifier for the refresh all pipeline caches command.
     */
    public final static String PIPELINE_ALL_CACHE_FLUSH =
        "flush_pipeline_all";

    /**
     * Constant identifier for the pipeline cache refresh command.
     */
    public final static String PIPELINE_CACHE_FLUSH =
        "flush_pipeline";

    /**
     * Constant identifier for the cache refresh command.
     */
    public final static String ALL_CACHE_FLUSH = "flush_all";

    /**
     * Constant identifier for log level DEBUG
     */
    private final static String DEBUG = "debug";

    /**
     * Constant identifier for log level INFO
     */
    private final static String INFO = "info";

    /**
     * Constant identifier for log level WARN
     */
    private final static String WARN = "warn";

    /**
     * Constant identifier for log level ERROR
     */
    private final static String ERROR = "error";

    /**
     * Constant identifier for log level FATAL
     */
    private final static String FATAL = "fatal";

    /**
     * Constant for the key used to obtain the vendor of the JDK being used
     * from {@link System#getProperty}
     */
    protected static final String JAVA_VENDOR = "java.vendor";

    /**
     * Constant for the key used to obtain the specification version of the
     * JDK being used.
     */
    protected static final String JAVA_SPECIFICATION_VERSION =
        "java.specification.version";

    /**
     * The server host that this agent should connect to. Can be null, in which
     * case the local host is used.
     */
    private final String host;

    /**
     * The server port that this agent should connect to.
     */
    private final int port;

    /**
     * The password used for connecting to the server.
     */
    private final String password;

    /**
     * The Volantis bean used by this agent.
     */
    private final Volantis volantis;

    /**
     * The ServerSocket instance used by this agent.
     */
    private ServerSocket ss;

    /**
     * Flag indicating whether the agent should stop itself.
     */
    private boolean shouldStop = false;

    /**
     * Used to obtain information about the JDK being used.
     */
    private JDKProperties jdkProperties;


    /**
     * Instantiates this class with the given details.
     *
     * @param password the password
     * @param host the host on which this agent should listen. Can be null.
     * @param port the port to listen to
     * @param volantis the Volantis bean
     */
    public MarinerAgent(String password,
                        String host,
                        int port,
                        Volantis volantis) {
        this.password = password;
        this.host = host;
        this.port = port;
        this.volantis = volantis;
        jdkProperties = new JDKProperties();
        setName("MarinerAgent");

        if (jdkProperties.getJavaSpecificationVersion().startsWith("1.3")) {

            if (!jdkProperties.isIBM1_3JDKBuild20041210orLater()) {

                // Due to a bug in the JDK being used (1.3).
                // MarinerAgent will not reinitialise properly
                // if the webapp is redeployed when running on WebSphere.
                logger.warn("mariner-agent-will-not-reinitialise-on-redeploy");
            }
        }
    }

    // javadoc inherited
    public void run() {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug("Mariner agent started");
                logger.debug("PASSWORD IS: " + password);
            }

            // If the host is null or empty then the default loopback interface
            // is returned which is what is used by the server socket in the
            // absence of any host. Therefore don't need to check for host
            // being null.
            final InetAddress ia = InetAddress.getByName(host);

            // Uses the default backlog when 0 is specified.
            ss = new ServerSocket(port, 0, ia);

            while (!shouldStop) {
                Socket socket = ss.accept();
                processRequest(socket);
            }
        } catch (SocketException e) {

            // This exception is expected when stopAgent is called when
            // this thread is blocked on an IO operation,
            // eg, ss.accept().
            if (shouldStop) {
                logger.info("mariner-agent-stopped-whilst-in-blocked-state");
            } else {
                logger.error("agent-thread-stopped", e);
            }
        } catch (Exception e) {
            logger.error("agent-thread-stopped", e);
        }
    }

    /**
     * Stops the agent running.
     */
    public synchronized void stopAgent() {
        this.shouldStop = true;
        try {
            // Interupting this thread via this.interrupt has no effect
            // if the thread is blocked on an IO operation such as
            // ServerSocket#accept.  Therefore we need to close the
            // socket directly.
            if (ss != null) {
                ss.close();
            }
        } catch (IOException e) {
            logger.error("agent-thread-stopped", e);
        }
    }

    /**
     * Processes the the request.
     *
     * @param socket the socket to read from
     */
    private void processRequest(Socket socket) {
        BufferedReader socketInput = null;
        BufferedWriter socketOutput = null;
        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        try {
            socketOutput = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            socketInput = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), "UTF-8"));

            String line = socketInput.readLine();
            Map commands = processParameters(line);

            String password = (String)commands.get(PASSWORD);
            String user = (String)commands.get(USER);
            commands.remove(PASSWORD);
            commands.remove(USER);

            // Calculate the true host name of the client machine
            String hostName = socket.getInetAddress().getHostName();
            InetAddress inetAddrs [] = InetAddress.getAllByName(hostName);
            StringBuffer clientHostName = new StringBuffer();
            for (int i = 0; i < inetAddrs.length; i++) {
                clientHostName.append(inetAddrs[i].getHostName()).append('/');
                clientHostName.append(inetAddrs[i].getHostAddress());
                if (i < inetAddrs.length - 1) {
                    clientHostName.append(',');
                }
            }

            String serverHostName = socket.getLocalAddress().getHostName();

            if (password != null && user != null && clientHostName != null &&
                password.equals(this.password)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("MarinerAgent invoked on " + serverHostName
                                 + " from client on " + clientHostName
                                 + " on port " + port
                                 + " by user " + user);
                }
                writer.println(MESSAGE_LOCALIZER.format(
                    "mariner-agent-logged-on"));
                processCommands(commands, writer);
                // NOTE: We print out the following message to indicate that
                // processing has completed successfully even if the user did
                // not provide any command that we understood. This is
                // required since we do not explicit reject unknown commands.
                // Yuck.
                writer.println(MESSAGE_LOCALIZER.format(
                    "mariner-agent-processed-all-commands"));
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Agent connection authorization"
                                 + " failed from " + host
                                 + ", user " + user);
                }
                writer.println(MESSAGE_LOCALIZER.format(
                    "mariner-agent-access-denied"));
            }
        } catch (Throwable e) {
            // Log the error and any output we had generated so far.
            logger.error("agent-call-error", e);

            // Add the stack trace to the information returned to the client.
            e.printStackTrace(writer);
        }

        try {
            // Write whatever we did back to the client.
            writer.close();
            String output = buffer.toString();
            if (logger.isDebugEnabled()) {
                logger.debug("Output:'" + output + "'");
            }
            socketOutput.write(output);
            socketOutput.flush();

            // And clean up.
            if (socketInput != null) {
                socketInput.close();
            }
            if (socketOutput != null) {
                socketOutput.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            logger.error("unexpected-ioexception", e);
        }
    }

    /**
     * Process the map of commands. As each command is processed it is removed
     * from the map. If any commands are left unprocessed then their names are
     * printed.
     *
     * @param commands The map of commands to process.
     * @param writer the place to write information to.
     */
    private void processCommands(Map commands, PrintWriter writer) {
        if (commands.containsKey(LEVEL)) {
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-setting-log-attempt"));
            String command = (String)commands.get(LEVEL);
            commands.remove(LEVEL);

            int newLevel = 0;

            try {
                newLevel = Integer.parseInt(command);
            } catch (Exception e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Level not integer, trying"
                                 + "string match");
                }
                if (command.equals(DEBUG)) {
                    newLevel = 1;
                } else if (command.equals(INFO)) {
                    newLevel = 2;
                } else if (command.equals(WARN)) {
                    newLevel = 3;
                } else if (command.equals(ERROR)) {
                    newLevel = 4;
                } else if (command.equals(FATAL)) {
                    newLevel = 5;
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Level string match failed");
                    }
                }
            }

            Level loggerLevel = null;
            switch (newLevel) {
                case 1:
                    loggerLevel = Level.DEBUG;
                    break;
                case 2:
                    loggerLevel = Level.INFO;
                    break;
                case 3:
                    loggerLevel = Level.WARN;
                    break;
                case 4:
                    loggerLevel = Level.ERROR;
                    break;
                case 5:
                    loggerLevel = Level.FATAL;
                    break;
                default:
                    writer.println(MESSAGE_LOCALIZER.format(
                        "mariner-agent-unknown-log-level",
                        new String[]{DEBUG, INFO, WARN, ERROR, FATAL}));
            }

            if (loggerLevel != null) {
                Enumeration loggerEnum = LogManager.getCurrentLoggers();
                while (loggerEnum.hasMoreElements()) {
                    Logger logger = (Logger)loggerEnum.nextElement();
                    final String name = logger.getName();

                    // Note that this updates logging categories for all
                    // Volantis products running in the same VM.
                    // @todo A better way would be to parse the log4j file for the categories, but this solution is deemed adequate for now.
                    // Ensure that we only match com.volantis and not
                    // for example, com.volantisXYZ by doing an equals
                    // comparison. This is for future-proofing in case
                    // we ever have any classes directly in the
                    // com.volantis package. The remaining startsWith
                    // matches are safe because they terminate with the
                    // package separator.
                    if ("com.volantis".equals(name) ||
                        name.startsWith("com.volantis.") ||
                        name.startsWith("our.")) {
                        logger.setLevel(loggerLevel);
                        writer.println(MESSAGE_LOCALIZER.format(
                            "mariner-agent-set-log",
                            new String[]{name, loggerLevel.toString()}));
                    }
                }
            }
        }

        if (commands.containsKey(PROPS)) {
            commands.remove(PROPS);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-jvm-properties"));
            writer.println("---------------------");
            Properties props = System.getProperties();
            Enumeration keys = props.propertyNames();
            while (keys.hasMoreElements()) {
                String name = (String)keys.nextElement();
                String value = props.getProperty(name);
                writer.println(name + "=" + value);
            }
        }
        if (commands.containsKey(DEVICE_PATTERN_CACHE_FLUSH)) {
            commands.remove(DEVICE_PATTERN_CACHE_FLUSH);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-device-pattern-flushing"));
            volantis.flushDeviceCache();
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-device-pattern-flushed"));
        }
        if (commands.containsKey(DEVICE_CACHE_FLUSH)) {
            commands.remove(DEVICE_CACHE_FLUSH);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-device-cache-flushing"));
            volantis.flushDeviceCache();
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-device-cache-flushed"));
        }
        if (commands.containsKey(LAYOUT_CACHE_FLUSH)) {
            commands.remove(LAYOUT_CACHE_FLUSH);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-device-layout-flushing"));
            volantis.flushLocalPolicyCache(PolicyType.LAYOUT);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-device-layout-flushed"));
        }
        if (commands.containsKey(THEME_CACHE_FLUSH)) {
            commands.remove(THEME_CACHE_FLUSH);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-themes-flushing"));
            volantis.flushLocalPolicyCache(PolicyType.THEME);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-themes-flushed"));
        }
        if (commands.containsKey(COMPONENT_CACHE_FLUSH)) {
            commands.remove(COMPONENT_CACHE_FLUSH);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-component-cache-flushing"));
            volantis.flushComponentAssetCache();
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-component-cache-flushed"));
        }
        if (commands.containsKey(RENDERED_PAGE_CACHE)) {
            commands.remove(RENDERED_PAGE_CACHE);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-rendered-pages-flushing"));
            volantis.flushRenderedPageCache();
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-rendered-pages-flushed"));
        }
        if (commands.containsKey(PIPELINE_ALL_CACHE_FLUSH)) {
            commands.remove(PIPELINE_ALL_CACHE_FLUSH);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-all-pipeline-flushing"));
            volantis.flushAllPipelineCaches();
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-all-pipeline-flushed"));
        }
        if (commands.containsKey(PIPELINE_CACHE_FLUSH)) {
            final String name = (String) commands.remove(PIPELINE_CACHE_FLUSH);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-pipeline-flushing", name));
            if (volantis.flushPipelineCache(name)) {
                writer.println(MESSAGE_LOCALIZER.format(
                    "mariner-agent-pipeline-flushed", name));
            } else {
                writer.println(MESSAGE_LOCALIZER.format(
                    "mariner-agent-pipeline-not-found", name));
            }
        }
        if (commands.containsKey(ALL_CACHE_FLUSH)) {
            commands.remove(ALL_CACHE_FLUSH);
            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-all-cache-flushing"));
            volantis.flushAllCaches();
            writer.println("mariner-agent-all-cache-flushed");
        }

        // tell the user that unrecognised commands were specified.
        if (commands.size() > 0) {

            writer.println("Unrecognised command"
                           + ((commands.size() == 1) ? "" : "s")
                           + " found and ignored:");
            String command = null;
            if (commands.size() == 1) {
                command = MESSAGE_LOCALIZER.format("mariner-agent-command");
            } else {
                command = MESSAGE_LOCALIZER.format("mariner-agent-commands");
            }

            StringBuffer sb = new StringBuffer();
            Iterator it = commands.keySet().iterator();
            while (it.hasNext()) {
                sb.append("\n");
                sb.append(it.next());
            }

            writer.println(MESSAGE_LOCALIZER.format(
                "mariner-agent-unrecognised-command",
                new String[]{command, sb.toString()}));
            writer.println();
        }
    }

    private Map processParameters(String line) {
        Map commands = new HashMap();
        StringTokenizer st1 = new StringTokenizer(line, " ");
        String command;
        while (st1.hasMoreTokens()) {
            command = st1.nextToken();
            StringTokenizer st2 = new StringTokenizer(command, ":");
            String name = st2.nextToken();
            String value = "";
            if (st2.hasMoreTokens()) {
                value = st2.nextToken();
            }
            commands.put(name, value);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Parameters: " + commands);
        }
        return commands;
    }

    /**
     * Used to determine if the MarinerAgent
     * is waiting for requests
     *
     * @return true if the MarinerAgent is waiting for incomming requests.
     */
    boolean isServiceRunning() {
        if (ss != null) {
            InetAddress inetAddress = ss.getInetAddress();
            return !(inetAddress == null);
        } else {
            return false;
        }
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Nov-05	10387/1	pabbott	VBM:2005110815 Fix code page problems with MarinerAgent and MarinerManager

 14-Sep-05	9514/1	pcameron	VBM:2005071113 Added host parameter to MarinerAgent

 12-Sep-05	9420/6	pcameron	VBM:2005071113 Added host parameter to MarinerAgent

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Jun-05	8866/1	rgreenall	VBM:2005062004 Merge from 323: Gaurding MarinerAgent against test case failure when using JDK with bug 4386498.

 20-Jun-05	8840/1	rgreenall	VBM:2005061401 Merge from 323: BindException exception thrown when redeploying on WebSphere.

 25-May-05	7890/4	pduffin	VBM:2005042705 Committing supermerge changes

 20-May-05	7762/7	doug	VBM:2005041916 Allow the MCSFilter cache to use post pipeline XDIME when calculating the cache key

 19-May-05	8158/11	emma	VBM:2005041508 Merge from 330: Moving MarinerAgent management from Volantis to a servlet

 19-May-05	8322/1	tom	VBM:2005051205 Localized command messages

 11-May-05	8173/12	pcameron	VBM:2005050523 Ensure com.volantis category cannot be misconstrued

 11-May-05	8154/17	pcameron	VBM:2005050523 Fixed mcsServerManager log level setting

 04-Feb-05	6871/1	doug	VBM:2005020402 Fixed problem with arguments being removed before they have been processed

 21-Dec-04	6529/6	matthew	VBM:2004121702 change device pattern cache flush message

 20-Dec-04	6529/3	matthew	VBM:2004121702 Allow MarinerAgent to flush the device pattern caches

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/5	tony	VBM:2004012601 rework changes

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 06-Aug-03	970/1	geoff	VBM:2003071607 merged from metis

 06-Aug-03	967/1	geoff	VBM:2003071607 merge from metis, mostly manual

 06-Aug-03	951/1	geoff	VBM:2003071607 fix up the agent and manager

 ===========================================================================
*/
