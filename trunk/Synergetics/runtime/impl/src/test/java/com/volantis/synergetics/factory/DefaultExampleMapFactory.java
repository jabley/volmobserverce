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
package com.volantis.synergetics.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * The default implementation of the AbstractExampleMapFactory. This default
 * implementation produced HashMaps rather then TreeMaps.
 */
public class DefaultExampleMapFactory extends AbstractExampleMapFactory {

    /**
     * @return an instance of a HashMap.
     */
    public Map createMap() {
        return new HashMap();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jun-05	487/1	matthew	VBM:2005062701 Create a DefaultFactory factory

 ===========================================================================
*/
