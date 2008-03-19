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
package com.volantis.mcs.eclipse.builder.editors.themes;

import java.util.List;
import java.util.ArrayList;

/**
 * Representation of a style category used to group properties within the style
 * editor.
 */
public class StyleCategory {
    /**
     * Name of the category
     */
    private final String name;

    /**
     * Flag that indicate whether the properties in the category are
     * synchronizable.
     */
    private final boolean isSynchronizable;

    /**
     * List of <code>StylePropertyDetails</code> objects that belong to
     * this category
     */
    private final List properties;

    /**
     * List of child categories: optional, so can be null
     */
    private List subCategories;

    /**
     * Parent category: optional, so can be null
     */
    private StyleCategory parent;

    /**
     * Initializes a <code>StyleCategory</code> instance with the given
     * parameters.
     * @param name the name of the category.
     * @param isSynchronizable true iff all the properties in this category
     * are synchronizable.
     */
    public StyleCategory(String name, boolean isSynchronizable) {

        // Initialise the non-optional instance fields
        this.name = name;
        this.isSynchronizable = isSynchronizable;

        // This is never expected to be empty so no point doing this
        // lazily - hence make it a blank final and create it here
        this.properties = new ArrayList();
    }

    /**
     * Returns true iff this category has sub categories
     * @return true iff this category has sub categories
     */
    public boolean hasSubCategories() {
        return (subCategories != null);
    }

    /**
     * Returns the list of children sub categories
     * @return the list of child categories. Could be null.
     */
    public List getSubCategories() {
        return subCategories;
    }

    /**
     * Sets the list of child categories
     * @param child the list of child categories.
     */
    public void addSubCategory(StyleCategory child) {
        // Ensure the list is created, add the child to the list and
        // set its parent to this
        if (subCategories == null) {
            subCategories = new ArrayList();
        }
        subCategories.add(child);
        child.setParent(this);
    }

    /**
     * Returns the parent category
     * @return the parent <code>StyleCategory</code>  or null if this
     * category does not have a parent.
     */
    public StyleCategory getParent() {
        return parent;
    }

    /**
     * Sets the parent of this category.
     * @param parent the parent <code>StyleCategory</code> instance.
     */
    private void setParent(StyleCategory parent) {
        this.parent = parent;
    }

    /**
     * Returns the name of this category.
     * @return the name of this category
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the list of {@link com.volantis.mcs.themes.StylePropertyDetails}
     * for the properties that belong to this category.
     * @return the list of properties
     */
    public List getProperties() {
        return properties;
    }

    /**
     * Returns true iff the properties in this category are synchronizable.
     * @return true iff the properties in this category are synchronizable.
     */
    public boolean isIsSynchronizable() {
        return isSynchronizable;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9886/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 28-Oct-05	9886/1	adrianj	VBM:2005101811 New theme GUI

 ===========================================================================
*/
