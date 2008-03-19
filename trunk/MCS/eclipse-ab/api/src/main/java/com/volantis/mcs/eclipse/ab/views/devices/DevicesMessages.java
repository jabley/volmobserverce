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
package com.volantis.mcs.eclipse.ab.views.devices;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import org.eclipse.swt.graphics.Image;

import java.util.ResourceBundle;

/**
 * The class for resource messages associated with device related views.
 */
public class DevicesMessages {

    /**
     * The bundle name for DevicesMessages.
     */
    private static final String BUNDLE_NAME =
            "com.volantis.mcs.eclipse.ab.views.devices.DevicesMessages"; //$NON-NLS-1$

    /**
     * The ResourceBundle for DevicesMessages.
     */
    private static final ResourceBundle RESOURCE_BUNDLE =
            ResourceBundle.getBundle(BUNDLE_NAME);


    private DevicesMessages() {
    }

    /**
     * Get the localized message for a given key.
     * @param key The message key.
     * @return The localized message derived from the key and the
     * property bundle.
     */
    public static String getString(String key) {
        return EclipseCommonMessages.getString(RESOURCE_BUNDLE, key);
    }

    /**
     * The EditorMessages resource bundle.
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
        return EclipseCommonMessages.getImage(DevicesMessages.class,
                imageFile);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Apr-04	3602/5	doug	VBM:2004030402 Added a StructurePolicyValueModifier

 01-Mar-04	3197/1	pcameron	VBM:2004021904 Added PolicyValueOriginSelector

 11-Feb-04	2862/1	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 ===========================================================================
*/
