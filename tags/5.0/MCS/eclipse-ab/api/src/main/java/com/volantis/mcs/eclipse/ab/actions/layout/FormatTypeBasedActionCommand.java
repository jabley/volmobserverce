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

import com.volantis.mcs.eclipse.common.odom.ODOMSelectionManager;
import com.volantis.mcs.layouts.FormatType;

import java.util.Arrays;

import org.jdom.Element;

/**
 * A base class for those action commands that utilize a format type to
 * determine specific action processing.
 */
public abstract class FormatTypeBasedActionCommand extends LayoutActionCommand {

    /**
     * This is the format type identifying the type of format to create
     *
     * @supplierCardinality 1
     */
    protected FormatType formatType;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param formatType identifies the type of format that this action command
     *                   will create. Must not be null and must pass the {@link
     *                   #checkFormatType} test
     * @param selectionManager selection manager that allows the
     *                         action to set the new format as the current
     *                         selection. Can not be null.
     */
    public FormatTypeBasedActionCommand(FormatType formatType,
                                        ODOMSelectionManager selectionManager) {
        super(selectionManager);
        if (formatType == null) {
            throw new IllegalArgumentException(
                    "A non-null format type must be specified"); //$NON-NLS-1$
        } else {
            checkFormatType(formatType);
        }

        this.formatType = formatType;
    }

    /**
     * Supporting method used to ensure that the format type is appropriate to
     * the action command implementation.
     *
     * @param formatType the format type to be used with this action command
     * @throws IllegalArgumentException if the format type is not appropriate
     */
    protected abstract void checkFormatType(FormatType formatType)
            throws IllegalArgumentException;

    /**
     * Supporting method that allows the given element to be made the
     * selection.
     *
     * @param element the element to be made the selection
     */
    protected void setSelection(Element element) {
        selectionManager.setSelection(Arrays.asList(new Object[]{element}));

    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jul-04	4886/1	allan	VBM:2004052812 Workaround TreeItem.getItems() bug and tidy up.

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Jan-04	2727/1	philws	VBM:2004012301 Fix clipboard management

 21-Jan-04	2635/1	philws	VBM:2003121513 Implement the New Grid and non-Grid Action Commands

 ===========================================================================
*/
