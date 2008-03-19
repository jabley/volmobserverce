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
package com.volantis.mcs.eclipse.ab.core;

import com.volantis.mcs.eclipse.ab.ABPlugin;

import java.util.ResourceBundle;

public class CoreMessages {

    private static final String BUNDLE_NAME =
            "com.volantis.mcs.eclipse.ab.core.CoreMessages"; //$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE =
            ResourceBundle.getBundle(BUNDLE_NAME);


    private CoreMessages() {
    }

    /**
     * @param key
     * @return the string mapping to the supplied key
     */
    public static String getString(String key) {
        String s = null;
        try {
            s = RESOURCE_BUNDLE.getString(key);
        } catch (Exception e) {
            ABPlugin.logError(CoreMessages.class, e);
        }

        return s;
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

 10-Oct-03	1512/1	allan	VBM:2003100702 Generic policy wizard with first wizard page

 ===========================================================================
*/
