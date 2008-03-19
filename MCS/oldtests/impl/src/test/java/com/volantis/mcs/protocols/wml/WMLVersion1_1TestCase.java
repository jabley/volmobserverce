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
package com.volantis.mcs.protocols.wml;

import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.builder.ProtocolBuilder;
import com.volantis.mcs.protocols.builder.TestProtocolRegistry;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.devices.InternalDevice;

/**
 * Test the WML 1.1 protocol
 */
public class WMLVersion1_1TestCase extends WMLRootTestCase {

    public WMLVersion1_1TestCase(String name) {
        super(name);
    }

    protected VolantisProtocol createTestableProtocol(
            InternalDevice internalDevice) {
        ProtocolBuilder builder = new ProtocolBuilder();
        VolantisProtocol protocol = (VolantisProtocol) builder.build(
                new TestProtocolRegistry.TestWMLVersion1_1Factory(),
                internalDevice);
        return protocol;
    }


    // javadoc inherited
    protected void templateTestDoMenuHorizontal(StringBuffer buf,
                                                MenuItem menuItem,
                                                boolean hasNext) {
        buf.append("<a href=\"").append(menuItem.getHref()).append("\">");
        buf.append(menuItem.getText()).append("</a>");
        if (hasNext) {
            buf.append("&#160;");
        }
    }

    // javadoc inherited
    protected String getExpectedDissectingPaneMarkup(DissectingPane pane, DissectingPaneAttributes attr) {
        String expected =
          "<" + DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT + "/>" +
          "<" + DissectionConstants.SHARD_LINK_GROUP_ELEMENT + ">" +
            "<p mode=\"wrap\">" +
              "<" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
                "<a href=\"" + DissectionConstants.URL_MAGIC_CHAR + "\">" +
                attr.getLinkText() +
                "</a>" +
              "</" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
              "<" + DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT + ">" +
        '\u00a0' +
          "</" + DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT + ">" + 
              "<" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
                "<a href=\"" + DissectionConstants.URL_MAGIC_CHAR +"\">" +
                attr.getBackLinkText() +
                "</a>" +
              "</" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
            "</p>" +
          "</" + DissectionConstants.SHARD_LINK_GROUP_ELEMENT + ">";
        return expected;
    }

    /**
     * WML v1.2 should pick up WML v1.1's name, dtd and code.
     */
    public void testProtocolHasCorrectDTD() throws Exception {

        final PublicIdCode publicIdCode = ((WMLRootConfiguration)protocol.
            getProtocolConfiguration()).publicIdCode;

        assertEquals("DTD should match",
                     "http://www.wapforum.org/DTD/wml_1.1.xml",
                     publicIdCode.getDtd());

        assertEquals("Name should match",
                     "-//WAPFORUM//DTD WML 1.1//EN",
                     publicIdCode.getName());

        assertEquals("Code should match",
                     0x04,
                     publicIdCode.getInteger());
    }

}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Sep-05	9375/3	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/2	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/5	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/2	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/5	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 03-Feb-05	6129/1	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 11-Oct-04	5744/1	claire	VBM:2004092801 mergevbm: Encoding of style class names for inclusions

 11-Oct-04	5742/1	claire	VBM:2004092801 Encoding of style class names for inclusions

 07-Oct-04	5729/3	claire	VBM:2004092801 Encoding of style class names for inclusions

 20-Sep-04	5575/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 20-Sep-04	5527/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 12-Jul-04	4783/1	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 14-May-04	4315/2	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 05-May-04	4167/3	steve	VBM:2004042901 Fix up javadoc

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 04-May-04	4117/1	steve	VBM:2004042901 Style class rendering fix

 31-Mar-04	3662/2	steve	VBM:2004032907 Incorrect DTD for WML 1.1

 30-Mar-04	3657/1	steve	VBM:2004032907 Incorrect DTD for WML 1.1

 ===========================================================================
*/
