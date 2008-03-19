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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/ElementDefinition.java,v 1.3 2002/04/27 16:10:54 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 27-Apr-02    Doug            VBM:2002040803 - Added getters and setters for
 *                              Annotations and ChoiceInfo objects.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the definition of an Element within the schema.
 */
public class ElementDefinition
  implements SchemaElement {

  /**
   * The <code>Scope</code> within which this definition is valid.
   */
  private Scope enclosingScope;

//  /**
//   * The structure of attributes associated with this element.
//   */
//  private AttributesStructure attributesStructure;

  /**
   * The name of this element.
   */
  private String name;

  /**
   * The ComplexType associated with this element.
   */
  private ComplexType complexType;

  /**
   * The Annotation associated with this element.
   */
  private Annotation annotation;

  /**
   * The ChoiceInfo  associated with this element.
   */
  private ChoiceInfo choice;


  /**
   * Create a new <code>ElementDefinition</code>.
   * @param name The name of the element.
   * @param enclosingScope The <code>Scope</code> within which this object
   * is valid.
   */
  public ElementDefinition (String name, Scope enclosingScope) {
    this.name = name;
    this.enclosingScope = enclosingScope;
  }

  // Javadoc inherited from super class.
  public ElementDefinition getDefinition () {
    return this;
  }

  /**
   * Set the <code>ComplexType</code> associated with this element.
   * @param complexType The <code>ComplexType</code> to associate with this
   * element.
   */
  public void setComplexType (ComplexType complexType) {
    this.complexType = complexType;
  }

  /**
   * Get the <code>ComplexType</code> associated with this element.
   * @param complexType The <code>ComplexType</code> associated with this
   * element.
   */
  public ComplexType getComplexType () {
    return complexType;
  }

  /**
   * Set the <code>Annoation</code> associated with this element.
   * @param annotation The <code>Annotation</code> to associate with this
   * element.
   */
  public void setAnnotation (Annotation annotation) {
    this.annotation = annotation;
  }

  /**
   * Get the <code>Annotation</code> associated with this element.
   * @param annotation The <code>Annotation</code> associated with this
   * element.
   */
  public Annotation getAnnotation () {
    return annotation;
  }

  /**
   * Set the <code>ChoiceInfo</code> associated with this element.
   * @param annotation The <code>ChoiceInfo</code> to associate with this
   * element.
   */
  public void setChoice(ChoiceInfo choice) {
    this.choice = choice;
  }

  /**
   * Get the <code>ChoiceInfo</code> associated with this element.
   * @param annotation The <code>Choice</code> associated with this
   * element.
   */
  public ChoiceInfo getChoice () {
    return choice;
  }


  // Javadoc inherited from super class.
  public String getName () {
    return name;
  }

  // Javadoc inherited from super class.
  public Scope getScope () {
    return enclosingScope;
  }

//  /**
//   * Get the structure of the attributes associated with this element.
//   * @return The structure of the attributes associated with this element.
//   */
//  public AttributesStructure getAttributesStructure () {
//    if (attributesStructure == null) {
//      attributesStructure = createAttributesStructure ();
//    }
//
//    return attributesStructure;
//  }

//  /**
//   * Create a new <code>AttributesStructure</code> which will be associated
//   * with this element.
//   * @return The new <code>AttributesStructure</code>.
//   */
//  protected AttributesStructure createAttributesStructure () {
//    return new AttributesStructure (this);
//  }

  // Javadoc inherited from super class.
  public String toString () {
    return super.toString () + " [" + paramString () + "]";
  }

  /**
   * Return a String representation of the state of the object.
   * @return The String representation of the state of the object.
   */
  protected String paramString () {
    return name;
//      + ", attributesStructure=" + attributesStructure;
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

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
