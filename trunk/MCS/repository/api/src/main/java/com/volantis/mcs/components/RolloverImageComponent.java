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
 * $Header: /src/voyager/com/volantis/mcs/components/RolloverImageComponent.java,v 1.19 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 09-Apr-01    Paul            Created.
 * 26-Jun-01    Paul            VBM:2001051103 - Fixed the constructor and
 *                              added equals and hashCode methods and updated
 *                              toString value.
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 24-Oct-01    Paul            VBM:2001092608 - Added the createIdentity
 *                              method.
 * 23-Nov-01    Allan           VBM:2001102504 - Added CATEGORY constant.
 * 04-Jan-02    Paul            VBM:2002010403 - Removed CATEGORY constant and
 *                              updated Category class to explicitly set the
 *                              value. This was necessary because the use of
 *                              this constant created an undesirable dependency
 *                              between the objects package and this package.
 *                              It was only ever used by Category and it is
 *                              only used internally so it is better if it is
 *                              not exposed in this public class.
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
 * 26-Apr-02    Mat             VBM:2002040814 - Added 
 *                              @mariner-generate-remote-accessor.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful and
 *                              added annotations to constrain field lengths.
 * 26-Jul-02    Allan           VBM:2002072602 - fallbackTextComponentName
 *                              property added. Updated equals(), hashCode()
 *                              and paramString() accordingly.
 * 09-Sep-02    Mat             VBM:2002040825 - Added shared accessor tag.
 * 18-Nov-02    Geoff           VBM:2002111504 - implement FallsBackToText so
 *                              that we can make access to fallback common.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.components;

import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * The RolloverImage component represents a set of two images used to
 * implement an image rollover. One image forms the state of the button
 * when the pointer is not over it while the second forms the state when
 * the pointer is over the rollover.
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
public class RolloverImageComponent
  extends AbstractComponent implements FallsBackToText {

  /**
   * The copyright statement.
   */
  private static String mark = "(c) Volantis Systems Ltd 2001.";

  /**
   * The name of a text component that provides alternate text for this
   * component. The alternate text can be used if there are no suitable
   * assets associated with the component or any of its specific fallbacks.
   *
   * @mariner-object-field-length 255
   */
  private String fallbackTextComponentName;

  /**
   * The name of the image component that represents the normal state of
   * the rollover
   *
   * @mariner-object-field-length 255
   */
  private String normalImageComponentName;

  /**
   * The name of the image component that represents the state of the
   * rollover when the pointer is over it
   *
   * @mariner-object-field-length 255
   */
  private String overImageComponentName;

  /**
   * Initialises a new instance with all instance
   * variables set to their default values.
   */
  public RolloverImageComponent () {
  }

  /**
   * Initialises a new instance with the supplied parameters.
   *
   * @param name The name of the rollover image component
   */  
  public RolloverImageComponent (String name) {
    super (name);
  }

  /**
   * Initialises a new instance with the supplied parameters.
   * 
   * @param altText The name of the component providing alternate text
   * @param normalImageComponentName The name of the component providing
   * the 'normal' image
   * @param overImageComponentName The name of the component providing the
   * 'over' image
   * @param name The name of the button image component
   */  
  public RolloverImageComponent (String name, String altText,
                                 String normalImageComponentName,
                                 String overImageComponentName) {
    super (name);
    setFallbackTextComponentName(altText);
    setNormalImageComponentName (normalImageComponentName);
    setOverImageComponentName (overImageComponentName);
  }
  
  /**
   * Create a new <code>RolloverImageComponent</code>.
   * @param identity The identity of the <code>RolloverImageComponent</code>.
   */
  public RolloverImageComponent (RolloverImageComponentIdentity identity) {
    super (identity);
  }

  /**
   * Access method for the fallbackTextComponentName property.
   *
   * @return   the current value of the fallbackTextComponentName property
   * @roseuid
   */
  public String getFallbackTextComponentName () {
    return fallbackTextComponentName;
  }

  /**
   * Sets the value of the fallbackTextComponentName property.
   *
   * @param fallbackTextComponentName the new value of the
   * fallbackTextComponentName property
   * @roseuid
   */
  public void setFallbackTextComponentName (String fallbackTextComponentName) {
    this.fallbackTextComponentName = fallbackTextComponentName;
  }

  /** Retrieve the name of the component providing the 'normal' image
   * @return The name of the component providing the 'normal' image
   */
  public String getNormalImageComponentName () {
    return normalImageComponentName;
  }

  /** Set the name of the component providing the 'norml' image
   * @param normalImageComponentName The name of the component providing the 
   * 'normal' image
   */
  public void setNormalImageComponentName (String normalImageComponentName) {
    this.normalImageComponentName = normalImageComponentName;
  }

  /** Retrieve the name of the component providing the 'over' image
   * @return The name of the component providing the 'over' image
   */
  public String getOverImageComponentName () {
    return overImageComponentName;
  }

  /** Set the name of the component providing the 'over' image
   * @param overImageComponentName The name of the component providing the 
   * 'over' image
   */
  public void setOverImageComponentName (String overImageComponentName) {
    this.overImageComponentName = overImageComponentName;
  }

  // Javadoc inherited from super class.
  protected RepositoryObjectIdentity createIdentity () {
    return new RolloverImageComponentIdentity(getProject(), getName());
  }

  /** Compare this component object with another compoenent object to see if 
   * they are equivalent
   * @param object The object to compare with this object
   * @return True if the objects are equivalent and false otherwise
   */
  public boolean equals (Object object) {
    if (object instanceof RolloverImageComponent) {
      RolloverImageComponent o = (RolloverImageComponent) object;
      return super.equals (o)
        && equals(fallbackTextComponentName, o.fallbackTextComponentName)
        && equals (normalImageComponentName, o.normalImageComponentName)
        && equals (overImageComponentName, o.overImageComponentName);
    }

    return false;
  }

  /** Generate a hash code representing this component.
   * @return The hash code that represents this object
   */  
  public int hashCode () {
    return super.hashCode ()
      + hashCode(fallbackTextComponentName)
      + hashCode (normalImageComponentName)
      + hashCode (overImageComponentName);
  }

  /** Generate a String from the parameters used to construct the component
   * @return The generated String
   */  
  protected String paramString () {
    return super.paramString ()
      + ", " + fallbackTextComponentName 
      + ", " + normalImageComponentName
      + ", " + overImageComponentName;
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 28-Sep-05	9445/1	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 20-May-05	8365/1	rgreenall	VBM:2005051614 Added Javadoc

 19-May-05	8302/3	rgreenall	VBM:2005051614 Post review improvements.

 19-May-05	8302/1	rgreenall	VBM:2005051614 Added Javadoc

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
