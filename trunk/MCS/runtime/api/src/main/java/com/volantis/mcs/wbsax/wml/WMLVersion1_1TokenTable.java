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
 *                              for WML 1.1. 
 * 29-May-03    Geoff           VBM:2003042905 - Update after implementing
 *                              WBDOM.
 * 09-Jun-03    Steve           VBM:2003060605 - Modified the 'format' attrStart
 *                              value. Code 0x12 is for format not m-data=format
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.wbsax.wml;

import com.volantis.mcs.wbsax.AttributeStartRegistrar;
import com.volantis.mcs.wbsax.AttributeValueRegistrar;
import com.volantis.mcs.wbsax.ElementRegistrar;
import com.volantis.mcs.wbsax.TokenTable;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.PublicIdFactory;

/**
 * Token table values for WML 1.1.
 * <p>
 * These were cut and pasted from the 1.1 spec.
 */ 
public class WMLVersion1_1TokenTable implements TokenTable {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    public VersionCode getVersion() {
        return VersionCode.V1_1;
    }

    public PublicIdCode getPublicId() {
        return PublicIdFactory.WML_1_1;
    }

    // Inherit Javadoc.
    public void registerTags(ElementRegistrar elements) {
        elements.registerElement(0x1C, "a");
        elements.registerElement(0x22, "anchor");
        elements.registerElement(0x23, "access");
        elements.registerElement(0x24, "b");
        elements.registerElement(0x25, "big");
        elements.registerElement(0x26, "br");
        elements.registerElement(0x27, "card");
        elements.registerElement(0x28, "do");
        elements.registerElement(0x29, "em");
        elements.registerElement(0x2A, "fieldset");
        elements.registerElement(0x2B, "go");
        elements.registerElement(0x2C, "head");
        elements.registerElement(0x2D, "i");
        elements.registerElement(0x2E, "img");
        elements.registerElement(0x2F, "input");
        elements.registerElement(0x30, "meta");
        elements.registerElement(0x31, "noop");
        elements.registerElement(0x20, "p");
        elements.registerElement(0x21, "postfield");
        elements.registerElement(0x32, "prev");
        elements.registerElement(0x33, "onevent");
        elements.registerElement(0x34, "optgroup");
        elements.registerElement(0x35, "option");
        elements.registerElement(0x36, "refresh");
        elements.registerElement(0x37, "select");
        elements.registerElement(0x3E, "setvar");
        elements.registerElement(0x38, "small");
        elements.registerElement(0x39, "strong");
        elements.registerElement(0x1F, "table");
        elements.registerElement(0x1D, "td");
        elements.registerElement(0x3B, "template");
        elements.registerElement(0x3C, "timer");
        elements.registerElement(0x1E, "tr");
        elements.registerElement(0x3D, "u");
        elements.registerElement(0x3F, "wml");
    }
    
    // Inherit Javadoc.
    public void registerAttrStarts(AttributeStartRegistrar attrStarts) {
        attrStarts.registerAttributeStart(0x05, "accept-charset"); 
        attrStarts.registerAttributeStart(0x52, "align"); 
        attrStarts.registerAttributeStart(0x06, "align", "bottom"); 
        attrStarts.registerAttributeStart(0x07, "align", "center"); 
        attrStarts.registerAttributeStart(0x08, "align", "left"); 
        attrStarts.registerAttributeStart(0x09, "align", "middle"); 
        attrStarts.registerAttributeStart(0x0A, "align", "right"); 
        attrStarts.registerAttributeStart(0x0B, "align", "top"); 
        attrStarts.registerAttributeStart(0x0C, "alt"); 
        attrStarts.registerAttributeStart(0x54, "class"); 
        attrStarts.registerAttributeStart(0x53, "columns"); 
        attrStarts.registerAttributeStart(0x0D, "content"); 
        attrStarts.registerAttributeStart(0x5C, "content", "application/vnd.wap.wmlc;charset="); 
        attrStarts.registerAttributeStart(0x0F, "domain"); 
        attrStarts.registerAttributeStart(0x10, "emptyok", "false"); 
        attrStarts.registerAttributeStart(0x11, "emptyok", "true"); 
        attrStarts.registerAttributeStart(0x12, "format"); 
        attrStarts.registerAttributeStart(0x56, "forua", "false"); 
        attrStarts.registerAttributeStart(0x57, "forua", "true"); 
        attrStarts.registerAttributeStart(0x13, "height"); 
        attrStarts.registerAttributeStart(0x4A, "href"); 
        attrStarts.registerAttributeStart(0x4B, "href", "http://"); 
        attrStarts.registerAttributeStart(0x4C, "href", "https://"); 
        attrStarts.registerAttributeStart(0x14, "hspace"); 
        attrStarts.registerAttributeStart(0x5A, "http-equiv"); 
        attrStarts.registerAttributeStart(0x5B, "http-equiv", "Content-Type"); 
        attrStarts.registerAttributeStart(0x5D, "http-equiv", "Expires"); 
        attrStarts.registerAttributeStart(0x55, "id"); 
        attrStarts.registerAttributeStart(0x15, "ivalue"); 
        attrStarts.registerAttributeStart(0x16, "iname"); 
        attrStarts.registerAttributeStart(0x18, "label"); 
        attrStarts.registerAttributeStart(0x19, "localsrc"); 
        attrStarts.registerAttributeStart(0x1A, "maxlength"); 
        attrStarts.registerAttributeStart(0x1B, "method", "get"); 
        attrStarts.registerAttributeStart(0x1C, "method", "post"); 
        attrStarts.registerAttributeStart(0x1D, "mode", "nowrap"); 
        attrStarts.registerAttributeStart(0x1E, "mode", "wrap"); 
        attrStarts.registerAttributeStart(0x1F, "multiple", "false");
        attrStarts.registerAttributeStart(0x20, "multiple", "true"); 
        attrStarts.registerAttributeStart(0x21, "name"); 
        attrStarts.registerAttributeStart(0x22, "newcontext", "false"); 
        attrStarts.registerAttributeStart(0x23, "newcontext", "true"); 
        attrStarts.registerAttributeStart(0x25, "onenterbackward"); 
        attrStarts.registerAttributeStart(0x26, "onenterforward"); 
        attrStarts.registerAttributeStart(0x24, "onpick"); 
        attrStarts.registerAttributeStart(0x27, "ontimer"); 
        attrStarts.registerAttributeStart(0x28, "optional", "false"); 
        attrStarts.registerAttributeStart(0x29, "optional", "true"); 
        attrStarts.registerAttributeStart(0x2A, "path"); 
        attrStarts.registerAttributeStart(0x2E, "scheme"); 
        attrStarts.registerAttributeStart(0x2F, "sendreferer", "false"); 
        attrStarts.registerAttributeStart(0x30, "sendreferer", "true"); 
        attrStarts.registerAttributeStart(0x31, "size"); 
        attrStarts.registerAttributeStart(0x32, "src"); 
        attrStarts.registerAttributeStart(0x58, "src", "http://"); 
        attrStarts.registerAttributeStart(0x59, "src", "https://"); 
        attrStarts.registerAttributeStart(0x33, "ordered", "true"); 
        attrStarts.registerAttributeStart(0x34, "ordered", "false"); 
        attrStarts.registerAttributeStart(0x35, "tabindex"); 
        attrStarts.registerAttributeStart(0x36, "title"); 
        attrStarts.registerAttributeStart(0x37, "type"); 
        attrStarts.registerAttributeStart(0x38, "type", "accept"); 
        attrStarts.registerAttributeStart(0x39, "type", "delete"); 
        attrStarts.registerAttributeStart(0x3A, "type", "help"); 
        attrStarts.registerAttributeStart(0x3B, "type", "password"); 
        attrStarts.registerAttributeStart(0x3C, "type", "onpick"); 
        attrStarts.registerAttributeStart(0x3D, "type", "onenterbackward"); 
        attrStarts.registerAttributeStart(0x3E, "type", "onenterforward"); 
        attrStarts.registerAttributeStart(0x3F, "type", "ontimer"); 
        attrStarts.registerAttributeStart(0x45, "type", "options"); 
        attrStarts.registerAttributeStart(0x46, "type", "prev"); 
        attrStarts.registerAttributeStart(0x47, "type", "reset"); 
        attrStarts.registerAttributeStart(0x48, "type", "text"); 
        attrStarts.registerAttributeStart(0x49, "type", "vnd."); 
        attrStarts.registerAttributeStart(0x4D, "value"); 
        attrStarts.registerAttributeStart(0x4E, "vspace"); 
        attrStarts.registerAttributeStart(0x4F, "width"); 
        attrStarts.registerAttributeStart(0x50, "xml:lang"); 
    }
    
    // Inherit Javadoc.
    public void registerAttrValues(AttributeValueRegistrar attrValues) {
        attrValues.registerAttributeValue(0x85, ".com/"); 
        attrValues.registerAttributeValue(0x86, ".edu/"); 
        attrValues.registerAttributeValue(0x87, ".net/"); 
        attrValues.registerAttributeValue(0x88, ".org/"); 
        attrValues.registerAttributeValue(0x89, "accept"); 
        attrValues.registerAttributeValue(0x8A, "bottom"); 
        attrValues.registerAttributeValue(0x8B, "clear"); 
        attrValues.registerAttributeValue(0x8C, "delete"); 
        attrValues.registerAttributeValue(0x8D, "help"); 
        attrValues.registerAttributeValue(0x8E, "http://"); 
        attrValues.registerAttributeValue(0x8F, "http://www."); 
        attrValues.registerAttributeValue(0x90, "https://"); 
        attrValues.registerAttributeValue(0x91, "https://www."); 
        attrValues.registerAttributeValue(0x93, "middle"); 
        attrValues.registerAttributeValue(0x94, "nowrap"); 
        attrValues.registerAttributeValue(0x96, "onenterbackward"); 
        attrValues.registerAttributeValue(0x97, "onenterforward"); 
        attrValues.registerAttributeValue(0x95, "onpick"); 
        attrValues.registerAttributeValue(0x98, "ontimer"); 
        attrValues.registerAttributeValue(0x99, "options"); 
        attrValues.registerAttributeValue(0x9A, "password"); 
        attrValues.registerAttributeValue(0x9B, "reset"); 
        attrValues.registerAttributeValue(0x9D, "text"); 
        attrValues.registerAttributeValue(0x9E, "top"); 
        attrValues.registerAttributeValue(0x9F, "unknown"); 
        attrValues.registerAttributeValue(0xA0, "wrap"); 
        attrValues.registerAttributeValue(0xA1, "www."); 
        // Note, spec says "Www" for this last entry. 
        // We consider this to be a typo. 
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

 09-Jun-03	349/1	steve	VBM:2003060605 format attribute correction

 ===========================================================================
*/
