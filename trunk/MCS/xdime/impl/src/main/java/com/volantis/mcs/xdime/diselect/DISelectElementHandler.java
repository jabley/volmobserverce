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
package com.volantis.mcs.xdime.diselect;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEElement;
import com.volantis.mcs.xdime.initialisation.ElementFactory;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapBuilder;
import com.volantis.mcs.xdime.initialisation.ElementFactoryMapPopulator;
import com.volantis.mcs.xdime.schema.DISelectElements;

/**
 * Define mapping between DISelect elements and their factories.
 */
public class DISelectElementHandler
        extends ElementFactoryMapPopulator {

    // Javadoc inherited.
    public void populateMap(ElementFactoryMapBuilder builder) {
        builder.addMapping(DISelectElements.SELECT, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new SelectElementImpl(context);
            }
        });
        builder.addMapping(DISelectElements.WHEN, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new WhenElementImpl(context);
            }
        });
        builder.addMapping(DISelectElements.OTHERWISE, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new OtherwiseElementImpl(context);
            }
        });
        builder.addMapping(DISelectElements.IF, new ElementFactory() {
            public XDIMEElement createElement(XDIMEContextInternal context) {
                return new IfElementImpl(context);
            }
        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9673/3	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 09-Sep-05	9415/1	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 ===========================================================================
*/
