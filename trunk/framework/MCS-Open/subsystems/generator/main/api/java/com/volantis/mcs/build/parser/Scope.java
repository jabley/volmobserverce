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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/Scope.java,v 1.3 2003/03/10 11:51:09 byron Exp $
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
 * 07-Mar-03    Byron           VBM:2003030527 - Added groupReferences and
 *                              groups member variables. Added getElementGroup/
 *                              Reference and addElementGroup methods.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * This class defines a mapping from objects names to definitions and
 * references and also creates new instances of them.
 */
public class Scope {

  /**
   * The scope which encloses this scope.
   */
  private Scope enclosingScope;

  /**
   * The factory to use to create objects within this scope.
   */
  private SchemaFactory factory;

  /**
   * A description of this scope, why it was created.
   */
  private String description;

  /**
   * A map from attribute names to <code>AttributeDefiniton</code>s.
   */
  private Map attributeDefinitions;
                       
  /**
   * A map from attribute names to <code>AttributeReference</code>s.
   */
  private Map attributeReferences;

  /**
   * A map from attribute group names to <code>AttributeGroupDefiniton</code>s.
   */
  private Map attributeGroupDefinitions;

  /**
   * A map from attribute group names to <code>AttributeGroupReference</code>s.
   */
  private Map attributeGroupReferences;

  /**
   * A map from element names to <code>ElementDefiniton</code>s.
   */
  private Map elementDefinitions;

  /**
   * A map from element names to <code>ElementReference</code>s.
   */
  private Map elementReferences;

    /**
     * A map from group names to <code>ElementGroup</code>s.
     */
    private Map elementGroups;

    /**
     * A map from group names to <code>ElementGroupReference</code>s.
     */
    private Map elementGroupReferences;

    /**
     * A map from name to <code>ComplexType</code>
     */
    private Map complexTypes;

  /**
   * Create a new <code>Scope</code>.
   * @param enclosingScope The enclosing scope.
   * @param factory The factory to use to create objects in this scope.
   * @param description A string which identifies where this scope was created.
   */
  protected Scope(Scope enclosingScope,
                  SchemaFactory factory,
                  String description) {

      this.enclosingScope = enclosingScope;
      this.factory = factory;
      this.description = description;

      attributeDefinitions = new HashMap();
      attributeGroupDefinitions = new HashMap();
      elementDefinitions = new HashMap();
      elementGroups = new HashMap();

      if (enclosingScope == null) {
          attributeReferences = new HashMap();
          attributeGroupReferences = new HashMap();
          elementReferences = new HashMap();
          elementGroupReferences = new HashMap();
          complexTypes = new HashMap();
      }
  }

  /**
   * Create a new <code>Scope</code>.
   * @param factory The factory to use to create objects in this scope.
   * @param description A string which identifies where this scope was created.
   */
  public Scope (SchemaFactory factory,
                String description) {
    this (null, factory, description);
  }

  /**
   * Create a new <code>Scope</code>.
   * @param enclosingScope The enclosing scope.
   * @param description A string which identifies where this scope was created.
   */
  public Scope (Scope enclosingScope,
                String description) {
    this (enclosingScope, enclosingScope.factory, description);
  }

  /**
   * Get the enclosing scope.
   * @return The enclosing scope.
   */
  public Scope getEnclosingScope () {
    return enclosingScope;
  }

  /**
   * Create and add a new <code>AttributeDefinition</code> to the scope.
   * @param name The name of the <code>AttributeDefinition</code> to add.
   * @return The newly created <code>AttributeDefinition</code>.
   * @throws IllegalStateException If an <code>AttributeDefinition</code>
   * already exists.
   */
  public AttributeDefinition addAttributeDefinition (String name) {

    if (attributeDefinitions.containsKey (name)) {
      throw new IllegalStateException ("Attribute " + name
                                       + " already defined in scope "
                                       + this);
    }

    AttributeDefinition definition = createAttributeDefinition (name);
    attributeDefinitions.put (name, definition);

    return definition;
  }

  /**
   * Create a new <code>AttributeDefinition</code>.
   * @param name The name of the <code>AttributeDefinition</code> to create.
   * @return The newly created <code>AttributeDefinition</code>.
   */
  protected AttributeDefinition createAttributeDefinition (String name) {
    return factory.createAttributeDefinition (this, name);
  }

  /**
   * Get the <code>AttributeDefinition</code> with the specified name.
   * @param name The name of the <code>AttributeDefinition</code> to get.
   * @return The <code>AttributeDefinition</code> which was found.
   * @throws IllegalStateException If the named
   * <code>AttributeDefinition</code> does not exist.
   */
  public AttributeDefinition getAttributeDefinition (String name) {
    AttributeDefinition definition
      = (AttributeDefinition) attributeDefinitions.get (name);
    if (definition == null) {
      throw new IllegalStateException ("Attribute " + name
                                       + " not defined in scope "
                                       + this);
    }

    return definition;
  }

  /**
   * Get an <code>AttributeReference</code> to the specified name.
   * <p>
   * If an <code>AttributeReference</code> with the specified name does not
   * exist then one is created and returned.
   * </p>
   * @param name The name of the <code>AttributeReference</code> to get.
   * @return The <code>AttributeReference</code> which was found, or created.
   */
  public AttributeReference getAttributeReference (String name) {
    if (enclosingScope != null) {
      return enclosingScope.getAttributeReference (name);
    }

    AttributeReference reference
      = (AttributeReference) attributeReferences.get (name);
    if (reference == null) {
      reference = createAttributeReference (name);
      attributeReferences.put (name, reference);
    }
    
    return reference;
  }

  /**
   * Create a new <code>AttributeReference</code>.
   * @param name The name of the <code>AttributeReference</code> to create.
   * @return The newly created <code>AttributeReference</code>.
   */
  protected AttributeReference createAttributeReference (String name) {
    return factory.createAttributeReference (this, name);
  }

  /**
   * Create and add a new <code>AttributeGroupDefinition</code> to the scope.
   * @param name The name of the <code>AttributeGroupDefinition</code> to add.
   * @return The newly created <code>AttributeGroupDefinition</code>.
   * @throws IllegalStateException If an <code>AttributeGroupDefinition</code>
   * already exists.
   */
  public AttributeGroupDefinition addAttributeGroupDefinition (String name) {

    if (attributeGroupDefinitions.containsKey (name)) {
      throw new IllegalStateException ("AttributeGroup " + name
                                       + " already defined in scope "
                                       + this);
    }

    AttributeGroupDefinition definition
      = createAttributeGroupDefinition (name);
    attributeGroupDefinitions.put (name, definition);

    return definition;
  }

  /**
   * Create a new <code>AttributeGroupDefinition</code>.
   * @param name The name of the <code>AttributeGroupDefinition</code> to
   * create.
   * @return The newly created <code>AttributeGroupDefinition</code>.
   */
  protected
    AttributeGroupDefinition createAttributeGroupDefinition (String name) {
    return factory.createAttributeGroupDefinition (this, name);
  }

  /**
   * Get the <code>AttributeGroupDefinition</code> with the specified name.
   * @param name The name of the <code>AttributeGroupDefinition</code> to get.
   * @return The <code>AttributeGroupDefinition</code> which was found.
   * @throws IllegalStateException If the named
   * <code>AttributeGroupDefinition</code> does not exist.
   */
  public AttributeGroupDefinition getAttributeGroupDefinition (String name) {
    AttributeGroupDefinition definition
      = (AttributeGroupDefinition) attributeGroupDefinitions.get (name);
    if (definition == null) {
      throw new IllegalStateException ("AttributeGroup " + name
                                       + " not defined in scope "
                                       + this);
    }

    return definition;
  }

  /**
   * Get an <code>AttributeGroupReference</code> to the specified name.
   * <p>
   * If an <code>AttributeGroupReference</code> with the specified name
   * does not exist then one is created and returned.
   * </p>
   * @param name The name of the <code>AttributeGroupReference</code> to get.
   * @return The <code>AttributeGroupReference</code> which was found, or
   * created.
   */
  public AttributeGroupReference getAttributeGroupReference (String name) {
    if (enclosingScope != null) {
      return enclosingScope.getAttributeGroupReference (name);
    }

    AttributeGroupReference reference
      = (AttributeGroupReference) attributeGroupReferences.get (name);
    if (reference == null) {
      reference = createAttributeGroupReference (name);
      attributeGroupReferences.put (name, reference);
    }
    
    return reference;
  }

  /**
   * Create a new <code>AttributeGroupReference</code>.
   * @param name The name of the <code>AttributeGroupReference</code> to
   * create.
   * @return The newly created <code>AttributeGroupReference</code>.
   */
  protected
    AttributeGroupReference createAttributeGroupReference (String name) {

    return factory.createAttributeGroupReference (this, name);
  }

  /**
   * Create and add a new <code>ElementDefinition</code> to the scope.
   * @param name The name of the <code>ElementDefinition</code> to add.
   * @return The newly created <code>ElementDefinition</code>.
   * @throws IllegalStateException If an <code>ElementDefinition</code>
   * already exists.
   */
  public ElementDefinition addElementDefinition (String name) {
    if (elementDefinitions.containsKey (name)) {
      throw new IllegalStateException ("Element " + name
                                       + " already defined in scope "
                                       + this);
    }

    ElementDefinition definition = createElementDefinition (name);
    elementDefinitions.put (name, definition);

    return definition;
  }

  /**
   * Create a new <code>ElementDefinition</code>.
   * @param name The name of the <code>ElementDefinition</code> to create.
   * @return The newly created <code>ElementDefinition</code>.
   */
  protected ElementDefinition createElementDefinition (String name) {
    return factory.createElementDefinition (this, name);
  }

  /**
   * Get the <code>ElementDefinition</code> with the specified name.
   * @param name The name of the <code>ElementDefinition</code> to get.
   * @return The <code>ElementDefinition</code> which was found.
   * @throws IllegalStateException If the named
   * <code>ElementDefinition</code> does not exist.
   */
  public ElementDefinition getElementDefinition (String name) {
    ElementDefinition definition
      = (ElementDefinition) elementDefinitions.get (name);
    if (definition == null) {
      throw new IllegalStateException ("Element " + name
                                       + " not defined in scope "
                                       + this);
    }

    return definition;
  }

  /**
   * Get an <code>ElementReference</code> to the specified name.
   * <p>
   * If an <code>ElementReference</code> with the specified name does not
   * exist then one is created and returned.
   * </p>
   * @param name The name of the <code>ElementReference</code> to get.
   * @return The <code>ElementReference</code> which was found, or created.
   */
  public ElementReference getElementReference (String name) {
    if (enclosingScope != null) {
      return enclosingScope.getElementReference (name);
    }

    ElementReference reference
      = (ElementReference) elementReferences.get (name);
    if (reference == null) {
      reference = createElementReference (name);
      elementReferences.put (name, reference);
    }
    
    return reference;
  }

  /**
   * Create a new <code>ElementReference</code>.
   * @param name The name of the <code>ElementReference</code> to create.
   * @return The newly created <code>ElementReference</code>.
   */
  protected ElementReference createElementReference (String name) {
    return factory.createElementReference (this, name);
  }

    /**
     * Create and add a new <code>ElementGroup</code> to the scope.
     *
     * @param  name                  The name of the <code>ElementGroup</code>
     *                               to add.
     * @return                       The newly created
     *                               <code>ElementGroup</code>.
     * @throws IllegalStateException If an <code>ElementGroup</code> already
     *                               exists.
     */
    public ElementGroup addElementGroup(String name) {
        if (elementGroups.containsKey(name)) {
            throw new IllegalStateException("ElementGroup " + name
                                            + " already defined in scope "
                                            + this);
        }
        ElementGroup definition = createElementGroup(name);
        elementGroups.put(name, definition);

        return definition;
    }

    /**
     * Create a new <code>ElementGroup</code>.
     *
     * @param  name The name of the <code>ElementGroup</code> to create.
     * @return      The newly created <code>ElementGroup</code>.
     */
    protected ElementGroup createElementGroup(String name) {
        return factory.createElementGroup(this, name);
    }

    /**
     * Get the <code>ElementGroup</code> with the specified name.
     *
     * @param  name                  The name of the <code>ElementGroup</code>
     *                               to get.
     * @return                       The <code>ElementGroup</code> which was
     *                               found.
     * @throws IllegalStateException If the named <code>ElementGroup</code>
     *                               does not exist.
     */
    public ElementGroup getElementGroup(String name) {
        ElementGroup definition = (ElementGroup) elementGroups.get(name);

        if (definition == null) {
            throw new IllegalStateException("ElementGroup " + name
                                            + " not defined in scope "
                                            + this);
        }
        return definition;
    }

    /**
     * Get a <code>ElementGroupReference</code> to the specified name.
     * <p>
     * If an <code>ElementGroupReference</code> with the specified name does not
     * exist then one is created and returned. </p>
     *
     * @param  name The name of the <code>ElementGroupReference</code> to get.
     * @return      The <code>ElementReference</code> which was found, or
     *              created.
     */
    public ElementGroupReference getElementGroupReference(String name) {
        if (enclosingScope != null) {
            return enclosingScope.getElementGroupReference(name);
        }
        ElementGroupReference reference =
                (ElementGroupReference) elementGroupReferences.get(name);
        if (reference == null) {
            reference = createElementGroupReference(name);
            elementGroupReferences.put(name, reference);
        }
        return reference;
    }

    public ComplexType createComplexType(String name) {
        return factory.createComplexType(this, name);
    }

    public ComplexType getComplexType(String name) {
        if (enclosingScope != null) {
            return enclosingScope.getComplexType(name);
        }

        return (ComplexType) complexTypes.get(name);
    }

    public ComplexType addComplexType(String name) {
        if (complexTypes.containsKey(name)) {
            throw new IllegalStateException("ComplexType " + name
                    + " already defined in scope "
                    + this);
        }

        ComplexType type = createComplexType(name);
        complexTypes.put(name, type);

        return type;
    }

    /**
     * Create a new <code>ElementGroupReference</code>.
     *
     * @param  name The name of the <code>ElementGroupReference</code> to create.
     * @return      The newly created <code>ElementGroupReference</code>.
     */
    protected ElementGroupReference createElementGroupReference(String name) {
        return factory.createElementGroupReference(this, name);
    }

  // Javadoc inherited from super class.
  public String toString () {
    return super.toString () + " [" + paramString () + "]";
  }

  /**
   * Return a String representation of the state of the object.
   * @return The String representation of the state of the object.
   */
  protected String paramString () {
    return description;
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
