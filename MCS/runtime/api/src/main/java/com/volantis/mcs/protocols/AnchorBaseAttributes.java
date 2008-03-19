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
 * $Header: /src/voyager/com/volantis/mcs/protocols/AnchorBaseAttributes.java,v 1.1 2003/04/10 12:53:24 philws Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Mar-03    Phil W-S        VBM:2002111502 - Created. Refactors common
 *                              attributes shared by Anchor and PhoneNumber
 *                              elements.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.protocols.assets.TextAssetReference;

/**
 * Encapsulate the attributes associated with both an anchor and a phone
 * number dialling link.
 */
public class AnchorBaseAttributes extends MCSAttributes {

    /**
     * The content of the anchor.
     */
    private Object content;

    /**
     * The shortcut to associate with this menu item, or null if there is none.
     * If specified this may be either a TextComponentName, or a String.
     */
    private TextAssetReference shortcut;

    private String tabindex;

    /**
     * This constructor delegates all its work to the initialise method,
     * no extra initialisation should be added here, instead it should be
     * added to the initialise method.
     */
    protected AnchorBaseAttributes() {
        initialise();
    }

    /**
     * This method should reset the state of this object back to its
     * state immediately after it was constructed.
     */
    public void resetAttributes() {
        super.resetAttributes();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise();
    }

    /**
     * Initialise all the data members. This is called from the constructor
     * and also from resetAttributes.
     */
    private void initialise() {
        content = null;
        shortcut = null;
        tabindex = null;
    }

    /**
     * Set the value of the content property.
     * @param content The new value of the content property.
     */
    public void setContent (Object content) {
      this.content = content;
    }

    /**
   * Get the value of the content property.
   * @return The value of the content property.
   */
  public Object getContent () {
    return content;
  }

    /**
     * Set the value of the shortcut property.
     * @param shortcut The new value of the shortcut property.
     */
    public void setShortcut (TextAssetReference shortcut) {
      this.shortcut = shortcut;
    }

    /**
   * Get the value of the shortcut property.
   * @return The value of the shortcut property.
   */
  public TextAssetReference getShortcut () {
    return shortcut;
  }

    /**
   * Set the value of the tabindex property.
   * @param tabindex The new value of the tabindex property.
   */
  public void setTabindex (String tabindex) {
    this.tabindex = tabindex;
  }

    /**
   * Get the value of the tabindex property.
   * @return The value of the tabindex property.
   */
  public String getTabindex () {
    return tabindex;
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 ===========================================================================
*/
