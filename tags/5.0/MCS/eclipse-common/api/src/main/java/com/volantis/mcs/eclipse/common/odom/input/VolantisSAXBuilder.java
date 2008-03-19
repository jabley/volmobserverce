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
package com.volantis.mcs.eclipse.common.odom.input;

import org.jdom.input.SAXBuilder;
import org.jdom.JDOMException;
import org.xml.sax.XMLReader;

/**
 * This JDOM SAX Builder specifically uses the Volantisized Xerces parser.
 *
 * <p><strong>NOTE:</strong> A standard {@link SAXBuilder} cannot be used with
 * a SAX driver location parameter because this causes class path issues when
 * using JRE 1.4 and Eclipse (due to the fact that JRE 1.4 comes with its own
 * XML APIs which utilize a JRE class loader to resolve the SAX driver class
 * instead of an Eclipse one).<p>
 * @todo later move this class to a more generally accessible package
 */
public class VolantisSAXBuilder extends SAXBuilder {
    /**
     * Initializes the new instance using the given parameters.
     */
    public VolantisSAXBuilder() {
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param validate indicates whether validation should be enabled or not.
     */
    public VolantisSAXBuilder(boolean validate) {
        super(validate);
    }

    // javadoc inherited
    protected XMLReader createParser() throws JDOMException {
        // Explicitly construct a Volantisized Xerces parser to avoid any
        // JRE 1.4 class loader issues
        XMLReader parser = new com.volantis.xml.xerces.parsers.SAXParser();

        // This relies on use of the Volantisized JDOM to access this method
        setFeaturesAndProperties(parser, true);

        return parser;
    }
 }

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Apr-04	3574/1	allan	VBM:2004032401 Implement merging of device hierarchies.

 16-Feb-04	3023/5	philws	VBM:2004010901 Fix JDK 1.4/Eclipse XML API issue with JDOM SAXBuilder and the Volantisized Xerces parser

 13-Feb-04	3023/1	philws	VBM:2004010901 Ensure that the Volantisized XERCES parser is explicitly used for compatibility with Eclipse under JRE 1.4

 ===========================================================================
*/
