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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.map.ics.imageprocessor.tool;

import com.volantis.synergetics.factory.MetaDefaultFactory;

/**
 * Factory for obtaining tools.
 */
public abstract class ToolFactory {

    private static final MetaDefaultFactory instance =
        new MetaDefaultFactory("com.volantis.map.ics.imageprocessor.tool.impl." +
                               "ToolFactoryImpl",
                               ToolFactory.class.getClassLoader());

    /**
     * Gets a tool.
     *
     * @param toolName - Tool name.
     * @return Tool - tool corresponding given name or null if there is no such
     *         tool.
     */
    public abstract Tool getTool(String toolName);

    /**
     * List tools names.
     *
     * @return java.lang.String[]
     */
    public abstract String[] listTools();

    /**
     * Gets an instance of ToolFactory.
     *
     * @return ToolFactory - instance of the factory.
     */
    public static ToolFactory getInstance() {
        return (ToolFactory) instance.getDefaultFactoryInstance();
    }
}
