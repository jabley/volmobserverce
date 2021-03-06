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

import org.eclipse.core.runtime.IPluginDescriptor;
import org.eclipse.core.runtime.Plugin;

/**
 * The ValidationPlugin plugin class.
 */
public class ValidationPlugin extends Plugin {

    /**
     * The id for the Validation plugin.
     * @todo remove When/if getDefault() works within junit.
     */
    public static final String PLUGIN_ID = "com.volantis.mcs.eclipse.validation"; //$NON-NLS-1$

    /**
     * A reference to the last ValidationPlugin instance. (NOTE: this
     * method of accessing a/the plugin instance copies the convention used
     * by Eclipse plugins such as JDT. I can't find any documentation
     * discussing this approach so I simply assume that it works.)
     */
    private static ValidationPlugin defaultValidationPlugin;

    /**
     * Construct a new ValidationPlugin with the specified descriptor.
     * @param descriptor The plugin descriptor.
     */
	public ValidationPlugin(IPluginDescriptor descriptor) {
		super(descriptor);
		defaultValidationPlugin = this;
	}

    /**
     * Get the default instance of this plugin. (NOTE: this
     * method of accessing a/the plugin instance copies the convention used
     * by Eclipse plugins such as JDT. I can't find any documentation
     * discussing this approach so I simply assume that it works. I did find
     * a reference in a newsgroup from someone at OTI - part of IBM heavilly
     * involved with Eclipse development - who beleives that not being able to
     * get this information is a hole in the architecture:
     * http://dev.eclipse.org/newslists/news.eclipse.platform/msg03508.html)
     */
    public static ValidationPlugin getDefault() {
        return defaultValidationPlugin;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 13-Oct-03	1549/1	allan	VBM:2003101302 Eclipse Common plugin

 03-Oct-03	1444/1	allan	VBM:2003091903 Port the validation framework to an Eclipse plugin

 ===========================================================================
*/
