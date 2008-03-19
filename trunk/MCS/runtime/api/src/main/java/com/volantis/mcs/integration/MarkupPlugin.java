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

package com.volantis.mcs.integration;

import com.volantis.mcs.context.MarinerRequestContext;

import java.util.Map;

/**
 * Markup plugins are Java classes whose methods can be invoked directly from
 * maml markup using the invoke element.
 *
 * <p>
 * The {@link #process} method provides a map parameter to contain each
 * argument, supplied on the <b>argument</b> elements contained within the
 * <b>invoke</b> element as name-value pairs.
 * <p>
 * Markup plugins can be defined to have the following scopes:
 * <ul>
 * <li><b>application</b>   The plugin is initialised when the web application
 *                          starts and terminates when the web application ends
 * <li><b>session</b>       The plugin is initialised when a new session starts
 *                          and terminates when the session ends
 * <li><b>page</b>          The plugin is initialised when a page starts and
 *                          terminates when page generation ends.
 * </ul>
 * The scope of the plugin determines when the initialization and release
 * methods are called. The rules are...
 *
 * <h3>Application scope</h3>
 * Mariner initialises the plugin during its own initialization. The Map passed
 * to the plugin's initialize() method will contain values from the
 * configuration entry, if these were specified. The Map will be empty if they
 * were not. The plugin's release() method is never called.
 *
 * <h3>Session Scope</h3>
 * Mariner initialises the plugin the first time it is encountered in a new
 * session. The Map passed to the plugin's initialize() method will contain
 * values from the configuration entry, if these were specified. The Map will
 * be empty if they were not. Mariner invokes the plugin's release() method
 * when the session is terminated.
 *
 * <h3>Page Scope</h3>
 * Mariner initialises the plugin the first time it is encountered in a new
 * page. The Map passed to the plugin's initialize() method will contain values
 * from the configuration entry, if these were specified. The Map will be empty
 * if they were not. Mariner invokes the plugin's release() method when the
 * page ends.
 *
 * @volantis-api-include-in PublicAPI
 *
 * @mock.generate base="IntegrationPlugin"
 */
public interface MarkupPlugin
        extends IntegrationPlugin {

    /**
     * This method is called to perform processing in the plugin.
     * @param context - The current RequestContext for the markup being
     * processed.
     * @param arguments - A map containing each argument, supplied on the
     * <b>argument</b> elements contained within the <b>invoke</b> element as
     * name-value pairs.
     */
    public void process(MarinerRequestContext context, Map arguments);

    /**
     * This method is called to indicate that use of the MarkupPlugin has
     * ended.  Usage of this method is scope specific:
     * <ul>
     * <li> Application Scope - this method is never called.
     * <li> Session Scope     - Mariner invokes this method when the current
     *                          session is terminated.
     * <li> Page Scope        - Mariner invokes this method when the page ends.
     * </ul>
     */
    public void release();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 25-Oct-04	5970/1	adrianj	VBM:2004101103 Added MarkupPlugin to the public API

 25-Oct-04	5964/1	adrianj	VBM:2004101103 Added MarkupPlugin to the public API

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
