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
 * Return results model class.
 */
public class ReturnResults {
    
    /**
     * Adds return result to return results list.
     * 
     * @param returnResult
     */
    public void addReturnResult(ReturnResult returnResult) {
        returnResultList.add(returnResult);
    }

    /**
     * Gets return result at specified index.
     * 
     * @param index index of return result to return.
     * @return return result.
     */
    public ReturnResult getReturnResult(int index) {
        return (ReturnResult) returnResultList.get(index);
    }

    /**
     * Returns size of return results list.
     * 
     * @return size of return results list.
     */
    public int sizeReturnResultList() {
        return returnResultList.size();
    }

    /**
     * List of return results.
     */
    protected ArrayList returnResultList = new ArrayList();

}
