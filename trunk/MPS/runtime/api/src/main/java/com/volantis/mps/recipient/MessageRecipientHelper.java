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
 * $Header: /src/mps/com/volantis/mps/recipient/MessageRecipientHelper.java,v 1.2 2003/03/26 17:43:13 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002111211 - Created
 * 26-Mar-03    Sumit           VBM:2003032602 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.recipient;

import com.volantis.mcs.runtime.configuration.MpsPluginConfiguration;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mps.localization.LocalizationFactory;

/**
 * This helper class holds static configuration information for 
 * <CODE>MessageRecipient</CODE>
 */
public class MessageRecipientHelper {
    
    private static final String mark = "(c) Volantis Systems Ltd 2002.";
    
    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(MessageRecipientHelper.class);
    
    private Volantis volantisBean;
    
    private MessageRecipientInfo messageRecipientInfo;
    
    private static MessageRecipientHelper instance = 
            new MessageRecipientHelper();
    
    /** Creates a new instance of MessageRecipientHelper */
    protected MessageRecipientHelper() {
                try {
            volantisBean = Volantis.getInstance();
            if (volantisBean == null) {
                throw new IllegalStateException
                        ("Volantis bean has not been initialised");
            }           
            MpsPluginConfiguration config = 
                (MpsPluginConfiguration)volantisBean
                    .getApplicationPluginConfiguration("MPS");
            String recipientResolver = config.getMessageRecipientInfo();
            if(logger.isDebugEnabled()) {
                logger.debug("MPS message-recipient-info: "+recipientResolver);
            }
            if (recipientResolver != null) {
                Class recipientResolverClass = Class.forName(recipientResolver);
                messageRecipientInfo = 
                    (MessageRecipientInfo)recipientResolverClass.newInstance();
            }
        } catch (Exception e) {
            if(logger.isDebugEnabled()) {
                logger.debug(e);
            }
        }

    }
    
    static MessageRecipientHelper getInstance() {
        return instance;
    }
    
    MessageRecipientInfo getMessageRecipientInfo() {
        return messageRecipientInfo;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 29-Nov-04	243/2	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 10-Jun-04	121/1	ianw	VBM:2004060111 Made to work with main 3.2 MCS stream

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 ===========================================================================
*/
