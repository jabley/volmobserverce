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
 * 18-May-03    Geoff           VBM:2003042904 - Created; a factory for 
 *                              creating PublicIdCodes. 
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * 30-May-03    Geoff           VBM:2003042918 - Implement dissection 
 *                              WBSAX serialiser.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax;

import java.util.Map;
import java.util.HashMap;

/**
 * A factory for creating {@link PublicIdCode}s.
 * <p>
 * Currently this factory does very little, apart from statically associate
 * the code, name and dtd url values for the small known set we require at the 
 * moment. This is all that is required for our current, limited "serialising" 
 * style usage. 
 * <p>
 * If this API was to support more dynamic usage and/or "parsing", we could 
 * extended this class to create public ids for dynamic values by adding: 
 * <ul>
 *   <li>a create(String) method to create from a generic lump of WML.
 *   <li>a create(id) method to create from a binary input stream.
 * </ul>
 */ 
public class PublicIdFactory {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    // The names for the various public id's we support.
    
    private static final String WML_1_1_NAME = "-//WAPFORUM//DTD WML 1.1//EN";
    private static final String WML_1_3_NAME = "-//WAPFORUM//DTD WML 1.3//EN";
    private static final String PHONE_DOT_COM_1_1_NAME = 
            "-//PHONE.COM//DTD WML 1.1//EN";
    private static final String OPENWAVE_1_3_NAME = 
            "-//OPENWAVE.COM//DTD WML 1.3//EN";

    // The DTD URLs for the various public id's we support.
    
    private static final String WML_1_1_DTD = 
            "http://www.wapforum.org/DTD/wml_1.1.xml";
    private static final String WML_1_3_DTD = 
            "http://www.wapforum.org/DTD/wml13.dtd";
    private static final String PHONE_DOT_COM_1_1_DTD = 
            "http://www.phone.com/dtd/wml11.dtd";
    private static final String OPENWAVE_1_3_DTD = 
            "http://www.openwave.com/dtd/wml13.dtd";
    
    // The codes for the various public id's we support.
    
    // Note that 0x00 is explicitly invalid.
    public static final int UNKNOWN_CODE = 0x01;
    private static final int WML_1_1_CODE = 0x04;
    private static final int WML_1_3_CODE = 0x0A;
    private static final int PHONE_DOT_COM_1_1_CODE = 0x1108;
    private static final int OPENWAVE_1_3_CODE = 0x110D;

    /**
     * A public id code representing an unknown public id. 
     */ 
    public static final PublicIdCode UNKNOWN = 
            new PublicIdCode(UNKNOWN_CODE, null, null);
    
    /**
     * A public id code representing WML 1.1.
     */ 
    public static final PublicIdCode WML_1_1 = 
            new PublicIdCode(WML_1_1_CODE, WML_1_1_NAME, WML_1_1_DTD);

    /**
     * A public id code representing WML 1.3.
     */ 
    public static final PublicIdCode WML_1_3 = 
            new PublicIdCode(WML_1_3_CODE, WML_1_3_NAME, WML_1_3_DTD);
    
    /**
     * A public id code representing Phone.com's WML 1.1 dialect.
     */ 
    public static final PublicIdCode PHONE_DOT_COM_1_1 = 
            new PublicIdCode(PHONE_DOT_COM_1_1_CODE, PHONE_DOT_COM_1_1_NAME,
                    PHONE_DOT_COM_1_1_DTD);
    /**
     * A public id code representing Openwave's WML 1.3 dialect.
     */ 
    public static final PublicIdCode OPENWAVE_1_3 = 
            new PublicIdCode(OPENWAVE_1_3_CODE, OPENWAVE_1_3_NAME, 
                    OPENWAVE_1_3_DTD);

    private Map publicIds;

    public PublicIdFactory() {
        publicIds = new HashMap();
        register(WML_1_1);
        register(WML_1_3);
        register(PHONE_DOT_COM_1_1);
        register(OPENWAVE_1_3);
    }

    public PublicIdCode create(String publicId) {
        PublicIdCode id = (PublicIdCode) publicIds.get(publicId);
        if (id == null) {
            id = UNKNOWN;
        }
        return id;
    }
    
    public void register(int code, String name, String uri) {
        publicIds.put(name, new PublicIdCode(code, name, uri));
    }

    protected void register(PublicIdCode code) {
        publicIds.put(code.getName(), code);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 31-Mar-04	3662/1	steve	VBM:2004032907 Incorrect DTD for WML 1.1

 30-Mar-04	3657/1	steve	VBM:2004032907 Incorrect DTD for WML 1.1

 ===========================================================================
*/
