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
 * $Header: /src/voyager/com/volantis/mcs/assets/DynamicVisualAsset.java,v 1.23 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Jun-01    Paul            VBM:2001051103 - Added this change history,
 *                              sorted out some comments, changed the
 *                              indentation from the rose style of 3 spaces
 *                              to 2, fixed the constructor, added equals and
 *                              hashCode methods and updated toString value.
 * 09-Jul-01    Paul            VBM:2001070902 - Added encoding property which
 *                              has moved back from Asset.
 * 16-Jul-01    Paul            VBM:2001070508 - Added identityMatches method.
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 24-Oct-01    Paul            VBM:2001092608 - Removed the identityMatches
 *                              method and added the createIdentity method.
 *                              Also made sure that any changes to those
 *                              fields which form part of this objects identity
 *                              causes the cached identity to be discarded.
 * 02-Jan-02    Paul            VBM:2002010201 - Fixed up header.
 * 14-Jan-02    Paul            VBM:2002011414 - Added annotations to indicate
 *                              which of the properties of this class
 *                              constitute its identity.
 * 31-Jan-02    Paul            VBM:2001122105 - Made encoding private.
 * 04-Mar-02    Adrian          VBM:2002021908 - Added annotations to to allow
 *                              generation of RepositoryAccessors for
 *                              DynamicVisualAssets
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 02-Apr-02    Mat             VBM:2002022009 - Added 
 *                              @mariner-generate-imd-accessor and also added
 *                              a constructor that takes an Attributes class.
 * 03-Apr-02    Mat             VBM:2002022009 - Removed constructor that 
 *                              took an Attributes class.
 * 26-Apr-02    Mat             VBM:2002040814 - Added 
 *                              @mariner-generate-remote-accessor.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful.
 * 26-Jul-02    Allan           VBM:2002072508 - Changed to extend
 *                              SubstantiveAsset.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * The DynamicVisual class represents some kind of moving image asset.
 * Examples of such assets include MPEG movies, Real Video clips and
 * Macromedia Flash movies. The combination of name, encoding, pixelsX and
 * pixelsY attributes is unique in the repository.
 *
 * @mariner-hidden-comment The following tags are used by a code generation
 * doclet to automatically generate an Identity class for this class. Each
 * tag specifies the name of a field which constitutes part of the identity.
 * The order of the fields is the order of the parameters to the Identity
 * classes constructor.
 *
 * @mariner-object-identity-field project
 * @mariner-object-identity-field name
 * @mariner-object-identity-field encoding
 * @mariner-object-identity-field pixelsX
 * @mariner-object-identity-field pixelsY
 * @mariner-object-null-is-empty-string-field value
 * @mariner-object-guardian com.volantis.mcs.components.DynamicVisualComponent

 * @mariner-generate-imd-accessor
 *
 * @mariner-generate-property-lookup
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public class DynamicVisualAsset
  extends SubstantiveAsset {

    /**
   * The value assigned to the encoding to indicate that the dynamic visual
   * is encoded as an animated gif
   */
  public static final int ANIMATED_GIF = 1;

  /**
   * The value assigned to the encoding to indicate that the dynamic visual
   * is encoded as an MPEG type 1 movie
   */
  public static final int MPEG1 = 2;

  /**
   * The value assigned to the encoding to indicate that the dynamic visual
   * is encoded as an MPEG type 4 movie
   */
  public static final int MPEG4 = 4;

  /**
   * The value assigned to the encoding to indicate that the dynamic visual
   * is encoded as a Windows media movie
   */
  public static final int WINDOWS_MEDIA_VIDEO = 8;

  /**
   * The value assigned to the encoding to indicate that the dynamic visual
   * is encoded as a Macromedia Flash movie
   */
  public static final int MACROMEDIA_FLASH = 16;

  /**
   * The value assigned to the encoding to indicate that the dynamic visual
   * is encoded as a Macromedia Shockwave movie
   */
  public static final int MACROMEDIA_SHOCKWAVE = 32;

  /**
   * The value assigned to the encoding to indicate that the dynamic visual
   * is encoded as a QuickTime movie
   */
  public static final int QUICKTIME_VIDEO = 64;

  /**
   * The value assigned to the encoding to indicate that the dynamic visual
   * is encoded as a Real Video clip
   */
  public static final int REAL_VIDEO = 128;

  /**
   * The value assigned to the encoding to indicate that the dynamic visual
   * is encoded as a TV clip
   */
  public static final int TV = 256;

    /**
     * "3GPP is the worldwide standards for the creation, delivery and
     * playback of multimedia over 3rd generation, high-speed wireless
     * networks, as defined by the 3rd Generation Partnership Project
     */
    public static final int THREE_GPP = 512;

  /**
   * @deprecated Internal use only.
   */
  public static final String ANIMATED_GIF_NAME = "gif";

  /**
   * @deprecated Internal use only.
   */
  public static final String MPEG1_NAME = "mpeg1";

  /**
   * @deprecated Internal use only.
   */
  public static final String MPEG4_NAME = "mpeg4";

  /**
   * @deprecated Internal use only.
   */
  public static final String WINDOWS_MEDIA_VIDEO_NAME = "winvideo";

  /**
   * @deprecated Internal use only.
   */
  public static final String MACROMEDIA_FLASH_NAME = "flash";

  /**
   * @deprecated Internal use only.
   */
  public static final String MACROMEDIA_SHOCKWAVE_NAME = "shock";

  /**
   * @deprecated Internal use only.
   */
  public static final String QUICKTIME_VIDEO_NAME = "quicktime";

  /**
   * @deprecated Internal use only.
   */
  public static final String REAL_VIDEO_NAME = "realvideo";

  /**
   * @deprecated Internal use only.
   */
  public static final String TV_NAME = "tv";

  /**
   * The natural width of the dynamic visual asset in pixels.
   */
  private int pixelsX;

  /**
   * The natural height of the dynamic visual asset in pixels.
   */
  private int pixelsY;

  /**
   * The encoding type of the dynamic visual asset.
   *
   * @mariner-object-field-xml-mapping DynamicVisualAsset.ANIMATED_GIF
   *                                   "gif"
   * @mariner-object-field-xml-mapping DynamicVisualAsset.MPEG1
   *                                   "mpeg1"
   * @mariner-object-field-xml-mapping DynamicVisualAsset.MPEG4
   *                                   "mpeg4"
   * @mariner-object-field-xml-mapping DynamicVisualAsset.WINDOWS_MEDIA_VIDEO
   *                                   "windowsVideo"
   * @mariner-object-field-xml-mapping DynamicVisualAsset.MACROMEDIA_FLASH
   *                                   "flash"
   * @mariner-object-field-xml-mapping DynamicVisualAsset.MACROMEDIA_SHOCKWAVE
   *                                   "shock"
   * @mariner-object-field-xml-mapping DynamicVisualAsset.QUICKTIME_VIDEO
   *                                   "quickTime"
   * @mariner-object-field-xml-mapping DynamicVisualAsset.REAL_VIDEO
   *                                   "realVideo"
   * @mariner-object-field-xml-mapping DynamicVisualAsset.TV
   *                                   "tv"
   * @mariner-object-field-xml-mapping DynamicVisualAsset.THREE_GPP
   *                                   "3gpp"
   */
  private int encoding;
  
  /** Constructor
   */  
  public DynamicVisualAsset () {
    this ((String) null);
  }

  /** Constructor
   * @param name The name of the asset
   */  
  public DynamicVisualAsset (String name) {
    this (name, null, null, ANIMATED_GIF, 0, 0);
  }

  /** Constructor
   * @param pixelsX The width of the asset in pixels
   * @param pixelsY The height of the asset in pixels
   * @param assetGroupName The name of the asset group for this asset
   * @param value The value of this asset
   * @param encoding The encoding of this asset
   * @param name The name of the asset */  
  public DynamicVisualAsset (String name, String assetGroupName,
                             String value, int encoding, int pixelsX,
                             int pixelsY) {

    setName (name);
    setAssetGroupName (assetGroupName);
    setValue (value);
    setEncoding (encoding);
    setPixelsX (pixelsX);
    setPixelsY (pixelsY);
  }
  
  /**
   * Create a new <code>DynamicVisualAsset</code>.
   * @param identity The identity of the <code>DynamicVisualAsset</code>.
   */
  public DynamicVisualAsset (DynamicVisualAssetIdentity identity) {
    super (identity);

    setEncoding (identity.getEncoding ());
    setPixelsX (identity.getPixelsX ());
    setPixelsY (identity.getPixelsY ());
  }
  
  /**
   * Get the value of encoding.
   * @return value of encoding.
   */
  public int getEncoding() {
    return encoding;
  }
  
  /**
   * Set the value of encoding.
   * @param encoding Value to assign to encoding.
   */
  public void setEncoding(int encoding) {
    this.encoding = encoding;
    identityChanged ();
  }

  /**
   * Get the width of the asset in pixels
   * @return The width of the asset in pixels
   */
  public int getPixelsX () {
    return pixelsX;
  }

  /**
   * Set the width of the asset in pixels
   * @param pixelsX The width of the asset in pixels
   */
  public void setPixelsX (int pixelsX) {
    this.pixelsX = pixelsX;
    identityChanged ();
  }

  /**
   * Get the height of the asset in pixels
   * @return The height of the asset in pixels
   */
  public int getPixelsY () {
    return pixelsY;
  }

  /**
   * Set the height of the asset in pixels
   * @param pixelsY The height of the asset in pixels
   */
  public void setPixelsY (int pixelsY) {
    this.pixelsY = pixelsY;
    identityChanged ();
  }

  // Javadoc inherited from super class.
  protected RepositoryObjectIdentity createIdentity () {
    return new DynamicVisualAssetIdentity(getProject(), getName(),
            getEncoding(), getPixelsX(), getPixelsY());
  }

  /** Compare this asset object with another asset object to see if 
   * they are equivalent
   * @param object The object to compare with this object
   * @return True if the objects are equivalent and false otherwise
   */
  public boolean equals (Object object) {
    if (object instanceof DynamicVisualAsset) {
      DynamicVisualAsset o = (DynamicVisualAsset) object;
      return super.equals (o)
        && pixelsX == o.pixelsX
        && pixelsY == o.pixelsY
        && encoding == o.encoding;
    }

    return false;
  }

  /** Generate a hash code representing this asset.
   * @return The hash code that represents this object
   */  
  public int hashCode () {
    return super.hashCode ()
      + pixelsX
      + pixelsY
      + encoding;
  }

  /** Generate a String from the parameters used to construct the asset
   * @return The generated String
   */  
  protected String paramString () {
    return super.paramString ()
      + "," + pixelsX + "x" + pixelsY
      + "," + encoding;
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 28-Sep-05	9445/1	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 19-May-05	8359/3	allan	VBM:2005051606 Rework issues

 19-May-05	8359/1	allan	VBM:2005051606 Support 3gpp dynamic visual encoding

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 06-Jan-04	2362/1	mat	VBM:2004010207 Enable fields to be tagged to output empty string values for attributes

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
