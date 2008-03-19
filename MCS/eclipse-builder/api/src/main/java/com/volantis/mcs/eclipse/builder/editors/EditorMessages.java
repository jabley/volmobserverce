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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.builder.editors;

import org.eclipse.swt.graphics.Image;

import java.util.ResourceBundle;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;

/**
 * General messages class for editors.
 */
public class EditorMessages {
    /**
     * The bundle name for EditorMessages.
     */
    private static final String BUNDLE_NAME =
            "com.volantis.mcs.eclipse.builder.editors.EditorMessages";

    /**
     * The ResourceBundle for EditorMessages.
     */
    private static final ResourceBundle RESOURCE_BUNDLE =
            ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Initializes the new instance. Private to enforce the singleton pattern.
     */
    private EditorMessages() {
    }

    /**
     * Get the localized message for a given key.
     *
     * @param key The message key.
     * @return The localized message derived from the key and the property
     *         bundle.
     */
    public static String getString(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }

    /**
     * The EditorMessages resource bundle.
     *
     * @return The EditorMessages resource bundle.
     */
    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }

    /**
     * Get a keyed property message as an Integer. This method delegates
     * to EclipseCommonMessages.getInteger().
     * @param key The message key.
     * @return The message or null if no message is found.
     */
    public static Integer getInteger(String key) {
        String number = RESOURCE_BUNDLE.getString(key);
        int integer = 0;
        try {
            if (number != null) {
                integer = Integer.parseInt(number);
            }
        } catch (NumberFormatException nfe) {
            // If the string was not a number, treat it as zero...
        }
        return new Integer(integer);
    }
    
    /**
     * Get a keyed property message as an Image. This method delegates
     * to EclipseCommonMessages.getImage().
     *
     * @param key The message key.
     * @return The image or null if no message is found.
     */
    public static Image getImage(String key) {
        return EclipseCommonMessages.getImage(EditorMessages.class, key);
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
