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
 * $Header: /src/mps/com/volantis/mps/recipient/MessageRecipientComparator.java,v 1.2 2002/11/20 11:44:24 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002111211 - Created
 * 20-Nov-02    Sumit           VBM:2002111815 - try/catch added for exceptions
 * 								being thrown
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.recipient;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mps.localization.LocalizationFactory;

/**
 * This comparator ensures that MessageRecipients are sorted by channel and 
 * device to enable sequential processing of channels and devices.
 */
public class MessageRecipientComparator implements java.util.Comparator {
    
    private static final String mark = "(c) Volantis Systems Ltd 2002.";
    
    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MessageRecipientComparator.class);
    
    /**
     * The exception localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer(
                    MessageRecipientComparator.class);

    /** Creates a new instance of MessageRecipientComparator */
    public MessageRecipientComparator() {
    }
    
    //javadoc inherited
    public int compare(Object o1, Object o2) {

        MessageRecipient mr1 = (MessageRecipient)o1;
        MessageRecipient mr2 = (MessageRecipient)o2;

        int result;
        if (mr1 != null) {
            try{
                StringBuffer key1 = new StringBuffer();
                key1.append(mr1.getChannelName());
                key1.append(mr1.getDeviceName());
                key1.append(mr1.getAddress());
                key1.append(mr1.getMSISDN());
                
                if (mr2 != null) {
                    StringBuffer key2 = new StringBuffer();
                    key2.append(mr2.getChannelName());
                    key2.append(mr2.getDeviceName());
                    key2.append(mr2.getAddress());
                    key2.append(mr2.getMSISDN());
                    
                    result = key1.toString().compareTo(key2.toString());
                } else {
                    return 1;
                }
            } catch(RecipientException e) {
                logger.error(localizer.format("comparison-failure"), e);
                result=-1;
            }
        } else {
            return -1;
        }
        return result;
        
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	243/2	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 ===========================================================================
*/
