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
package com.volantis.mcs.protocols.trans;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.HashSet;
import java.util.Set;

/**
 * A style emulation element tracker that is used to store information about 
 * whether a particular element has been visited already. <p>
 *
 * This may be used whilst traversing the DOM in order to determine whether an
 * element has been visited (opened) and if so it may be safely removed (it is
 * redundant). <p>
 *
 * For example:
 *
 * <pre>
 *         n0
 *         |
 *         b
 *       /  \
 *     n1    b   <-- current element being processed.
 *          / \
 *        n2    p
 *             / \
 *            n4  n5
 * </pre>
 *
 * The tracker will have visited n0, b and n1 already (depth-first traversal)
 * and these elements will be 'open'. When the current element is processed we
 * know that it is already open and it may be safely removed.
 */
public final class StyleEmulationElementTracker {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(StyleEmulationElementTracker.class);

    /**
     * The set of open elements.
     */
    private final Set openedElements = new HashSet();

    /**
     * Track this element as being opened. If it has already been opened,
     * the open request will be ignored.
     *
     * @param element the element name to open.
     */
    void opened(String element) {
        if (isOpen(element)) {
            if (logger.isDebugEnabled()) {
                logger.debug("This element is already tracked as open: " +
                        element);
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Opened: " + element);
            }
            openedElements.add(element);
        }
    }

    /**
     * Track this element as closed. If it has already been closed, the close
     * request will be ignored.
     *
     * @param element the element name to close.
     */
    void closed(String element) {
        if (logger.isDebugEnabled()) {
            logger.debug("Closed: " + element);
        }
        openedElements.remove(element);
    }

    /**
     * Return true if this element is open, false otherwise.
     *
     * @param element the element name to check to see if it has been opened.
     * @return true if this element is open, false otherwise.
     */
    boolean isOpen(String element) {
        return openedElements.contains(element);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 14-Jul-04	4783/2	geoff	VBM:2004062302 Implementation of theme style options: WML Family (fixes after style inversion approval)

 14-Jul-04	4752/5	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities - addressed rework issues

 13-Jul-04	4752/3	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 13-Jul-04	4752/1	byron	VBM:2004062301 Implementation of theme style options: Style Inversion Facilities

 ===========================================================================
*/
