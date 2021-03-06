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
/* (c) Volantis Systems Ltd 2003.  */

package com.volantis.testtools.config;

/**
 * Interface that ConfigFileBuilders for plugin configurations should implement.
 * @author mat
 */
public interface PluginConfigFileBuilder {

    /**
     * Create the configuration markup for this plugin.
     * 
     * @param configValue The configValue
     * @return The configuration markup.
     */
    public String build(PluginConfigValue configValue);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Oct-03	1585/1	mat	VBM:2003101502 Add plugin config builders to ConfigFileBuilder

 ===========================================================================
*/
