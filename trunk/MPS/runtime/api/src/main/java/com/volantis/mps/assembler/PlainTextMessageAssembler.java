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
 * $Header: /src/mps/com/volantis/mps/assembler/PlainTextMessageAssembler.java,v 1.4 2002/12/04 17:33:32 ianw Exp $
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
 * 04-Dec-02    Ian             VBM:2002103006 - Recheckin under correct VBM.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.assembler;

import com.volantis.mps.attachment.MessageAttachments;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.ProtocolIndependentMessage;



/**
 * This <CODE>MessageAssembler</CODE> assembles plain text messages such as
 * SMS or plain/text emails. 
 */
public class PlainTextMessageAssembler extends MessageAssembler {
    
    private static final String mark = "(c) Volantis Systems Ltd 2002.";
    

    /** Creates a new instance of PlainTextMessageAssembler */
    public PlainTextMessageAssembler() {
    }
    
    /** Assembles the body of the message. Ignores the attachments object.
     * @exception MessageException not throw for this <CODE>MessageAssembler</CODE>.
     *
     */
    public Object assembleMessage(ProtocolIndependentMessage message,
                                  MessageAttachments attachments) throws MessageException {
        return message.getMessage();
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/1	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 29-Nov-04	243/1	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 ===========================================================================
*/
