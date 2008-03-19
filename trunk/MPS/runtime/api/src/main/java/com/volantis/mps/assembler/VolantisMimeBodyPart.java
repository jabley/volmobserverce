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
 * $Header: /src/mps/com/volantis/mps/assembler/VolantisMimeBodyPart.java,v 1.1 2003/01/21 17:17:41 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 21-Jan-2003  Chris W         VBM:2003012103 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mps.assembler;

import java.io.InputStream;

import javax.mail.MessagingException;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;

/**
 * Extend javax.mail.internet.MimeBodyPart and override updateHeaders()
 * to ensure that smil attachments have the correct Content-Transfer-Encoding
 * of 7bit. In JavaMail 1.2 smil attachements can end up being base64 encoded
 * which, of course, makes them unintelligible. 
 */
public class VolantisMimeBodyPart extends MimeBodyPart
{

    /**
     * Constructor for VolantisMimeBodyPart.
     */
    public VolantisMimeBodyPart()
    {
        super();
    }

    /**
     * Constructor for VolantisMimeBodyPart.
     * @param arg0
     * @throws MessagingException
     */
    public VolantisMimeBodyPart(InputStream arg0) throws MessagingException
    {
        super(arg0);
    }

    /**
     * Constructor for VolantisMimeBodyPart.
     * @param arg0
     * @param arg1
     * @throws MessagingException
     */
    public VolantisMimeBodyPart(InternetHeaders arg0, byte[] arg1)
        throws MessagingException
    {
        super(arg0, arg1);
    }

    
    /* (non-Javadoc)
     * @see javax.mail.internet.MimeBodyPart#updateHeaders()
     */
    protected void updateHeaders() throws MessagingException
    {
        super.updateHeaders();
        if ("application/smil".equals(getContentType()))
        {
            setHeader("Content-Transfer-Encoding", "7bit");
        }        
    }
}
