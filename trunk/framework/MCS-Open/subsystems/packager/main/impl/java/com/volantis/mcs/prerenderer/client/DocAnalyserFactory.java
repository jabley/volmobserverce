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

package com.volantis.mcs.prerenderer.client;

/**
 * Factory for creating PrerendererDocAnalyser instance.
 */

public class DocAnalyserFactory {

    /**
     * The DocAnalyserFactory instance for this class.
     */    
    private static DocAnalyserFactory docAnalyserFactoryInstance = new DocAnalyserFactory();
    
    private PrerendererDocAnalyser instanceDocAnalyser;
    
    private DocAnalyserFactory(){        
    }
        
    /**
     * Get the default instance of this factory.
     * @return The default instance of this factory.
     */
    public static DocAnalyserFactory getDefaultInstance(){
        return docAnalyserFactoryInstance;
    }
    
    /**
     * Return and create if not exist PrerendererDocAnalyser instance  
     * @return PrerendererDocAnalyser instance
     */
    public PrerendererDocAnalyser createPrerendererDocAnalyser(){
        if(instanceDocAnalyser == null){
            instanceDocAnalyser = new PrerendererDocAnalyser();
        }
        return instanceDocAnalyser;
    }
}
