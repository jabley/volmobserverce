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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
 
package com.volantis.mcs.ibm.websphere.mcsi;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.papi.PAPIAttributes;
import com.volantis.mcs.papi.PAPIException;

import java.io.Writer;


/**
 * This interface defines the methods which must be implemented by all
 * MCSI elements.
 * 
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * 
 */
public abstract class MCSIElement implements PAPIElement {


    public void elementContent(MarinerRequestContext context, PAPIAttributes papiAttributes, String content)
            throws PAPIException {
        throw new UnsupportedOperationException("MCSI do not support this operation");
    }

    public Writer getContentWriter(MarinerRequestContext context, PAPIAttributes papiAttributes) throws PAPIException {
        throw new UnsupportedOperationException("MCSI do not support this operation");
    }

}




/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/3	ianw	VBM:2004090605 New Build system

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 ===========================================================================
*/
