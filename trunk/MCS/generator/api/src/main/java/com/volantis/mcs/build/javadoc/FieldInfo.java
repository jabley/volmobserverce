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
 * $Header: /src/voyager/com/volantis/mcs/build/doclets/FieldInfo.java,v 1.7 2002/09/10 09:37:16 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 14-Jan-02    Paul            VBM:2002011414 - Created.
 * 06-Feb-02    Paul            VBM:2001122103 - Added support for specifying
 *                              a list of valid values and mappings in a
 *                              repository object's javadoc for a field.
 * 04-Mar-02    Adrian          VBM:2002021908 - Added new constructor to take
 *                              another FieldInfo.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 22-Mar-02    Adrian          VBM:2002031503 - added support for specifying
 *                              a different external field name
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              properties / method to make them clearer and
 *                              used new javadoc tags including one to indicate
 *                              a maximum length.
 * 09-Sep-02    Mat             VBM:2002040825 - Added code to check for fields
 *                              being cache fields.  The shared repository
 *                              accessors need this information when they 
 *                              are generated.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.javadoc;

import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.Tag;
import com.volantis.mcs.build.javadoc.ClassInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class contains information about a field in a class which is used
 * for generating code.
 */
public class FieldInfo {

  /**
   * The class to which this object belongs.
   */
  private ClassInfo classInfo;

  /**
   * The xml mappings.
   */
  private Map xmlMappings;

  /**
   * The valid values.
   */
  private List values;

  /**
   * The name of the field this object represents.
   */
  private String name;

  /**
   * The name of the xml attribute.
   */
  private String xmlAttributeName;

    /**
     * The name of the ControlType associated with this field.
     */
    private String controlType;

  /**
   * The type of the field this object represents.
   */
  private TypeInfo type;

  /**
   * The comment associated with the field this object represents.
   */
  private String comment;

  /**
   * The maximum length of a string field.
   */
  private int maximumLength;

  /**
   * The underlying JavaDoc object.
   */
  private FieldDoc fieldDoc;

  /**
   * Flag which indicates whether this field is inherited from another class.
   */
  //private boolean inherited;

  /**
   * Flag which indicates whether this field is static.
   */
  private boolean isStatic;
  //private boolean inherited;

  /**
   * Flag which indicates whether this field's values are checkabe.
   */
  private boolean checkable;
  
  /**
   * Is this field a field associated with the cache?
   */
  private boolean cacheField;

  /**
   * Create a new <code>FieldInfo</code>.
   * @param classInfo The class information for the class which the field
   * which this object represents belongs.
   * @param fieldDoc The underlying JavaDoc object representing a class.
   */
  public FieldInfo (ClassInfo classInfo, FieldDoc fieldDoc) {
    this.classInfo = classInfo;
    this.fieldDoc = fieldDoc;

    this.name = fieldDoc.name ();
    this.type = new TypeInfo (fieldDoc.type ());

    this.comment = fieldDoc.getRawCommentText ();
    this.isStatic = fieldDoc.isStatic ();

    Tag [] tags;

    tags = fieldDoc.tags ("mariner-object-field-xml-mapping");
    if (tags.length != 0) {
      xmlMappings = new TreeMap ();
      values = new ArrayList ();
      for (int i = 0; i < tags.length; i += 1) {
        String text = tags [i].text ().trim ();
        //System.out.println ("Mapping text " + text);
        int index = text.indexOf (' ');

        if (index == -1) {
          throw new IllegalStateException ("Invalid mapping format \""
                                           + text + "\"");
        }

        String internalValue = text.substring (0, index).trim ();
        String externalValue = text.substring (index + 1).trim ();
        
        xmlMappings.put (internalValue, externalValue);
        values.add (internalValue);
      }
    }

    tags = fieldDoc.tags ("mariner-object-field-xml-attribute");
    if (tags.length != 0) {
      xmlAttributeName = tags [0].text ().trim ();
    }


    tags = fieldDoc.tags("mariner-object-field-control-type");
    if (tags.length != 0) {
      controlType = tags [0].text().trim();
    }

    tags = fieldDoc.tags ("mariner-object-field-value");
    if (tags.length != 0) {
      values = new ArrayList ();
      for (int i = 0; i < tags.length; i += 1) {
        String text = tags [i].text ().trim ();
        //System.out.println ("Value text " + text);
        values.add (text);
      }
    }

    tags = fieldDoc.tags ("mariner-object-check");
    if (tags.length != 0) {
      checkable = true;
    }

    tags = fieldDoc.tags ("mariner-object-field-length");
    maximumLength = -1;
    if (tags.length != 0) {
      maximumLength = Integer.parseInt (tags [0].text ().trim ());
    }

    // This field requires checking if there are values set for it,
    // or it has a maximum length, or it is required.
    if (values != null) {
      
    }
    
    /**
     * Check to see whether this field is a field associated with the cache.
     */
    tags = fieldDoc.tags("mariner-cache-field");
    if(tags.length != 0) {
        cacheField = true;
    }
  }

  /**
   * Create a new <code>FieldInfo</code>.
   * @param name The name of the field which this object represents.
   * @param type The type of the field which this object represents.
   * @param comment The comment associated with the field which this object
   * represents.
   */
  public FieldInfo (String name,
                    TypeInfo type,
                    String comment) {
    this.name = name;
    this.type = type;
    this.comment = comment;

    maximumLength = -1;
  }

  /**
   * Create a new <code>FieldInfo</code>.
   * @param name The name of the field which this object represents.
   * @param type The type of the field which this object represents.
   * @param comment The comment associated with the field which this object
   * represents.
   */
  public FieldInfo (FieldInfo fieldInfo, String comment) {
    setClassInfo (fieldInfo.getClassInfo ());
    this.xmlMappings = fieldInfo.getXMLMappings ();
    this.name = fieldInfo.getName ();
    this.type = fieldInfo.getType ();
    this.comment = comment;
    setStatic (fieldInfo.isStatic ());
    this.checkable = fieldInfo.requiresChecking ();

    maximumLength = fieldInfo.getMaximumLength ();
  }

  /**
   * Set the class information of the class which the constructor
   * which this object represents belongs.
   * @param classInfo The class information for the class which the constructor
   * which this object represents belongs.
   */
  public void setClassInfo (ClassInfo classInfo) {
    this.classInfo = classInfo;
  }

  public ClassInfo getClassInfo () {
    return this.classInfo;
  }

  /**
   * Get the name of the xml attribute.
   * @return The name of the xml attribute which may be the same as the
   * field name but may not.
   */
  public String getXMLAttributeName () {
    if (xmlAttributeName == null) {
      return getName ();
    } else {
      return xmlAttributeName;
    }
  }

    /**
     * Get the name of the ControlType associated with this field or null
     * if no ControlType is specified.
     * @return controlType
     */
    public String getControlType() {
        return controlType;
    }


  public Map getXMLMappings () {
    return xmlMappings;
  }

  public boolean requiresXMLMapping () {
    return (xmlMappings != null && xmlMappings.size () != 0);
  }

  public List getValues () {
    return values;
  }

  public boolean requiresChecking () {
    return checkable;
  }

  /**
   * Get the name of the field this object represents.
   * @return The name.
   */
  public String getName () {
    return name;
  }

  /**
   * Get the type of the field this object represents.
   * @return The type.
   */
  public TypeInfo getType () {
    return type;
  }

  /**
   * Get the name of the type (including dimension) of the field this object
   * represents.
   * @return The name of the type.
   */
  public String getTypeName () {
    return type.getName ();
  }

  /**
   * Get the comment associated wth the field this object represents.
   * @return The comment.
   */
  public String getComment () {
    return comment;
  }

  /**
   * Return the maximum length that this field is allowed to be, this only
   * applies to fields of type string.
   */
  public int getMaximumLength () {
    return maximumLength;
  }

  /*
  public void setInherited (boolean inherited) {
    this.inherited = inherited;
  }

  public boolean isInherited () {
    return inherited;
  }
  */

  /**
   * Set the static flag.
   * @param isStatic True if this field is static and false otherwise.
   */
  public void setStatic (boolean isStatic) {
    this.isStatic = isStatic;
  }

  /**
   * Get the static flag.
   * @return True if this field is static and false otherwise.
   */
  public boolean isStatic () {
    return isStatic;
  }
  
  /**
   * Get the cache field flag
   * 
   * @return True if this field is associated with the cache
   */
  public boolean isCacheField() {
      return cacheField;
  }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 27-Nov-03	2013/1	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 02-Sep-03	1295/2	geoff	VBM:2003082109 Certify & package GUIs, runtime & CLIs against IBM JRE/JDK 1.4

 ===========================================================================
*/
