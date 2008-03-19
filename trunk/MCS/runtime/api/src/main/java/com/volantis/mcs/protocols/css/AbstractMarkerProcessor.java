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
package com.volantis.mcs.protocols.css;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.protocols.Inserter;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.Styles;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * todo: documentation
 */
public abstract class AbstractMarkerProcessor
        extends RecursingDOMVisitor
        implements MarkerProcessor {

    private Inserter inserter;

    protected Inserter getInserter() {
        return inserter;
    }

    // javadoc inherited
    public void process(Element listElement, Inserter inserter) {
        this.inserter = inserter;
        listElement.forEachChild(this);
    }

    /**
     * Removes the list-style-property style from the property values.
     *
     * @param element the element containing the style.
     */
    protected void removeListStylePosition(final Element element) {
        Styles styles = element.getStyles();
        MutablePropertyValues propertyValues = styles.getPropertyValues();
        propertyValues.markAsUnspecified(
                StylePropertyDetails.LIST_STYLE_POSITION);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	9440/2	pduffin	VBM:2005070711 Merged changes from main trunk

 05-Oct-05	9440/1	schaloner	VBM:2005070711 Added marker pseudo-element support

 ===========================================================================
*/
