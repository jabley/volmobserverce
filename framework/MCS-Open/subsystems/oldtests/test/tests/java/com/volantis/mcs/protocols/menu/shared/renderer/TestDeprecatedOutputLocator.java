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
package com.volantis.mcs.protocols.menu.shared.renderer;

import com.volantis.mcs.protocols.html.menu.DeprecatedEventAttributeUpdater;
import com.volantis.mcs.protocols.html.menu.DeprecatedExternalShortcutRenderer;
import com.volantis.mcs.protocols.html.menu.TestDeprecatedEventAttributeRenderer;

/**
 * A test implementation of {@link DeprecatedOutputLocator} which returns
 * Test implementations of the markup output interfaces.
 */ 
public class TestDeprecatedOutputLocator implements DeprecatedOutputLocator {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    private final DeprecatedDivOutput divOutput;
    private final DeprecatedSpanOutput spanOutput;
    private final DeprecatedImageOutput imageOutput;
    private final DeprecatedAnchorOutput anchorOutput;
    private final DeprecatedEventAttributeUpdater eventOutput;
    private final DeprecatedExternalShortcutRenderer shortcut;

    public TestDeprecatedOutputLocator() {
        divOutput = new TestDeprecatedDivOutput();
        spanOutput = new TestDeprecatedSpanOutput();
        imageOutput = new TestDeprecatedImageOutput();
        anchorOutput = new TestDeprecatedAnchorOutput();
        eventOutput = new TestDeprecatedEventAttributeRenderer();
        shortcut = null;
    }

    // Javadoc inherited.
    public DeprecatedDivOutput getDivOutput() {
        return divOutput;
    }

    // Javadoc inherited.
    public DeprecatedSpanOutput getSpanOutput() {
        return spanOutput;
    }

    // Javadoc inherited.
    public DeprecatedImageOutput getImageOutput() {
        return imageOutput;
    }

    // Javadoc inherited.
    public DeprecatedAnchorOutput getAnchorOutput() {
        return anchorOutput;
    }

    // Javadoc inherited.
    public DeprecatedLineBreakOutput getLineBreakOutput() {
        return null;
    }

    // Javadoc inherited.
    public DeprecatedEventAttributeUpdater getEventAttributeUpdater() {
        return eventOutput;
    }

    // Javadoc inherited.
    public DeprecatedExternalShortcutRenderer getExternalShortcutRenderer() {
        return shortcut;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Oct-04	5635/1	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 ===========================================================================
*/
