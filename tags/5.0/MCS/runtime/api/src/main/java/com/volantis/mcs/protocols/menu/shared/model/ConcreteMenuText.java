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

package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuText;

/**
 * Represents the text used in a menu label, supporting two deprecated
 * stylistic properties for covering text colouration.
 */
public class ConcreteMenuText extends AbstractModelElement implements MenuText {

    /**
     * The text associated with the menu label.
     */
    private OutputBuffer text;

    /**
     * Initializes the new instance.
     */
    public ConcreteMenuText() {
        this(null);
    }

    /**
     * Initialises a new instance of the text for a menu label.
     *
     * @param elementDetails information about the PAPI element that is
     *                       associated with this menu entity.
     */
    public ConcreteMenuText(ElementDetails elementDetails) {
        super(elementDetails);
    }

    // JavaDoc inherited
    public OutputBuffer getText() {
        if (text == null) {
            throw new IllegalStateException("text should never be null");
        }

        return text;
    }

    /**
     * Sets the text of this label and replaces any existing text.  If this
     * old text may be needed, a call to this method should be coupled with
     * a call to the {@link #getText() getText()} method.
     *
     * @param text The new text to use on the label
     */
    public void setText(OutputBuffer text) {
        if (text == null) {
            throw new IllegalArgumentException("text cannot be null");
        }

        this.text = text;
    }

/* Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        // This class extends one that provides its own equals implementation,
        // so call it
        if (!super.equals(o)) {
            return false;
        }

        final ConcreteMenuText concreteMenuText = (ConcreteMenuText) o;

        // Check text for equality... @todo handle outputbuffer equality?
        if ((text != null && !text.equals(concreteMenuText.text)) ||
                (text == null && concreteMenuText.text != null)) {
            return false;
        }

        // Text was equal so check deprecatedNormalColour for equality
        if ((deprecatedNormalColour != null && !deprecatedNormalColour
                .equals(concreteMenuText.deprecatedNormalColour)) ||
                (deprecatedNormalColour == null &&
                    concreteMenuText.deprecatedNormalColour != null)) {
            return false;
        }

        // Previous fields were equal so check deprecatedOverColour for equality
        if ((deprecatedOverColour != null && !deprecatedOverColour
                .equals(concreteMenuText.deprecatedOverColour)) ||
                (deprecatedOverColour == null &&
                    concreteMenuText.deprecatedOverColour != null)) {
            return false;
        }

        // Must be equal if it gets to here :-)
        return true;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (deprecatedNormalColour != null ?
                                deprecatedNormalColour.hashCode() : 0);
        result = 29 * result + (deprecatedOverColour != null ?
                                deprecatedOverColour.hashCode() : 0);
        result = 29 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
*/
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Sep-05	9609/4	ibush	VBM:2005082215 Move on/off color values for menu items

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 02-Apr-04	3429/1	philws	VBM:2004031502 MenuLabelElement implementation

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
