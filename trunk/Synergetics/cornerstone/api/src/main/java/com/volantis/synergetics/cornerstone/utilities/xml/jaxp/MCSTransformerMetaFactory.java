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
 * Creates transformer factories which are always instances of the repackaged
 * Xalan transformer factory.
 * <p>
 * This deliberately avoids the dynamic JAXP lookup facilities for finding
 * transformer factories.
 * <p>
 * This implementation is suitable for use in the runtime (and also the cli
 * tools and unit tests), but not in Eclipse.
 */
public class MCSTransformerMetaFactory implements TransformerMetaFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Create a transformer factory for repackaged Xalan.
     */
    public TransformerFactory createTransformerFactory() {

        // Instantiate a TransformerFactory.
        // NOTE: the transformer factory class must be hardcoded rather than
        // looked up by JAXP on our behalf since our repackaged Xalan is
        // (deliberately) not registered with JAXP.
        return new com.volantis.xml.xalan.processor.TransformerFactoryImpl();
    }

    /**
     * Create a transformer factory for repackaged Xalan xsltc.
     */
    public TransformerFactory createXsltcTransformerFactory() {

        // Instantiate an xsltc TransformerFactory.
        // NOTE: the transformer factory class must be hardcoded rather than
        // looked up by JAXP on our behalf since our repackaged Xalan is
        // (deliberately) not registered with JAXP.
        return new com.volantis.xml.xalan.xsltc.trax.TransformerFactoryImpl();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9500/1	ianw	VBM:2005091308 Interim commit for Ian B

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 ===========================================================================
*/
