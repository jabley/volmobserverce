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

import javax.xml.transform.TransformerFactory;

/**
 * A factory for creating {@link javax.xml.transform.TransformerFactory}
 * implementations.
 * <p>
 * We require this so that we can avoid using JAXP lookup mechanisms to create
 * transformers when we are using a repackaged XSL implementation.
 * <p>
 * @todo later This would probably be better defined as part of the XML pipeline.
 * @todo later We could use a matching abstraction for XMLReader as well.
 */
public interface TransformerMetaFactory {

    /**
     * The copyright statement.
     */
    String mark = "(c) Volantis Systems Ltd 2004. ";

    /**
     * Create a transformer factory, using this "meta" factory.
     *
     * @return the created transformer factory, or null if one cannot be
     *      created.
     */
    TransformerFactory createTransformerFactory();

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Aug-04	5298/2	geoff	VBM:2004081720 Make automagic mdpr migration compatible with the runtime

 ===========================================================================
*/
