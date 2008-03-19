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

package com.volantis.mps.sample.bms;

import com.volantis.mps.bms.Failures;
import com.volantis.mps.bms.MalformedAddressException;
import com.volantis.mps.bms.Message;
import com.volantis.mps.bms.MessageFactory;
import com.volantis.mps.bms.MessageService;
import com.volantis.mps.bms.MessageServiceException;
import com.volantis.mps.bms.MessageServiceFactory;
import com.volantis.mps.bms.Recipient;
import com.volantis.mps.bms.SMTPAddress;
import com.volantis.mps.bms.SendRequest;
import com.volantis.mps.bms.Sender;
import com.volantis.mps.message.MessageException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class demonstrating how to call the remote client to the Basic Message
 * Service.
 */
public class BasicMessageServiceTest {

    private String endpoint = "http://localhost:8080/volantis/bms/";

    private String smtpAddress = "somebody@example.com";

    private String device = "PC";


    private String channel = "smtp";

    private URL url;

    /**
     * The default test message to be sent if a URL is not specified.
     */
    private static String MESSAGE =
            "<message>" +
                "<html xmlns=\"http://www.w3.org/2002/06/xhtml2\" " +
                      "xmlns:pipeline=\"http://www.volantis.com/xmlns/marlin-pipeline\" " +
                      "xmlns:template=\"http://www.volantis.com/xmlns/marlin-template\" " +
                      "xmlns:urid=\"http://www.volantis.com/xmlns/marlin-uri-driver\" " +
                      "xmlns:mcs=\"http://www.volantis.com/xmlns/2006/01/xdime/mcs\">" +

                "<head>" +
                    "<title> Message Title (Required) </title>" +
                "</head>" +
                "<body>" +
                        "<div>" +
                            "<p> This is an test message </p>" +
                        "</div>" +
                    "</body>" +
                "</html>" +
            "</message>";

    public BasicMessageServiceTest(String [] args) throws MalformedURLException {
        if (0 == args.length) {
            showUsage();
            System.exit(0);
        }

        processArgs(args);
    }

    /**
     * Set fields based on the provided args.
     *
     * @param args
     * @throws MalformedURLException
     */
    private void processArgs(String[] args) throws MalformedURLException {
        for (int i = 0; i < args.length; ++i) {
            String arg = args[i];

            if ("-a".equals(arg)) {
                smtpAddress = args[++i];
            } else if ("-d".equals(arg)) {
                device = args[++i];
            } else if ("-e".equals(arg)) {
                endpoint = args[++i];
            } else if ("-c".equals(arg)) {
                channel = args[++i];
            } else if ("-u".equals(arg)) {
                url = new URL(args[++i]);
            }
        }
    }

    private void run() throws MalformedAddressException,
            MessageServiceException, MessageException {

        displaySettings();

        final MessageFactory factory = MessageFactory.getDefaultInstance();
        final SMTPAddress address = factory.createSMTPAddress(smtpAddress);
        final Recipient recipient = factory.createRecipient(
                address, device);
        recipient.setChannel(channel);


        Message message = createMessage(factory);
        message.setSubject("hello from sample test client");

        SendRequest request = factory.createSendRequest();
        request.addRecipient(recipient);
        request.setMessage(message);
        Sender sender = factory.createSender(null, address);
        request.setSender(sender);

        MessageService messageService =
                MessageServiceFactory.getDefaultInstance()
                        .createMessageService(endpoint);

        Failures failures = messageService.process(request);

        if (failures.isEmpty()) {
            System.out.println("Success!");
            System.exit(0);
        }

        System.out.println("Failed");
        System.exit(1);
    }

    /**
     * Creates a message with the message source obtained from a URL
     * (if specified) or if not a default test message will be sent.
     *
     * @param messageFactory the factory used to create the message.
     *
     * @return a message to be sent.
     */
    private Message createMessage(MessageFactory messageFactory) {

        Message message = null;
        if (this.url != null) {
            message = messageFactory.createMessage(this.url);
        } else {
            // Lets use our default message.
            message = messageFactory.createMessage(MESSAGE);
        }
        return message;
    }

    private void displaySettings() {
        System.out.println("destination address: " + smtpAddress);
        System.out.println("Device name: " + device);
        System.out.println("channel: " + channel);
        System.out.println("endpoint: " + endpoint);
    }

    public static void main(String[] args) throws Exception {
        new BasicMessageServiceTest(args).run();
    }

    private void showUsage() {
        System.out.println("java -cp ... "
                + "com.volantis.mps.sample.BasicMessageService "
                + "[-a smtpaddress] "
                + "[-c channelname] "
                + "[-d devicename] "
                + "[-e endpoint] "
                + "[-u message-url] ");
        System.out.println("a - recipient destination SMTP address");
        System.out.println("c - optional MPS channel name. Defaults to smtp");
        System.out.println("d - optional MCS device name. Defaults to PC");
        System.out.println("e - optional web service endpoint. Defaults to "
                + "http://localhost:8080/volantis/bms/");
        System.out.println("u - optional message url. If not specified a default " +
                           "test message will be sent.");


    }
}
