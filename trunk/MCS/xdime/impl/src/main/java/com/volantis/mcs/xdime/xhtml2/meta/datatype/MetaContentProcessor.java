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
package com.volantis.mcs.xdime.xhtml2.meta.datatype;

import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEContextInternal;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xhtml2.MetaInformationElement;

/**
 * Base interface of meta element content processors. Implementations are used
 * to process the contents of meta elements.
 */
public interface MetaContentProcessor {

    /**
     * XML Schema namespace URI for standard XML Schema data types.
     */
    public static final String NAMESPACE_XML_SCHEMA =
        "http://www.w3.org/2001/XMLSchema";

    /**
     * Returns the object that describes the data type.
     * @return the object describing the data type. Never returns null.
     */
    public DataType getType();

    /**
     * Starts the processing of the content of the meta element. Called before
     * the content handler enters the content of the meta element.
     *
     * @param context the xdime context
     * @param metaElement the meta element whose content to be processed
     * @param attributes the attributes of the meta element
     */
    public void startProcess(XDIMEContextInternal context,
            MetaInformationElement metaElement, XDIMEAttributes attributes)
        throws XDIMEException;

    /**
     * Post processing. Called after the content handler leaved the content of
     * the meta element.
     *
     * @param context the xdime context
     * @param metaElement the meta element
     */
    public void endProcess(XDIMEContextInternal context,
                           MetaInformationElement metaElement)
        throws XDIMEException;

    /**
     * Returns the processed content. May return null.
     * @return the processed content
     */
    public Object getResult() throws XDIMEException;
}
