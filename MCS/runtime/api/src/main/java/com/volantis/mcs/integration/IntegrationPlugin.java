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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.integration;

import com.volantis.mcs.application.MarinerApplication;

import java.util.Map;

/**
 * Base of the integration plugins.
 *
 * <p>
 * <strong>Warning: This is an abstract interface and must only be implemented
 * indirectly through one of the concrete derived interfaces. Classes
 * implementing derived interfaces <i>MUST</i> be public and have a public
 * no-arg constructor.</strong>
 * </p>
 *
 * @volantis-api-include-in PublicAPI
 *
 * @mock.generate
 */
public interface IntegrationPlugin {

    /**
     * The key to use to retrieve the {@link MarinerApplication} instance.
     */
    public static final Object APPLICATION = new Object();

    /**
     * This method is called to initialize the plugin.
     *
     * <p>The arguments parameter will contain values from the configuration
     * entry if these were specified). It also contains a reference to the
     * {@link MarinerApplication} that can be retrieved using
     * {@link #APPLICATION} as the key.</p>
     *
     * @param arguments - A {@link Map} containing each argument as a
     * name-value pair of {@link String}.
     */
    void initialize(Map arguments);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-May-05	8277/1	pduffin	VBM:2005051704 Added support for automatically generating mock objects

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
