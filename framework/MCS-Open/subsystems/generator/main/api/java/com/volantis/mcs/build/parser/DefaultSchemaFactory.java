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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/DefaultSchemaFactory.java,v 1.3 2003/03/10 11:51:09 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
 *                              to string.
 * 07-Mar-03    Byron           VBM:2003030527 - Added createElementGroup and
 *                              createElementGroupReference.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.parser;

/**
 * This class creates the default implementations of the schema objects.
 */
public class DefaultSchemaFactory
  implements SchemaFactory {

  // Javadoc inherited from super class.
  public AttributeDefinition createAttributeDefinition (Scope scope,
                                                        String name) {
    return new AttributeDefinition (name, scope);
  }

  // Javadoc inherited from super class.
  public AttributeReference createAttributeReference (Scope scope,
                                                      String name) {
    return new AttributeReference (name, scope);
  }
  
  // Javadoc inherited from super class.
  public
    AttributeGroupDefinition createAttributeGroupDefinition (Scope scope,
                                                             String name) {
    return new AttributeGroupDefinition (name, scope);
  }

  // Javadoc inherited from super class.
  public AttributeGroupReference createAttributeGroupReference (Scope scope,
                                                                String name) {
    return new AttributeGroupReference (name, scope);
  }

  // Javadoc inherited from super class.
  public AttributesStructure createAttributesStructure (SchemaObject owner) {
    return new AttributesStructure (owner);
  }
  
  // Javadoc inherited from super class.
  public ElementDefinition createElementDefinition (Scope scope,
                                                    String name) {
    return new ElementDefinition (name, scope);
  }

  // Javadoc inherited from super class.
  public ElementReference createElementReference (Scope scope,
                                                  String name) {
    return new ElementReference (name, scope);
  }

    public ElementGroup createElementGroup(Scope scope, String name) {
        return new ElementGroup(scope, name);
    }

    public ElementGroupReference createElementGroupReference(Scope scope,
                                                             String name) {
        return new ElementGroupReference(scope, name);
    }

    public ComplexType createComplexType(Scope scope, String name) {
        return new ComplexType(scope, name);
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
