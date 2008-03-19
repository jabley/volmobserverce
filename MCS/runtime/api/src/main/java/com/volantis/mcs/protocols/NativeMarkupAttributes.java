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
/*
 * $Header: /src/voyager/com/volantis/mcs/protocols/NativeMarkupAttributes.java,v 1.2 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Apr-2003  Chris W         VBM:2003030404 - Ported from metis.
 * ----------------------------------------------------------------------------

 */
package com.volantis.mcs.protocols;

/**
 * This class contains the attributes needed by a protocol to render a
 * NativeMarkupElement.
 */
public class NativeMarkupAttributes
        extends MCSAttributes {
    /**
     * Literal denoting that the native markup should written in the head
     * of the wml deck.
     */
    public static final String WML_DECK_HEAD = "wml.deck.head";

    /**
     * Literal denoting that the native markup should written in the area
     * of the wml deck that holds <template> elements.
     */
    public static final String WML_DECK_TEMPLATE = "wml.deck.template";

    /**
     * Literal denoting that the native markup should written in the area
     * of the wml deck that holds timer definitions.
     */
    public static final String WML_CARD_TIMER = "wml.card.timer";

    /**
     * Literal denoting that the native markup should written in the area
     * of the wml deck that holds onevent definitions.
     */
    public static final String WML_CARD_ONEVENT = "wml.card.onevent";

    /**
     * Literal denoting that the native markup should written in the area
     * of a card before its body markup, such as <do> and <p>
     */
    public static final String WML_CARD_BEFOREBODY = "wml.card.beforebody";

    /**
     * Literal denoting that the native markup should written in the head
     * element of an HTML page.
     */
    public static final String HTML_HEAD = "html.head";

    /**
     * Literal denoting that the native markup should written in the head
     * element of an VDXML page.
     */
    public static final String VDXML_HEAD = "vdxml.head";

    /**
     * Literal denoting that the native markup should written in place
     * of the native markup element. This must be within the normal
     * content targetted at a pane.
     */
    public static final String HERE = "here";

    /**
     * Literal denoting that the native markup should written to a pane
     * specified by the pane attribute.
     */
    public static final String PANE = "pane";

    /**
     * This is the value of the targetLocation attribute
     */
    private String targetLocation;

    /**
     * This is the value of the pane attribute. It is only valid when the
     * targetLocation is "pane".
     */
    private String pane;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */

    public NativeMarkupAttributes() {
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
        targetLocation = null;
        pane = null;
    }

    /**
     * Returns the pane
     *
     * @return Pane
     */
    public String getPane() {
        return pane;
    }

    /**
     * Returns the target location
     *
     * @return String
     */
    public String getTargetLocation() {
        return targetLocation;
    }

    /**
     * Sets the pane.
     *
     * @param pane The pane to set
     */
    public void setPane(String pane) {
        this.pane = pane;
    }

    /**
     * Sets the target location
     *
     * @param targetLocation The target to set
     */
    public void setTargetLocation(String targetLocation) {
        this.targetLocation = targetLocation;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 08-Dec-03	2169/1	steve	VBM:2003120506 native markup html.head output patched from Proteus

 08-Dec-03	2164/1	steve	VBM:2003120506 html.head support

 ===========================================================================
*/
