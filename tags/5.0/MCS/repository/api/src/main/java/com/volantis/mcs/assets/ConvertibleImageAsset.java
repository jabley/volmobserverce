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
 * $Header: /src/voyager/com/volantis/mcs/assets/ConvertibleImageAsset.java,v 1.5 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Aug-02    Ian             VBM:2002081303 - Created from 
 *                              GenericImageAsset.
 * 06-Sep-02    Ian             VBM:2002081307 - Changed identity to just name 
 *                              for selection policy.
 * 17-Dec-02    Steve           VBM:2002090601 - Added stub setDeviceName method
 *                              as the rpdm xsd has a device attribute.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;
import com.volantis.mcs.utilities.PreservedArea;


/**
 * The ConvertibleImageAsset class represents an image asset that is not associated
 * with a particular device. This kind of image asset is appropriate in
 * situations where the task of assigning images to devices would be too
 * onerous. The combination of all of the attributes in the ImageAsset,
 * from which it inherits, is unique within the repository.
 *
 * @mariner-hidden-comment The following tags are used by a code generation
 * doclet to automatically generate an Identity class for this class. Each
 * tag specifies the name of a field which constitutes part of the identity.
 * The order of the fields is the order of the parameters to the Identity
 * classes constructor.
 *
 * @mariner-object-identity-field project
 * @mariner-object-identity-field name
 * 
 * @mariner-object-required-field pixelsX
 * @mariner-object-required-field pixelsY
 * @mariner-object-required-field pixelDepth
 * @mariner-object-required-field rendering
 * @mariner-object-required-field encoding
 * @mariner-object-required-field value
 * @mariner-object-null-is-empty-string-field preserveLeftAsString
 * @mariner-object-null-is-empty-string-field preserveRightAsString
 *
 * @mariner-object-ignore-field localSrc
 * @mariner-object-ignore-field preserveLeft
 * @mariner-object-ignore-field preserveRight
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
public class ConvertibleImageAsset
  extends ImageAsset {

    /**
     *  The left X bound of the image preserved area (not clippable) in px.
     *   @mariner-object-field-xml-attribute preserveLeft
     *   @mariner-object-field-length 20
     */
    private int preserveLeft = PreservedArea.PRESERVE_ALL_LEFT;

    /**
     *  The right X bound of the image preserved area (not clippable) in px.
     *  @mariner-object-field-xml-attribute preserveRight
     *  @mariner-object-field-length 20
     */
    private int preserveRight = PreservedArea.PRESERVE_ALL_RIGHT;


  /** Constructor
   */
  public ConvertibleImageAsset () {
  }

  /** Constructor
   * @param name The name of the asset */
  public ConvertibleImageAsset (String name) {
    setName (name);
    // setRendering (MONOCHROME);
    setRendering (COLOR);
    setEncoding (BMP);
  }

    /** Constructor
     * @param name The name of the asset
     * @param pixelsX The width of the asset in pixels
     * @param pixelsY The height of the asset in pixels
     * @param pixelDepth The number of bits per pixel
     * @param rendering The rendering
     * @param encoding The encoding of this asset
     * @param assetGroupName The name of the asset group for this asset
     * @param value The value of this asset
     */

    public ConvertibleImageAsset (String name, int pixelsX, int pixelsY,
            int pixelDepth, int rendering, int encoding,
            String assetGroupName,
            String value)
    {
        this(name,  pixelsX,  pixelsY,
                pixelDepth,  rendering,  encoding,
                assetGroupName,
                value,PreservedArea.PRESERVE_ALL_LEFT,PreservedArea.PRESERVE_ALL_RIGHT);
    }

    /** Constructor
     * @param name The name of the asset
     * @param pixelsX The width of the asset in pixels
     * @param pixelsY The height of the asset in pixels
     * @param pixelDepth The number of bits per pixel
     * @param rendering The rendering
     * @param encoding The encoding of this asset
     * @param assetGroupName The name of the asset group for this asset
     * @param value The value of this asset
     * @param preserveLeft The preserveLeft of this asset
     * @param preserveRight The preserveRight of this asset
     */
    public ConvertibleImageAsset (String name, int pixelsX, int pixelsY,
            int pixelDepth, int rendering, int encoding,
            String assetGroupName,
            String value,
            int preserveLeft,
            int preserveRight) {
        setName(name);
        setPixelsX(pixelsX);
        setPixelsY(pixelsY);
        setPixelDepth(pixelDepth);
        setRendering(rendering);
        setEncoding(encoding);
        setAssetGroupName(assetGroupName);
        setValue(value);
        setPreserveLeft(preserveLeft);
        setPreserveRight(preserveRight);
    }

   /**
    * Create a new <code>ConvertibleImageAsset</code>.
    * @param identity The identity of the <code>ConvertibleImageAsset</code>.
    */
   public ConvertibleImageAsset (ConvertibleImageAssetIdentity identity) {
     super (identity);
 
   }
  

   /**
    * Override this method in order to make sure that the cached identity
    * reference is cleared.
    */
   // Some javadoc inherited from super class.
   public void setPixelsX (int pixelsX) {
     super.setPixelsX (pixelsX);
   }

   /**
    * Override this method in order to make sure that the cached identity
    * reference is cleared.
    */
   // Some javadoc inherited from super class.
   public void setPixelsY (int pixelsY) {
     super.setPixelsY (pixelsY);
   }

   /**
    * Override this method in order to make sure that the cached identity
    * reference is cleared.
    */
   // Some javadoc inherited from super class.
   public void setPixelDepth (int pixelDepth) {
     super.setPixelDepth (pixelDepth);
   }

   /**
    * Override this method in order to make sure that the cached identity
    * reference is cleared.
    */
   // Some javadoc inherited from super class.
   public void setEncoding(int encoding) {
     super.setEncoding (encoding);
   }

   /**
    * Override this method in order to make sure that the cached identity
    * reference is cleared.
    */
   // Some javadoc inherited from super class.
   public void setRendering (int rendering) {
     super.setRendering (rendering);
   }


   // Javadoc inherited from super class.
   protected RepositoryObjectIdentity createIdentity () {
     return new ConvertibleImageAssetIdentity(getProject(), getName());
   }

   /** Compare this asset object with another asset object to see if
    * they are equivalent
    * @param object The object to compare with this object
    * @return True if the objects are equivalent and false otherwise
    */
   public boolean equals (Object object) {
     if (object instanceof ConvertibleImageAsset) {
       ConvertibleImageAsset o = (ConvertibleImageAsset) object;
       return super.equals (o);
     }

     return false;
   }

   /**
    * Stub method as device is in the xsd
    */
   public void setDeviceName( String dev )
   {
   }

    // This class specific properties

    /**
     * Gets the left X bound of the preserved area in px
     * @return left X bound of the preserved area in px
     */
    public int getPreserveLeft() {
        return preserveLeft;
    }

    /**
     * Sets the left  X bound of the preserved area
     * @param pl left X bound of the preserved area in px
     */
    public void setPreserveLeft(int pl) {
        this.preserveLeft = pl;
    }

    /**
     * Gets the right X bound of the preserved area in px
     * @return right X bound of the preserved area in px
     */
    public int getPreserveRight() {
        return preserveRight;
    }

    /**
     * Sets the right  X bound of the preserved area
     * @param pr - right X bound of the preserved are in px
     */
    public void setPreserveRight(int pr) {
        this.preserveRight = pr;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Nov-05	9789/4	emma	VBM:2005101113 Supermerge: Refactor JDBC Accessors to use chunked accessor

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 07-Nov-05	10168/1	ianw	VBM:2005102504 port forward web clipping

 07-Nov-05	10170/1	ianw	VBM:2005102504 port forward web clipping

 28-Sep-05	9445/1	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
