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
 * $Header: /src/voyager/com/volantis/mcs/build/parser/SchemaParser.java,v 1.11 2003/03/10 11:51:09 byron Exp $
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
 * 02-Apr-02    Mat             VBM:2002022009 - Put a check in for a 'fixed'
 *                              attribute.
 * 27-Apr-02    Doug            VBM:2002040803 - Several changes to enable
 *                              the theme schema to be parsed.
 * 02-May-02    Doug            VBM:2002040803 - Ensured that the content of a
 *                              sequence is parsed.
 * 16-May-02    Doug            VBM:2002040803 - Removed unnecessary
 *                              System.out.println messages.
 * 21-Jun-02    Adrian          VBM:2002041702 - updated method processAll to
 *                              call parseContent.
 * 08-Oct-02    Sumit           VBM:2002100707 - Added extra ignore targets for
 *                              simpleContent in parse
 * 04-Feb-03    Mat             VBM:2003020410 - Ignore import element
 * 25-Feb-03    Byron           VBM:2003022105 - Modified parse(..) to ignore
 *                              'any' element.
 * 07-Mar-03    Byron           VBM:2003030527 - Modified methods processGroup
 *                              and processChoice. Added elementGroupList
 *                              member variable.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;

/**
 * This class takes a JDOM tree and creates a set of schema specific objects.
 */
public class SchemaParser {

  /**
   * The default SchemaFactory to use.
   */
  private static SchemaFactory defaultFactory = new DefaultSchemaFactory ();

  /**
   * The current scope.
   */
  private Scope scope;

  /**
   * A Map from target names to <code>ProcessingInstructionTarget</code>s.
   */
  private Map piTargets;

  /**
   * The list of all the top level objects.
   */
  private List list;

  /**
   * The current list of attributes.
   */
  private List attributeList;

  /**
   * The current list of attribute groups.
   */
  private List attributeGroupList;

  /**
   * The current list of elements.
   */
  private List elementList;

    /**
     * The current list of groups.
     */
    private List elementGroupList;

  /**
   * The current list of complexTypes.
   */
  private List complexTypeList;


  /**
   * The namespace relating to XMLSchema definition elements.
   */
  private Namespace xsdNamespace;

  /**
   * The factory to use to create the different objects.
   */
  private SchemaFactory factory;

  /**
   * A stack of the schema objects.
   */
  private Stack schemaObjects;

  /**
   * The prefix to use to indicate the nesting of the schema when generating
   * debugging output.
   */
  private String prefix;

  /**
   * Create a new <code>SchemaParser</code> which uses the default factory.
   */
  public SchemaParser () {
    this (defaultFactory);
  }

  /**
   * Create a new <code>SchemaParser</code> which uses the specified factory.
   * @param factory The factory to use to create schema objects.
   */
  public SchemaParser (SchemaFactory factory) {
    xsdNamespace = Namespace.getNamespace ("http://www.w3.org/2001/XMLSchema");

    if (factory == null) {
      factory = defaultFactory;
    }
    this.factory = factory;

    scope = new Scope (factory, "root");
    //attributeScope = scope;
    //attributeGroupScope = scope;
    //elementScope = scope;

    piTargets = new HashMap ();

    //attributeList = new ArrayList ();
    //attributeGroupList = new ArrayList ();
    //elementList = new ArrayList ();
    list = new ArrayList ();
    attributeList = list;
    attributeGroupList = list;
    elementList = list;
      elementGroupList = list;
    complexTypeList = list;

    schemaObjects = new Stack ();

    prefix = "";
  }

  /**
   * Parse the document and return a list of the top level schema objects.
   * @return The list of top level schema objects.
   */
  public List parse (Document document) {
    
    Element root = document.getRootElement ();
    
    parse (root);

    //System.out.println ("Attributes " + attributeList);
    //System.out.println ("AttributeGroups " + attributeGroupList);
    //System.out.println ("Elements " + elementList);

    return list;
  }

  /**
   * Register a processing instruction target.
   * @param targetName The target name which is to be handled.
   * @param target The target which will handle all processing instructions
   * with the specified name.
   */
  public
    void addProcessingInstructionTarget (String targetName,
                                         ProcessingInstructionTarget target) {
    piTargets.put (targetName, target);
  }

  /**
   * Get the current scope.
   * @return The current scope.
   */
  public Scope getScope () {
    return scope;
  }

  /**
   * Print the specified text with the correct prefix.
   * @param line The text to print.
   */
  public void println (String line) {
    System.out.println (prefix + line);
  }

  /**
   * Print an empty line.
   */
  public void println () {
    System.out.println ();
  }

  /**
   * Parse the content of the specified element.
   * @param element The element whose content should be parsed.
   */
  protected void parseContent (Element element) {

    String oldPrefix = prefix;
    prefix = "  " + prefix;

    List content = element.getContent ();

    for (Iterator i = content.iterator (); i.hasNext ();) {
      Object object = i.next ();

      if (object instanceof Comment) {
        // Do nothing with the comment.
      } else if (object instanceof Element) {
        // Parse the JDOM element.
        parse ((Element) object);
      } else if (object instanceof ProcessingInstruction) {
        // Parse the processing instruction.
        processProcessingInstruction ((ProcessingInstruction) object);
      } else if (object instanceof String) {
        // Check to make sure that the CDATA is just white space.
        String s = (String) object;
        s = s.trim ();
        if (s.length () != 0) {
          throw new IllegalStateException ("Non whitespace cdata found '"
                                           + s + "'");
        }
      } else if (object instanceof Text) {
          // Do nothing with Text ('\n').
      } else {
        throw new IllegalStateException ("Unhandled content " + object
                                         + " of " + object.getClass ());
      }

      /*
      if (i.hasNext ()) {
        println ();
      }
      */
    }

    prefix = oldPrefix;
  }

  /**
   * Parse the specified element.
   * @param element The element to parse.
   */
  public void parse (Element element) {

    Namespace namespace = element.getNamespace ();
    String name = element.getName ();

    if (namespace != xsdNamespace
        && namespace.getURI ().equals ("http://www.w3.org/2001/XMLSchema")) {
      /*
      println ("Overriding xsdNamespace " + xsdNamespace
               + " with " + namespace);
      */
      xsdNamespace = namespace;
    }

    if (namespace == xsdNamespace) {
      if ("annotation".equals (name)) {
        processAnnotation (element);
      }	else if ("appinfo".equals (name)) {
        processAppInfo (element);
      } else if ("attribute".equals (name)) {
        processAttribute (element);
      } else if ("attributeGroup".equals (name)) {
        processAttributeGroup (element);
      } else if ("choice".equals (name)) {
        processChoice (element);
      } else if ("complexType".equals (name)) {
        processComplexType (element);
      }	else if ("documentation".equals (name)) {
        processDocumentation (element);
      } else if ("element".equals (name)) {
        processElement (element);
      } else if ("group".equals (name)) {
        processGroup (element);
      } else if ("schema".equals (name)) {
        processSchema (element);
      } else if ("sequence".equals (name)) {
        processSequence (element);
      } else if ("simpleType".equals (name)) {
        processSimpleType (element);
      } else if ("all".equals (name)) {
        processAll (element);
      } else if ("complexContent".equals(name)) {
          processAll(element);
      } else if ("any".equals(name)) {
          System.out.println("Ignoring 'any'");
      } else if ("unique".equals(name)) {
          System.out.println("Ignoring 'unique'");
      } else if ("include".equals (name)) {
        System.out.println("Ignoring 'include'");
      } else if ("simpleContent".equals (name)) {
        System.out.println("Ignoring 'simpleContent'");
      } else if ("extension".equals (name)) {
          String base = element.getAttributeValue("base");
          if (base != null) {
              ComplexType complexType =
                      (ComplexType) scope.getComplexType(base);
              AttributesStructure structure =
                      complexType.getAttributesStructure();
              attributeGroupList.addAll(structure.getAttributeGroups());
              attributeList.addAll(structure.getAttributes());
          }
          processAll(element);
//        System.out.println("Ignoring 'extension'");
      } else if ("import".equals (name)) {
        System.out.println("Ignoring 'import'");
      } else {
        throw new IllegalArgumentException ("Unhandled element " + name);
      }
    } else {
      throw new IllegalArgumentException ("Unhandled namespace " + namespace);
    }
  }

  /**
   * Get the current object.
   * @return The current object.
   */
  public SchemaObject getCurrentObject () {
    if (schemaObjects.isEmpty ()) {
      return null;
    } else {
      return (SchemaObject) schemaObjects.peek ();
    }
  }

  /**
   * Process an "annotation" element.
   * @param element The "annotation" element.
   */
  protected void processAnnotation (Element element) {
 
    Annotation annotation = new Annotation();

    //  get the current object
    SchemaObject current = getCurrentObject ();

    // if current is an istance of Element then add the annotation to the 
    // element
    if (current instanceof ElementDefinition) {
      ElementDefinition definition = (ElementDefinition) current;
      definition.setAnnotation (annotation);
    }

    // Push the annotation onto the stack.
    schemaObjects.push (annotation);

//     // Push a new list of Elements.
//     List oldElementList = elementList;
//     elementList = new ArrayList ();

    // Parse the content of the annotaion type.
    parseContent (element);

//     // Pop the list of Elements.
//     elementList = oldElementList;

    // Pop the annotation from the stack.
    schemaObjects.pop ();
  }

  /**
   * Process an "appinfo" element.
   * @param element The "appinfo" element.
   */
  protected void processAppInfo (Element element) {
    
    // get the annotation object
    SchemaObject current = getCurrentObject ();
    if(current instanceof Annotation) {
      Annotation annotation = (Annotation)current;
      AppInfo appinfo = new AppInfo();
      appinfo.setElements(element.getChildren());
      annotation.setAppInfo(appinfo);
    }
  }

  /**
   * Process an "attribute" element.
   * @param element The "attribute" element.
   */
  protected void processAttribute (Element element) {
    
    // Check to see whether this is an attribute reference first.
    String ref = element.getAttributeValue ("ref");
    if (ref != null) {

      println ("Parsing attribute reference to '"
               + ref + "'");

      AttributeReference reference
        = scope.getAttributeReference (ref);
      
      // Add the attribute reference to the enclosing attribute list.
      attributeList.add (reference);

      // Nothing else to do.
      return;
    }

    // Ensure that the element has a name.
    String name = element.getAttributeValue ("name");
    if (name == null) {
      throw new IllegalStateException ("Attribute does not have a name");
    }

    println ("Parsing attribute definition for '" + name
             + "'");

    // Create a new AttributeDefinition object in the current scope, this
    // will throw an exception if there is a clash.
    AttributeDefinition definition = scope.addAttributeDefinition (name);

    // Process the attributes.
    List attributes  = element.getAttributes ();
    for (Iterator i = attributes.iterator (); i.hasNext ();) {
      Attribute attribute = (Attribute) i.next ();
      Namespace attributeNamespace = attribute.getNamespace ();
      String attributeName = attribute.getName ();

      if (attributeNamespace == xsdNamespace
          || attributeNamespace == Namespace.NO_NAMESPACE) {

        if ("default".equals (attributeName)) {
          // Set the attribute default value.
          definition.setDefault (attribute.getValue ());
        } else if ("name".equals (attributeName)) {
          // Don't do anything else.
        } else if ("fixed".equals (attributeName)) {
          // Don't do anything else.
        } else if ("type".equals (attributeName)) {
          // Set the attribute type.
          definition.setType (attribute.getValue ());
        } else if ("use".equals (attributeName)) {
          // Set the attribute use.
          definition.setUse (attribute.getValue ());
        } else {
          throw new IllegalStateException ("Unknown schema attribute '"
                                           + attributeName + "'");
        }
      }
      else {
        throw new IllegalStateException ("Unknown attribute namespace '"
                                         + attributeNamespace + "'");
      }
    }

    // Add the attribute definition to the list of attributes.
    attributeList.add (definition);

    // Push the attribute definition object onto the stack.
    schemaObjects.push (definition);

    // Process the contents of the attribute.
    parseContent (element);

    // Pop the attribute definition object from the stack.
    schemaObjects.pop ();
  }

  /**
   * Process an "attributeGroup" element.
   * @param element The "attributeGroup" element.
   */
  protected void processAttributeGroup (Element element) {
    
    // Check to see whether this is an attribute group reference first.
    String ref = element.getAttributeValue ("ref");
    if (ref != null) {

      println ("Parsing attribute group reference to '"
               + ref + "'");

      AttributeGroupReference reference
        = scope.getAttributeGroupReference (ref);
      
      // Add the attributeGroup reference to the enclosing attributeGroup list.
      attributeGroupList.add (reference);

      // Nothing else to do.
      return;
    }

    // Ensure that the element has a name.
    String name = element.getAttributeValue ("name");
    if (name == null) {
      throw new IllegalStateException ("AttributeGroup does not have a name");
    }

    println ("Parsing attribute group definition for '"
             + name + "'");


    // Create a new AttributeGroupDefinition object in the current scope, this
    // will throw an exception if there is a clash.
    AttributeGroupDefinition definition
      = scope.addAttributeGroupDefinition (name);

    // Process the attributes.
    List attributes  = element.getAttributes ();
    for (Iterator i = attributes.iterator (); i.hasNext ();) {
      Attribute attribute = (Attribute) i.next ();
      Namespace attributeNamespace = attribute.getNamespace ();
      String attributeName = attribute.getName ();

      if (attributeNamespace == xsdNamespace
          || attributeNamespace == Namespace.NO_NAMESPACE) {

        if ("name".equals (attributeName)) {
          // Don't do anything else.
        }
        else {
          throw new IllegalStateException ("Unknown schema attribute '"
                                           + attributeName + "'");
        }
      }
      else {
        throw new IllegalStateException ("Unknown attribute namespace '"
                                         + attributeNamespace + "'");
      }
    }

    // Add the attribute group definition to the list of attribute groups.
    attributeGroupList.add (definition);

    // Push the attribute group definition object onto the stack.
    schemaObjects.push (definition);

    // Push a new scope.
    scope = new Scope (scope, "Attribute group " + name);

    // Get the AttributesStructure.
    AttributesStructure attributesStructure
      = definition.getAttributesStructure ();

    // Push a new list of Attributes.
    List oldAttributeList = attributeList;
    attributeList = attributesStructure.getAttributes ();

    // Push a new list of AttributeGroups.
    List oldAttributeGroupList = attributeGroupList;
    attributeGroupList = attributesStructure.getAttributeGroups ();

    // Process the contents of the attribute group.
    parseContent (element);

    // Pop the list of AttributeGroups.
    attributeGroupList = oldAttributeGroupList;

    // Pop the list of Attributes.
    attributeList = oldAttributeList;

    // Pop the scope.
    scope = scope.getEnclosingScope ();

    // Pop the attribute group definition object from the stack.
    schemaObjects.pop ();
  }

  /**
   * Process a "choice" element.
   * @param element The "choice" element.
   */
  protected void processChoice (Element element) {
    println ("Parsing choice");
    
    SchemaObject current = getCurrentObject ();
    if (current instanceof ComplexType) {
      ComplexType complexType = (ComplexType) current;
      complexType.setEmpty (false);
    }
    ChoiceInfo choice = new ChoiceInfo();

    if (current instanceof ElementDefinition) {
      ElementDefinition definition = (ElementDefinition) current;
      definition.setChoice(choice);
    } else if (current instanceof ElementGroup) {
        ElementGroup groupDefinition = (ElementGroup) current;
        groupDefinition.setChoice(choice);
    }

    // Push the element definition object onto the stack.
    schemaObjects.push (choice);

    // Push a new scope.
    scope = new Scope (scope, "Choice");

    // Process the contents of the element.
    parseContent (element);

    // Pop the scope.
    scope = scope.getEnclosingScope ();

    // Pop the Choice definition object from the stack.
    schemaObjects.pop ();
  }

  /**
   * Process a "complexType" element.
   * @param element The "complexType" element.
   */
  protected void processComplexType (Element element) {

    println ("Parsing complexType");

    // might not be named
    String name = element.getAttributeValue ("name");

      ComplexType complexType;

      if (name == null) {
          // Anonymous complex type so must be inside an element.
          complexType = scope.createComplexType(null);

          // If we are defining an element then store the complex type into the
          // element definition.
          SchemaObject current = getCurrentObject ();
          if (current instanceof ElementDefinition) {
            ElementDefinition definition = (ElementDefinition) current;
            definition.setComplexType (complexType);
          }

      } else {

          complexType = scope.addComplexType(name);
          complexTypeList.add(complexType);
      }

      if ("true".equals (element.getAttributeValue ("mixed"))) {
        complexType.setMixed (true);
      }

      // Element is empty unless we find anything in the body of this element
      // to change out mind.
      complexType.setEmpty (true);

    // Push the complex type object onto the stack.
    schemaObjects.push (complexType);

    scope = new Scope (scope, "ComplexType " + name);

      // Get the AttributesStructure.
      AttributesStructure attributesStructure
        = complexType.getAttributesStructure ();

      // Push a new list of Attributes.
      List oldAttributeList = attributeList;
      attributeList = attributesStructure.getAttributes ();

      // Push a new list of AttributeGroups.
      List oldAttributeGroupList = attributeGroupList;
      attributeGroupList = attributesStructure.getAttributeGroups ();

    // Push a new list of Elements.
    List oldElementList = elementList;
    elementList = new ArrayList ();

    // Parse the content of the complex type.
    parseContent (element);

    // Pop the list of Elements.
    elementList = oldElementList;

      // Pop the list of AttributeGroups.
      attributeGroupList = oldAttributeGroupList;

      // Pop the list of Attributes.
      attributeList = oldAttributeList;

    scope = scope.getEnclosingScope ();

    // Pop the complex type object from the stack.
    schemaObjects.pop ();
  }

  /**
   * Process a "documentation" element.
   * @param element The "documentation" element.
   */
  protected void processDocumentation(Element element) {
    println ("Ignoring documentation");
  }

  /**
   * Process an "element" element.
   * @param element The "element" element.
   */
  protected void processElement(Element element) {

      // Check to see whether this is an element reference first as it is
      // easier.
      String ref = element.getAttributeValue("ref");
      if (ref != null) {

          println("Parsing element reference to '" + ref + "'");

          ElementReference reference = scope.getElementReference(ref);

          // Add the element reference to the enclosing element list.
          elementList.add(reference);

          // Nothing else to do.
          return;
      }

      // Ensure that the element has a name.
      String name = element.getAttributeValue("name");
      if (name == null) {
          throw new IllegalStateException("Element does not have a name");
      }

      println("Parsing element definition for '"
              + name + "'");

      // Create a new ElementDefinition object in the current scope, this will
      // throw an exception if there is a clash.
      ElementDefinition definition = scope.addElementDefinition(name);

      // Add the element definition to the list of elements.
      elementList.add(definition);

      String type = element.getAttributeValue("type");
      if (type == null) {

          // Process the attributes.
          List attributes = element.getAttributes();
          for (Iterator i = attributes.iterator(); i.hasNext();) {
              Attribute attribute = (Attribute) i.next();
              Namespace attributeNamespace = attribute.getNamespace();
              String attributeName = attribute.getName();

              if (attributeNamespace == xsdNamespace
                      || attributeNamespace == Namespace.NO_NAMESPACE) {


                  if ("Name".equals(attributeName)) {
                      // Don't do anything else.
                  }
//         else {
//           throw new IllegalStateException ("Unknown schema attribute '"
//                                            + attributeName + "'");
//         }
              } else {
                  throw new IllegalStateException("Unknown attribute namespace '"
                          + attributeNamespace + "'");
              }
          }

          // If we are defining an element then store the complex type into the
          // element definition.
          SchemaObject current = getCurrentObject();

          // @todo not sure what the point of this is since nothing seems to use
          // the result of this operation.
          if (current instanceof ChoiceInfo) {
              ChoiceInfo choice = (ChoiceInfo) current;
              choice.addElement(definition);
          }

      } else {
          ComplexType complexType = scope.getComplexType(type);
          if (complexType == null) {
              throw new IllegalArgumentException("Cannot resolve complex type " +
                      type);
          }

          println("Resolved complex type " + type);

          definition.setComplexType(complexType);
      }

      // Push the element definition object onto the stack.
      schemaObjects.push(definition);

      // Push a new scope.
      System.out.println("NEW SCOPE FOR ELEMENT " + name);
      scope = new Scope(scope, "Element " + name);

      // Process the contents of the element.
      parseContent(element);

      // Pop the scope.
      scope = scope.getEnclosingScope();
      System.out.println("POPPING SCOPE FOR ELEMENT " + name);

      // Pop the element definition object from the stack.
      schemaObjects.pop();
  }

    /**
     * Process a "group" element.
     * @param element The "group" element.
     * @todo processGroup, processElement and processAttributeGroup are similar
     * and may benefit from a bit of refactoring and code re-use.
     */
    protected void processGroup(Element element) {

        String ref = element.getAttributeValue("ref");
        if (ref != null) {
            // We have a group reference.
            println("Parsing group reference to '" + ref + "'");

            ElementGroupReference reference = scope.getElementGroupReference(ref);
            // Add the group reference to the enclosing group list.
            elementGroupList.add(reference);

            // Nothing else to do.
            return;
        }

        // Ensure that the element has a name.
        String name = element.getAttributeValue ("name");
        if (name == null) {
          throw new IllegalStateException ("Group does not have a name");
        }

        println("Parsing group definition for '" + name + "'");

        // Create a new Group object in the current scope, this will
        // throw an exception if there is a clash.
        ElementGroup definition = scope.addElementGroup(name);

        // Process the attributes.
        List attributes = element.getAttributes();
        for (Iterator i = attributes.iterator(); i.hasNext();) {
            Attribute attribute = (Attribute) i.next();
            Namespace attributeNamespace = attribute.getNamespace();
            String attributeName = attribute.getName();

            if (attributeNamespace == xsdNamespace
                    || attributeNamespace == Namespace.NO_NAMESPACE) {
            } else {
                throw new IllegalStateException("Unknown attribute namespace '"
                                                + attributeNamespace + "'");
            }
        }

        // Add the group definition to the list of groups.
        elementGroupList.add(definition);

        // Push the group definition object onto the stack.
        schemaObjects.push(definition);

        // Push a new scope.
        System.out.println("NEW SCOPE FOR GROUP " + name);
        scope = new Scope(scope, "Group " + name);

        // Get the AttributesStructure.
        AttributesStructure attributesStructure =
                definition.getAttributesStructure();

        // Push a new list of Attributes.
        List oldAttributeList = attributeList;
        attributeList = attributesStructure.getAttributes();

        // Push a new list of AttributeGroups.
        List oldAttributeGroupList = attributeGroupList;
        attributeGroupList = attributesStructure.getAttributeGroups();

        // Process the contents of the group.
        parseContent(element);

        // Pop the list of AttributeGroups.
        attributeGroupList = oldAttributeGroupList;

        // Pop the list of Attributes.
        attributeList = oldAttributeList;

        // Pop the scope.
        scope = scope.getEnclosingScope();
        System.out.println("POPPING SCOPE FOR GROUP " + name);

        // Pop the group definition object from the stack.
        schemaObjects.pop();
    }

  /**
   * Process a "group" element.
   * @param element The "group" element.
   */
  protected void processAll (Element element) {
    parseContent(element);
  }

  /**
   * Process a processing instruction.
   * @param pi The processing instruction element.
   */
  protected void processProcessingInstruction (ProcessingInstruction pi) {
    String targetName = pi.getTarget ();
    ProcessingInstructionTarget target
      = (ProcessingInstructionTarget) piTargets.get (targetName);

    if (target == null) {
      println ("Ignoring processing instruction '"
               + pi + "'");
    } else {
      target.handleProcessingInstruction (this, targetName, pi.getData ());
    }
  }

  /**
   * Process a "schema" element.
   * @param element The "schema" element.
   */
  protected void processSchema (Element element) {
    parseContent (element);
  }

  /**
   * Process a "sequence" element.
   * @param element The "sequence" element.
   */
  protected void processSequence (Element element) {
    
    println ("Parsing  sequence");
    SchemaObject current = getCurrentObject ();
    if (current instanceof ComplexType) {
      ComplexType complexType = (ComplexType) current;
      complexType.setEmpty (false);
    } 
    // Parse the content of the sequence.
    parseContent (element);
  }

  /**
   * Process a "simpleType" element.
   * @param element The "simpleType" element.
   */
  protected void processSimpleType (Element element) {
    println ("Ignoring simple type '"
             + element.getAttributeValue ("name") + "'");
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Jul-03	693/1	byron	VBM:2003070207 Versioning now handled via librarian

 ===========================================================================
*/
