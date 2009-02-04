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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.PublicIdFactory;
import com.volantis.mcs.wbsax.wml.WMLVersion1_1TokenTable;

/**
 * A clunky config object for Phone.com's version of WML 1.1.
 * <p/>
 * This will be cleaned up when we make protocol configuration work with
 * with the WBDOM properly, hopefully.
 */
public class WMLPhoneDotComConfiguration extends WMLVersion1_1Configuration {

    public WMLPhoneDotComConfiguration() {
        super();
        // Generating WMLC using wget and -U "SIE-C3I/3.0 UP/4.1.16m" and
        // --header='Accept:application/vnd.wap.wmlc', and viewing the WMLC
        // on a Siemens C35 has proved that 0x76 works for access keys and the
        // 0x5E value from the WML 1.3 spec does not. See VBM 2005052708 for
        // other codes that have been obtained.
        attributeStartFactory.registerAttributeStart(0x76, "accesskey");

        // Use the WML v1.1 token table with updated PublicId (DTD)
        useTokenTable(new WMLVersion1_1TokenTable() {
            public PublicIdCode getPublicId() {
                return PublicIdFactory.PHONE_DOT_COM_1_1;
            }
        });
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Oct-05	9522/1	ibush	VBM:2005091502 no_save on images

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 02-Jun-05	8647/1	pcameron	VBM:2005052708 Fixed accesskey code for openwave wmlc browsers

 02-Jun-05	8645/3	pcameron	VBM:2005052708 Fixed accesskey code for openwave wmlc browsers

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 04-Sep-03	1331/2	byron	VBM:2003090201 OpenWave protocols not using correct DTD

 10-Jul-03	774/1	geoff	VBM:2003070703 merge from mimas and fix renames manually

 10-Jul-03	770/1	geoff	VBM:2003070703 merge from metis and rename files manually

 10-Jul-03	751/1	geoff	VBM:2003070703 second go at cleaning up WBDOM test cases

 10-Jun-03	347/1	geoff	VBM:2003060901 implement accesskey attribute for WBSAX output; not tested on phone.

 ===========================================================================
*/
