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
package com.volantis.mcs.eclipse.ab.actions.layout;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;

/**
 * Provides a method that can be used, in an AssetsSection compatible manner,
 * to identify the type of transfer for a given element.
 *
 * @see com.volantis.mcs.eclipse.ab.editors.dom.AssetsSection
 */
public class TransferSupport {
    /**
     * This identities transfers of all layout formats (excluding the layout
     * and device layout elements which, while suffixed with the word "format",
     * are not really formats).
     */
    public final static String FORMAT_TRANSFER_ID = "Format"; //$NON-NLS-1$

    /**
     * Determines the transfer identifier to be used for a given element.
     *
     * <p><strong>NOTE</strong>: This is specifically designed to be compatible
     * with the {@link
     * com.volantis.mcs.eclipse.ab.editors.dom.AssetsSection#copySelectionToClipboard}
     * method implementation and must be kept in sync with that
     * implementation.</p>
     *
     * @param element the element to be transferred into the clipboard
     * @return the transfer ID to be used for the given element
     */
    public String getTransferId(ODOMElement element) {
        String result = null;

        if (element == null) {
            throw new IllegalArgumentException(
                "The element must be non-null"); //$NON-NLS-1$
        } else if (element.getParent() == null) {
            throw new IllegalArgumentException(
                "The element must not be the root node"); //$NON-NLS-1$
        } else if (element.getParent().getParent() == null) {
            // In order to be consistent with the AssetsSection, any immediate
            // child of the root node is given an ID of the root node
            result = element.getParent().getName();
        } else {
            result = FORMAT_TRANSFER_ID;
        }

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Jan-04	2720/1	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 13-Jan-04	2534/1	philws	VBM:2003121511 Action support classes and stub implementations of Layout actions

 ===========================================================================
*/
