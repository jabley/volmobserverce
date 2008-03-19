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
import com.volantis.mcs.protocols.MenuItem;
import com.volantis.mcs.protocols.DissectingPaneAttributes;
import com.volantis.mcs.protocols.dissection.DissectionConstants;

/**
 * Abstract class that sits between the WML 1.1 protocol and later versions.
 * All later versions support the access key attribute on the anchor tag so
 * it is added here. In addition, the phone.com version of the 1.1 protocol also
 * supports the access key attribute and can therefore extend this class too.
 * This then gets rid of the duplicated methods in the Phone.com and WML 1.2
 * test cases.
 */
abstract public class WMLVersion1_1AccessTestCaseAbstract
    extends WMLVersion1_1TestCase {

    /**
     * Create a WML 1.1 test case with access key support.
     */
    public WMLVersion1_1AccessTestCaseAbstract(String name) {
        super(name);
    }

    /**
     * Create a horizontal menu with access key support
     */
    protected void templateTestDoMenuHorizontal(StringBuffer buf,
            MenuItem menuItem, boolean hasNext) {
        buf.append("<a");
        if (menuItem.getShortcut() != null) {
            buf.append(" accesskey=\"" + menuItem.getShortcut() + "\"");
        }
        buf.append(" href=\"").append(menuItem.getHref()).append("\">");
        buf.append(menuItem.getText()).append("</a>");
        if (hasNext) {
            buf.append("&#160;");
        }
    }

    /**
     * Create the markup for a dissecting pane with access key support
     */
    protected String getExpectedDissectingPaneMarkup(DissectingPane pane, DissectingPaneAttributes attr) {
        String expected =
            "<" + DissectionConstants.DISSECTABLE_CONTENTS_ELEMENT + "/>" +
            "<" + DissectionConstants.SHARD_LINK_GROUP_ELEMENT + ">" +
            "<p mode=\"wrap\">" +
            "<" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
            "<a accesskey=\"" + pane.getNextShardShortcut() + "\" " +
            "href=\"" + DissectionConstants.URL_MAGIC_CHAR + "\">" +
                pane.getNextShardShortcut() + " " +
            attr.getLinkText() +
            "</a>" +
            "</" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
            "<" + DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT + ">" +
            '\u00a0' +
            "</" + DissectionConstants.SHARD_LINK_CONDITIONAL_ELEMENT + ">" + 
            "<" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
            "<a accesskey=\"" + pane.getPreviousShardShortcut() + "\" " +
            "href=\"" + DissectionConstants.URL_MAGIC_CHAR +"\">" +
                pane.getPreviousShardShortcut() + " " +
            attr.getBackLinkText() +
            "</a>" +
            "</" + DissectionConstants.SHARD_LINK_ELEMENT + ">" +
            "</p>" +
            "</" + DissectionConstants.SHARD_LINK_GROUP_ELEMENT + ">";
        return expected;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Aug-05	9353/3	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Sep-04	5575/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 20-Sep-04	5527/1	tom	VBM:2004091402 Previous and Next work with correct names and are the right way around

 14-May-04	4318/1	pduffin	VBM:2004051207 Integrated separators into menu rendering

 14-May-04	4315/2	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 05-May-04	4167/3	steve	VBM:2004042901 Fix up javadoc

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 04-May-04	4117/1	steve	VBM:2004042901 Style class rendering fix

 30-Mar-04	3657/1	steve	VBM:2004032907 Incorrect DTD for WML 1.1

 ===========================================================================
*/
