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
 * $Header: /src/voyager/com/volantis/mcs/protocols/wml/WMLEmptyOK1_3.java,v 1.3 2002/07/26 12:12:10 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 * ----------------------------------------------------------------------------
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Jul-02    Steve           VBM:2002072301 - Extension to the WML1.3 protocol
 *                              that outputs emptyok=true attributes on all 
 *                              input fields.
 * 26-Jul-02    Steve           VBM:2002072301 - No longer uses the 
 *                              requiresEmptyOK flag as it has been removed 
 *                              from WMLRoot. Instead the flag is set in 
 *                              XFTextInputAttributes, the attributes are then
 *                              passed on to the super class where the 
 *                              rendering is done.
 * 01-Jun-03    Mat             VBM:2003042906 - Removed doProtocolString()   
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.protocols.ProtocolConfiguration;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.protocols.ProtocolSupportFactory;
import com.volantis.mcs.protocols.XFTextInputAttributes;
import com.volantis.mcs.protocols.forms.validation.TextInputFormatParser;

public class WMLEmptyOK1_3 extends WMLVersion1_3 {

    /**
     * A text input validation parser that forces all formats to be
     * treated as if they allowed empty fields.
     */
    private static final TextInputFormatParser TEXT_INPUT_FORMAT_PARSER =
            new TextInputFormatParser(true);

    public WMLEmptyOK1_3(ProtocolSupportFactory supportFactory,
            ProtocolConfiguration configuration) {
        super(supportFactory, configuration);
    }

    /**
     * Override to force an empty ok attribute to be added to all input fields.
     */
    protected TextInputFormatParser getTextInputFormatParser() {
        return TEXT_INPUT_FORMAT_PARSER;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Nov-05	10321/1	emma	VBM:2005103109 Forward port: Styling not applied correctly to some xf selectors

 14-Nov-05	10300/1	emma	VBM:2005103109 Styling not applied correctly to some xf selectors

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 06-Jun-03	335/1	mat	VBM:2003042906 Merged changes to MCS

 ===========================================================================
*/
