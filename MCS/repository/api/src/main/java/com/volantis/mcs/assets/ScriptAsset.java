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
 * $Header: /src/voyager/com/volantis/mcs/assets/ScriptAsset.java,v 1.14 2003/04/23 09:44:19 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 06-Feb-02    Paul            VBM:2001122103 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 02-Apr-02    Mat             VBM:2002022009 - Added 
 *                              @mariner-generate-imd-accessor and also added
 *                              a constructor that takes an Attributes class.
 * 03-Apr-02    Mat             VBM:2002022009 - Removed constructor that 
 *                              took an Attributes class.
 * 16-Apr-02    Allan           VBM:2002041602 - Added the JAVA_SCRIPT constant
 *                              and added to list of possible programming
 *                              languages. 
 * 26-Apr-02    Mat             VBM:2002040814 - Added 
 *                              @mariner-generate-remote-accessor.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful and
 *                              added annotations to constrain field lengths,
 *                              and flag the required fields.
 * 10-Jun-02    Allan           VBM:2002030615 - Added object-field-value 
 *                              javadoc properties to mimeType.
 * 26-Jul-02    Allan           VBM:2002072508 - Changed to extend
 *                              SubstantiveAsset.
 * 06-Aug-02    Sumit           VBM:2002080509 - Added WMLTask language
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 23-Apr-03    Geoff           VBM:2003040305 - Add dubious "content type" 
 *                              for WMLTasks as per Pauls instruction.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * The ScriptAsset class represents a text asset associated with a particular
 * device or device family. The asset references a file containing text using a
 * URI.
 *
 * @mariner-hidden-comment The following tags are used by a code generation
 * doclet to automatically generate an Identity class for this class. Each
 * tag specifies the name of a field which constitutes part of the identity.
 * The order of the fields is the order of the parameters to the Identity
 * classes constructor.
 *
 * @mariner-object-identity-field project
 * @mariner-object-identity-field name
 * @mariner-object-identity-field deviceName
 *
 * @mariner-object-required-field programmingLanguage
 * @mariner-object-required-field mimeType
 * @mariner-object-required-field valueType
 *
 * @mariner-object-null-is-empty-string-field value
 * @mariner-object-null-is-empty-string-field characterSet
 * @mariner-object-guardian com.volantis.mcs.components.ScriptComponent
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
public class ScriptAsset
  extends SubstantiveAsset {

    /**
   * URL value type constant.
   */
  public final static int URL = 1;

  /**
   * LITERAL value type constant.
   */
  public final static int LITERAL = 2;

  /**
   * Constant for specifying a programming language of JavaScript, this
   * is written out to the protocol so must not be changed.
   */
  public final static String JAVA_SCRIPT = "JavaScript";

  /**
   * Constant for specifying a programming language of JavaScript 1.0, this
   * is written out to the protocol so must not be changed.
   */
  public final static String JAVA_SCRIPT_1_0 = "JavaScript1.0";

  /**
   * Constant for specifying a programming language of JavaScript 1.1, this
   * is written out to the protocol so must not be changed.
   */
  public final static String JAVA_SCRIPT_1_1 = "JavaScript1.1";

  /**
   * Constant for specifying a programming language of JavaScript 1.2, this
   * is written out to the protocol so must not be changed.
   */
  public final static String JAVA_SCRIPT_1_2 = "JavaScript1.2";

  /**
   * Constant for specifying a programming language of JavaScript 1.3, this
   * is written out to the protocol so must not be changed.
   */
  public final static String JAVA_SCRIPT_1_3 = "JavaScript1.3";

  /**
   * Constant for specifying a programming language of JavaScript 1.4, this
   * is written out to the protocol so must not be changed.
   */
  public final static String JAVA_SCRIPT_1_4 = "JavaScript1.4";

  /**
   * Constant for specifying a programming language of WMLTask, this
   * is written out to the protocol so must not be changed.
   */
  public final static String WML_TASK = "WMLTask";


  

  /**
   * The name of the device or device family to which this asset applies
   *
   * @mariner-object-field-length 255
   */
  private String deviceName;

  /**
   * The programming language in which the script is written.
   *
   * @mariner-object-field-value ScriptAsset.JAVA_SCRIPT
   * @mariner-object-field-value ScriptAsset.JAVA_SCRIPT_1_0
   * @mariner-object-field-value ScriptAsset.JAVA_SCRIPT_1_1
   * @mariner-object-field-value ScriptAsset.JAVA_SCRIPT_1_2
   * @mariner-object-field-value ScriptAsset.JAVA_SCRIPT_1_3
   * @mariner-object-field-value ScriptAsset.JAVA_SCRIPT_1_4
   * @mariner-object-field-value ScriptAsset.WML_TASK
   *
   * @mariner-object-field-length 255
   */
  private String programmingLanguage;

  /**
   * The mime type associated with the scripting language.
   *
   * @mariner-object-field-value "text/javascript"
   * @mariner-object-field-value "application/x-javascript"
   * @mariner-object-field-value "text/vnd.wap.wml"
   *
   * @mariner-object-field-length 255
   */
  private String mimeType;

  /**
   * The character set associated with the scripting language.
   *
   * @mariner-object-field-length 255
   */
  private String characterSet;

  /**
   * The type of the value of the asset.
   *
   * @mariner-object-field-xml-mapping ScriptAsset.URL "url"
   * @mariner-object-field-xml-mapping ScriptAsset.LITERAL "literal"
   */
  private int valueType;

  /**
   * Create a new <code>ScriptAsset</code>.
   */  
  public ScriptAsset () {
    this ((String) null);
  }

  /**
   * Create a new <code>ScriptAsset</code>.
   * @param name The name of the asset
   */  
  public ScriptAsset (String name) {
    this (name, null, null, null, null, URL, null, null);
  }

  /**
   * Create a new <code>ScriptAsset</code>.
   * @param name The name of the asset
   * @param deviceName The name of the device associated with the asset
   * @param programmingLanguage The programming language in which the script
   * is written.
   * @param mimeType The MIME type associated with the programming language.
   * @param valueType The type of value that this asset represents, either
   * URL or LITERAL. 
   * @param assetGroupName The name of the asset group associated with this
   * asset.
   * @param value The value of the asset
   */  
   public ScriptAsset (String name,
                       String deviceName,
                       String programmingLanguage,
                       String mimeType,
                       String characterSet,
                       int valueType,
                       String assetGroupName,
                       String value) {

    setName (name);
    setDeviceName (deviceName);
    setProgrammingLanguage (programmingLanguage);
    setMimeType (mimeType);
    setCharacterSet (characterSet);
    setValueType (valueType);
    setAssetGroupName (assetGroupName);
    setValue (value);
  }

  /**
   * Create a new <code>ScriptAsset</code>.
   * @param identity The identity of the <code>ScriptAsset</code>.
   */
  public ScriptAsset (ScriptAssetIdentity identity) {
    super (identity);

    setDeviceName (identity.getDeviceName ());
  }
  
  /**
   * Set the character set
   * @param characterSet The character set
   */
  public void setCharacterSet (String characterSet) {
    this.characterSet = characterSet;
    identityChanged ();
  }

  /**
   * Get the character set
   * @return The character set
   */
  public String getCharacterSet () {
    return characterSet;
  }

  /**
   * Set the device name
   * @param deviceName The device name
   */
  public void setDeviceName (String deviceName) {
    this.deviceName = deviceName;
    identityChanged ();
  }

  /**
   * Retrieve the device name
   * @return The device name
   */
  public String getDeviceName () {
    return deviceName;
  }

  /**
   * Set the mime type
   * @param mimeType The mime type
   */
  public void setMimeType (String mimeType) {
    this.mimeType = mimeType;
    identityChanged ();
  }

  /**
   * Get the mime type
   * @return The mime type
   */
  public String getMimeType () {
    return mimeType;
  }

  /**
   * Set the programming language
   * @param programmingLanguage The programming language
   */
  public void setProgrammingLanguage (String programmingLanguage) {
    this.programmingLanguage = programmingLanguage;
    identityChanged ();
  }

  /**
   * Get the programming language
   * @return The programming language
   */
  public String getProgrammingLanguage () {
    return programmingLanguage;
  }

  /**
   * Set the type of value
   * @param valueType The type of value
   */
  public void setValueType (int valueType) {
    this.valueType = valueType;
  }

  /**
   * Get the type of value
   * @return The type of value
   */
  public int getValueType () {
    return valueType;
  }

  // Javadoc inherited from super class.
  protected RepositoryObjectIdentity createIdentity () {
    return new ScriptAssetIdentity(getProject(), getName(), getDeviceName());
  }

  /**
   * Compare this asset object with another asset object to see if 
   * they are equivalent
   * @param object The object to compare with this object
   * @return True if the objects are equivalent and false otherwise
   */
  public boolean equals (Object object) {
    if (object instanceof ScriptAsset) {
      ScriptAsset o = (ScriptAsset) object;
      return super.equals (o)
        && equals (deviceName, o.deviceName)
        && equals (programmingLanguage, o.programmingLanguage)
        && equals (mimeType, o.mimeType)
        && equals (characterSet, o.characterSet)
        && valueType == o.valueType;
    }

    return false;
  }

  /**
   * Generate a hash code representing this asset.
   * @return The hash code that represents this object
   */  
  public int hashCode () {
    return super.hashCode ()
      + hashCode (deviceName)
      + hashCode (programmingLanguage)
      + hashCode (mimeType)
      + hashCode (characterSet)
      + valueType;
  }

  /** Generate a String from the parameters used to construct the asset
   * @return The generated String
   */  
  protected String paramString () {
    return super.paramString ()
      + ", deviceName=" + deviceName
      + ", programmingLanguage=" + programmingLanguage
      + ", mimeType=" + mimeType
      + ", characterSet=" + characterSet
      + ", valueType=" + valueType;
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

 19-Jan-04	2672/1	geoff	VBM:2004011511 Export: CharcterSet is not exported from JDBC repository for script component

 06-Jan-04	2362/1	mat	VBM:2004010207 Enable fields to be tagged to output empty string values for attributes

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
