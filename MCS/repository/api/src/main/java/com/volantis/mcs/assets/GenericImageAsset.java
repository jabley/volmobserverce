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
 * $Header: /src/voyager/com/volantis/mcs/assets/GenericImageAsset.java,v 1.19 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 24-May-01    Paul            VBM:2001051103 - Added default encoding and
 *                              rendering.
 * 26-Jun-01    Paul            VBM:2001051103 - Added equals and hashCode
 *                              methods and updated toString value.
 * 16-Jul-01    Paul            VBM:2001070508 - Added identityMatches method.
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc
 * 24-Oct-01    Paul            VBM:2001092608 - Removed the identityMatches
 *                              method and added the createIdentity method.
 *                              Also made sure that any changes to those
 *                              fields which form part of this objects identity
 *                              causes the cached identity to be discarded.
 * 14-Jan-02    Paul            VBM:2002011414 - Added annotations to indicate
 *                              which of the properties of this class
 *                              constitute its identity.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - add annotations for auto
 *                              generated accessors
 * 02-Apr-02    Mat             VBM:2002022009 - Added
 *                              @mariner-generate-imd-accessor and also added
 *                              a constructor that takes an Attributes class.
 * 03-Apr-02    Mat             VBM:2002022009 - Removed constructor that
 *                              took an Attributes class.
 * 26-Apr-02    Adrian          VBM:2002040811 - Added annotation to ignore the
 *                              localSrc superclass field when generating the
 *                              accessors.
 * 30-Apr-02    Mat             VBM:2002040814 - Added
 *                              @mariner-generate-remote-accessor 
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful and
 *                              added annotations to flag the required fields.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * The GenericImageAsset class represents an image asset that is not associated
 * with a particular device. This kind of image asset is appropriate in
 * situations where the task of assigning images to devices would be too
 * onerous. The combination of all of the attributes in the ImageAsset,
 * from which it inherits, together with its own widthHint attribute, is unique
 * within the repository.
 *
 * @mariner-hidden-comment The following tags are used by a code generation
 * doclet to automatically generate an Identity class for this class. Each
 * tag specifies the name of a field which constitutes part of the identity.
 * The order of the fields is the order of the parameters to the Identity
 * classes constructor.
 *
 * @mariner-object-identity-field project
 * @mariner-object-identity-field name
 * @mariner-object-identity-field pixelsX
 * @mariner-object-identity-field pixelsY
 * @mariner-object-identity-field pixelDepth
 * @mariner-object-identity-field rendering
 * @mariner-object-identity-field encoding
 * @mariner-object-identity-field widthHint
 *
 * @mariner-object-required-field value
 *
 * @mariner-object-ignore-field localSrc
 *
 * @mariner-object-guardian com.volantis.mcs.components.ImageComponent
 *
 * @mariner-generate-accessor-import com.volantis.mcs.assets.ImageAsset
 *
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
public class GenericImageAsset
  extends ImageAsset {

    /**
   * The maximum width of the image, given here as a percentage of the
   * width of the device. This asset will be considered a candidate only
   * if the width of the image is less than this percentage of the width
   * of the device display.
   */
  private int widthHint;

  /** Constructor
   */
  public GenericImageAsset () {
  }

  /** Constructor
   * @param name The name of the asset */
  public GenericImageAsset (String name) {
    setName (name);
    setRendering (MONOCHROME);
    setEncoding (BMP);
  }

  /** Constructor
   * @param name The name of the asset
   * @param pixelsX The width of the asset in pixels
   * @param pixelsY The height of the asset in pixels
   * @param pixelDepth The number of bits per pixel
   * @param rendering The rendering
   * @param encoding The encoding of this asset
   * @param widthHint Percentage of the device width that the image should occupy
   * @param assetGroupName The name of the asset group for this asset
   * @param value The value of this asset
   */
  public GenericImageAsset (String name, int pixelsX, int pixelsY,
                            int pixelDepth, int rendering, int encoding,
                            int widthHint, String assetGroupName,
                            String value) {
    setName(name);
    setPixelsX(pixelsX);
    setPixelsY(pixelsY);
    setPixelDepth(pixelDepth);
    setRendering(rendering);
    setEncoding(encoding);
    setWidthHint(widthHint);
    setAssetGroupName(assetGroupName);
    setValue(value);
  }

  /**
   * Create a new <code>GenericImageAsset</code>.
   * @param identity The identity of the <code>GenericImageAsset</code>.
   */
  public GenericImageAsset (GenericImageAssetIdentity identity) {
    super (identity);

    setPixelsX (identity.getPixelsX ());
    setPixelsY (identity.getPixelsY ());
    setPixelDepth (identity.getPixelDepth ());
    setRendering (identity.getRendering ());
    setEncoding (identity.getEncoding ());
    setWidthHint (identity.getWidthHint ());
  }

  /**
   * Override this method in order to make sure that the cached identity
   * reference is cleared.
   */
  // Some javadoc inherited from super class.
  public void setPixelsX (int pixelsX) {
    super.setPixelsX (pixelsX);
    identityChanged ();
  }

  /**
   * Override this method in order to make sure that the cached identity
   * reference is cleared.
   */
  // Some javadoc inherited from super class.
  public void setPixelsY (int pixelsY) {
    super.setPixelsY (pixelsY);
    identityChanged ();
  }

  /**
   * Override this method in order to make sure that the cached identity
   * reference is cleared.
   */
  // Some javadoc inherited from super class.
  public void setPixelDepth (int pixelDepth) {
    super.setPixelDepth (pixelDepth);
    identityChanged ();
  }

  /**
   * Override this method in order to make sure that the cached identity
   * reference is cleared.
   */
  // Some javadoc inherited from super class.
  public void setEncoding(int encoding) {
    super.setEncoding (encoding);
    identityChanged ();
  }

  /**
   * Override this method in order to make sure that the cached identity
   * reference is cleared.
   */
  // Some javadoc inherited from super class.
  public void setRendering (int rendering) {
    super.setRendering (rendering);
    identityChanged ();
  }

  /**
   * Set the width hint
   * @param widthHint The width hint
   */
  public void setWidthHint (int widthHint) {
    this.widthHint = widthHint;
    identityChanged ();
  }

  /**
   * Get the width hint
   * @return The width hint
   */
  public int getWidthHint () {
    return widthHint;
  }

  // Javadoc inherited from super class.
  protected RepositoryObjectIdentity createIdentity () {
    return new GenericImageAssetIdentity (getProject(),
                                          getName (),
                                          getPixelsX (),
                                          getPixelsY (),
                                          getPixelDepth (),
                                          getRendering (),
                                          getEncoding (),
                                          getWidthHint ());
  }

  /** Compare this asset object with another asset object to see if
   * they are equivalent
   * @param object The object to compare with this object
   * @return True if the objects are equivalent and false otherwise
   */
  public boolean equals (Object object) {
    if (object instanceof GenericImageAsset) {
      GenericImageAsset o = (GenericImageAsset) object;
      return super.equals (o)
        && widthHint == o.widthHint;
    }

    return false;
  }

  /** Generate a hash code representing this asset.
   * @return The hash code that represents this object
   */
  public int hashCode () {
    return super.hashCode ()
      + widthHint;
  }

  /** Generate a String from the parameters used to construct the asset
   * @return The generated String
   */
  protected String paramString () {
    return super.paramString ()
      + "," + widthHint + "%";
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 28-Sep-05	9445/1	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
