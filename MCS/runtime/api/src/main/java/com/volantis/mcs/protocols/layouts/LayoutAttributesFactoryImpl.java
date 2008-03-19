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

package com.volantis.mcs.protocols.layouts;

import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.FormAttributes;
import com.volantis.mcs.protocols.LayoutAttributes;
import com.volantis.mcs.protocols.SlideAttributes;
import com.volantis.mcs.protocols.SpatialFormatIteratorAttributes;

/**
 * Implementation of {@link LayoutAttributesFactory}.
 */
public class LayoutAttributesFactoryImpl
        implements LayoutAttributesFactory {

    // Javadoc inherited.
    public DissectingPaneAttributes createDissectingPaneAttributes() {
        return new DissectingPaneAttributes();
    }

    // Javadoc inherited.
    public FormAttributes createFormAttributes() {
        return new FormAttributes();
    }

    // Javadoc inherited.
    public SlideAttributes createSlideAttributes() {
        return new SlideAttributes();
    }

    // Javadoc inherited.
    public LayoutAttributes createLayoutAttributes() {
        return new LayoutAttributes();
    }

    // Javadoc inherited.
    public SpatialFormatIteratorAttributes createSpatialFormatIteratorAttributes() {
        return new SpatialFormatIteratorAttributes();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/
