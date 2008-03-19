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
package com.volantis.mcs.eclipse.ab.editors;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;

import java.util.ResourceBundle;

import org.eclipse.swt.graphics.Image;

/**
 * General messages class for editors.
 */
public class EditorMessages {
    /**
     * The bundle name for EditorMessages.
     */
    private static final String BUNDLE_NAME =
            "com.volantis.mcs.eclipse.ab.editors.EditorMessages"; //$NON-NLS-1$

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
        return EclipseCommonMessages.getString(RESOURCE_BUNDLE, key);
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
     * Get a keyed property message as an Image. This method delegates
     * to EclipseCommonMessages.getImage().
     *
     * @param key The message key.
     * @return The image or null if no message is found.
     */
    public static Image getImage(String key) {
        return EclipseCommonMessages.getImage(EditorMessages.class, key);
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
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Apr-05	7588/1	philws	VBM:2005040107 Port I18N updates from 3.3

 07-Apr-05	7537/1	philws	VBM:2005040107 Fix I18N issues

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 07-Jan-04	2436/1	pcameron	VBM:2003121720 A few tweaks

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 30-Nov-03	2069/1	allan	VBM:2003111903 Implement init, doSave and supporting methods.

 ===========================================================================
*/
