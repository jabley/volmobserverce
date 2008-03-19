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
package com.volantis.mcs.xdime.xhtml2.meta.property;

import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xhtml2.meta.datatype.DataType;

/**
 * Interface for classes handling meta properties. Implementations of this
 * interface will process contents of meta elements.
 *
 * All implementations must be thread-safe.
 */
public interface MetaPropertyHandler {

    /**
     * The defaul type of the meta property content.
     * Never returns null.
     *
     * @return the default content type
     */
    DataType getDefaultDataType();

    /**
     * Returns true if the specified type can be processed by this meta property
     * handler.
     *
     * @param type the type to check
     * @return true if the type can be processed
     */
    boolean isAcceptableType(DataType type);

    /**
     * Processes the given content.
     *
     * @param content the content to process
     * @param context the xdime context
     * @param id the id of the element this meta property refers to
     * @param propertyName the name of the property
     * @throws XDIMEException if something went wrong during processing the
     * content. Implementations should follow the fail fast strategy performing
     * all the reasonable checks on the content.
     */
    void process(Object content, XDIMEContextInternal context, String id,
                 String propertyName)
        throws XDIMEException;
}
