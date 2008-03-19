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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.xdime.xforms;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.initialisation.ElementFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapPopulator;
import com.volantis.mcs.xdime.schema.SIElements;
import com.volantis.mcs.xdime.schema.XDIMECPInterimSIElements;
import com.volantis.mcs.xdime.schema.XDIMECPSIElements;

/**
 * Define mapping between XDIME 2 Simple Initialisation elements and their
 * factories.
 */
public class SIElementHandler
        extends ElementFactoryMapPopulator {

    // Javadoc inherited.
    public void populateMap(ElementFactoryMapBuilder builder) {

        ElementFactory instanceFactory = new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SIInstanceElementImpl(context);
            }
        };
        ElementFactory itemFactory = new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SIItemElementImpl(context);
            }
        };

        // Mappings for XDIME 2 Simple Initialisation elements.
        builder.addMapping(SIElements.INSTANCE, instanceFactory);
        builder.addMapping(SIElements.ITEM, itemFactory);

        // Mappings for XDIME CP Simple Initialisation elements.
        builder.addMapping(XDIMECPSIElements.INSTANCE, instanceFactory);
        builder.addMapping(XDIMECPSIElements.ITEM, itemFactory);

        // Mappings for XDIME CP Interim Simple Initialisation elements.
        builder.addMapping(XDIMECPInterimSIElements.INSTANCE, instanceFactory);
        builder.addMapping(XDIMECPInterimSIElements.ITEM, itemFactory);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/3	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 ===========================================================================
*/
