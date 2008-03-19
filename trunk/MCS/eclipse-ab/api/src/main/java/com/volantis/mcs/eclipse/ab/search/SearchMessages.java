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
package com.volantis.mcs.eclipse.ab.search;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;

import java.util.ResourceBundle;

import org.eclipse.swt.graphics.Image;

/**
 * Messages class for the search package.
 */
public class SearchMessages {

    private static final String BUNDLE_NAME =
            "com.volantis.mcs.eclipse.ab.search.SearchMessages";

    private static final ResourceBundle RESOURCE_BUNDLE =
            ResourceBundle.getBundle(BUNDLE_NAME);


    /**
     * The private constructor.
     */
    private SearchMessages() {
    }

    /**
     * Get a keyed property message as a String. This method delegates
     * to EclipseCommonMessages.getString().
     * @param key The message key.
     * @return The message or null if no message is found.
     */
    public static String getString(String key) {
        return EclipseCommonMessages.getString(getResourceBundle(), key);
    }

    /**
     * Get a keyed property message as an Integer. This method delegates
     * to EclipseCommonMessages.getInteger().
     * @param key The message key.
     * @return The message or null if no message is found.
     */
    public static Integer getInteger(String key) {
        return EclipseCommonMessages.getInteger(getResourceBundle(), key);
    }


    /**
     * Get a keyed property message as an Image where the property message
     * is the name of an image file relative to this Class.
     * @param key The message key.
     * @return The Image derived from the keyed message.
     */
    public static Image getImage(String key) {
        String imageFile = getString(key);
        return EclipseCommonMessages.getImage(SearchMessages.class,
                imageFile);
    }

    /**
     * Get the ResourceBundle for Controls.
     * @return The Controls ResourceBundle.
     */
    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 08-Oct-04	5557/2	allan	VBM:2004070608 Device search

 ===========================================================================
*/
