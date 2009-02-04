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
 * $Header: /src/voyager/com/volantis/mcs/protocols/ImageAttributes.java,v 1.19 2002/12/23 10:25:57 chrisw Exp $
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
 * 02-Aug-01    Allan           VBM:2001072604 - Added a pane property.
 * 31-Jan-02    Paul            VBM:2001122105 - Removed unused dynSrc, loop
 *                              and start attributes.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 25-Apr-02    Paul            VBM:2002042202 - Removed useMap.
 * 20-Dec-02    Chris W         VBM:2002121904 - Added convertibleImageAsset
 *                              boolean property and isConvertibleImageAsset and
 *                              setConvertibleImageAsset methods.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.protocols.MAPAttributes;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;

public class ImageAttributes
  extends MAPAttributes {

    private String align;
  private TextAssetReference altText;
  private String border;
  private String hSpace;
  private String height;
  private boolean localSrc;
  private String src;
  private String vSpace;
  private String width;
  private Pane pane;
  private String assetURLSuffix;   

  /**
   * Flag specifying if these attributes are for a convertible image asset
   */
  private boolean convertibleImageAsset;

    /**
     * Get the value of pane.
     *
     * @return value of pane.
   * @deprecated replaced by the container stack.
     */
    public Pane getPane() {
        return pane;
    }

  /**
     * Set the value of pane.
     *
     * @param v  Value to assign to pane.
   * @deprecated replaced by the container stack.
     */
    public void setPane(Pane v) {
        this.pane = v;
    }


  /**
   * This constructor delegates all its work to the initialise method,
   * no extra initialisation should be added here, instead it should be
   * added to the initialise method.
   */
  public ImageAttributes () {
    initialise ();
  }

  /**
   * This method should reset the state of this object back to its
   * state immediately after it was constructed.
   */
  public void resetAttributes () {
    if (null == getFinalizer()) {
        super.resetAttributes ();

        // Call this after calling super.resetAttributes to allow initialise to
        // override any inherited attributes.
        initialise ();
    }
  }

  /**
   * Initialise all the data members. This is called from the constructor
   * and also from resetAttributes.
   */
  private void initialise () {

    // Set the default tag name, this is the name of the tag which makes
    // the most use of this class.
    setTagName ("img");

    align = null;
    altText = null;
    border = null;
    hSpace = null;
    height = null;
    localSrc = false;
    src = null;
    vSpace = null;
    width = null;
  }

  /**
   * Set the align property.
     *
   * @param align The new value of the align property.
   */
  public void setAlign (String align) {
    this.align = align;
  }

  /**
   * Get the value of the align property.
     *
   * @return The value of the align property.
   */
  public String getAlign () {
    return align;
  }

  /**
   * Set the altText property.
     *
   * @param altText The new value of the altText property.
   */
  public void setAltText(TextAssetReference altText) {
    this.altText = altText;
  }

    /**
     * Set the altText property.
     *
     * @param altText The new value of the altText property.
     */
    public void setAltText(String altText) {
      this.altText = new LiteralTextAssetReference(altText);
    }

  /**
   * Get the value of the altText property.
   *
   * @return The value of the altText property.
   */
  public TextAssetReference getAltText () {
    return altText;
  }

  /**
   * Set the border property.
     *
   * @param border The new value of the border property.
   */
  public void setBorder (String border) {
    this.border = border;
  }

  /**
   * Get the value of the border property.
     *
   * @return The value of the border property.
   */
  public String getBorder () {
    return border;
  }

  /**
   * Set the hSpace property.
     *
   * @param hSpace The new value of the hSpace property.
   */
  public void setHSpace (String hSpace) {
    this.hSpace = hSpace;
  }

  /**
   * Get the value of the hSpace property.
     *
   * @return The value of the hSpace property.
   */
  public String getHSpace () {
    return hSpace;
  }

  /**
   * Set the height property.
     *
   * @param height The new value of the height property.
   */
  public void setHeight (String height) {
    this.height = height;
  }

  /**
   * Get the value of the height property.
     *
   * @return The value of the height property.
   */
  public String getHeight () {
    return height;
  }

  /**
   * Set the localSrc property.
     *
   * @param localSrc The new value of the localSrc property.
   */
  public void setLocalSrc (boolean localSrc) {
    this.localSrc = localSrc;
  }

  /**
   * Get the value of the localSrc property.
     *
   * @return The value of the localSrc property.
   */
  public boolean isLocalSrc () {
    return localSrc;
  }

  /**
   * Set the src property.
     *
   * @param src The new value of the src property.
   */
  public void setSrc (String src) {
    this.src = src;
  }

  /**
   * Get the value of the src property.
     *
   * @return The value of the src property.
   */
  public String getSrc () {
    return src;
  }

  /**
   * Set the vSpace property.
     *
   * @param vSpace The new value of the vSpace property.
   */
  public void setVSpace (String vSpace) {
    this.vSpace = vSpace;
  }

  /**
   * Get the value of the vSpace property.
     *
   * @return The value of the vSpace property.
   */
  public String getVSpace () {
    return vSpace;
  }

  /**
   * Set the width property.
     *
   * @param width The new value of the width property.
   */
  public void setWidth (String width) {
    this.width = width;
  }

  /**
   * Get the value of the width property.
     *
   * @return The value of the width property.
   */
  public String getWidth () {
    return width;
  }


    /**
     * Returns whether this is a convertible image asset or not.
     *
     * @return boolean
     */
    public boolean isConvertibleImageAsset() {
        return convertibleImageAsset;
    }

    /**
     * Sets whether this is a convertible image asset
     *
     * @param convertible Set this boolean to true to signify a convertible image asset.
     */
    public void setConvertibleImageAsset(boolean convertible) {
        this.convertibleImageAsset = convertible;
    }

    /**
     * Returns the suffix to the src url
     *
     * @return Returns the assetURLSuffix.
     */
    public String getAssetURLSuffix() {
        return assetURLSuffix;
    }

    /**
     * sets the suffix to the src url
     *
     * @param assetURLSuffix The assetURLSuffix to set.
     */
    public void setAssetURLSuffix(String assetURLSuffix) {
        this.assetURLSuffix = assetURLSuffix;
    }
    
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Dec-05	9839/1	geoff	VBM:2005101702 Fix the XDIME2 Object element

 11-Mar-05	7308/3	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 11-Mar-05	7308/1	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 08-Jun-04	4661/1	steve	VBM:2004060309 enable asset URL suffix attribute

 08-Jun-04	4643/1	steve	VBM:2004060309 enable asset URL suffix attribute

 03-Nov-03	1760/1	philws	VBM:2003031710 Port image alt text component reference handling from PROTEUS

 02-Nov-03	1751/1	philws	VBM:2003031710 Permit image alt text to be component reference

 ===========================================================================
*/
