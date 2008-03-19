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
package com.volantis.map.sti.model;

import java.util.ArrayList;

/**
 * Adaptation classes element model.
 */
public class AdaptationClasses {
    
    /**
     * Adding adaptation class element into this element.
     * 
     * @param adaptationClass adaptation class model.
     */
    public void addAdaptationClass(AdaptationClass adaptationClass) {
        adaptationClassList.add(adaptationClass);
    }

    /**
     * Getter for adaptation class elements. Parameter index specifing
     * which element should be returned.
     * 
     * @param index number of element to return.
     * @return adaptation class.
     */
    public AdaptationClass getAdaptationClass(int index) {
        return (AdaptationClass)adaptationClassList.get( index );
    }

    /**
     * Returns size of stored adaptation class elements.
     * 
     * @return size of stored adaptation class elements.
     */
    public int sizeAdaptationClassList() {
        return adaptationClassList.size();
    }

    /**
     * Array list to store adaptation class elements.
     */
    protected ArrayList adaptationClassList = new ArrayList();

}
