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

package com.volantis.mcs.builder.client;
/**
 * Factory for creating builder object.
 */
public class BuilderFactory {

    /**
     * The BuilderFactory instance for this class.
     */        
    private static BuilderFactory builderFactoryInstance = new BuilderFactory();
    
    private Builder instanceBuilder;
    
    private BuilderFactory(){        
    }
        
    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */    
    public static BuilderFactory getDefaultInstance(){
        return builderFactoryInstance;
    }
    
    /**
     * Return and create instance of Builder if not exist   
     * @return Builder instance
     */
    public Builder createBuilder(){
        if(instanceBuilder == null){
            instanceBuilder = new BuilderCore();
        }
        return instanceBuilder;
    }
}
