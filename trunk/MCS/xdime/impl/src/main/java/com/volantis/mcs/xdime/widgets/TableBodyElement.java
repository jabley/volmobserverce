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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.widgets;

import com.volantis.mcs.protocols.widgets.attributes.LoadAttributes;
import com.volantis.mcs.protocols.widgets.attributes.TableBodyAttributes;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;

/**
 * Table XDIME element.
 */
public class TableBodyElement extends WidgetElement implements Loadable {
    /**
     * Creates and initialises new instance of Deck element.
     * @param context
     */
    public TableBodyElement(XDIMEContextInternal context) {
        super(WidgetElements.TBODY, context);
        
        protocolAttributes = new TableBodyAttributes();
    }

    public void setLoadAttributes(LoadAttributes loadAttributes) {
        getTableBodyAttributes().setLoadAttributes(loadAttributes);
    }
    
    public TableBodyAttributes getTableBodyAttributes() {
        return (TableBodyAttributes) protocolAttributes;
    }
    
    // Javadoc inherited
    protected void initialiseElementSpecificAttributes(XDIMEContextInternal context, XDIMEAttributes attributes) throws XDIMEException {
        TableBodyAttributes tableBodyAttributes = getTableBodyAttributes();
        
        // Initialize XHTML2 table attributes.
        com.volantis.mcs.protocols.TableBodyAttributes xhtml2Attributes =
            new com.volantis.mcs.protocols.TableBodyAttributes();
        
        xhtml2Attributes.copy(tableBodyAttributes);
        
        tableBodyAttributes.setXHTML2Attributes(xhtml2Attributes);

        // Initialize "cachedPagesCount" attribute.
        String cachedPagesCountAttribute = attributes.getValue("", "cached-pages-count");
        
        if (cachedPagesCountAttribute != null) {
            int cachedPagesCount; 
        
            try {
                cachedPagesCount = Integer.parseInt(cachedPagesCountAttribute);
            } catch (NumberFormatException e) {
                throw new XDIMEException("Invalid value for cached-pages-count attribute.");
            }
        
            tableBodyAttributes.setCachedPagesCount(cachedPagesCount);
        }
    }
    
    // Javadoc inherited
    protected void styleElementStart(XDIMEContextInternal context, XDIMEAttributes attributes) {
        super.styleElementStart(context, attributes);
        
        getTableBodyAttributes().getXHTML2Attributes().setStyles(protocolAttributes.getStyles());
    }
}
