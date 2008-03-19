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
 
package com.volantis.mcs.integration.iapi;

import com.volantis.mcs.integration.iapi.IAPIAttributes;

/**
 * The attributes of an invoke element. 
 */
public class InvokeAttributes implements IAPIAttributes {
    
    /**
     * The Volantis copyright statement
     */
    private static final String mark =
            "(c) Volantis Systems Ltd 2003. ";
    
    /**
     * The name of the markup plugin.
     */
    private String name;

    /**
     * The method to invoke on the MarkupPlugin.
     */
    private String methodName;   

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
     * Set the method to invoke on the MarkupPlugin.
     * @param methodName the method to invoke on the MarkupPlugin.
     */
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    /**
     * Get the method to invoke on the MarkupPlugin.
     * @return the method to invoke on the MarkupPlugin.
     */
    public String getMethodName() {
        return methodName;
    }

    // Javadoc inherited from IAPIAttributes interface
    public void reset() {
        name = null;
        methodName = null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 ===========================================================================
*/
