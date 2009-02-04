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
package com.volantis.mcs.build.themes;

import org.jdom.ProcessingInstruction;

/**
 * Default, abstract implementation of ThemeVersionInfo.
 */
public abstract class AbstractThemeVersionInfo implements ThemeVersionInfo {

    boolean supportedInCSS2;

    boolean supportedInCSS1;

    boolean supportedInCSSMobile;

    boolean supportedInCSSWAP;

    /**
     * Set the versions information in this info object by inspecting the
     * related values in a processing instruction.
     *
     * @param pi the processing instruction with version information.
     */
    public void setVersions(ProcessingInstruction pi) {
        supportedInCSS2 = !"false".equals(pi.getValue("css2"));
        supportedInCSS1 = !"false".equals(pi.getValue("css1"));
        supportedInCSSMobile = !"false".equals(pi.getValue("cssmobile"));
        supportedInCSSWAP = !"false".equals(pi.getValue("csswap"));
    }

    // Javadoc inherited.
    public boolean isSupportedInCSS1() {
        return supportedInCSS1;
    }

    // Javadoc inherited.
    public boolean isSupportedInCSS2() {
        return supportedInCSS2;
    }

    // Javadoc inherited.
    public boolean isSupportedInCSSMobile() {
        return supportedInCSSMobile;
    }

    // Javadoc inherited.
    public boolean isSupportedInCSSWAP() {
        return supportedInCSSWAP;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
