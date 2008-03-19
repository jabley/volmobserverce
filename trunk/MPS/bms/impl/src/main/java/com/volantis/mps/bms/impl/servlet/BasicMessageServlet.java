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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.bms.impl.servlet;

import com.volantis.mps.bms.Failures;
import com.volantis.mps.bms.MessageService;
import com.volantis.mps.bms.MessageServiceException;
import com.volantis.mps.bms.MessageServiceFactory;
import com.volantis.mps.bms.SendRequest;
import com.volantis.mps.bms.impl.ser.ModelParser;
import com.volantis.mps.bms.impl.ser.ModelParserFactory;
import com.volantis.mps.bms.impl.local.InternalMessageService;
import com.volantis.mps.session.Session;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.synergetics.io.IOUtils;
import com.volantis.synergetics.log.LogDispatcher;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

/**
 * A basic servlet for handling the POST requests. It can also be used for
 * testing purposes during development. Just remove the call to
 * {@link super#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}
 * to allow GET to be processed, and set the web.xml as desired. Sample config
 * below.
 *
 * <pre>
 *
 * <servlet>
 *     <servlet-name>BMS</servlet-name>
 *     <display-name>Basic Messaging Service</display-name>
 *     <servlet-class>com.volantis.mps.bms.impl.servlet.BasicMessageServlet</servlet-class>
 *     <init-param>
 *         <param-name>address</param-name>
 *         <param-value>james.abley@volantis.com</param-value>
 *         <description>The address that the test message will be sent to</description>
 *     </init-param>
 *     <init-param>
 *         <param-name>device.name</param-name>
 *         <param-value>PC</param-value>
 *         <description>The MCS device name used to send the test message</description>
 *     </init-param>
 *     <init-param>
 *         <param-name>channel.name</param-name>
 *         <param-value>smtp</param-value>
 *         <description>The MPS channel named used to deliver the test message</description>
 *     </init-param>
 *     <init-param>
 *         <param-name>message.url</param-name>
 *         <param-value>http://localhost:8080/volantis/welcome/welcome.xdime</param-value>
 *         <description>The url used by MPS to get the content prior to creating the message.
 *             The default welcome.xdime out of the box in MSC does not work, since it needs
 *             to be wrapped in a &lt;message/&gt; element, plus it might be a ilttle complex.
 *             Hence this parameter, to let you point it at any simple XDIME page!</description>
 *     </init-param>
 *     <load-on-startup>0</load-on-startup>
 * </servlet>
 *
 * <servlet-mapping>
 *     <servlet-name>BMS</servlet-name>
 *     <url-pattern>/bms/*</url-pattern>
 * </servlet-mapping>
 *
 * </pre>
 */
public class BasicMessageServlet extends HttpServlet {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(BasicMessageServlet.class);

    private String smtpAddress;

    private String deviceName;

    private String channel;

    private String url;

    private Session session;

    // javadoc inherited
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        setSession(request.getSession());

        ModelParser parser =
                ModelParserFactory.getDefaultInstance().createModelParser();

        SendRequest sendRequest = parser.readSendRequest(
                request.getInputStream());

        IOUtils.closeQuietly(request.getInputStream());

        // Create an internal message service to process the request.
        InternalMessageService service =
                (InternalMessageService)MessageServiceFactory.
                        getDefaultInstance().createMessageService("internal:");

        Failures failures;
        try {
            failures = service.process(sendRequest, session);
        } catch (MessageServiceException e) {
            throw new ServletException(e.getMessage(), e);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        parser.write(failures, response.getOutputStream());
        IOUtils.closeQuietly(response.getOutputStream());
    }

    // javadoc inherited
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        setSession(request.getSession());

        // Comment this out for testing purposes - production implementation
        // calls super.doGet to return a 405 method not supported response.
        super.doGet(request, response);

        // Sample code for test purposes.

//        MessageFactory factory = MessageFactory.getDefaultInstance();
//
//        Address address;
//        try {
//            address = factory.createSMTPAddress(smtpAddress);
//        } catch (MalformedAddressException e) {
//            throw new ServletException(e);
//        }
//
//        Recipient recipient = factory.createRecipient(address, deviceName);
//        recipient.setChannel(channel);
//
//        Message message = factory.createMessage(new URL(url));
//
//        SendRequest sendRequest = factory.createSendRequest();
//        sendRequest.addRecipient(recipient);
//        sendRequest.setContent(message);
//
//        MessageService service = MessageServiceFactory.getDefaultInstance()
//                .createMessageService("internal:");
//
//        Failures failures;
//        try {
//            failures = service.send(sendRequest);
//        } catch (MessageServiceException e) {
//            throw new ServletException(e);
//        }
//
//        if (failures.isEmpty()) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            response.getWriter().write("Message sent OK!");
//        } else {
//            response.sendError(
//                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                    failures.getRecipients()[0].toString());
//        }
//
//        response.flushBuffer();
    }

    // javadoc inherited
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Get the servlet init-params required for testing.
        setSMTPAddress(config);
        setDeviceName(config);
        setChannel(config);
        setURL(config);
    }

    // Javadoc inherited.
    public void destroy() {
        if (session != null) {
            session.invalidate();
        }
        super.destroy();
    }

    /**
     * Retrieves the existing {@link Session} from the {@link HttpSession},
     * creating a new one if none exists.
     *
     * @param httpSession from which to retrieve the MPS session
     * @throws ServletException if there was a problem setting the session
     */
    private void setSession(HttpSession httpSession) throws ServletException {
        if (session == null) {
            session = (Session) httpSession.getAttribute(Session.class.getName());
            if (session == null) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("No MPS Session was stored in the HTTP " +
                            "Session, so attempting to create a new MPS Session");
                }
                session = new Session();
                httpSession.setAttribute(Session.class.getName(), session);
            }
        }
    }

    private void setURL(ServletConfig config) {
        url = config.getInitParameter("message.url");

        // fallback to default?
        if (null == url) {
            url = "http://localhost:8080/volantis/welcome/welcome.xdime";
        }
    }

    /**
     * Set the MPS channel name for the message used in testing.
     *
     * @param config
     */
    private void setChannel(ServletConfig config) {
        channel = config.getInitParameter("channel.name");

        // fallback to default?
        if (null == channel) {
            channel = "smtp";
        }
    }

    /**
     * Set the MCS device name for the recipient used in testing.
     *
     * @param config
     */
    private void setDeviceName(ServletConfig config) {
        deviceName = config.getInitParameter("device.name");

        // fallback to default?
        if (null == deviceName) {
            deviceName = "PC";
        }
    }

    /**
     * Set the SMTP address for the recipient used in testing.
     *
     * @param config
     */
    private void setSMTPAddress(ServletConfig config) {
        smtpAddress = config.getInitParameter("address");

        // fallback to default?
        if (null == smtpAddress) {
            smtpAddress = "james.abley@volantis.com";
        }
    }
}
