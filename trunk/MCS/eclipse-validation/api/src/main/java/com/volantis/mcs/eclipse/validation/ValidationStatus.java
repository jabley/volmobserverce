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
package com.volantis.mcs.eclipse.validation;

import org.eclipse.core.runtime.Status;

/**
 * A class that describes validation status. This class is a
 * specialization of org.eclipse.core.runtime.Status and uses fixed values
 * for plugin id and validation code. As such this class should not be used
 * to provide status for runtime or unrecoverable checked exceptions. In
 * these cases org.eclipse.core.runtime.Status should be used directly.
 */
public class ValidationStatus extends Status {

    /**
     * Volantis copyright.
     */
    private static String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The title of this status (used for specialized rendering)
     */
    protected String title;

    /**
     * Create a new <code>ValidationStatus</code>
     * @param severity The severity of the ValidationStatus.
     * @param message The message associated with the severity.
     */
    public ValidationStatus(int severity, String message) {
        super(severity, ValidationPlugin.PLUGIN_ID, 0, message==null ? "" :
                message, null);
    }

    /**
     * Get the title.
     * @return The title of this status
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of this status.
     * @param title the title of this status.
     */
    public void setTitle(String title) {
         this.title = title;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 06-Oct-03	1444/3	allan	VBM:2003091903 Fix to plugin.xml and some javadoc

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
