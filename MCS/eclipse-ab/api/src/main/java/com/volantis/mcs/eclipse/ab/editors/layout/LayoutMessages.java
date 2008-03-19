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
package com.volantis.mcs.eclipse.ab.editors.layout;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;

import java.util.ResourceBundle;

/**
 * The messages class for the layout editor.
 */
public final class LayoutMessages {
    /**
     * The resource bundle name
     */
    private static final String BUNDLE_NAME =
        "com.volantis.mcs.eclipse.ab.editors.layout.LayoutMessages"; //$NON-NLS-1$

    /**
     * The resource bundle
     */
    private static final ResourceBundle RESOURCE_BUNDLE =
        ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Prevents instances of this class from being created
     */
    private LayoutMessages() {
    }

    /**
     * Get a keyed property message as a String. This method delegates to
     * EclipseCommonMessages.getString().
     *
     * @param key The message key.
     * @return The message or null if no message is found.
     */
    public static String getString(String key) {
        return EclipseCommonMessages.getString(getResourceBundle(), key);
    }

    /**
     * Get a keyed property message as an Integer. This method delegates to
     * EclipseCommonMessages.getInteger().
     *
     * @param key The message key.
     * @return The message or null if no message is found.
     */
    public static Integer getInteger(String key) {
        return EclipseCommonMessages.getInteger(getResourceBundle(), key);
    }

    /**
     * Get the ResourceBundle for Layouts.
     *
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

 15-Jan-04	2583/1	philws	VBM:2003121512 Add layout constraints

 ===========================================================================
*/
