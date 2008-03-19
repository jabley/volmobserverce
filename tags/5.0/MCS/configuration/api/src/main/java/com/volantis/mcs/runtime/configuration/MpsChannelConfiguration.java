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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Class to hold configuration information for MPS channels.
 */ 
public class MpsChannelConfiguration {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004. ";

    private String name;
    
    /**
     * The name of the class implementing the ChannelAdapter interface for 
     * this channel.
     */ 
    private String className;
    
    /**
     * A List of {@link ArgumentConfiguration}s for the markup plugin
     */ 
    private List arguments = new ArrayList();


    /**
     * Get the name of the channel
     * @return The name of the channel
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the channel
     * @param name The name of the channel
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name of the class that implements this channel
     * @return The class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Set the name of the class that implements this channel
     * @param className The class name
     */
    public void setClassName(String className) {
        this.className = className;
    }
    
    /**
     * Add an {@link ArgumentConfiguration} to the List of arguments.
     * @param arg The ArgumentConfiguration to add
     */ 
    public void addArgument(ArgumentConfiguration arg) {
        arguments.add(arg);
    }
    
    /**
     * Get the Iterator of the List of {@link ArgumentConfiguration}
     * @return the Iterator of the List of {@link ArgumentConfiguration}
     */ 
    public Iterator getArgumentsListIterator() {
        return arguments.iterator();
    }
    
    /**
     * Get the arguments as a Map.
     * @return the arguments as a Map.
     */ 
    public Map getArguments() {
        HashMap result = new HashMap();
        for (int i = 0; i < arguments.size(); i++) {
            ArgumentConfiguration ac = (ArgumentConfiguration)arguments.get(i);
            result.put(ac.getName(), ac.getValue());
        }
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/6	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 17-Jun-04	4619/3	ianw	VBM:2004060111 Fixed Javadoc issues

 07-Jun-04	4619/1	ianw	VBM:2004060111 Fixed MPS Configuration

 ===========================================================================
*/
