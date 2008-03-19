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
 * $Header: /src/mps/com/volantis/mps/recipient/MessageRecipientInfo.java,v 1.1 2002/11/13 11:22:37 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002111211 - Created
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.recipient;

/**
 * This interface should be supported by a user supplied implementation that
 * provides device and channel information for a particular
 * {@link MessageRecipient}.
 *
 * <p>The use of a given implementation is configured via the
 * <em>mcs-config.xml</em> file.</p>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface MessageRecipientInfo {
    /**
     * Resolves the device name for the <code>MessageRecipient</code>.
     *
     * @param recipient The <code>MessageRecipient</code> whose device
     * needs to be resolved.
     * @return The device or <code>null</code> if unresolved.
     */  
    public String resolveDeviceName(MessageRecipient recipient);
    
    /**
     * Resolves the channel name for the <code>MessageRecipient</code>.
     *
     * @param recipient The <code>MessageRecipient</code> whose channel
     * needs to be resolved.
     * @return The channel or <code>null</code> if unresolved.
     */  
    public String resolveChannelName(MessageRecipient recipient);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Apr-05	610/1	philws	VBM:2005040503 Port Public API changes from 3.3

 22-Apr-05	608/1	philws	VBM:2005040503 Update MPS Public API

 17-Nov-04	238/1	pcameron	VBM:2004111608 PublicAPI doc fixes and additions

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 ===========================================================================
*/
