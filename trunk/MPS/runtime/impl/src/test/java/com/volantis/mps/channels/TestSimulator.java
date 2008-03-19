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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.channels;

import java.io.IOException;

import org.smpp.smscsim.DeliveryInfoSender;
import org.smpp.smscsim.PDUProcessorGroup;
import org.smpp.smscsim.SMSCListener;
import org.smpp.smscsim.SMSCListenerImpl;
import org.smpp.smscsim.SMSCSession;

/**
 * Class which simulates an SMSC for use in test cases (extracted from
 * {@link LogicaSMSChannelAdapterTestCase} for use in other tests).
 */
public class TestSimulator {

    private SMSCListener smscListener = null;
    private PDUProcessorGroup processors = null;
    private DeliveryInfoSender deliveryInfoSender = null;
    private int port;
    
    /**
     * Initialize a new instance using the given parameters.
     * @param port on which the simulator is listening
     */
    public TestSimulator(int port) {
        this.port = port;
    }

    /**
     * Start listening.
     *
     * @param listener      listens for messages
     * @throws IOException if there was a problem starting listening
     */
    public void start(MessageListener listener)
            throws IOException {
        if (smscListener == null) {
            System.out.print("Starting listener... ");
            smscListener = new SMSCListenerImpl(port, true);
            processors = new PDUProcessorGroup();
            deliveryInfoSender = new DeliveryInfoSender();
            deliveryInfoSender.start();
            TestPDUProcessorFactory factory =
                    new TestPDUProcessorFactory(listener);
            factory.setDisplayInfo(false);
            smscListener.setPDUProcessorFactory(factory);
            smscListener.start();
            System.out.println("started.");
        } else {
            System.out.println("Listener is already running.");
        }
    }

    /**
     * Stop listening.
     * @throws IOException if there was a problem stopping listening.
     */
    public void stop() throws IOException {
        if (smscListener != null) {
            System.out.println("Stopping listener...");
            synchronized (processors) {
                int procCount = processors.count();
                TestPDUProcessor proc;
                SMSCSession session;
                for (int i = 0; i < procCount; i++) {
                    proc = (TestPDUProcessor) processors.get(i);
                    session = proc.getSession();
                    System.out.print("Stopping session ... ");
                    session.stop();
                    System.out.println(" stopped.");
                }
            }
            smscListener.stop();
            smscListener = null;
            if (deliveryInfoSender != null) {
                deliveryInfoSender.stop();
            }
            System.out.println("Stopped.");
        }
    }
}
