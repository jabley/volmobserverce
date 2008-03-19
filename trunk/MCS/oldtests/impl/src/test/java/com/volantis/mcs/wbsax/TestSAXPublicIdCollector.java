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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-May-03    Geoff           VBM:2003042905 - Created.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.PublicIdFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.StringReader;

/**
 * A little class to suck out the public id from a stream of SAX events.
 * <p>
 * This is run in a separate parse from the main SAX parse because WBSAX
 * requires that the public id be passed in the startDocument event rather 
 * than after it, whereas SAX generates the public id event after the start
 * document event.
 * <p>
 * It'd be simpler to get rid of this class and have a lazy startDocument 
 * call in {@link com.volantis.mcs.wbsax.TestSAXConsumer} but I don't have time to do that now.
 */ 
public class TestSAXPublicIdCollector implements EntityResolver {
    
    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    private PublicIdFactory publicIdFactory;
    
    private PublicIdCode publicIdCode = PublicIdFactory.UNKNOWN;

    public TestSAXPublicIdCollector(PublicIdFactory publicIdFactory) {
        this.publicIdFactory = publicIdFactory;
    }

    /**
     * Collects the public id, creating the appropriate code for it.
     * Return a dummy entity to prevent it trying to go to the internet
     */ 
    public InputSource resolveEntity(String publicId, String systemId) 
            throws SAXException {
        InputSource inputSource = new InputSource(new StringReader(""));
        inputSource.setPublicId(publicId);
        inputSource.setSystemId(systemId);
        this.publicIdCode = publicIdFactory.create(publicId);
        // should check that the system id matches the expected one?
        return inputSource;
    }
    
    public PublicIdCode getPublicIdCode() {
        return publicIdCode;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Jul-03	709/2	geoff	VBM:2003070209 hacked port from metis, without synergetics changes

 03-Jul-03	696/5	geoff	VBM:2003070209 clean up WBSAX test cases

 ===========================================================================
*/
