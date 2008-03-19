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
 * $Header: /src/mps/com/volantis/mps/assembler/MessageAssembler.java,v 1.5 2002/11/29 17:27:20 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002092305 - Created
 * 28-Nov-02    Sumit           VBM:2002112602 - Changed assembleMessage() sig
 *                              to accept MessageAttachments parameter
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.assembler;


import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mps.attachment.MessageAttachments;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.ProtocolIndependentMessage;

import java.net.URL;

/**
 * This singleton assembles a MessageContext
 *
 * @todo Make into a singleton (not)
 */
abstract public class MessageAssembler {

    private static final String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * Creates a new MessageContextBuilder
     */

    public MessageAssembler() {
    }

    /**
     * Assembles the body of the message
     * @exception MessageException thrown if there are any problems retrieving
     * the assets.
     */
    abstract public Object assembleMessage(ProtocolIndependentMessage Message,
                                           MessageAttachments attachments)
            throws MessageException;

    /*
     * Makes a URL fully qualified by using information from a base
     * request URL.
     *
     * @parm requestURL The base URL.
     * @parm url The URL to fixup.
     * @return The fixedup URL.
     */
    protected MarinerURL fixupURL(URL requestURL, MarinerURL url) {
        if (url.containsHostRelativePath()) {
            url.setProtocol(requestURL.getProtocol());
            url.setHost(requestURL.getHost());
            url.setPort(requestURL.getPort());
        } else if (url.containsDocumentRelativePath()) {
            url.setProtocol(requestURL.getProtocol());
            url.setHost(requestURL.getHost());
            url.setPort(requestURL.getPort());
            url.setPath(requestURL.getPath() + url.getPath());
        }
        return url;
    }


}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 ===========================================================================
*/
