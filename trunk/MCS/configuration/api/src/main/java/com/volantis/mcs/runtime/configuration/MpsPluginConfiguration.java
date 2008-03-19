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
 * $Header: /src/voyager/com/volantis/mcs/runtime/configuration/MpsPluginConfiguration.java,v 1.1 2003/03/20 12:03:17 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 17-Mar-03    Geoff           VBM:2002112102 - Created; holds configuration 
 *                              information about MPS.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Holds configuration information about MPS.
 * 
 * @todo Make plugin configuration a public API and move this all to MPS!
 * I.e. this, along with the various Channel objects, the related RuleSet and
 * test cases, should be moved into the MPS repository once the "plugin 
 * configuration" API is made public. This will also require that the 
 * {@link com.volantis.mcs.runtime.configuration.xml.digester.MarinerDigester}
 * is made part of that public API. 
 */ 
public class MpsPluginConfiguration implements ApplicationPluginConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";
    
    /**
     * The URL to use for asset resolution from internal requests.
     */ 
    private String internalBaseUrl;
    
    /**
     * The user supplied class used to resolve recipient devices and channels.
     */ 
    private String messageRecipientInfo;
    
    /**
     * List of the channels that MPS is to use.
     */ 
    private List channels = new ArrayList();

    /**
     * Application name
     */
    
    private final static String application = "MPS";
    
    // Inherit Javadoc.
    public String getName() {
        return application;
    }

    /**
     * Returns the Base URL from the configuration for MPS.
     * @return The Base URL
     */
    public String getInternalBaseUrl() {
        return internalBaseUrl;
    }

    /**
     * Set the Base URL for MPS.
     * @param internalBaseUrl The Base URL from the configuration
     */
    public void setInternalBaseUrl(String internalBaseUrl) {
        this.internalBaseUrl = internalBaseUrl;
    }

    /**
     * Gets the string representation of the class name for the user
     * supplied MessageRecipientInfo module for MPS.
     * @return The name of the MessageRecipientInfo class.
     */
    public String getMessageRecipientInfo() {
        return messageRecipientInfo;
    }

    /**
     * Sets the name of the MessageRecipientInfo class
     * @param messageRecipientInfo The name of the class
     */
    public void setMessageRecipientInfo(String messageRecipientInfo) {
        this.messageRecipientInfo = messageRecipientInfo;
    }

    /**
     * Get an iterator for the @link 
     * com.volantis.mcs.runtime.configuration.MpsChannelConfiguration
     * @return an Iterator of @link 
     * com.volantis.mcs.runtime.configuration.MpsChannelConfiguration
     */
    public Iterator getChannelsIterator() {
        return channels.iterator();
    }
    /**
     * Add a @link 
     * com.volantis.mcs.runtime.configuration.MpsChannelConfiguration 
     * to the configuration
     * @param channel The @link 
     * com.volantis.mcs.runtime.configuration.MpsChannelConfiguration
     */
    public void addChannel(MpsChannelConfiguration channel) {
        channels.add(channel);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 17-Jun-04	4619/5	ianw	VBM:2004060111 Fixed Javadoc issues

 07-Jun-04	4619/3	ianw	VBM:2004060111 Fixed MPS Configuration

 07-Jun-04	4619/1	ianw	VBM:2004060111 Fixed MPS Configuration

 ===========================================================================
*/
