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
 * Constructs instances of layout related protocol attributes classes.
 *
 * @mock.generate 
 */
public interface LayoutAttributesFactory {

    /**
     * Create {@link DissectingPaneAttributes}.
     *
     * @return A newly created {@link DissectingPaneAttributes}.
     */
    DissectingPaneAttributes createDissectingPaneAttributes();

    /**
     * Create {@link FormAttributes}.
     *
     * @return A newly created {@link FormAttributes}.
     */
    FormAttributes createFormAttributes();

    /**
     * Create {@link SlideAttributes}.
     *
     * @return A newly created {@link SlideAttributes}.
     */
    SlideAttributes createSlideAttributes();

    /**
     * Create {@link LayoutAttributes}.
     *
     * @return A newly created {@link LayoutAttributes}.
     */
    LayoutAttributes createLayoutAttributes();

    /**
     * Create {@link SpatialFormatIteratorAttributes}.
     *
     * @return A newly created {@link SpatialFormatIteratorAttributes}.
     */
    SpatialFormatIteratorAttributes createSpatialFormatIteratorAttributes();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/
