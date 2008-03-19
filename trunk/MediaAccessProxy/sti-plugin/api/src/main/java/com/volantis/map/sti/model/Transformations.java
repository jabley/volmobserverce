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
 * Transformations element model class. Container for Transformation elements.
 */
public class Transformations {
    
    /**
     * Adds transformation.
     * 
     * @param transformation
     */
    public void addTransformation(Transformation transformation) {
        transformationList.add(transformation);
    }

    /**
     * Gets transformation at specified index.
     * 
     * @param index index of transformation to be returned.
     * @return transformation.
     */
    public Transformation getTransformation(int index) {
        return (Transformation) transformationList.get(index);
    }

    /**
     * Gets size of transformation list.
     * 
     * @return size of transformation list. 
     */
    public int sizeTransformationList() {
        return transformationList.size();
    }

    /**
     * List of transformations.
     */
    protected ArrayList transformationList = new ArrayList();

}
