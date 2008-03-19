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
package com.volantis.xml.pipeline.sax.drivers.web.conditioners;

import com.volantis.xml.pipeline.sax.drivers.web.WebDriverConditionerFactory;
import com.volantis.xml.pipeline.sax.conditioners.ContentConditioner;
import org.xml.sax.XMLFilter;

/**
 * Implementation of {@link WebDriverConditionerFactory} to create a
 * {@link HTMLResponseConditioner}
 */
public class HTMLResponseConditionerFactory
        implements WebDriverConditionerFactory {

    // javadoc inherited
    public ContentConditioner createConditioner(XMLFilter filter) {
        return new HTMLResponseConditioner(filter);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Mar-04	650/1	adrian	VBM:2004032906 Added factory for HTMLResponseConditioner

 ===========================================================================
*/
