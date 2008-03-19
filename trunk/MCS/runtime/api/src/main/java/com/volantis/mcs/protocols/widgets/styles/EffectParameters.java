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

package com.volantis.mcs.protocols.widgets.styles;

import java.util.HashMap;
    
/**
 * Base class for encapsulating effect parameteres. 
 */
public abstract class EffectParameters {
        
    private static final HashMap parsers = new HashMap();
    static {
       parsers.put("random", new RandomEffectParameters.Parser());
    }

    /**
     * Factory method for obtaining parser for parameters of
     * the specified effect   
     */
    public static Parser getParser(String effect) {        
        return (null != effect) ? (Parser)parsers.get(effect): null; 
    }

    /**
     * Generates JavaScript representations of this class. 
     * Actual implementation to be provided be concrete subclasses.
     */        
    public abstract String toScript(); 
    
    /**
     * Base class for parameters parsers
     */
    public abstract static class Parser {
        
        /**
         * Parses parameters string to the appropriate subclass of EffectParameters.
         * Internally call a protected method implemented by a concerete parser.
         */
        public EffectParameters parse(String params) {
            String[] paramsArray = params.split(",");
            for (int i = 0; i < paramsArray.length; i++) {
                paramsArray[i] = paramsArray[i].trim(); 
            }
            return parse(paramsArray);
        }
        
        /**
         * Parses array of string to the appropriate subclass of EffectParameters.
         * Should be implemented by derived classes
         */
        abstract protected EffectParameters parse(String[] paramsArray);        
    }
}
