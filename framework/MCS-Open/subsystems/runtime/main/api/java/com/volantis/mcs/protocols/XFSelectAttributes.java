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
 * $Header:  $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062810 - Created.
 * 23-Jul-01    Paul            VBM:2001070507 - Cleaned up.
 * 24-Jul-01    Paul            VBM:2001071103 - Added implementation of
 *                              doFormField method.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 19-Feb-02    Paul            VBM:2001100102 - Removed doFormField method.
 * 04-Mar-02    Paul            VBM:2001101803 - Changed getOptions method
 *                              to return a List rather than a Collection as
 *                              it is more efficient to access a list by index
 *                              than by iterator.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 31-Jul-2002  Sumit           VBM:2002073109 Added SelectOptionGroup methods
 *                              to allow optgroup nesting
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Node;

import java.util.ArrayList;
import java.util.List;

public class XFSelectAttributes
        extends XFFormFieldAttributes {

    private boolean multiple;
    private List options;

    /**
     * The entry pane node associated with these attributes.
     *
     * @see com.volantis.mcs.protocols.html.XHTMLBasic#doSelectInput
     */
    private Node insertAfterEntryPaneNode;

    /**
     * The caption pane node associated with these attributes.
     *
     * @see com.volantis.mcs.protocols.html.XHTMLBasic#doSelectInput
     */
    private Node insertAfterCaptionPaneNode;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public XFSelectAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {
        multiple = false;

        if (options == null) {
            options = new ArrayList();
        } else {
            options.clear();
        }

    }

    /**
     * Add a selection option to the collection of selection options.
     */
    public void addOption(SelectOption option) {
        options.add(option);
    }

    /**
     * Get the list of selection options.
     */
    public List getOptions() {
        return options;
    }

    public List getOptionGroup() {
        return options;
    }

    public void addOptionGroup(SelectOptionGroup optionlist) {
        options.add(optionlist);
    }

    /**
     * Set the multiple property.
     *
     * @param multiple The new value of the multiple property.
     */
    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    /**
     * Get the value of the multiple property.
     *
     * @return The value of the multiple property.
     */
    public boolean isMultiple() {
        return multiple;
    }

    /**
     * Return the associated pane node.
     *
     * @return the associated pane node.
     */
    public Node getInsertAfterEntryPaneNode() {
        return insertAfterEntryPaneNode;
    }

    /**
     * Associate a node with these attributes.
     *
     * @param node the node to be associated with.
     */
    public void setInsertAfterEntryPaneNode(Node node) {
        insertAfterEntryPaneNode = node;
    }

    /**
     * Return the associated caption pane node.
     *
     * @return the associated caption pane node.
     */
    public Node getInsertAfterCaptionPaneNode() {
        return insertAfterCaptionPaneNode;
    }

    /**
     * Associate a caption node with these attributes.
     *
     * @param node the node to be associated with.
     */
    public void setInsertAfterCaptionPaneNode(Node node) {
        this.insertAfterCaptionPaneNode = node;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 20-Jun-03	424/1	byron	VBM:2003022825 Enhance behaviour of pane element within xfform

 ===========================================================================
*/
