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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.conditioners;

import com.volantis.xml.pipeline.sax.XMLProcess;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * An object that can generate a stream of XML events from binary and or
 * character data.
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface ContentConditioner {

    /**
     * The volantis copyright statement
     */
    String mark = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Indicates whether this conditioner can condition binary data.
     * @return True if this conditioner supports the conditioning of binary data
     * and false otherwise.
     */
    public boolean supportsBinaryData();

    /**
     * Indicates whether this conditioner can condition character data.
     * @return True if this conditioner supports the conditioning of character
     * data and false otherwise.
     */
    public boolean supportsCharacterData();

    /**
     * Conditions binary data.
     * @param source The source of the binary data.
     * @param output The process to which the XML events should be sent.
     * @throws IOException
     * @throws SAXException
     */
    public void condition(InputSource source, XMLProcess output)
            throws IOException, SAXException;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 04-Aug-03	294/1	allan	VBM:2003070709 Fixed merge conflicts

 30-Jul-03	217/4	allan	VBM:2003071702 Separated WebDriverAccessor from configuration. Updated type safe enums. Updated conditioners

 16-Jul-03	208/1	doug	VBM:2003071603 Added as part of Pipeline public API

 ===========================================================================
*/
