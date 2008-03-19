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
 * $Header: /src/voyager/com/volantis/mcs/protocols/MenuItem.java,v 1.12 2003/04/24 16:42:23 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 23-Jul-01    Paul            VBM:2001070507 - Created.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 04-Sep-01    Paul            VBM:2001081707 - Changed accessKey's type to
 *                              Object to allow TextComponentNames to be
 *                              passed through to the protocol.
 * 20-Sep-01    Paul            VBM:2001091904 - Renamed accessKey to shortcut.
 * 21-Sep-01    Doug            VBM:2001090302 - Changed href's type to
 *                              Object to allow LinkComponentNames to be
 *                              passed through to the protocol.
 * 25-Sep-01    Allan           VBM:2001090609 - Removed orientation property.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 06-Aug-02    Paul            VBM:2002080604 - Added target attribute.
 * 10-Aug-03    Sumit           VBM:2003032713 - Implements the visitable 
 *                              interface
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for  
 *                              ProtocolException where necessary.
 * 17-Apr-03    Adrian          VBM:2003040903 - Added new prompt attribute 
 *                              with getter and setter. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.TextAssetReference;

/**
 * This class contains all the attributes associated with a menu item.
 *
 * @deprecated Old style menu needs to be removed.
 */
public class MenuItem
        extends MCSAttributes
        implements MenuChildVisitable {

    /**
     * This is the off colour of the text.
     */
    private String offColor;

    /**
     * This is the URL for the off image.
     */
    private String offImage;

    /**
     * This is the on colour of the text.
     */
    private String onColor;

    /**
     * This is the URL for the on image.
     */
    private String onImage;

    /**
     * This is the URL for the rollover image.
     */
    private String rolloverImage;

    /**
     * This is the segment name in which the page returned after following
     * the href is displayed.
     */
    private String segment;

    /**
     * The shortcut to associate with this menu item, or null if there is none.
     * If specified this may be either a TextComponentName, or a String.
     */
    private TextAssetReference shortcut;

    /**
     * The name of the region where the content referred to by the anchor will
     * be placed.
     */
    private String target;

    /**
     * This is the text to associate with the menu item.
     */
    private String text;

    /**
     * This is either rolloverimage or rollovertext or something else.
     */
    private String type;

    /**
     * This is the prompt or caption for the menu item.
     */
    private TextAssetReference prompt;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    public MenuItem() {
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
        offColor = null;
        offImage = null;
        onColor = null;
        onImage = null;
        rolloverImage = null;
        segment = null;
        shortcut = null;
        target = null;
        text = null;
        type = null;
    }

    /**
     * Set the value of the offColor property.
     *
     * @param offColor The new value of the offColor property.
     */
    public void setOffColor(String offColor) {
        this.offColor = offColor;
    }

    /**
     * Get the value of the offColor property.
     *
     * @return The value of the offColor property.
     */
    public String getOffColor() {
        return offColor;
    }

    /**
     * Set the value of the offImage property.
     *
     * @param offImage The new value of the offImage property.
     */
    public void setOffImage(String offImage) {
        this.offImage = offImage;
    }

    /**
     * Get the value of the offImage property.
     *
     * @return The value of the offImage property.
     */
    public String getOffImage() {
        return offImage;
    }

    /**
     * Set the value of the onColor property.
     *
     * @param onColor The new value of the onColor property.
     */
    public void setOnColor(String onColor) {
        this.onColor = onColor;
    }

    /**
     * Get the value of the onColor property.
     *
     * @return The value of the onColor property.
     */
    public String getOnColor() {
        return onColor;
    }

    /**
     * Set the value of the onImage property.
     *
     * @param onImage The new value of the onImage property.
     */
    public void setOnImage(String onImage) {
        this.onImage = onImage;
    }

    /**
     * Get the value of the onImage property.
     *
     * @return The value of the onImage property.
     */
    public String getOnImage() {
        return onImage;
    }

    /**
     * Set the value of the rolloverImage property.
     *
     * @param rolloverImage The new value of the rolloverImage property.
     */
    public void setRolloverImage(String rolloverImage) {
        this.rolloverImage = rolloverImage;
    }

    /**
     * Get the value of the rolloverImage property.
     *
     * @return The value of the rolloverImage property.
     */
    public String getRolloverImage() {
        return rolloverImage;
    }

    /**
     * Set the value of the segment property.
     *
     * @param segment The new value of the segment property.
     */
    public void setSegment(String segment) {
        this.segment = segment;
    }

    /**
     * Get the value of the segment property.
     *
     * @return The value of the segment property.
     */
    public String getSegment() {
        return segment;
    }

    /**
     * Set the value of the shortcut property.
     *
     * @param shortcut The new value of the shortcut property.
     */
    public void setShortcut(TextAssetReference shortcut) {
        this.shortcut = shortcut;
    }

    /**
     * Get the value of the shortcut property.
     *
     * @return The value of the shortcut property.
     */
    public TextAssetReference getShortcut() {
        return shortcut;
    }

    /**
     * Set the value of the target property.
     *
     * @param target The new value of the target property.
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Get the value of the target property.
     *
     * @return The value of the target property.
     */
    public String getTarget() {
        return target;
    }

    /**
     * Set the value of the text property.
     *
     * @param text The new value of the text property.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Get the value of the text property.
     *
     * @return The value of the text property.
     */
    public String getText() {
        return text;
    }

    /**
     * Set the value of the type property.
     *
     * @param type The new value of the type property.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the value of the type property.
     *
     * @return The value of the type property.
     */
    public String getType() {
        return type;
    }

    public void visit(
            MenuChildRendererVisitor visitor, DOMOutputBuffer dom,
            MenuAttributes attributes, boolean notLast,
            boolean iteratorPane, MenuOrientation orientation)
            throws ProtocolException {
        visitor.renderMenuChild(dom, attributes, this, notLast, iteratorPane,
                orientation);
    }

    /**
     * Get the value of the prompt property.
     *
     * @return The value of the prompt property
     */
    public TextAssetReference getPrompt() {
        return prompt;
    }

    /**
     * Set the value of the prompt property
     *
     * @param prompt The new value of the prompt property
     */
    public void setPrompt(TextAssetReference prompt) {
        this.prompt = prompt;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
