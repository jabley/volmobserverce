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
 * $Header: /src/voyager/com/volantis/charset/configuration/Alias.java,v 1.2 2003/04/28 15:36:22 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 31-Mar-03    Mat             VBM:2003040701 - Holds information on a 
 *                              charset alias.
 * 22-Apr-03    Mat             VBM:2003040701 - Added equals() method.
 * 09-May-03    Mat             VBM:2003040701 - Added an extra test to equals()
 * ----------------------------------------------------------------------------
 */

package com.volantis.charset.configuration;

/**
 * Holds information on a charset alias.
 * @author  mat
 */
public class Alias {

    /** 
     * The name of the alias.
     */
    private String name;
    
    /** Creates a new instance of CharsetConfiguration */
    public Alias() {
    }

    /** Getter for property name.
    * @return Value of property name.
    *
     */
    public String getName() {
      return name;
    }

    /** Setter for property name.
    * @param name New value of property name.
    *
     */
    public void setName(String name) {
      this.name = name.toLowerCase();
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(!(o instanceof Alias)) {
            return false;
        }

        Alias a1 = (Alias) o;
        
        if(getName().equals(a1.getName())) {
            return true;
        } else {
            return false;
        }
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
