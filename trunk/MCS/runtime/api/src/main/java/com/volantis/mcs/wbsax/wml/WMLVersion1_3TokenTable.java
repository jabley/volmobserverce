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
 * $Header:$
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-May-03    Geoff           VBM:2003042904 - Created; token table values 
 *                              for WML 1.3. 
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax.wml;

import com.volantis.mcs.wbsax.AttributeStartRegistrar;
import com.volantis.mcs.wbsax.ElementRegistrar;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.PublicIdFactory;

/**
 * Token table values for WML 1.3 (and 1.2, if there were any).
 * <p>
 * These were cut and pasted from the 1.3 spec.
 */ 
public class WMLVersion1_3TokenTable extends WMLVersion1_1TokenTable {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public VersionCode getVersion() {
        return VersionCode.V1_3;
    }

    public PublicIdCode getPublicId() {
        return PublicIdFactory.WML_1_3;
    }

    // Inherit Javadoc.
    public void registerTags(ElementRegistrar tags) {
        // Pick up all the 1.1 definitions.
        super.registerTags(tags);

        // And add the (1.2? &) 1.3 specific values.
        tags.registerElement(0x1B, "pre");
    }
    
    // Inherit Javadoc.
    public void registerAttrStarts(AttributeStartRegistrar attrStarts) {
        // Pick up all the 1.1 definitions.
        super.registerAttrStarts(attrStarts);
        
        // And add the (1.2? &) 1.3 specific values.
        attrStarts.registerAttributeStart(0x5E, "accesskey");
        
        attrStarts.registerAttributeStart(0x64, "cache-control", "no-cache");
        
        attrStarts.registerAttributeStart(0x5F, "enctype"); 
        attrStarts.registerAttributeStart(0x60, "enctype", "application/xwww-formurlencoded"); 
        attrStarts.registerAttributeStart(0x61, "enctype", "multipart/form-data");
        
        attrStarts.registerAttributeStart(0x62, "xml:space", "preserve"); 
        attrStarts.registerAttributeStart(0x63, "xml:space", "default");
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-Jul-03	790/3	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 11-Jul-03	781/1	geoff	VBM:2003070404 first clean up of WBSAX; javadocs and todos

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 ===========================================================================
*/
