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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.dom2wbsax;

import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbdom.EmptyElementType;

/**
 * This class defines properties of the WBSAX processor
 */
public interface WBSAXProcessorConfiguration {

    /**
     * Getter for the version code.
     *
     * @return The VersionCode for this processor.
     */
    VersionCode getVersionCode();

    /**
     * Getter for the public ID code.
     *
     * @return the public ID code for this processor.
     */
    PublicIdCode getPublicIdCode();

    /**
     * Get the empty {@link com.volantis.mcs.wbdom.EmptyElementType} using the element name.
     *
     * @param elementName
     *         the name of the element.
     * @return the empty {@link com.volantis.mcs.wbdom.EmptyElementType} using the element name.
     */
    EmptyElementType getEmptyElementType(String elementName);

    /**
     * Determine whether element and attribute is part of the element attribute
     * mapping that my contain asset URLs.
     * @param elementName
     *         the name of the element.
     * @param attributeName
     *         the name of the attribute.
     * @return
     */
    boolean isURLAttribute(String elementName, String attributeName);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/2	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Jun-04	4720/1	byron	VBM:2004061604 Core Emulation Facilities

 ===========================================================================
*/
