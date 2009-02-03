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
package com.volantis.synergetics.cornerstone.utilities.xml.jaxp;

import com.volantis.synergetics.cornerstone.utilities.xml.jaxp.TransformerMetaFactory;

import javax.xml.transform.TransformerFactory;

/**
 * Creates transformer factories using the "normal" dynamic JAXP lookup
 * facilities.
 * <p>
 * This will end up using whatever XSL processor that JAXP is configured to use.
 * <p>
 * This implementation may be suitable for use in Eclipse, but not in the
 * runtime or the cli tools, or unit tests.
 */
public class JAXPTransformerMetaFactory implements TransformerMetaFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Create a transformer factory using "normal" JAXP dynamic lookup.
     */
    public TransformerFactory createTransformerFactory() {

        // This does lots of things behind the scenes to find an appropriate
        // JAXP implementation, like look in META-INF/services, etc.
        // See http://java.sun.com/xml/jaxp/reference/faqs/index.html
        return TransformerFactory.newInstance();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	7946/2	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 ===========================================================================
*/
