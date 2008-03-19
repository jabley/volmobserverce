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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.volantis.mcs.servlet.MarinerServletApplication;
import com.volantis.mcs.servlet.MarinerServletRequestContext;
import com.volantis.mcs.marlin.sax.MarlinSAXHelper;
import com.volantis.mps.attachment.DeviceMessageAttachment;
import com.volantis.mps.attachment.MessageAttachments;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.MultiChannelMessage;
import com.volantis.mps.message.MultiChannelMessageImpl;
import com.volantis.mps.recipient.MessageRecipient;
import com.volantis.mps.recipient.MessageRecipients;
import com.volantis.mps.recipient.RecipientException;
import com.volantis.mps.session.Session;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.assembler.ContentUtilities;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import org.xml.sax.InputSource;

/**
 * A sample servlet that send messages using MPS. See the MpsRecipient.html
 * page for details on the form. The following request parameters are used to
 * collect recipient and message information:
 *
 * <dl>
 *
 * <dt>recipients</dt>
 *
 * <dd>list of recipients to send to</dd>
 *
 * <dt>device</dt>
 *
 * <dd>list of devices for each recipient (empty list item will use default
 * device)</dd>
 *
 * <dt>type</dt>
 *
 * <dd>type of recipient (to, cc, bcc)</dd>
 *
 * <dt>channel</dt>
 *
 * <dd>channel to send to (smtpc, mmsc, smsc or as set up in config file)</dd>
 *
 * <dt>subject</dt>
 *
 * <dd>message subject</dd>
 *
 * <dt>url</dt>
 *
 * <dd>the url to send as the message</dd>
 *
 * <dt>xml</dt>
 *
 * <dd>the XDIME xml to parse and send as message. Only used if url is
 * empty</dd>
 *
 * <dt>attachment</dt>
 *
 * <dd>list of attachments either a path to a local file or a URL</dd>
 *
 * <dt>attachmentValueType</dt>
 *
 * <dd>list of the type of attachments (1 = File, 2= URL)</dd>
 *
 * <dt>attachmentChannel</dt>
 *
 * <dd>channel this attachment will get attached to</dd>
 *
 * <dt>attachmentDevice</dt>
 *
 * <dd>sevice message for attaching attachment to</dd>
 *
 * <dt>attachmentMimeType</dt>
 *
 * <dd>content/MIME type of this attachment</dd>
 *
 * </dl>
 *
 * <p>Note that this sample does not perform localization of exceptions or
 * other messages.</p>
 */
public class RunMps extends HttpServlet {

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(RunMps.class);

    /**
     * The exception localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer(RunMps.class);

    // The direct recipients
    private final String RECIPIENTS = "to";

    // The copied recipients
    private final String CCRECIPIENTS = "cc";

    // The blind copied recipients
    private final String BCCRECIPIENTS = "bcc";

    /**
     * The HTML we use to generate the response.
     */
    private static final String HTML_MESSAGE_START = "<html><body>";

    /**
     * The HTML we use to generate the response.
     */
    private static final String HTML_MESSAGE_END = "</body></html>";

    /**
     * The XDIME we use to generate the response.
     */
    private static final String XDIME_MESSAGE_START = "<canvas layoutName=\"/error.mlyt\">" +
            "<pane name=\"error\">";

    /**
     * The XDIME we use to generate the response.
     */
    private static final String XDIME_MESSAGE_END = "</pane></canvas>";

    /**
     * The MSA to initialise MCS.
     */
    MarinerServletApplication mpsTest;

    /**
     * Initializes the new instance.
     */
    public RunMps() {
    }

    /**
     * Init the MarinerServletApplication as MPS
     */
    public void init() throws ServletException {
        super.init();

        mpsTest = MarinerServletApplication.getInstance(
                getServletConfig().getServletContext(), true);
    }

    /**
     * Collects recipient information from the servlet request, set up the MPS
     * recipients and session and then send the message.
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        MessageRecipients recipients = null;
        MessageRecipients ccRecipients = null;
        MessageRecipients bccRecipients = null;

        // Get the recipients from the servlet request
        try {
            recipients = getRecipients(request, RECIPIENTS);
            ccRecipients = getRecipients(request, CCRECIPIENTS);
            bccRecipients = getRecipients(request, BCCRECIPIENTS);

            if (recipients == null) {
                throw new RecipientException("No recipients could be found");
            }
        } catch (Exception ae) {
            logger.error("Error loading recipient set", ae);

            writeError(request, response, ae);

            return;
        }

        // According to the Servlet 2.4 spec:
        // "..The default encoding of a request the container uses to create the
        // request reader and parse POST data must be ???ISO-8859-1??? if none has
        // been specified by the client request. However, in order to
        // indicate to the developer in this case the failure of the client
        // to send a character encoding, the container returns null
        // from the getCharacterEncoding method.."
        String requestEncoding = request.getCharacterEncoding();
        if (requestEncoding == null) {
            requestEncoding = "ISO-8859-1";
        }
        // Are we sending a URL or an XML message?
        String url = request.getParameter("url");
        MultiChannelMessage message = null;

        // Do we have an explicit charset encoding? Null values are allowed and
        // should result in device default charset being used. N.B. the "null"
        // string is sent by the form element when the user does not want to
        // specify a character encoding
        String characterSet = request.getParameter("charset");
        if (characterSet.equalsIgnoreCase("null")) {
            characterSet = null;
        }


        // do we need to generate and save the message to file
        boolean genMessage = false;
        if (request.getParameter("genMsg") != null &&
            request.getParameter("genMsg").equalsIgnoreCase("on")) {
            genMessage = true;
        }

        // get the subject
        String subject = request.getParameter("subject");
        if (characterSet != null) {
            subject = ContentUtilities.convertEncoding(
                    subject,
                    requestEncoding,
                    characterSet
            );
        }

        if (!(url.equals(""))) {
            message = new MultiChannelMessageImpl(new URL(url), subject, characterSet);
        } else {
            String xml = request.getParameter("xml");
            if (characterSet != null) {
                // convert from request char set to specified charset
                xml = ContentUtilities.convertEncoding(
                        xml,
                        requestEncoding,
                        characterSet
                );
            }
            message = new MultiChannelMessageImpl(xml, subject, characterSet);
        }

        try {
            message.addAttachments(getAttachments(request));
        } catch (Exception ee) {
            // log the fact that this failed and carry on.
            // Probably not the best thing to do...
            logger.error("Failed to attach attachments", ee);
        }

        // Now set up the from recipient and send the messages to each
        // recipient specified in the list
        MessageRecipient fromUser = null;
        MessageRecipients failures = null;

        // file used to for debugging
        File outputFile = null;

        try {
            fromUser = new MessageRecipient();
            fromUser.setAddress(new InternetAddress("mps@volantis.com"));

            Session session = new Session();
            session.addRecipients("toList", recipients);
            session.addRecipients("ccList", ccRecipients);
            session.addRecipients("bccList", bccRecipients);

            // debug output
            if (genMessage) {
                outputFile = saveMessages(session, message, "toList",
                             "ccList", "bccList", fromUser);
            }

            // Save failures for display later on
            failures = session.send(message, "toList",
                                    "ccList", "bccList", fromUser);
        } catch (Exception rec) {
            logger.error("Error sending message ", rec);

            writeError(request, response, rec);

            return;
        }

        // Write out the success message
        writeMesg(request, response, buildSuccessMessage(failures, outputFile));
    }

    /**
     * Generates a file output containing all information about generated messages
     *
     * @param session   Current session
     * @param mcm       MultiChannelMessage object
     * @param toList    The list of 'TO' recipients
     * @param ccList    The list of 'CC' recipients
     * @param bccList   The list of 'BCC' recipients
     * @param fromUser  The user from which the message is from
     *
     * @return  The <code>File</code> object that refers to the log file
     *
     * @throws Exception    Catch all exception
     */
    private File saveMessages(Session session,
                              MultiChannelMessage mcm,
                              String toList,
                              String ccList, 
                              String bccList,
                              MessageRecipient fromUser) throws Exception {

        // create temporary file
        String charSet = "UTF-8";
        if (mcm.getCharacterEncoding() != null) {
            charSet = mcm.getCharacterEncoding();
        }
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File temp = File.createTempFile("RUNMPS_",
                                        "_GeneratedOutput_" + charSet + ".txt",
                                        tempDir);
        OutputStreamWriter osw = new OutputStreamWriter(
                new FileOutputStream(temp), "UTF8");

        // use this to store devices names for generation
        List devices = new ArrayList(10);

        // write contents to file
        osw.write("Generated output\n");
        osw.write("-------------------------------------------------------\n");
        osw.write("MultiChannelMessage:\n");
        osw.write("Subject: " + mcm.getSubject() + "\n");
        osw.write("Content: " + mcm.getMessage() + "\n");
        osw.write("URL    : " + mcm.getMessageURL() + "\n");
        osw.write("Charset: " + mcm.getCharacterEncoding() + "\n");
        osw.write("-------------------------------------------------------\n");
        osw.write("Sender\n");
        osw.write("Address: " + fromUser.getAddress() + "\n");
        osw.write("MSISDN : " + fromUser.getMSISDN() + "\n");
        osw.write("Channel: " + fromUser.getChannelName() + "\n");
        osw.write("Device : " + fromUser.getDeviceName() + "\n");
        osw.write("\nMessageRecipients\n");
        MessageRecipients mrs = session.getRecipients(toList);
        osw.write("\n'TO' recipients\n");
        Iterator it = mrs.getIterator();
        while(it.hasNext()) {
            MessageRecipient mr = (MessageRecipient)it.next();
            osw.write("Address: " + mr.getAddress() + "\n");
            osw.write("MSISDN : " + mr.getMSISDN() + "\n");
            osw.write("Channel: " + mr.getChannelName() + "\n");
            osw.write("Device : " + mr.getDeviceName() + "\n\n");

            // add this device if not already present
            if (!devices.contains(mr.getDeviceName())) {
                devices.add(mr.getDeviceName());
            }
        }
        mrs = session.getRecipients(ccList);
        osw.write("\n'CC' recipients\n");
        it = mrs.getIterator();
        while(it.hasNext()) {
            MessageRecipient mr = (MessageRecipient)it.next();
            osw.write("Address: " + mr.getAddress() + "\n");
            osw.write("MSISDN : " + mr.getMSISDN() + "\n");
            osw.write("Channel: " + mr.getChannelName() + "\n");
            osw.write("Device : " + mr.getDeviceName() + "\n\n");

            // add this device if not already present
            if (!devices.contains(mr.getDeviceName())) {
                devices.add(mr.getDeviceName());
            }
        }
        mrs = session.getRecipients(bccList);
        osw.write("\n'BCC' recipients\n");
        it = mrs.getIterator();
        while(it.hasNext()) {
            MessageRecipient mr = (MessageRecipient)it.next();
            osw.write("Address: " + mr.getAddress() + "\n");
            osw.write("MSISDN : " + mr.getMSISDN() + "\n");
            osw.write("Channel: " + mr.getChannelName() + "\n");
            osw.write("Device : " + mr.getDeviceName() + "\n\n");

            // add this device if not already present
            if (!devices.contains(mr.getDeviceName())) {
                devices.add(mr.getDeviceName());
            }
        }
        osw.write("-------------------------------------------------------\n");
        it = devices.iterator();
        while (it.hasNext()) {
            String device = (String)it.next();
            osw.write("Message generation for device: " + device +"\n");
            osw.write("\nGenerate as string\n");
            try {
                String ret = new String(mcm.generateTargetMessageAsString(device).getBytes("utf-8"), "utf-8");
                osw.write(ret);
                osw.write("\n");
            } catch(MessageException e) {
                osw.write("ERROR: " + e.getMessage() + "\n");
            }

            osw.write("\nGenerate as URL\n");
            try {
                URL ret = mcm.generateTargetMessageAsURL(device, null);
                osw.write(ret.toString());
                osw.write("\n");
            } catch(MessageException e) {
                osw.write("ERROR: " + e.getMessage() + "\n");
            }

            osw.write("\nGenerate as Mime\n");
            try {
                MimeMultipart ret = mcm.generateTargetMessageAsMimeMultipart(device);
                writeMimeMultipart(ret, osw);
                osw.write("\n");
            } catch(MessageException e) {
                osw.write("ERROR: " + e.getMessage() + "\n");
            }
        }
        osw.write("-------------------------------------------------------\n");

        osw.flush();

        // close down I/O
        osw.close();

        // log the file's location so users can read it
        logger.info("Generated output placed in: " + temp.getAbsolutePath());

        return temp;
    }

    /**
     * Write the contents of the <code>MimeMultipart</code> message to the
     * specified output writer
     *
     * @param mimeMultipart     The <code>MimeMultipart</code> object to log
     * @param osw               The writer object to write to
     *
     * @throws MessageException Thrown by errors accessing the multipart content
     */
    private void writeMimeMultipart(MimeMultipart mimeMultipart, OutputStreamWriter osw) throws MessageException {
        try {
            int count = mimeMultipart.getCount();
            osw.write("MimeMultipart Object has " + count + " parts\n");
            MimeBodyPart b;
            Object o;
            InputStream in;
            InputStreamReader inr;
            /*      Display the headers that describe the Body Part*/
            for (int k = 0; k < count; k++) {
                b = (MimeBodyPart) mimeMultipart.getBodyPart(k);
                osw.write("Body Part index: " + k + " ID: " +
                        b.getContentID() + "\n");
                osw.write("MD5: " + b.getContentMD5() + "\n");
                osw.write("Content Type: " + b.getContentType() + "\n");
                osw.write("Description: " + b.getDescription() + "\n");
                osw.write("Disposition: " + b.getDisposition() + "\n");
                osw.write("Encoding: " + b.getEncoding() + "\n");
                osw.write("Filename: " + b.getFileName() + "\n");
                osw.write("Line Count: " + String.valueOf(b.getLineCount()) + "\n");
                osw.write("Size: " + String.valueOf(b.getSize()) + "\n");

                o = b.getContent();
                osw.write("MimeBodyPart Object: " + o.getClass().getName() + "\n");
                if (o instanceof String) {
                    osw.write("Value is String type" + "\n");
                    String s = (String)o;
                    osw.write("Value: " + s + "\n");
                } else if (o instanceof MimeMultipart) {
                    osw.write(">>>>>" + "\n");
                    // recursive call to print out Body Parts
                    // contained within this Body Part
                    writeMimeMultipart((MimeMultipart)o, osw);
                    osw.write("<<<<<" + "\n");
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append("Value: ");
                    in = b.getInputStream();
                    // read content using UTF-8
                    inr = new InputStreamReader(in, "UTF-8");
                    int l = inr.read();
                    while (l != -1) {
                        sb.append((char) l);
                        l = inr.read();
                    }
                    osw.write(sb.toString());
                }

            }
        }
        catch(Exception e) {
            throw new MessageException(e.getMessage());
        }

    }

    /**
     * Uses the canvas defined by failures start/end to build a message.
     *
     * @param failures List of MessageRecipients that failed
     * @return String String of XML that represnet HTML
     */
    private String buildSuccessMessage(MessageRecipients failures, File log) {
        StringBuffer mesg = new StringBuffer();
        mesg.append("<h1>MPS Test Complete. Messages sent</h1>");

        if (log != null) {
            mesg.append("<h3>Output written to " + log.getAbsolutePath() + "</h3>");
        }
        Iterator itr = failures.getIterator();

        if (itr.hasNext()) {
            mesg.append("<h3>Failures are</h3>");
            while (itr.hasNext()) {
                MessageRecipient rec = (MessageRecipient) itr.next();
                try {
                    String reason = rec.getFailureReason();
                    if(rec.getAddress() != null) {
                        mesg.append(rec.getAddress().toString()).append(" (");
                    } else if(rec.getMSISDN() != null) {
                        mesg.append(rec.getMSISDN()).append(" (");
                    } else {
                        mesg.append("UNKNOWN-ADDRESS").append(" (");
                    }

                    if (reason == null) {
                        mesg.append("no reason specified");
                    } else {
                        mesg.append(reason);
                    }

                    mesg.append(')');
                } catch (Exception e) {

                }
                mesg.append("<br />");
            }
        }

        return mesg.toString();
    }

    /**
     * Creates a MessageAttachments object from the parameters coming in from
     * the HTTP request.
     *
     * @param request
     * @return MessageAttachments
     */
    private MessageAttachments getAttachments(HttpServletRequest request) {

        String attachment[] = request.getParameterValues("attachment");
        String attachmentValueType[] = request.getParameterValues(
                "attachmentValueType");
        String attachmentChannel[] = request.getParameterValues(
                "attachmentChannel");
        String attachmentDevice[] = request.getParameterValues(
                "attachmentDevice");
        String attachmentMimeType[] = request.getParameterValues(
                "attachmentMimeType");

        MessageAttachments messageAttachments = new MessageAttachments();
        for (int i = 0; i < attachment.length; i++) {
            if (!attachment[i].equals("")) {
                DeviceMessageAttachment dma = new DeviceMessageAttachment();
                try {
                    dma.setChannelName(attachmentChannel[i]);
                    dma.setDeviceName(attachmentDevice[i]);
                    dma.setValue(attachment[i]);
                    dma.setValueType(Integer.parseInt(attachmentValueType[i]));
                    if (!attachmentMimeType[i].equals("")) {
                        dma.setMimeType(attachmentMimeType[i]);
                    }
                    messageAttachments.addAttachment(dma);
                } catch (MessageException me) {
                    logger.error("Failed to create attachment for " + attachment[i],
                        me);
                }
            }
        }

        return messageAttachments;
    }

    /**
     * Loads a recipient set from the ServletRequest by looking at parameters
     * "recipients" and "device". If "device" is "" or there are fewer devices
     * than recipients then no device is specified for the recipient. If the
     * channel is specified as SMS or WAPPush then the MSISDN of the recipient
     * is set rather than the address.
     *
     * @param request The servletRequest
     * @param inType  The type of recipients we are trying to load
     *                (to,cc,bcc);
     * @return MessageRecipients A list of recipients for the inType specified
     * @throws RecipientException If there is a problem extracting the
     *                            recipients from the request
     * @throws AddressException   If there is a problem extracting the
     *                            recipients from the request
     */
    private MessageRecipients getRecipients(HttpServletRequest request,
                                            String inType)
            throws RecipientException, AddressException {
        String[] names = request.getParameterValues("recipients");
        String[] devices = request.getParameterValues("device");
        String[] type = request.getParameterValues("type");
        String[] channel = request.getParameterValues("channel");

        MessageRecipients messageRecipients = new MessageRecipients();

        for (int i = 0; i < type.length; i++) {
            if (type[i].equals(inType)) {
                if (!names[i].equals("")) {
                    MessageRecipient messageRecipient = new MessageRecipient();

                    // Is there a channel for this index? Is it not empty?
                    if (channel.length > i && !channel[i].equals("")) {
                        // set the channel for the recipient
                        messageRecipient.setChannelName(channel[i]);

                        if (channel[i].equals("smsc")) {
                            // The channel is smsc so set the MSISDN rather
                            // than the address
                            messageRecipient.setMSISDN(names[i]);
                        } else if (channel[i].equals("wappush")) {
                            // The channel is wappush so set the MSISDN rather
                            // than the address
                            messageRecipient.setMSISDN(names[i]);
                        } else if (channel[i].equals("mmsc") &&
                                names[i].charAt(0) == '+') {
                            messageRecipient.setMSISDN(names[i]);
                        } else {
                            messageRecipient.setAddress(
                                    new InternetAddress(names[i]));
                        }
                    } else {
                        // channel is not present or empty so use smtp
                        // as default - This will have to be changed if the
                        // smtp channel is not present in the mcs-config file
                        messageRecipient.setChannelName("smtp");
                    }

                    // If there is a device then set it otherwise let MPS use
                    // the defaults
                    if (devices.length > i) {
                        String device = devices[i];
                        if (!device.equals("")) {
                            messageRecipient.setDeviceName(device);
                        }
                    }

                    // Add recipient to the list of MessageRecipients
                    messageRecipients.addRecipient(messageRecipient);
                }
            }
        }

        return messageRecipients;
    }

    /**
     * Writes an error out to the response using the canvas defined in
     * failureStart and failureEnd. The layout "error" with a pne error must be
     * defined in the repository for this to work.
     *
     * @param request
     * @param response
     * @param except   The exception to write out
     */
    private void writeError(HttpServletRequest request,
                            HttpServletResponse response,
                            Exception except) {

        // Try to send the response as XDIME
        try {
            MarinerServletRequestContext msrc =
                    new MarinerServletRequestContext(
                            getServletConfig().getServletContext(),
                            request, response);

            StringWriter writer = new StringWriter();
            writer.write(XDIME_MESSAGE_START);
            writer.write("<h1>MPS error occured</h1>");
            writer.write(
                    "<h3>Check Servlet and Volantis log for more information</h3>");
            writer.write(XDIME_MESSAGE_END);

            MarlinSAXHelper.parse(msrc,
                                  MarlinSAXHelper.getDefaultErrorHandler(),
                                  new InputSource(new StringReader(
                                          writer.toString())));
        } catch (Exception e) {
            // If we can't send the response as XDIME then
            // try sending it in html e.g. if customer has disable web page
            // generation set to true in their license.
            try {
                response.setContentType("text/html; charset=\"UTF-8\"");
                OutputStream os = response.getOutputStream();
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                out.println(HTML_MESSAGE_START);
                out.println("<h1>MPS error occured</h1>");
                out.println(
                        "<h3>Check Servlet and Volantis log for more information</h3>");
                out.println(HTML_MESSAGE_END);
            } catch (IOException ie) {
                logger.error("Failed to write error because of ", ie);
            }
        }
    }

    /**
     * Writes a message out to the HTTP response. Uses the failureStart and
     * failureEnd as XDIME for the message. The layout error with a pane called
     * error must be defined in the repository.
     *
     * @param request
     * @param response
     * @param mesg     The message to write out
     */
    private void writeMesg(HttpServletRequest request,
                           HttpServletResponse response,
                           String mesg) {
        // Try to send the response as XDIME
        try {
            MarinerServletRequestContext msrc =
                    new MarinerServletRequestContext(
                            getServletConfig().getServletContext(), request,
                            response);

            MarlinSAXHelper.parse(msrc,
                                  MarlinSAXHelper.getDefaultErrorHandler(),
                                  new InputSource(
                                          new StringReader(
                                                  XDIME_MESSAGE_START +
                                                  mesg +
                                                  XDIME_MESSAGE_END)));
        } catch (Exception e) {
            // If we can't send the response as XDIME then
            // try sending it in html e.g. if customer has disable web page
            // generation set to true in their license.
            try {
                response.setContentType("text/html; charset=\"UTF-8\"");
                OutputStream os = response.getOutputStream();
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                out.println(mesg);
            } catch (IOException ie) {
                logger.error("Failed to write error because of ", ie);
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/3	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/8	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 17-May-05	744/1	amoore	VBM:2005051206 Updated MPS to ensure correct encoding of messages when using DBCS

 10-May-05	710/1	amoore	VBM:2005042815 Incorporated error logging for recipients destined for unconfigured channels

 04-May-05	666/1	philws	VBM:2005050311 Port of failureReason from 3.3

 04-May-05	660/1	philws	VBM:2005050311 Add failureReason property API to MessageRecipient, set failureReasons in channel adapters and show example usage of failureReason

 29-Apr-05	654/1	philws	VBM:2005040509 Port exception localization from 3.3

 29-Apr-05	647/1	philws	VBM:2005040509 Minor exception message changes and switch to MarlinSAXHelper

 24-Nov-04	236/1	philws	VBM:2004111209 Re-worked MPS to use new build

 13-Aug-04	155/3	claire	VBM:2004073006 WAP Push for MPS: Servlet to store and retrieve messages

 08-Jul-04	127/1	claire	VBM:2004070702 Update layout names to include extensions

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 ===========================================================================
*/
