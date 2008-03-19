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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.widgets;

/**
 * A factory of JavaScriptContainer that can be easily removed. In opposite
 * to DefaultJavaScriptContainerFactory created containers are not stored 
 * in list, they are not stored internaly only externaly.  
 */
public class RemovableJavaScriptContainerFactory {
    
    /**
     * static instance of factory
     */
    private static RemovableJavaScriptContainerFactory instance = null;;
    
    /**
     * Singleton so should have private constructor
     */
    private RemovableJavaScriptContainerFactory(){
        
    }
    
    /**
     * public static accessor
     * @return
     */
    public static RemovableJavaScriptContainerFactory getInstance(){
        if(null == instance){
            instance = new RemovableJavaScriptContainerFactory();
        }
        return instance;
    }
    
    /**
     * Creates and returns new instance of JavaScriptContainer.
     * 
     * @return The new instance of JavaScriptContainer.
     */
    public JavaScriptContainer createJavaScriptContainer() {
        JavaScriptContainer container = new RemovableJavaScriptContainer();
        return container;
    }
    
}
