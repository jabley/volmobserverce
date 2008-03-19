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
 * $Header: /src/voyager/com/volantis/mcs/atg/dynamo561/Attic/HiddenFieldsSessionMap.java,v 1.1.2.1 2002/09/24 10:22:25 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * 18-Sep-02    Chris W         VBM:2002081511 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.atg.dynamo;

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class holds information about the hidden fields in atg forms. Whilst
 * this class is similar to MarinerSessionContext, it is needed because
 * MarinerSessionContext stores form information using the form specifier e.g.
 * s0. The information on hidden atg fields i.e. the _D, _V and _BUTTONS
 * needs to be stored before the form specifier is available. So, an object of
 * this class is stored in the HttpSession instead.
 */
public class HiddenFieldsSessionMap {

    /**
     * Parameters and their values
     */
    private HashMap groups;

    /**
     * Create a new <code>HiddenFieldsSessionMap</code>
     */
    public HiddenFieldsSessionMap () {
        groups = new HashMap();    
    }

    /**
     * Get the names of all the attribute groups
     * @return java.util.Iterator to iterate through the group names
     */
    public Iterator getAttributeGroupNames()
    {
        return groups.keySet().iterator();
    }

    /**
     * Get the names of all the attributes in a group.
     * @param group The name of the group to retrieve attributes for
     * @return java.util.Iterator of all the attribute names in the group or
     * null if the group does not exist
     */
    public Iterator getAttributeNames( String group )
    {
        if( groups.containsKey( group ) )
        {
            HashMap attrs = (HashMap)groups.get( group );
            return attrs.keySet().iterator();
        }
        return null;
    }

    /**
     * Get the value of an attribute within a group.
     * @param group The name of the group containing the attribute
     * @param name The name of the attribute within the group to get the value for.
     * @return The value of the attribute or null if it does not exist.
     */
    public String getAttribute( String group, String name )
    {
        if( groups.containsKey( group ) )
        {
            HashMap attrs = (HashMap)groups.get( group );
            return (String)attrs.get( name );
        }
        return null;
    }

    /**
     * Remove an attribute from a group.
     * @param group The group to remove the attribute from
     * @param name The name of the attribute to remove.
     */
    public void removeAttribute( String group, String name )
    {
        if( groups.containsKey( group ) )
        {
            HashMap attrs = (HashMap)groups.get( group );
            attrs.remove(name);
        }
    }

    /**
     * Remove all the attributes for a group.
     * @param group The group to remove the attribute from     
     */
    public void removeAttributes( String group )
    {
        if( groups.containsKey( group ) )
        {
            groups.remove( group );            
        }
    }
    
    /**
     * Set the value of an attribute within a group. If the attribute does
     * not exist, it is created and added to the group. If the attribute
     * already exists, the value of the attribute is changed.
     * @param group The group to add the attribute to.
     * @param name The name of the attribute
     * @param value The value of the attribute
     */
    public void setAttribute( String group, String name, String value )
    {
        if( groups.containsKey( group ) )
        {
            HashMap attrs = (HashMap)groups.get( group );
            attrs.put( name, value );
        } else {
            HashMap attrs = new HashMap();
            attrs.put( name, value );
            groups.put( group, attrs );
        }
    }
  
    /**
     * Set the value of a form button within a group. If the form button
     * attribute does not exist, it is created and added to the group. If the
     * attribute already exists, the value of the atribute is changed.
     * As browsers send image form buttons in different ways e.g.
     * IE sends i_button.x=anInt&i_button.y=anInt
     * Mozilla: i_button.x=anInt&i_button.y=anInt&i_button=valueOfCaptionAttribute
     * we need to store the name and the value of the caption so that we can
     * work out which button has been pressed.
     * @param group The group to add the button to.
     * @param name The name of the button attribute
     * @param value The value of the button attribute
     */
    public void setButton( String group, String name, String value)
    {
        if( groups.containsKey( group ) )
        {
            HashMap attrs = (HashMap)groups.get( group );
            HashMap buttons;
            if (attrs.containsKey("_BUTTONS"))
            {
                buttons = (HashMap)attrs.get("_BUTTONS");
                buttons.put( name, value );
            }
            else
            {
                buttons = new HashMap();
                buttons.put( name, value );
                attrs.put("_BUTTONS", buttons);
            }            
        }
        else
        {
            HashMap attrs = new HashMap();
            HashMap buttons = new HashMap();
            buttons.put( name, value );
            attrs.put("_BUTTONS",  buttons);
            groups.put( group, attrs );
        } 
    }
  
    /**
     * Gets the map containing the form buttons within a group.
     * @param group The name of the group's buttons
     * @return A map of the form's buttons or null if it does not exist.
     */
    public HashMap getButtons(String group)
    {
        if( groups.containsKey( group ) )
        {
            HashMap attrs = (HashMap)groups.get(group);
            return (HashMap)attrs.get("_BUTTONS");
        }
        return null;
    }
  
    /**
     * For debugging
     */
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        Iterator groupNames = getAttributeGroupNames();
        if (groupNames != null)
        {
            while (groupNames.hasNext())
            {
                String groupName = (String)groupNames.next();
                sb.append(groupName + " [");
                Iterator attrNames = getAttributeNames(groupName);
            
                if (attrNames != null)
                {
                    while (attrNames.hasNext())
                    {
                        String name = (String)attrNames.next();
                        if (name.equals("_BUTTONS"))
                        {
                            sb.append(name+"="+getButtons(groupName)+" ");
                        }
                        else
                        {
                            sb.append(name+"="+getAttribute(groupName, name)+", ");
                        }
                    }                
                }
                else
                {
                    sb.append("no elements");
                }
                sb.append("] ");
            }
        }
        else
        {
            sb.append("no groupNames or elements");
        }
        return sb.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Jan-05	6565/1	adrianj	VBM:2004122902 Created Dynamo 7 version of Volantis custom tags

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 27-Apr-04	3843/1	ianw	VBM:2004041408 Port forward ATG 5.6.1 integration

 ===========================================================================
*/
