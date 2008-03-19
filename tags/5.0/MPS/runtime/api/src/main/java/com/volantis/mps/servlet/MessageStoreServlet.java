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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.servlet;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.marlin.sax.MarlinSAXHelper;
import com.volantis.mcs.servlet.MarinerServletApplication;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.xml.schema.JarFileEntityResolver;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.store.MessageStoreConfig;
import com.volantis.mps.message.store.MessageStoreDaemon;
import com.volantis.mps.message.store.MessageStoreException;
import com.volantis.mps.message.store.MessageStoreMessageEnumeration;
import com.volantis.mps.assembler.ContentUtilities;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.Log4jHelper;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.log.ServletContextConfigurationResolver;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;

import org.jaxen.JaxenException;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.XPath;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This is an implementation of a Message Store Servlet (MSS) that allows for
 * XML reponses to be cached against an ID and then retrieved later using
 * that id as a parameter to the request.
 */
public class MessageStoreServlet extends HttpServlet {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The log4j object to log to.
     */
    private static LogDispatcher logger =
            LocalizationFactory.createLogger(MessageStoreServlet.class);

    /**
     * The exception localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer(
                    MessageStoreServlet.class);

    /**
     * The default location of the MessageStoreServlet (MSS) configuration file
     * if one has not be specified by the user.
     */
    public final static String CONFIG_FILE = "/WEB-INF/mss-config.xml";

    /**
     * The location of the MSS schema as specified in the config files
     */
    public static final String MSS_CONFIG_SCHEMA_URI =
            "http://www.volantis.com/schema/config/v1.0/mss-config.xsd";

    /**
     * Where the MSS schema can be found in the MPS core jar file
     */
    public static final String MSS_CONFIG_SCHEMA_LOCATION =
            "com/volantis/mps/message/store/mss-config.xsd";

    /**
     * The root element of the MSS config file.
     */
    protected static final String MSS_CONFIG_ROOT_ELEMENT =
            "messageStoreServer";

    /**
     * The environment element in the MSS config file.
     */
    public static final String MSS_CONFIG_ENVIRONMENT_ELEMENT = "environment";

    /**
     * The message-store element in the MSS config file.
     */
    public static final String MSS_CONFIG_MESSAGESTORE_ELEMENT =
            "message-store";

    /**
     * The location attribute on the {@link #MSS_CONFIG_MESSAGESTORE_ELEMENT}.
     */
    public static final String MSS_CONFIG_LOCATION_ATTRIBUTE = "location";

    /**
     * The id size attribute on the {@link #MSS_CONFIG_MESSAGESTORE_ELEMENT}.
     */
    public static final String MSS_CONFIG_IDSIZE_ATTRIBUTE = "id-size";

    /**
     * The timeout attribute on the {@link #MSS_CONFIG_MESSAGESTORE_ELEMENT}.
     */
    public static final String MSS_CONFIG_TIMEOUT_ATTRIBUTE = "timeout";

    /**
     * The validate attribute on the {@link #MSS_CONFIG_MESSAGESTORE_ELEMENT}.
     */
    public static final String MSS_CONFIG_VALIDATE_ATTRIBUTE = "validate";

    /**
     * The log4j configuration file attribute on the
     * {@link #MSS_CONFIG_ENVIRONMENT_ELEMENT}.
     */
    public static final String MSS_CONFIG_LOG4J_ATTRIBUTE =
            "log4jConfigurationFile";

    /**
     * The name of the parameter used in a request to store a message.
     */
    public static final String MESSAGE_STORE_PARAM_NAME = "xml";

    /**
     * The name of the parameter used in a request to retrieve a message.
     */
    public static final String MESSAGE_RETRIEVE_PARAM_NAME = "pageid";

    /**
     * The name of the header used to return the message id to the client
     */
    public static final String MESSAGE_RESPONSE_HEADER_NAME = "xmlid";

    /**
     * Used to represent an unlimited timeout in a Volantis config file xslt.
     * This is a string that can be used to compare against the value
     * returned when extracting the attribute/contents and will be true
     * if unlimited timeout has been specified.
     */
    protected static final String UNLIMITED = "unlimited";

    /**
     * All on-disk message store entries are stored as the plain (xdime) xml
     * that can be processed by MCS.  This extension is used for all of the
     * on-disk files.
     */
    protected static final String STORED_XML_EXTENSION = ".xml";

    /**
     * This is the number of minutes between the tasks being executed as part
     * of the message store daemon.
     */
    protected static final int TASK_REPEAT_MINUTES = 5;

    /**
     * The namespace prefix to use when using XPath functions with the MSS
     * config file.
     */
    private static final String NAMESPACE_PREFIX = "config";


    /**
     * The default location of the log4j configuration file for MSS.
     */
    private static final String DEFAULT_LOG4J_LOCATION =
            "/WEB-INF/mss-log4j.xml";

    /**
     * The location of the predefined log4j configuration file resource for
     * MSS. This is used if a location was not specified as an init parameter
     * to this servlet, and if the default location does not exist. The
     * predefined location is the last resort fallback.
     */
    private static final String PREDEFINED_LOG4J_LOCATION =
            "com/volantis/mps/logging/mss-log4j-predefined.xml";

    /**
     * The name of the init parameter for the log4j configuration file of
     * MSS.
     */
    private static final String LOG4J_FILE_PARAM = "mss.log4j.config.file";

    /**
     * Configuration read from the mss config file.  The location of this file
     * is specified in the web.xml deployment information, although if this is
     * missing this default to looking in the location specified by
     * {@link #CONFIG_FILE}.
     */
    protected MessageStoreConfig globalConfig = null;

    /**
     * A random number generator that is used as part of the ID generation.
     * This is seeded with the current system time to provide variation.
     */
    protected Random globalGenerator = new Random(System.currentTimeMillis());

    /**
     * The daemon that runs periodically to remove entries from the message
     * store cache that have reached their timeout limit.
     */
    protected MessageStoreDaemon daemon = null;

    /**
     * This is the in-memory message store cache.  It is represented as
     * a Map of key-value pairs.  The keys are the ID strings, whilst the
     * values are Date instances that represent the timestamp of the entry.
     * This is initialised by the code in {@link #initializeMessageStore}.
     * Any changes to the initialisation should ensure that it is
     * created as a synchronized map (see the Collections).
     */
    protected Map messageStoreIDs = null;

    /**
     * Initialise a new instance of the Message Store Servlet.
     */
    public MessageStoreServlet() {
        super();
    }

    // javadoc inherited
    public void init() throws ServletException {
        super.init();

        Log4jHelper.initializeLogging(
                this.getInitParameter(LOG4J_FILE_PARAM),
                new ServletContextConfigurationResolver(getServletContext()),
                DEFAULT_LOG4J_LOCATION,
                PREDEFINED_LOG4J_LOCATION);

        // As MCS resets all logging configuration on startup we force it to
        // initialise first. Then mss specific logging is appended to the mcs
        // logging configuration.
        MarinerServletApplication mariner = MarinerServletApplication.
                getInstance(getServletContext());
        mariner.initialize(getServletContext());

        // Configure this servlet
        String configFile = this.getInitParameter("config.file");
        if (configFile == null) {
            configFile = CONFIG_FILE;
        }
        globalConfig = readConfiguration(configFile);

        if (logger.isDebugEnabled()) {
            logger.debug("Configuration:-");
            logger.debug("\tmessage store location = " +
                    globalConfig.getLocation());
            logger.debug("\tnumber of digits in ID  = " +
                    globalConfig.getIdSize());
        }

        // Configure and initialise the message store
        initializeMessageStore();

        // Ensure the cleaning daemon is started
        daemon = new MessageStoreDaemon(TASK_REPEAT_MINUTES,
                createCleaningDaemonTask());
        daemon.start();
    }

    // JavaDoc inherited
    public void destroy() {
        daemon.stop();
        super.destroy();
    }

    /**
     * This creates a deamon task for cleaing the message store.  It scans
     * the cached entries for those whose creation time + timeout is before
     * the current time.  These entries are then removed from the message
     * store.
     *
     * @return An initialised task that can be used with
     * {@link java.util.Timer}s.
     */
    protected TimerTask createCleaningDaemonTask() {
        // Implemented as an inner anonymous class so that it has access to
        // the message store and timeout value.
        return new TimerTask() {

            // JavaDoc inherited
            public void run() {
                // Don't bother doing anything if there is an unlimited
                // timeout!
                if (!globalConfig.isUnlimitedTimeout()) {
                    scanEntries();
                }
            }

            /**
             * Scan the entries stored in the in-memory cache to determine
             * those to be deleted.  Once all candidates have been identified
             * then remove them from the in-memory cache and the filesystem.
             */
            protected void scanEntries() {
                // This is not threaded as the timer task should be run
                // in the timer thread as handled by the {@link Timer}
                // mechanism.
                List toDelete = new ArrayList();
                Date now = new Date();
                Set keys = messageStoreIDs.keySet();

                // Scan the entries for those to be deleted
                synchronized (messageStoreIDs) {
                    for (Iterator i = keys.iterator(); i.hasNext();) {
                        Object key = i.next();
                        Date timeout = (Date) messageStoreIDs.get(key);

                        // Multiply timeout by 1000 to convert to milliseconds
                        Date expiry = new Date(
                                timeout.getTime() +
                                (globalConfig.getTimeout() * 1000));
                        if (expiry.compareTo(now) < 0) {
                            toDelete.add(key);
                        }
                    }
                }

                // Delete all entries flagged for deletion
                for (Iterator i = toDelete.iterator(); i.hasNext();) {
                    deleteEntry(i.next());
                }
            }

            /**
             * Given an id key actually remove the file associated with this
             * id.  This is of the form
             * <pre>/location/of/cache/&ltid&gt.xml</pre>
             *
             * @param key The id key to delete.
             */
            protected void deleteEntry(Object key) {
                // Remove from the in-memory cache
                Date removedTimeout = (Date) messageStoreIDs.remove(key);

                // Create a reference to the persistent entry file
                File file = createEntryFile(key);
                // And delete that too!
                boolean deletedOK = file.delete();

                // Any failures to delete log a warning
                if (!deletedOK) {
                    logger.warn("persistent-file-deletion-failure",
                                file.toString());
                }

                // Log what's been happening to a debug log
                if (logger.isDebugEnabled()) {
                    logger.debug("Removed " + key + " (with timout of " +
                                 removedTimeout + ") from the message store");
                    if (deletedOK) {
                        logger.debug("Deleted persistent file: " +
                                     file.toString());
                    }
                }
            }
        };
    }

    // JavaDoc inherited
    protected void doGet(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        processRequest(httpServletRequest, httpServletResponse);
    }

    // JavaDoc inherited
    protected void doPost(HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        processRequest(httpServletRequest, httpServletResponse);
    }

    /**
     * Given a servlet request, this method will handle the necessary store
     * or retrieval activities and build an appropriate response.
     *
     * @param request  The servlet request that has been received.
     * @param response The servlet response that this servlet is contributing
     *                 to.
     *
     * @throws ServletException If there is a problem in processing the
     *                          request or generating the response.
     * @throws IOException      If an input or output error is detected during
     *                          the handling of the request.
     */
    protected void processRequest(HttpServletRequest request,
                                  HttpServletResponse response)
            throws ServletException, IOException {
        // Determine if this is a retrival or storage request.  Usually a store
        // request comes in with a POST and a retrieval with a GET. However, it
        // is possible to determine the operation being undertaken by looking
        // for the {@link #MESSAGE_RETRIEVE_PARAM_NAME} parameter (which is
        // only present for retrival requests)
        String id = request.getParameter(MESSAGE_RETRIEVE_PARAM_NAME);

        if ((id != null) && !"".equals(id)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Retreival request for ID: " + id);
            }
            try {
                retrieve(id, request, response);
            } catch (MessageStoreException mse) {
                // Handle the exceptions as error codes
                response.sendError(mse.getErrorCode());
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Storage request");
            }
            try {
                // This is a store request for an xml message provided
                // but only if the method was "POST", "GET" is not allowed,
                // and doPut is not implemented.
                if ("get".equalsIgnoreCase(request.getMethod())) {
                    response.sendError(
                                HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    return;
                }

                String encoding = null;
                if (request.getHeader("Accept-Charset") != null) {
                    encoding = request.getHeader("Accept-Charset");
                } else {
                    encoding = request.getCharacterEncoding();
                }

                // read in message from POST body
                if (logger.isDebugEnabled()) {
                    logger.debug("Body content size: " +
                            request.getContentLength());
                }

                String message = readMessage(request, response);
                if (message != null && message.length() > 0) {
                    if (encoding != null) {
                        // encoding has been specified for message content
                        // so we need to convert it here since we have already
                        // read a parameter from the stream and cannot change the
                        // character encoding for the servlet input stream
                        message = ContentUtilities.convertEncoding(
                                                message,
                                                request.getCharacterEncoding(),
                                                encoding);
                    }

                    // ensure XML prolog is set before storing the message
                    message = setXMLProlog(message, encoding);
                    id = store(message, encoding);
                    response.setHeader(MESSAGE_RESPONSE_HEADER_NAME, id);
                } else {
                    // return a 400 Bad request since a Message should
                    // have content!
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                                       "Message content cannot be empty");
                    return;
                }
            } catch (MessageStoreException mse) {
                // Handle the exceptions as error codes
                response.sendError(mse.getErrorCode());
            }
        }
    }

    /**
     * Reads the XML message from the body of the request. The message is
     * obtained using the Reader obtained by <code>request.getReader()</code>.
     * Hence, the message is encoded using the default encoding of the
     * reader.
     *
     * @param request   HttpServletRequest object
     * @param response  HttpServletResponse object
     *
     * @return The XML message
     *
     * @throws ServletException
     * @throws IOException
     */
    protected String readMessage(HttpServletRequest request,
                                 HttpServletResponse response)
            throws ServletException, IOException {
        Reader reader = request.getReader();

        char[] buf = new char[1024];
        int read = reader.read(buf);
        StringBuffer sb = new StringBuffer();
        while(read != -1) {
            sb.append(buf, 0, read);
            read = reader.read(buf);
        }

        return sb.toString().trim();
    }

    /**
     * Given an id create a fully qualified file reference to its on disk
     * counterpart.  This will contain the location of the entry cache on
     * disk and append the platform dependent file separator and then the
     * id and finally the {@link #STORED_XML_EXTENSION} extension.
     *
     * @param id The id to create a file reference for.
     * @return An initialised file object that represents the id provided.
     */
    protected File createEntryFile(Object id) {
        StringBuffer buffer = new StringBuffer(globalConfig.getLocation());
        buffer.append(System.getProperty("file.separator"));
        buffer.append((String) id + STORED_XML_EXTENSION);
        return new File(buffer.toString());
    }

    /**
     * This initializes the in-memory message store based on those files that
     * are found in the on-disk store.  It should only be done as the servlet
     * initializes.  It is also important to realise that changing the location
     * of the on-disk message store in the config file will mean that the next
     * run of the servlet will have nothing to initialize from!
     */
    protected void initializeMessageStore() {
        // Locate message store
        String messageStore = globalConfig.getLocation();
        final File storePath = new File(messageStore);
        if (!storePath.isDirectory()) {
            // This should be picked up by the config but checked here
            // to prevent the message store getting into a bodgy state.
            throw new RuntimeException("Unable to initialise the message " +
                                       "store. Path speciifed not a directory");
        } else if (!storePath.canWrite()) {
            throw new RuntimeException(
                    "Unable to initialise the message " +
                    "store. Path speciifed cannot be written to");
        } else if (!storePath.canRead()) {
            throw new RuntimeException("Unable to initialise the message " +
                                       "store. Path speciifed cannot be read");
        }

        // Check existing cached files
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                // Only want files that are in the message store directory
                // and have a prefix of xml
                return (dir.equals(storePath) &&
                        name.endsWith(STORED_XML_EXTENSION));
            }
        };
        File[] cachedFiles = storePath.listFiles(filter);
        int numFiles = cachedFiles.length;

        // Initialise the message store to be a synchronized map, with an
        // initial size of twice the number of the cached files (some may be
        // removed by the daemon so there is some flexbility in sizing).
        messageStoreIDs =
                Collections.synchronizedMap(new HashMap(numFiles * 2));

        // Add existing cached files to the in memory store
        for (int i = 0; i < numFiles; i++) {
            File currentFile = cachedFiles[i];
            String name = currentFile.getName();
            messageStoreIDs.put(
                    name.substring(0, name.lastIndexOf(STORED_XML_EXTENSION)),
                    generateTimeStamp(currentFile));
        }
    }

    /**
     * This method generates a string sized to contain the number of characters
     * specified by {@link #MSS_CONFIG_IDSIZE_ATTRIBUTE}.  This id string will
     * contain only numerical and lowercase alpha characters.  Upper case is
     * not used to ensure any filesystem that do not do case distinction will
     * work.
     * <p>
     * This id is unique enough for the purposes of this servlet.  Two random
     * seeds are used along with a request time to hopefully ensure no clashes,
     * even across threads.  The proof of true uniqueness is beyond the time-
     * scale available!
     *
     * @return A string containing a combination of chraracters that provides
     *         a unique ID.
     */
    protected String generateID() {
        int idSize = globalConfig.getIdSize();

        // The number of characters of the ID that that the time component
        // occupies.
        final int TIME_CHARACTERS = 5;

        // Get the current system time as this is used as part of the
        // generated ID and also for part of the seed of a random generator
        long currentTime = System.currentTimeMillis();

        // Create a hex string of the current time which returns a String
        // containing only 0123456789abcdef characters.
        String timeString = Long.toHexString(currentTime);

        // The size (in characters) that the time dependent part of the ID
        // will be.  It will always be the first part of the ID.
        final int timeLength = TIME_CHARACTERS;

        // Set the timeSize to be the length of the string minus the
        // number of chracters required.  This allows a substring to extract
        // the correct number!
        int timeSize = timeString.length() - timeLength;

        // Calculate how much dataSize is left for other characters
        int dataSize = idSize - TIME_CHARACTERS;

        // Create the buffer to contain the ID being generated
        StringBuffer idBuffer = new StringBuffer(dataSize);

        // Grab the last timeSize characters of this String (the bit that
        // varies most) for the first timeSize characters of the ID being
        // generated
        idBuffer.append(timeString.substring(timeSize));

        // Create a generator for the random part of the key.  This is seeded
        // on the current time and also affected by the global random generator
        // to ensure a wide spread of returned values.
        Random generator = new Random(currentTime + globalGenerator.nextLong());

        // The ID can only contain digits and letters (only one range as the ID
        // is case insensitive).  This defines the valid characters which can
        // then be randomly indexed.
        String validChars = "0123456789abcdefghijklmnopqrstuvwxyz";

        // Fill the rest of the ID with values.
        for (int i = 0; i < dataSize; i++) {
            idBuffer.append(validChars.charAt(
                    generator.nextInt(validChars.length())));
        }

        // And finally return the ID generated
        return idBuffer.toString();
    }

    /**
     * Given an id ensure that it is valid by checking the in-memory cache
     * and the on-disk cache.  If it does not exist in either location,
     * then indicate that it is a valid, unique id.
     *
     * @param id The id to check for validity.
     *
     * @return True if the id is valid and unique.  False otherwise.
     */
    protected boolean checkValidID(String id) {
        // Default to the ID being in use
        boolean notused = false;

        synchronized (messageStoreIDs) {
            Date date = (Date) messageStoreIDs.get(id);
            if (date == null) {
                // Not in memory cache so check disk
                File file = createEntryFile(id);
                // Not on disk either so the ID is not in use
                if (!file.exists()) {
                    notused = true;
                } else {
                    // Add to the in-memory cache
                    messageStoreIDs.put(id, generateTimeStamp(file));
                }
            }
        }

        return notused;
    }

    /**
     * Given a file, which may be null, generate a timestap based on the file
     * or the current time if the file is null or does not exist.
     *
     * @param file The file to base the timestamp on.  May be null.
     *
     * @return A new timestamp.
     */
    protected Date generateTimeStamp(File file) {
        Date timestamp = null;

        if (file != null && file.exists()) {
            // This will set the timestamp to be that of the file
            timestamp = new Date(file.lastModified());
        } else {
            // This defaults to creating a date/time of now
            timestamp = new Date();
        }

        return timestamp;
    }

    /**
     * Store the xml provided and return the id against which it was stored.
     *
     * @param xml The XML to store
     * @param encoding  The encoding of the XML string. <code>null</code>
     * value results in a falback to platform default
     *
     * @return    The generated id for the xml that is used to retrieve it
     *
     * @throws MessageStoreException If there is a problem storing the XML.
     */
    protected String store(String xml, String encoding)
            throws MessageStoreException {
        String newID = null;

        try {
            // Validate the XML if necessary
            if (globalConfig.isValidate()) {
                try {
                    DefaultHandler handler = createValidationHandler();

                    // Explicitly using the Volantis parser here and not
                    // relying on import jars or anything else on the classpath
                    // to avoid the wrong parser being instantiated.
                    XMLReader reader =
                            new com.volantis.xml.xerces.parsers.SAXParser();
                    reader.setContentHandler(handler);
                    reader.setErrorHandler(handler);
                    reader.setEntityResolver(new JarFileEntityResolver());

                    // Parse the XML to check for correct tags etc.
                    reader.parse(new InputSource(new StringReader(xml)));
                } catch (SAXException se) {
                    // Invalid XML
                    logger.error("xml-validation-failure", se);
                    throw new MessageStoreException(
                            localizer.format("xml-validation-failure"),
                            MessageStoreMessageEnumeration.NOT_ACCEPTABLE);
                }
            }

            // Store the xml to disk
            File file = null;
            do {
                // Generate or regenerate the ID.  It is possible that in the
                // intervening time another thread has used the id and written
                // a file!
                newID = generateValidID();
                file = createEntryFile(newID);
            } while (!file.createNewFile());

            // get the xml as a byte array using specified encoding in prolog
            // if available - we assume that the XML prolog provides the
            // definitive answer w.r.t. the XML encoding
            byte[] xmlBuffer = null;

            if (encoding != null) {
                xmlBuffer = xml.getBytes(encoding);
            } else {
                xmlBuffer = xml.getBytes();
            }

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(xmlBuffer, 0, xmlBuffer.length);
                fos.flush();
            } catch (IOException ioe) {
                if (file != null) {
                    // Tidy up in case writing failed
                    file.delete();
                }
                logger.error("xml-write-error", ioe);
                throw new MessageStoreException(
                        localizer.format("xml-write-error"),
                        MessageStoreMessageEnumeration.BAD_REQUEST);
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch(IOException ioe) {
                        // ignore
                    }
                }
            }

            synchronized (messageStoreIDs) {
                // Store the id/timestamp in the memory cache
                messageStoreIDs.put(newID, generateTimeStamp(file));
            }

        } catch (IOException ioe) {
            logger.error("xml-write-error", ioe);
            throw new MessageStoreException(
                    localizer.format("xml-write-error"),
                    MessageStoreMessageEnumeration.BAD_REQUEST);
        } catch (MessageStoreException mse) {
            logger.error("id-generation-error", mse);
            throw new MessageStoreException(
                    localizer.format("id-generation-error"),
                    MessageStoreMessageEnumeration.BAD_REQUEST);
        }

        return newID;
    }

    /**
     * This creates a very simple error handler for use with JDOM parsing.
     * It throws exceptions for any error, warning, or fatal error which can
     * be caught to indicate an error in the processing of the file.  The
     * default behaviour, even with a validating parse, is only to handle
     * fatal errors and not all parsing errors are flagged as fatal errors!
     *
     * @return An initialised instance of a DefaultHandler for use with JDOM.
     */
    protected DefaultHandler createValidationHandler() {
        return new DefaultHandler() {
            // JavaDoc inherited
            public void error(SAXParseException e)
                    throws SAXException {
                throw new SAXException("Error during parsing");
            }

            // JavaDoc inherited
            public void fatalError(SAXParseException e)
                    throws SAXException {
                throw new SAXException("Fatal error during parsing");
            }

            // JavaDoc inherited
            public void warning(SAXParseException e)
                    throws SAXException {
                throw new SAXException("Warning during parsing");
            }
        };
    }

    /**
     * This method generates a new, valid ID.  It will repeatedly generate
     * ids until it finds one which is valid.
     *
     * @return The ID string generated.
     *
     * @throws MessageStoreException If the supply of unique IDs is exhausted.
     */
    protected String generateValidID() throws MessageStoreException {
        String newID = generateID();

        // Create a loop limit of 10^idsize
        BigInteger limit = new BigInteger("10");
        limit = limit.pow(globalConfig.getIdSize());
        BigInteger count = new BigInteger("0");
        BigInteger increment = new BigInteger("1");

        // Repeat whilst the generated ID is invalid and the count < limit
        while (!checkValidID(newID) && (count.compareTo(limit) < 0)) {
            newID = generateID();
            count = count.add(increment);
        }

        // If the count has reached the limit there are no more IDs available
        if (count.compareTo(limit) == 0) {
            logger.error("id-limit-reached", new Object[] {limit});
            throw new MessageStoreException(
                    localizer.format("id-limit-reached", new Object[]{limit}),
                    MessageStoreMessageEnumeration.INTERNAL_SERVER_ERROR);
        }

        return newID;
    }

    /**
     * Retrieve the xml for a given id, process it according to the request,
     * and then return it via the response.
     *
     * @param id       The id to use to determine which XML to retrieve
     * @param request  The servlet request which includes the id parameter
     * @param response The servlet reponse into which the processed XML will be
     *                 put
     * @throws MessageStoreException If there is a problem retrieving and/or
     *                               processing the XML.
     */
    protected void retrieve(String id,
                            HttpServletRequest request,
                            HttpServletResponse response)
            throws MessageStoreException {
        String message = null;
        Date oldTimestamp = null;

        // If in the memory cache - adjust the timestamp
        if (messageStoreIDs.containsKey(id)) {
            synchronized (messageStoreIDs) {
                // Add an hour to the timestap on a temporary basis
                oldTimestamp = (Date) messageStoreIDs.put(
                        id, new Date(System.currentTimeMillis() +
                                     (60 * 60 * 1000)));
            }
        }

        // Read from the file
        File file = createEntryFile(id);

        // File not found
        if (!file.exists()) {
            logger.error("file-non-existant-for-id", id);
            throw new MessageStoreException(
                    localizer.format("file-non-existant-for-id"),
                    MessageStoreMessageEnumeration.NOT_FOUND);
        }

        try {
            FileInputStream fis = new FileInputStream(file);

            // Restore old timestamp
            if (oldTimestamp != null) {
                synchronized (messageStoreIDs) {
                    messageStoreIDs.put(id, oldTimestamp);
                }
            }

            processXML(request, response, fis);

        } catch (IOException ioe) {
            logger.error("xml-read-error", ioe);
            throw new MessageStoreException(
                    localizer.format("xml-read-error"),
                    MessageStoreMessageEnumeration.NOT_FOUND);
        } catch (MarinerContextException mce) {
            logger.error("device-processing-error", mce);
            throw new MessageStoreException(
                    localizer.format("device-processing-error"),
                    MessageStoreMessageEnumeration.NOT_FOUND);
        } catch (ServletException se) {
            logger.error("mcs-comms-error", se);
            throw new MessageStoreException(
                    localizer.format("mcs-comms-error"),
                    MessageStoreMessageEnumeration.NOT_FOUND);
        } catch (SAXException se) {
            logger.error("xml-parse-error", se);
            throw new MessageStoreException(
                    localizer.format("xml-parse-error"),
                    MessageStoreMessageEnumeration.NOT_FOUND);
        }
    }

    /**
     * This processes the message provided (assumed to be XML) through MCS
     * to render it appropriately for the device making the request.  The
     * processed XML is added to the response within MCS.
     *
     * @param request  The request made to the servlet
     * @param response The response to be sent from the servlet
     * @param msgStream  The XML to process
     *
     * @throws IOException             If there is a problem processing the XML
     * @throws MarinerContextException If there is a problem processing the XML
     * @throws SAXException            If there is a problem processing the XML
     */
    protected void processXML(HttpServletRequest request,
                              HttpServletResponse response,
                              InputStream msgStream)
            throws IOException, MarinerContextException, SAXException,
            ServletException {

        // Get the MarinerServletApplication.  This will initialise
        // MCS if it hasn't already been done.  Which is required below!
        MarinerServletApplication.getInstance(this.getServletContext());

        // Take the basic cached XML and process it through MCS and then
        // write it to the resonse stream.  Generate xdime based on device
        // etc. available in request.
        MarinerServletRequestContext mcsContext =
                new MarinerServletRequestContext(this.getServletContext(),
                                                 request,
                                                 response);

        // The processed page will be written to the response, so
        // we don't need to do anything further.
        XMLReader reader = MarlinSAXHelper.getXMLReader(mcsContext, null);

        EnvironmentContext environmentContext =
                ContextInternals.getEnvironmentContext(mcsContext);

        // The getXMLReader() call above will set up the pipelineContext in
        // the environmentContext.  See MarlinSAXHelper.setPipelineContext()
        XMLPipelineContext pipelineContext =
                environmentContext.getPipelineContext();

        // Set the Base URI in the pipeline's context
        URL baseURI = this.getServletContext().getResource("/");
        if (logger.isDebugEnabled()) {
            logger.debug("Setting Base URI " + baseURI.toExternalForm());
        }
        pipelineContext.pushBaseURI(baseURI.toExternalForm());

        reader.parse(new InputSource(msgStream));
    }


    /**
     * Read the MSS configuration from the file specified.  A suitable
     * configuration object is then constructed from the values found in
     * the config file.
     *
     * @param configFile The mss config file to use.
     * @return An initialised configuration object that represents the values
     *         found in the config file.
     * @throws ServletException If there is a problem in handling the config
     *                          file.
     */
    protected MessageStoreConfig readConfiguration(String configFile)
            throws ServletException {
        MessageStoreConfig config = new MessageStoreConfig();

        try {
            InputStream is = new FileInputStream(configFile);
            if (is != null) {
                SAXBuilder builder = new SAXBuilder();

                // Create a jar file entity resolver on the MPS core jar
                JarFileEntityResolver resolver =
                        new JarFileEntityResolver(this);
                // Add the mapping for the MSS config schema file
                resolver.addSystemIdMapping(MSS_CONFIG_SCHEMA_URI,
                                            MSS_CONFIG_SCHEMA_LOCATION);
                // And tell the builder about it
                builder.setEntityResolver(resolver);

                // Read the config file into an XML document.
                Document configDoc = builder.build(is);

                Element root = configDoc.getRootElement();

                // Ensure an appropriate namesepace is setup
                SimpleNamespaceContext nsContext = new SimpleNamespaceContext();
                nsContext.addNamespace(NAMESPACE_PREFIX,
                                       root.getNamespaceURI());

                // Grab the message-store element to allow extraction of the
                // various attributes
                XPath xpath = new JDOMXPath("//" +
                                            NAMESPACE_PREFIX + ":" +
                                            MSS_CONFIG_MESSAGESTORE_ELEMENT);
                xpath.setNamespaceContext(nsContext);

                Element element = (Element) xpath.selectSingleNode(root);

                // Location
                config.setLocation(element.getAttributeValue(
                        MSS_CONFIG_LOCATION_ATTRIBUTE));

                // ID Size
                config.setIdSize(Integer.parseInt(element.getAttributeValue(
                        MSS_CONFIG_IDSIZE_ATTRIBUTE)));

                // Timeout
                String timeout = element.getAttributeValue(
                        MSS_CONFIG_TIMEOUT_ATTRIBUTE);
                if (timeout.equalsIgnoreCase(UNLIMITED)) {
                    config.setUnlimitedTimeout(true);
                } else {
                    config.setTimeout(Integer.parseInt(timeout));
                }

                // Validation
                String validate = element.getAttributeValue(
                        MSS_CONFIG_VALIDATE_ATTRIBUTE);
                config.setValidate(validate.equals("true"));

                // Get the environment element and extract the log4j attribute
                xpath = new JDOMXPath("//" +
                                      NAMESPACE_PREFIX + ":" +
                                      MSS_CONFIG_ENVIRONMENT_ELEMENT);
                xpath.setNamespaceContext(nsContext);

                element = (Element) xpath.selectSingleNode(root);
                config.setLog4jConfigurationFile(
                        element.getAttributeValue(MSS_CONFIG_LOG4J_ATTRIBUTE));
            } else {
                handleConfigError(null);
            }
        } catch (IOException ioe) {
            handleConfigError(ioe);
        } catch (JDOMException jdome) {
            handleConfigError(jdome);
        } catch (JaxenException je) {
            handleConfigError(je);
        }

        return config;
    }

    /**
     * A utility method that given an exception logs it as a configuration
     * exception with a suitable error message.  A servlet exception is then
     * thrown to cascade the error through the servlet mechanism in a tidy
     * manner.
     *
     * @param e The exception that is being handled as a configuration error.
     *
     * @throws ServletException Always as this method provides a means of
     *         logging the problem and then wrapping and rethrowing the
     *         exception.
     */
    private void handleConfigError(Exception e)
            throws ServletException {
        final String errorMessage = "configuration-load-error";
        if (e == null) {
            logger.error(errorMessage);
            throw new ServletException(localizer.format(errorMessage));
        } else {
            logger.error(errorMessage, e);
            throw new ServletException(localizer.format(errorMessage), e);
        }
    }

    /**
     * Sets the XML prolog with specified encoding attribute
     *
     * @param message   The message content in XML format
     * @param encoding  The character encoding of the message
     *
     * @return  An XML string with XML prolog including encoding attribute
     */
    protected String setXMLProlog(String message, String encoding) {
        // convert content to msg buffer
        StringBuffer msgContent = new StringBuffer(message);
        char quote = '\'';

        // check if xml prolog is present
        if (msgContent.indexOf("<?xml", 0) != -1) {
            // prolog found - check presence of encoding attribute
            int prologEndPos = msgContent.indexOf("?>", 0);

            // check what quote style should be used for attribute values
            if (msgContent.lastIndexOf("\"", prologEndPos) != -1) {
                quote = '\"';
            }
            int encAttrPos = msgContent.indexOf("encoding=", 0);
            if (encAttrPos == -1 || encAttrPos > prologEndPos) {
                // need to set the encoding
                if (encoding != null) {
                    String encAttr = " encoding=" + quote +
                                     encoding + quote + " ";
                    msgContent.insert(5, encAttr);
                }
            }
            // do nothing if prolog encoding is set
            // we assume it is right!
        }
        else {
            String prologStr = "<?xml version=" + quote + "1.0" + quote;
            if (encoding != null) {
                prologStr +=  " encoding=" + quote + encoding + quote + " ?> ";
            } else {
                prologStr += " ?>";
            }
            msgContent.insert(0, prologStr);
        }
        return msgContent.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Jul-05	829/4	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 07-Jul-05	829/2	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/11	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 30-Jun-05	818/3	pcameron	VBM:2005062305 Fixed some message localisations for exception throwing

 26-Apr-05	586/1	pcameron	VBM:2005040505 Logging initialisation changed

 20-Apr-05	534/1	pcameron	VBM:2005040505 Logging initialisation changed

 29-Nov-04	243/2	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	208/2	matthew	VBM:2004101315 Make timeout value work in seconds rather then milliseconds

 19-Oct-04	205/1	matthew	VBM:2004100705 Change the message retrieval parameter from 'id' to 'pageid'

 19-Oct-04	202/1	matthew	VBM:2004100702 Stop MessageStoreServlet from responding to get requests that attempt to store a message

 19-Oct-04	198/3	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 13-Aug-04	155/3	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 ===========================================================================
*/
