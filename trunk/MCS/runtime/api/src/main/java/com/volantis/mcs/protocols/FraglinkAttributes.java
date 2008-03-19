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
 * $Header: /src/voyager/com/volantis/mcs/protocols/FraglinkAttributes.java,v 1.10 2002/03/18 12:41:16 ianw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 05-Jul-01    Paul            VBM:2001070509 - Added this header and set the
 *                              default tag name.
 * 26-Jul-01    Paul            VBM:2001071707 - Fixed problems with resetting
 *                              the state of the object.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

public class FraglinkAttributes
  extends MCSAttributes {

  private OutputBuffer linkBuffer;
    
  /**
   * True if this fragment link is participating in a fragment peer/parent link
   * list, i.e. the mariner-list-* style attributes apply.
   */ 
  private boolean isInList;

    /**
     * The name of the fragment, required for VDXML support where we look
     * up the url to use by name.
     */
    private String name;

  /**
   * This constructor delegates all its work to the initialise method,
   * no extra initialisation should be added here, instead it should be
   * added to the initialise method.
   */
  public FraglinkAttributes () {
    initialise ();
  }

  /**
   * This method should reset the state of this object back to its
   * state immediately after it was constructed.
   */
  public void resetAttributes () {
    super.resetAttributes ();

    // Call this after calling super.resetAttributes to allow initialise to
    // override any inherited attributes.
    initialise ();
  }

  /**
   * Initialise all the data members. This is called from the constructor
   * and also from resetAttributes.
   */
  private void initialise () {
    linkBuffer = null;
  }

  /**
   * Set the linkText property.
   * @param linkBuffer The new value of the linkText property.
   */
  public void setLinkText (OutputBuffer linkBuffer) {
    this.linkBuffer = linkBuffer;
  }

  /**
   * Get the value of the linkText property.
   * @return The value of the linkText property.
   */
  public OutputBuffer getLinkText () {
    return linkBuffer;
  }

    /**
     * @see #isInList
     */
    public boolean isInList() {
        return isInList;
    }

    /**
     * @see #isInList
     */
    public void setInList(boolean inList) {
        isInList = inList;
    }

    /**
     * @see #name
     */
    public String getName() {
        return name;
    }

    /**
     * @see #name
     */
    public void setName(String name) {
        this.name = name;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Sep-05	9128/1	pabbott	VBM:2005071114 Add XHTML 2 elements

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 24-Sep-04	5613/1	geoff	VBM:2004092215 Port VDXML to MCS: update fragment link support

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 17-Sep-03	1412/1	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 ===========================================================================
*/
