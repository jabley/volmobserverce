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
package com.volantis.mcs.eclipse.ab.actions;

import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.ab.editors.dom.ODOMElementTransfer;
import com.volantis.mcs.xml.xpath.XPath;

import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.Clipboard;

/**
 * Provides common ODOM element selection to clipboard copying.
 */
public class ClipboardSupport {
    /**
     * The factory to be placed in the transfer.
     */
    private ODOMFactory factory = new ODOMFactory();

    /**
     * The attribute which contains the xpath of the cloned element. Should not
     * appear in output.
     */
    public static final String XPATH = "xpath";

    /**
     * The clipboard to be managed.
     */
    Clipboard clipboard;

    public ClipboardSupport(Clipboard clipboard) {
        if (clipboard == null) {
            throw new IllegalArgumentException(
                "A non-null clipboard must be provided"); //$NON-NLS-1$
        }

        this.clipboard = clipboard;
    }

    /**
     * Copies the detail's element selection to the clipboard using the given
     * <code>transferId</code> as the transfer identifier.
     *
     * @param details    the event action details who's element selections are
     *                   to be copied into the clipboard
     * @param transferId the ID for the transfer placed in the clipboard
     */
    public void copySelectionToClipboard(ODOMActionDetails details,
                                         String transferId) {
        ODOMElement[] clones = details.getElementsClone();
        for (int i = 0; i < clones.length; i++) {
            final String xpath =
                    new XPath(details.getElement(i)).getExternalForm();
            clones[i].setAttribute(XPATH, xpath);
        }
        Object[] values = {clones};

        Transfer[] types = {
            new ODOMElementTransfer(factory,
                                    transferId)
        };

        clipboard.setContents(values, types);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 11-Feb-04	2939/1	eduardo	VBM:2004020506 ODOM DeleteActionCommand changed to be undo/redo friendly

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Jan-04	2727/2	philws	VBM:2004012301 Fix clipboard management

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
