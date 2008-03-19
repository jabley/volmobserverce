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

import com.volantis.mcs.protocols.assets.ImageAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.MenuIcon;

/**
 * Represents an optionally two-state icon for use in a menu label.
 */
public class ConcreteMenuIcon extends AbstractModelElement implements MenuIcon {

    /**
     * The primary image URL for the icon. Will not be null
     */
    private ImageAssetReference normalURL;

    /**
     * The optional secondary image URL for the icon.
     */
    private ImageAssetReference overURL;

    /**
     * Initializes the new instance.
     */ 
    public ConcreteMenuIcon() {
        this(null);
    }

    /**
     * Initialises a new instance of the menu icon.
     *
     * @param elementDetails information about the PAPI element that is
     * associated with this menu entity
     */
    public ConcreteMenuIcon(ElementDetails elementDetails) {
        super(elementDetails);
        setOverURL(null);
    }

    // JavaDoc inherited
    public ImageAssetReference getNormalURL() {
        if (normalURL == null) {
            throw new IllegalStateException("normal url should never be null");
        }
        return normalURL;
    }

    /**
     * Sets the location (URL) of the main image for this menu icon.
     * It cannot not be null.  This will overwrite any
     * existing value.  If it is necessary to know the previous value then a
     * call to this method should be coupled with a call to the
     * {@ link #getNormalURL() getNormalURL()} method.
     *
     * @param normalURL The location of the normal image for this menu icon
     */
    public void setNormalURL(ImageAssetReference normalURL) {
        if (normalURL == null) {
            throw new IllegalArgumentException(
                    "The normal icon URL cannot be null");
        }
        this.normalURL = normalURL;
    }

    // JavaDoc inherited
    public ImageAssetReference getOverURL() {
        return overURL;
    }

    /**
     * Sets the location (URL) of the secondary image for this menu icon.
     * This will overwrite any existing value.  If it is necessary to know the
     * previous value then a call to this method should be coupled with a call
     * to the {@ link #setOverURL() setOverURL()} method.
     *
     * @param overURL The location of the secondary image for this menu icon
     */
    public void setOverURL(ImageAssetReference overURL) {
        this.overURL = overURL;
    }

/* Commented out until we resolve VBM:2004040703.
    // JavaDoc inherited
    public boolean equals(Object o) {
        // This class extends one that provides its own equals implementation,
         // so call it
        if (!super.equals(o)) {
            return false;
        }

        final ConcreteMenuIcon concreteMenuIcon = (ConcreteMenuIcon) o;

        // Check the normalURL for equality
        if ((normalURL != null &&
                !normalURL.equals(concreteMenuIcon.normalURL)) ||
                (normalURL == null && concreteMenuIcon.normalURL != null)) {
            return false;
        }

        // The normalURL was equal so check the overURL for equality
        if ((overURL != null && !overURL.equals(concreteMenuIcon.overURL)) ||
                (overURL == null && concreteMenuIcon.overURL != null)) {
            return false;
        }

        // Must be equal if it gets to here :-)
        return true;
    }

    // JavaDoc inherited
    public int hashCode() {
        int result = super.hashCode();
        result = 29 * result + (normalURL != null ? normalURL.hashCode() : 0);
        result = 29 * result + (overURL != null ? overURL.hashCode() : 0);
        return result;
    }
*/
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	8888/1	emma	VBM:2005062405 Annotate DOM elements generated from menu model classes with styles

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-May-04	4246/1	philws	VBM:2004050709 Fix StyleInfo handling for MenuIcon, MenuText and MenuLabel

 07-Apr-04	3767/1	geoff	VBM:2004040702 Enhance Menu Support: Address issues with model equals and hashcode

 26-Mar-04	3500/2	claire	VBM:2004031806 Initial implementation of abstract component image references

 23-Mar-04	3491/1	philws	VBM:2004031912 Make Menu Model conform to updated Architecture

 10-Mar-04	3306/3	claire	VBM:2004022706 Refactoring of menu model test cases

 ===========================================================================
*/
