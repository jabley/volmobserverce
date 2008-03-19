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

/**
 * Return result element model class.
 */
public class ReturnResult {
    
    /**
     * Return code of result.
     */
    protected int returnCode;

    /**
     * Return information of result.
     */
    protected String returnString;

    /**
     * Gets return code.
     * 
     * @return return code.
     */
    public int getReturnCode() {
        return this.returnCode;
    }

    /**
     * Setter for return code.
     * 
     * @param returnCode
     */
    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * Gets return string message.
     * 
     * @return return string.
     */
    public String getReturnString() {
        return this.returnString;
    }

    /**
     * Setter for return string.
     * 
     * @param returnString
     */
    public void setReturnString(String returnString) {
        this.returnString = returnString;
    }

}
