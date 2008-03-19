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
 * $Header: /src/voyager/com/volantis/mcs/assets/TextAsset.java,v 1.26 2003/03/24 16:35:25 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Jun-01    Paul            VBM:2001051103 - Added this change history,
 *                              sorted out some comments and changed the
 *                              indentation from the rose style of 3 spaces
 *                              to 2, fixed the constructor, added equals and
 *                              hashCode methods and updated toString value.
 * 29-Jun-01    Paul            VBM:2001062906 - Rename textType to encoding
 *                              and add a valueType.
 * 09-Jul-01    Paul            VBM:2001070902 - Added encoding property which
 *                              has moved back from Asset.
 * 05-Aug-01    Kula            VBM:2001080609 - VoiceXML encodings and Form
 *                              Validation encoding added
 * 17-Aug-01    Paul            VBM:2001081704 - Corrected a spelling mistake
 *                              in VOICEXML_NUANCE_GRAMME(A)R so it matches
 *                              the task document and added the identityMatches
 *                              method.
 * 27-Sep-01    Allan           VBM:2001091104 - Javadoc.
 * 01-Oct-01    Doug            VBM:2001091102 - removed the javadoc that 
 *                              specified that the valueType property is 
 *                              deprecated. It is Not.
 * 24-Oct-01    Paul            VBM:2001092608 - Removed the identityMatches
 *                              method and added the createIdentity method.
 *                              Also made sure that any changes to those
 *                              fields which form part of this objects identity
 *                              causes the cached identity to be discarded.
 * 14-Jan-02    Paul            VBM:2002011414 - Added annotations to indicate
 *                              which of the properties of this class
 *                              constitute its identity.
 * 31-Jan-02    Paul            VBM:2001122105 - Update javadoc of encoding.
 * 06-Feb-02    Paul            VBM:2001122103 - Added some annotations which
 *                              would be used to automatically generate
 *                              accessors for it when this is enabled for this
 *                              class.
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
 *                              added annotations to constrain field lengths,
 *                              and flag the required fields.
 * 26-Jul-02    Allan           VBM:2002072508 - Changed to extend
 *                              SubstantiveAsset.
 * 24-Mar-02    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.assets;

import com.volantis.mcs.objects.RepositoryObjectIdentity;

/**
 * The TextAsset class represents a text asset associated with a particular
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
 * @mariner-object-identity-field language
 *
 * @mariner-object-required-field encoding
 * @mariner-object-required-field valueType
 *
 * @mariner-object-null-is-empty-string-field value
 * @mariner-object-guardian com.volantis.mcs.components.TextComponent
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
public class TextAsset
  extends SubstantiveAsset {

    /**
   * Value assigned to encoding to indicate that the text is plain and does
   * not  contain any Volantis markup.
   */
  public final static int PLAIN = 1;

  /**
   * Value assigned to encoding to indicate that the text does contain
   * Volantis markup.
   */
  public final static int VT_MARKUP = 2;

  /**
   * Value assigned to encoding to indicate that the text consists of an XML
   * document.
   */
  public final static int XML_MARKUP = 3;

  /**
   * VoiceXML-Encodings
   */

  /**
   * Value assigned to encoding to indicate that the text asset contains help
   * text used for VoiceXML.
   */
  public final static int VOICEXML_HELP = 100;

  /**
   * Value assigned to encoding to indicate that the text asset contains prompt
   * text used for VoiceXML.
   */
  public final static int VOICEXML_PROMPT = 101;

  /**
   * Value assigned to encoding to indicate that the text asset contains error
   * message text used for VoiceXML.
   */
  public final static int VOICEXML_ERROR = 102;

  /**
   * Value assigned to encoding to indicate that the text asset contains a
   * Nuance grammar used for VoiceXML.
   */
  public final static int VOICEXML_NUANCE_GRAMMAR = 103;
  
  /**
   * Form Validation Encodings
   */

  /**
   * Defines the text asset contains a validator for a Mariner form
   */
  public final static int FORM_VALIDATOR = 200;  
  
  /**
   * URL value type constant.
   */
  public final static int URL = 1;

  /**
   * LITERAL value type constant.
   */
  public final static int LITERAL = 2;

  /** Indicates that the text is in the default language
   */  
  public final static String DEFAULT_LANGUAGE = "-";

  /**
   * The language in which the text is written.
   *
   * @mariner-object-field-length 32
   */
  private String language;

  /**
   * The name of the device or device family to which this asset applies
   *
   * @mariner-object-field-length 255
   */
  private String deviceName;

  /**
   * The type of the value of the asset.
   *
   * @mariner-object-field-xml-mapping TextAsset.URL "url"
   * @mariner-object-field-xml-mapping TextAsset.LITERAL "literal"
   */
  private int valueType;

  /**
   * The encoding of this asset.
   *
   * @mariner-object-field-xml-mapping TextAsset.PLAIN
   *                                   "plain"
   * @mariner-object-field-xml-mapping TextAsset.VT_MARKUP
   *                                   "volantisMarkup"
   * @mariner-object-field-xml-mapping TextAsset.XML_MARKUP
   *                                   "xmlMarkup"
   * @mariner-object-field-xml-mapping TextAsset.VOICEXML_HELP
   *                                   "voiceXMLHelp"
   * @mariner-object-field-xml-mapping TextAsset.VOICEXML_PROMPT
   *                                   "voiceXMLPrompt"
   * @mariner-object-field-xml-mapping TextAsset.VOICEXML_ERROR
   *                                   "voiceXMLError"
   * @mariner-object-field-xml-mapping TextAsset.VOICEXML_NUANCE_GRAMMAR
   *                                   "voiceXMLNuanceGrammar"
   * @mariner-object-field-xml-mapping TextAsset.FORM_VALIDATOR
   *                                   "formValidator"
   */
  private int encoding;
  
  /** Constructor
   */  
  public TextAsset () {
    this ((String) null);
  }

  /** Constructor
   * @param name The name of the asset
   */  
  public TextAsset (String name) {
    this (name, DEFAULT_LANGUAGE);
  }

  /** Constructor
   * @param name The name of the asset
   * @param language The language associated with the asset
   */  
  public TextAsset (String name, String language) {
    this (name, URL, null, null, PLAIN, language, null);
  }

  /** Constructor
   * @param valueType The type of value that this asset represents, for 
   * example, plain text, Mariner markup or XML
   * @param value The value of the asset
   * @param deviceName The name of the device associated with the asset
   * @param encoding The encoding of the asset
   * @param assetGroupName The name of the asset group associated with this 
   * asset
   * @param name The name of the asset
   * @param language The language associated with the asset
   */  
   public TextAsset (String name, int valueType, String value,
                    String deviceName, int encoding, String language,
                    String assetGroupName) {
    setName (name);
    setValueType (valueType);
    setValue (value);
    setDeviceName (deviceName);
    setEncoding (encoding);
    setLanguage (language);
    setAssetGroupName (assetGroupName);
    setEncoding(encoding);
  }

  /**
   * Create a new <code>TextAsset</code>.
   * @param identity The identity of the <code>TextAsset</code>.
   */
  public TextAsset (TextAssetIdentity identity) {
    super (identity);

    setDeviceName (identity.getDeviceName ());
    setLanguage (identity.getLanguage ());
  }
  
  /**
   * Retrieve the device name
   * @return The device name
   */
  public String getDeviceName () {
    return deviceName;
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
   * Get the encoding
   * @return The encoding.
   */
  public int getEncoding() {
    return encoding;
  }
  
  /**
   * Set the encoding
   * @param encoding The encoding
   */
  public void setEncoding(int encoding) {
    this.encoding = encoding;
  }

  /**
   * Get the type of value
   * @return The type of value
   */
  public int getValueType () {
    return valueType;
  }

  /**
   * Set the type of value
   * @param valueType The type of value
   */
  public void setValueType (int valueType) {
    this.valueType = valueType;
  }


  /**
   * Get the language
   * @return The language
   */
  public String getLanguage () {
    return language;
  }

  /**
   * Set the language
   * @param language The language
   */
  public void setLanguage (String language) {
      // HACK: JiBX calls this with null if the optional language is not present
      // in the schema, which then breaks the JDBC accessors since language is
      // mandatory in the database. This used to be handled by the old XML
      // accessors by a default= in the xsd but JiBX does not use this. This is
      // inconsistent and needs to be fixed but for now we just hack the
      // default langauge in.
      if (language == null) {
          language = DEFAULT_LANGUAGE;
      }
    this.language = language;
    identityChanged ();
  }

  // Javadoc inherited from super class.
  protected RepositoryObjectIdentity createIdentity () {
    return new TextAssetIdentity(getProject(), getName(),
                                 getDeviceName(), getLanguage());
  }

  /** Compare this asset object with another asset object to see if 
   * they are equivalent
   * @param object The object to compare with this object
   * @return True if the objects are equivalent and false otherwise
   */
  public boolean equals (Object object) {
    if (object instanceof TextAsset) {
      TextAsset o = (TextAsset) object;
      return super.equals (o)
        && valueType == o.valueType
        && encoding == o.encoding
        && equals (deviceName, o.deviceName)
        && equals (language, o.language);
    }
    return false;
  }

  /** Generate a hash code representing this asset.
   * @return The hash code that represents this object
   */  
  public int hashCode () {
    return super.hashCode ()
      + valueType
      + encoding
      + hashCode (deviceName)
      + hashCode (language);
  }

  /** Generate a String from the parameters used to construct the asset
   * @return The generated String
   */  
  protected String paramString () {
    return super.paramString ()
      + "," + valueType
      + "," + encoding
      + "," + deviceName
      + "," + language;
  }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 03-Nov-05	9789/4	emma	VBM:2005101113 Supermerge: Migrate JDBC Accessors to use chunked accessors

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 27-Oct-05	9986/1	geoff	VBM:2005102512 MCS35: Investigate and fix any JDBC repository import/export problems

 28-Sep-05	9445/1	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 21-Jan-04	2701/1	mat	VBM:2004012105 Changed length of language field to 32

 06-Jan-04	2362/1	mat	VBM:2004010207 Enable fields to be tagged to output empty string values for attributes

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
