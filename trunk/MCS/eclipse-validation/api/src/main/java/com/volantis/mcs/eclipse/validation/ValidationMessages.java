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
package com.volantis.mcs.eclipse.validation;

import com.volantis.mcs.eclipse.common.EclipseCommonMessages;

import java.util.ResourceBundle;

/**
 * The messages class for the validation plugin.
 */
public class ValidationMessages {

    private static final String BUNDLE_NAME =
            "com.volantis.mcs.eclipse.validation.ValidationMessages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE =
            ResourceBundle.getBundle(BUNDLE_NAME);


    /**
     * The private constructor.
     */
    private ValidationMessages() {
    }

    /**
     * Get the ResourceBundle for Validation.
     * @return The Validation ResourceBundle.
     */
    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
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

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Apr-04	3740/1	allan	VBM:2004040508 UpdateClient/Server enhancements & fixes.

 ===========================================================================
*/
