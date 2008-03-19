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
package com.volantis.devrep.repository.impl.devices.category;

import com.volantis.mcs.devices.category.CategoryDescriptor;

/**
 * Default implementation of {@link CategoryDescriptor}.
 */
public class DefaultCategoryDescriptor implements CategoryDescriptor {

    /**
     * The descriptive name of the category.
     * <p>
     * NOTE: this is currently null if this category descriptor was created from
     * a JDBC repository, since JDBC does not currently store this information.
     */
    private String categoryDescriptiveName;

    /**
     * Language of the descriptive name.
     */
    private String language;

    /**
     * Set the descriptive name for this descriptor.
     *
     * @param categoryDescriptiveName the descriptive name for this descriptor.
     * @see #categoryDescriptiveName
     */
    public void setCategoryDescriptiveName(final String categoryDescriptiveName) {
        this.categoryDescriptiveName = categoryDescriptiveName;
    }

    // Javadoc inherited.
    public String getCategoryDescriptiveName() {
        return categoryDescriptiveName;
    }

    // Javadoc inherited.
    public String toString() {
        // This unusual format is so we can diff the output and also to
        // make it a bit more readable.
        return "[DefaultCategoryDescriptor:" +
                "  descriptiveName=" + categoryDescriptiveName + "  ]";
    }

    /**
     * Sets the language.
     *
     * @param language the new language value
     */
    public void setLanguage(final String language) {
        this.language = language;
    }

    // javadoc inherited
    public String getLanguage() {
        return language;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 ===========================================================================
*/
