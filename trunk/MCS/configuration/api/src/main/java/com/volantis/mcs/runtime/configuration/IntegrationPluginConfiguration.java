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

package com.volantis.mcs.runtime.configuration;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

public class IntegrationPluginConfiguration {
    /**
     * The name of the markup plugin.
     */
    private String name;
    /**
     * The fully qualified classname of the markup plugin.
     */
    private String className;
    /**
     * A mutable map of arguments;
     */
    private Map mutableArguments = new HashMap();
    /**
     * An unmodifiable wrapper around the arguments.
     */
    private Map arguments = Collections.unmodifiableMap(mutableArguments);

    /**
     * Set the name of the markup plugin.
     * @param name the name of the markup plugin
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name of the markup plugin
     * @return the name of the markup plugin
     */
    public String getName() {
        return name;
    }

    /**
     * Set the fully qualified classname of the markup plugin.
     * @param className the fully qualified classname of the markup plugin
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Get the fully qualified classname of the markup plugin.
     * @return the fully qualified classname of the markup plugin.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Add an {@link ArgumentConfiguration} to the List of mutableArguments.
     * @param arg
     */
    public void addArgument(ArgumentConfiguration arg) {
        mutableArguments.put(arg.getName(), arg.getValue());
    }

    /**
     * Get the arguments as an unmodifiable Map.
     * @return the arguments as an unmodifiable Map.
     */
    public Map getArguments() {
        return arguments;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 ===========================================================================
*/
