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

package com.volantis.mcs.protocols.widgets;

/**
 * A member name.
 */
public abstract class MemberName {
    /**
     * Returns the member name.
     * 
     * @return the member name.
     */
    public abstract String getName();
    
    /**
     * Returns the member type.
     * 
     * @return The member type
     */
    public abstract MemberType getMemberType();
    
    /**
     * Returns an instance of MemberName for given name.
     *  
     * @param name The member name.
     * @return An instance of MemberName.
     * @throws IllegalArgumentException if no such member exists. 
     */
    public final static MemberName getMemberNameFor(String name) {
        MemberName memberName = null;
        
        try {
            memberName = ActionName.forName(name);
        } catch (IllegalArgumentException e) {
            
        }
        
        try {
            memberName = PropertyName.forName(name);
        } catch (IllegalArgumentException e) {
            
        }
        
        try {
            memberName = EventName.forName(name);
        } catch (IllegalArgumentException e) {
            
        }
        
        if (memberName == null) {
            throw new IllegalArgumentException("Unknown member name.");
        }
        
        return memberName;
    }
}
