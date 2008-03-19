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

package com.volantis.mcs.protocols.html.menu;

import com.volantis.mcs.protocols.menu.shared.renderer.HorizontalSeparatorRenderer;
import com.volantis.mcs.protocols.menu.shared.renderer.MenuItemRenderedContent;
import com.volantis.mcs.protocols.separator.ArbitratorDecision;
import com.volantis.mcs.protocols.separator.SeparatedContent;
import com.volantis.mcs.protocols.separator.SeparatorArbitrator;
import com.volantis.mcs.protocols.separator.SeparatorRenderer;
import com.volantis.mcs.protocols.separator.shared.AbstractSeparatorArbitrator;

/**
 * Responsible for deciding whether orientation separators are written between
 * content.
 */
public class HTMLMenuOrientationSeparatorArbitrator
        extends AbstractSeparatorArbitrator {

    public static final SeparatorArbitrator INSTANCE
            = new HTMLMenuOrientationSeparatorArbitrator();

    protected void separatorTriggered(ArbitratorDecision decision) {
        decision.use(decision.getDeferredSeparator());
    }

    protected void flushTriggered(ArbitratorDecision decision) {
        makeDecision(decision);
    }

    protected void contentTriggered(ArbitratorDecision decision) {
        makeDecision(decision);
    }

    private void makeDecision(ArbitratorDecision decision) {
        SeparatorRenderer deferredSeparator = decision.getDeferredSeparator();

        SeparatedContent previousContent = decision.getPreviousContent();
        if (previousContent == null) {

            // Ignore the separator if this is at the start.
            decision.ignore();

        } else if (deferredSeparator instanceof HorizontalSeparatorRenderer) {
            // Horizontal separators are written unless both pieces of content are
            // images in which case they are not written out.
            SeparatedContent triggeringContent = decision.getTriggeringContent();

            if (triggeringContent == null) {
                triggeringContent = MenuItemRenderedContent.IMAGE;
            }

            // Ignore the separator if it is between two images.
            if (previousContent == MenuItemRenderedContent.IMAGE
                    && triggeringContent == MenuItemRenderedContent.IMAGE) {

                decision.ignore();
            } else {
                decision.use(deferredSeparator);
            }

        } else {
            // Vertical and all other separators are always rendered.
            decision.use(deferredSeparator);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 14-May-04	4318/2	pduffin	VBM:2004051207 Integrated separators into menu rendering

 12-May-04	4279/1	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 07-May-04	4164/1	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 ===========================================================================
*/
