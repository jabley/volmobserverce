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

package com.volantis.map.ics.imageprocessor.tool.impl;

import com.volantis.map.ics.imageprocessor.tool.Tool;
import com.volantis.map.ics.imageprocessor.tool.ToolFactory;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ToolFactoryImpl extends ToolFactory {

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(ToolFactoryImpl.class);

    /**
     * Used for localizing exceptions.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory
        .createExceptionLocalizer(ToolFactoryImpl.class);

    /**
     * tools map.
     */
    public static final Map TOOLS;

    // Initialize
    static {
        Map tools = new HashMap();

        // Size tools.
        tools.put("ClippingTool", new ClippingTool());
        tools.put("ResizingTool", new ResizingTool());
        tools.put("RGBConverterTool", new RGBConverterTool());
        tools.put("WatermarkingTool", new WatermarkingTool());
        tools.put("CroppingTool", new CroppingTool());

        TOOLS = Collections.synchronizedMap(tools);
    }

    //javadoc inherited
    public Tool getTool(String toolName) {
        if (toolName == null) {
            String msg = EXCEPTION_LOCALIZER.format("argument-is-null",
                                                    "toolName");
            throw new IllegalArgumentException(msg);
        }
        if ("".equals(toolName)) {
            String msg = EXCEPTION_LOCALIZER.format(
                "argument-value-is-empty-string",
                "toolName");
            throw new IllegalArgumentException(msg);
        }
        if (!TOOLS.containsKey(toolName)) {
            String msg = EXCEPTION_LOCALIZER.format("rule-unknown",
                                                    "toolName");
            throw new IllegalArgumentException(msg);
        }
        return (Tool) TOOLS.get(toolName);
    }

    //javadoc inherited
    public String[] listTools() {
        String[] listOfTools = new String[TOOLS.size()];
        TOOLS.keySet().toArray(listOfTools);
        return listOfTools;
    }


}
