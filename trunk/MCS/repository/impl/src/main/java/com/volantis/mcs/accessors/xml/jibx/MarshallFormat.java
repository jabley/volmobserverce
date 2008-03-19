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
package com.volantis.mcs.accessors.xml.jibx;

import com.volantis.mcs.accessors.xml.jdom.XMLAccessorConstants;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * Marshall/Unmarshall layouts.
 */
public class MarshallFormat implements IMarshaller,
                                             IUnmarshaller,
                                             IAliasable,
                                             XMLAccessorConstants {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    MarshallFormat.class);

    private String uri;
    private String name;
    private int nsIndex;

    /**
     * Default constructor used by JIBX.
     */
    public MarshallFormat() {
        this(null,
             0,
             STYLE_PROPERTIES_ELEMENT);
    }

    /**
     * Constructor used by JIBX to set the structure name, namespace URI and
     * namespace index.
     *
     * @param uri namespace URI
     * @param nsIndex namespace index
     * @param name XML element or mapping name
     */
    public MarshallFormat(String uri,
                          int nsIndex,
                          String name) {

        this.name = name;
        this.uri = uri;
        this.nsIndex = nsIndex;
    }

    // Javadoc interited from interface
    public boolean isExtension(int index) {
        return false;
    }

    // Javadoc interited from interface
    public void marshal(Object obj, IMarshallingContext ictx)
            throws JiBXException {

    }

    // Javadoc inherited from interface
    public boolean isPresent(IUnmarshallingContext ictx) throws JiBXException {
        return ictx.isAt(uri,
                         name);
    }

    // Javadoc interited from interface
    public Object unmarshal(Object obj,
                            IUnmarshallingContext ictx)
            throws JiBXException {

        return "myUnmarshalledObject";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 ===========================================================================
*/
