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

package com.volantis.mcs.protocols.html;

import com.volantis.mcs.protocols.EventConstants;
import com.volantis.mcs.protocols.ValidationHelper;

public class XHTMLFullConfiguration
        extends XHTMLBasicConfiguration {

    /**
     * Initialise.
     */
    public XHTMLFullConfiguration() {
        this(false);
    }

    /**
     * Initialise.
     *
     * @param supportsDissection Specifies whether the protocol supports
     * dissection.
     */
    protected XHTMLFullConfiguration(boolean supportsDissection) {
        super(supportsDissection);
        this.canSupportSubScriptElement = true;
        this.canSupportSuperScriptElement = true;
        this.canSupportEvents = true;
        supportedGeneralEvents = EventConstants.GENERAL_EVENTS_MASK;

        highlightRuleSet = new com.volantis.mcs.protocols.highlight.HtmlRuleSet();
        cornersRuleSet = new com.volantis.mcs.protocols.corners.CornersRuleSet();
    }

    public ValidationHelper getValidationHelper() {
        return XHTMLFullValidationHelper.getDefaultInstance();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/3	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 14-Sep-05	9472/3	ibush	VBM:2005090808 Add default styling for sub/sup elements

 09-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 01-Sep-05	9375/1	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 23-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 ===========================================================================
*/
