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

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mps.localization.LocalizationFactory;

import org.smpp.SmppObject;
import org.smpp.ServerPDUEventListener;
import org.smpp.ServerPDUEvent;
import org.smpp.pdu.PDU;

 /**
 * Class that acts as a listener for responses to asynchronous send requests.
 *
 */
public class SMSCResponseListener extends SmppObject
         implements ServerPDUEventListener {

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(SMSCResponseListener.class);

    /**
     * @see org.smpp.ServerPDUEventListener#handleEvent(org.smpp.ServerPDUEvent)
     */
    public void handleEvent(ServerPDUEvent event) {
        PDU pdu = event.getPDU();
        if(pdu.isResponse()){
            logger.info("response-received", pdu.debugString());
        } else {
            logger.warn("unknown-event", pdu.debugString());
        }
    }
}
