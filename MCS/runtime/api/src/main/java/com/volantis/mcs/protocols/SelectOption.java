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
 * $Header: /src/voyager/com/volantis/mcs/protocols/SelectOption.java,v 1.8 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Jun-01    Paul            VBM:2001062810 - Created.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 04-Sep-01    Paul            VBM:2001081707 - Changed caption's type to
 *                              Object to allow TextComponentNames to be
 *                              passed through to the protocol.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 26-Jun-02    Steve           VBM:2002062401 Added attributes entryPane,
 *                              captionPane and captionClass
 * 01-Oct-02    Allan           VBM:2002093002 - Added the prompt property.
 * 20-Jan-03    Doug            VBM:2002120213 - Class now implements the
 *                              Option interface. Implemented the visit()
 *                              method.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Node;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.styling.Styles;

import java.util.List;

/**
 * The select option attributes.
 * @todo this class (and SelectOptionGroup) should be renamed to SelectOptionAttributes. However empty/dummy SelectOption and SelectOptionGroup interfaces may need to be introduced.
 * @mock.generate 
 */
public class SelectOption
        extends MCSAttributes implements Option, CaptionAwareXFFormAttributes {

    private TextAssetReference caption;
    private boolean selected;
    private String value;
    private ContainerInstance captionContainerInstance;
    private ContainerInstance entryContainerInstance;
    private Styles captionStyles;
    private TextAssetReference prompt;

    /**
     * Store the pane node that may be used to insert elements after.
     * @see com.volantis.mcs.protocols.html.XHTMLBasic#doSelectInput
     */
    private Node insertAfterEntryPaneNode;

    /**
     * Store the pane node that may be used to insert elements after.
     * @see com.volantis.mcs.protocols.html.XHTMLBasic#doSelectInput
     */
    private Node insertAfterCaptionPaneNode;


    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public SelectOption() {
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
        caption = null;
        selected = false;
        value = null;
        captionContainerInstance = null;
        entryContainerInstance = null;
        captionStyles = null;
        prompt = null;
    }

    /**
     * Set the value of the caption property.
     * @param caption The new value of the caption property.
     */
    public void setCaption(TextAssetReference caption) {
        this.caption = caption;
    }

    /**
     * Get the value of the caption property.
     * @return The value of the caption property.
     */
    public TextAssetReference getCaption() {
        return caption;
    }

    /**
     * Set the value of the prompt property.
     * @param prompt The new value of the prompt property.
     */
    public void setPrompt(TextAssetReference prompt) {
        this.prompt = prompt;
    }

    /**
     * Get the value of the prompt property.
     * @return The value of the prompt property.
     */
    public TextAssetReference getPrompt() {
        return prompt;
    }

    /**
     * Set the value of the selected property.
     * @param selected The new value of the selected property.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Get the value of the selected property.
     * @return The value of the selected property.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Set the value of the value property.
     * @param value The new value of the value property.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the value of the value property.
     * @return The value of the value property.
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the caption styles
     */
    public void setCaptionStyles(final Styles value) {
        this.captionStyles = value;
    }

    /**
     * Get the caption styles
     */
    public Styles getCaptionStyles() {
        return captionStyles;
    }

    // Javadoc inherited.
    public void setCaptionContainerInstance(
            ContainerInstance captionContainerInstance) {
        this.captionContainerInstance = captionContainerInstance;
    }

    // Javadoc inherited.
    public ContainerInstance getCaptionContainerInstance() {
        return captionContainerInstance;
    }

    // Javadoc inherited.
    public void setEntryContainerInstance(
            ContainerInstance entryContainerInstance) {
        this.entryContainerInstance = entryContainerInstance;
    }

    // Javadoc inherited.
    public ContainerInstance getEntryContainerInstance() {
        return entryContainerInstance;
    }

    // javadoc inherited from Option interface
    public void visit(OptionVisitor optionVisitor, Object object)
            throws ProtocolException {
        optionVisitor.visit(this, object);
    }

    /**
     * Set selected if the given value set contains this value.
     *
     * @param selectedValues which may be null
     */
    public void selectedValues(List selectedValues) {

        selected = value != null && selectedValues.contains(value);

    }

    /**
     * Return the associated pane node.
     *
     * @return      the associated pane node.
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
     * Return the associated pane node.
     *
     * @return      the associated pane node.
     */
    public Node getInsertAfterCaptionPaneNode() {
        return insertAfterCaptionPaneNode;
    }

    /**
     * Associate a node with these attributes.
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

 30-Sep-05	9637/1	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 25-Aug-05	9370/1	gkoch	VBM:2005070507 xform select option to store caption styles instead of caption (style) class

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-Nov-04	6135/4	byron	VBM:2004081726 Allow spatial format iterators within forms

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 20-Jun-03	424/1	byron	VBM:2003022825 Enhance behaviour of pane element within xfform

 ===========================================================================
*/
