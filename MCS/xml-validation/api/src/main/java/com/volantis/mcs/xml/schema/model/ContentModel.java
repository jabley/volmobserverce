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

package com.volantis.mcs.xml.schema.model;

import java.util.Iterator;


/**
 */
public interface ContentModel {

    public void accept(ContentModelVisitor visitor);

    /**
     * Exclude all content types in the specified model from this content model
     * and the content of all nested elements.
     *
     * @param excludedContent The content to be excluded.
     */
    ContentModel exclude(ContentModel excludedContent);

    /**
     * Exclude the specified element from this content model and the content of
     * all nested elements.
     *
     * @param element The content to be excluded.
     */
    ContentModel exclude(ElementSchema element);

    /**
     * Get an iterator over all the excluded models.
     *
     * @return An iterator over all the excluded models.
     */
    Iterator excluded();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Oct-05	9673/1	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 ===========================================================================
*/
