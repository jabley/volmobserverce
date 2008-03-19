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
package com.volantis.mcs.eclipse.builder.wizards;

import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;
import com.volantis.mcs.eclipse.builder.BuilderPlugin;

import java.util.ResourceBundle;

/**
 * Shared message resources specific to wizards.
 */
public class WizardMessages {

    private static final String BUNDLE_NAME =
            "com.volantis.mcs.eclipse.builder.wizards.WizardMessages";

    private static final ResourceBundle RESOURCE_BUNDLE =
            ResourceBundle.getBundle(BUNDLE_NAME);


    private WizardMessages() {
    }

    /**
     * @param key
     * @return the string mapped to the passed key
     */
    public static String getString(String key) {
        String s = null;
        try {
            s = RESOURCE_BUNDLE.getString(key);
        } catch (Exception e) {
            BuilderPlugin.logError(WizardMessages.class, e);
        }

        return s;
    }

    /**
     * Get a specified property as an Integer using a bundle and key.
     * Exceptions and Errors are logged using EclipseCommonPlugin.logError.
     * @param key The key of the property to find.
     * @return The property or null if not found.
     */
    public static Integer getInteger(String key) {
        Integer i = null;
        try {
            String s = RESOURCE_BUNDLE.getString(key);
            i = Integer.valueOf(s);
        } catch (Throwable t) {
            EclipseCommonPlugin.handleError(EclipseCommonPlugin.getDefault(), t);
        }
        return i;
    }

    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Feb-05	7094/1	emma	VBM:2005021517 mergevbm from MCS 3.3 - fixing chkpii problem

 24-Feb-05	7096/1	emma	VBM:2005021517 Renaming Core and Wizard.properties, updating generation of policies.properties to work with chkpii

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 13-Feb-04	2985/1	allan	VBM:2004012803 Allow policies to be created in non-MCS projects.

 15-Nov-03	1835/1	pcameron	VBM:2003102801 Some tweaks to GenericAssetCreationPage, and FileFilter

 10-Oct-03	1512/1	allan	VBM:2003100702 Generic policy wizard with first wizard page

 ===========================================================================
*/
