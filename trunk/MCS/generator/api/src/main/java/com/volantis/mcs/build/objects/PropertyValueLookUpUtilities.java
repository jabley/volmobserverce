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
package com.volantis.mcs.build.objects;

import com.volantis.mcs.build.GenerateUtilities;
import com.volantis.mcs.build.javadoc.ClassInfo;
import com.volantis.mcs.build.javadoc.FieldInfo;
import com.volantis.mcs.build.javadoc.PackageInfo;

import java.io.*;
import java.util.*;

/**
 * Provides utilities for property value lookups
 */
public class PropertyValueLookUpUtilities {

    private static int propertyValueLookUpInsertionPos = -1;

    private PropertyValueLookUpUtilities() {
    }

    /**
     * Generate a utilities package for the current object if it is needed.
     * @param repInfo
     * @param generatedDir The directory that the utilities are written to.
     */
    public static void generateUtilities(RepositoryObjectInfo repInfo, File generatedDir) {
        ClassInfo objectClassInfo = repInfo.getObjectClassInfo();
        String objectClassName = objectClassInfo.getName();
        PackageInfo objectPackage = objectClassInfo.getPackageInfo();
        String utilitiesPackage = objectPackage.getName();
        String utilitiesName = objectClassName + "Utilities";

        List allObjectFields = repInfo.getAllObjectFields();
        boolean utilitiesNeeded = false;
        for (Iterator i = allObjectFields.iterator(); i.hasNext();) {
            FieldInfo field = (FieldInfo) i.next();

            if (field.getValues() != null || field.requiresChecking()) {
                utilitiesNeeded = true;
                break;
            }
        }

        if (!utilitiesNeeded) {
            return;
        }

        String utilitiesQualifiedName = utilitiesPackage + "." + utilitiesName;

        // Open a file.
        PrintStream out = openFileForClass(utilitiesQualifiedName, generatedDir);

        // Write the header.
        GenerateUtilities.writeHeader(out, "PropertyValueLookupUtilities" );

        // Write the package declaration.
        out.println();
        out.println("package " + utilitiesPackage + ";");

        // Write the imports.
        SortedSet imports = new TreeSet();
        imports.add("java.util.HashMap");
        imports.add("java.util.Map");

        //imports.add ("org.apache.log4j.Category");

        GenerateUtilities.writeImports(out, imports);

        out.println();
        out.println("public class " + utilitiesName + " {");

        // Create the log4j object.
        //GenerateUtilities.writeLogger (out, utilitiesPackage, accessorName);

        // Declare any variables needed for the mapping.
        for (Iterator i = allObjectFields.iterator(); i.hasNext();) {
            FieldInfo field = (FieldInfo) i.next();
            writeDeclareUtilityFields(out, field);
        }

        out.println();
        out.println("  static {");

        out.println("    Object internal;");
        out.println("    String external;");

        // Initialise the variables needed for the mapping.
        for (Iterator i = allObjectFields.iterator(); i.hasNext();) {
            FieldInfo field = (FieldInfo) i.next();
            writeInitialiseUtilityFields(out, field);
        }

        out.println("  }");

        // Expose the variables.
        for (Iterator i = allObjectFields.iterator(); i.hasNext();) {
            FieldInfo field = (FieldInfo) i.next();
            writeUtilityMethods(out, field);
        }

        out.println("}");

        // Write the footer.
        GenerateUtilities.writeFooter(out);

        // Close the output stream.
        out.close();

        out = null;
    }

    /**
     *
     * @param out The outputstream to print to.
     * @param field The field to write.
     */
    private static void writeDeclareUtilityFields(PrintStream out, FieldInfo field) {
        List values = field.getValues();
        if (values == null || values.size() == 0) {
            return;
        }

        String fieldName = field.getName();
        ClassInfo objectClassInfo = field.getClassInfo();
        String objectClassName = objectClassInfo.getName();
        String[] objectClassWords
                = GenerateUtilities.getWords(objectClassName);
        String objectNaturalName
                = GenerateUtilities.getNaturalName(objectClassWords);
        if (values != null && values.size() != 0) {
            GenerateUtilities.writeJavaDocComment
                    (out, "  ",
                            "An array of the allowable values for the " + objectNaturalName
                    + " " + fieldName + ".");
            out.println("  private static Object [] " + fieldName + "Array;");
        }

        if (field.requiresXMLMapping()) {
            GenerateUtilities.writeJavaDocComment
                    (out, "  ",
                            "A bidirectional map between internal and external representations"
                    + " of the " + objectNaturalName + " " + fieldName + ".");
            out.println("  private static Map " + fieldName + "Map;");
        }
    }

    /**
     * Write methods for accessing data in the utilities classes.
     * @param out The outputstream to print to.
     * @param field The field which may need some utility values.
     */
    private static void writeUtilityMethods(PrintStream out, FieldInfo field) {

        List values = field.getValues();
        if (values == null || values.size() == 0) {
            return;
        }

        String fieldName = field.getName();
        String titledFieldName = GenerateUtilities.getTitledString(fieldName);
        out.println();
        out.println("  public static Object [] get" + titledFieldName
                + "Array () {");
        out.println("    return " + fieldName + "Array;");
        out.println("  }");

        if (field.requiresXMLMapping()) {
            out.println();
            out.println("  public static Map get" + titledFieldName + "Map () {");
            out.println("    return " + fieldName + "Map;");
            out.println("  }");
        }
    }

    /**
     * Write code which initialises any static fields needed by the utilities
     * class for the specified field.
     * @param out The outputstream to print to.
     * @param field The field which may need some utility values.
     */
    private static void writeInitialiseUtilityFields(PrintStream out, FieldInfo field) {

        List values = field.getValues();
        if (values == null || values.size() == 0) {
            return;
        }

        String fieldName = field.getName();
        String typeName = field.getTypeName();

        String objectType;
        if (typeName.equals("String")) {
            objectType = typeName;
        } else if (typeName.equals("int")) {
            objectType = "Integer";
        } else if (typeName.equals("boolean")) {
            objectType = "Boolean";
        } else {
            throw new IllegalStateException("Unhandled type " + typeName
                    + " on field " + fieldName);
        }

        // Write the array of objects.
        out.println();
        out.println("    " + fieldName + "Array = new " + objectType + " [] {");

        for (Iterator i = values.iterator(); i.hasNext();) {
            String internalValue = (String) i.next();

            if (objectType.equals(typeName)) {
                out.println("      " + internalValue + ",");
            } else {
                out.println("      new " + objectType
                        + " (" + internalValue + "),");
            }
        }

        out.println("    };");

        // Write the mappings.
        Map mappings = field.getXMLMappings();
        if (mappings != null) {
            out.println();
            out.println("    " + fieldName + "Map = new HashMap ();");

            int index = 0;
            for (Iterator i = values.iterator(); i.hasNext(); index += 1) {
                String internalValue = (String) i.next();
                String externalValue = (String) mappings.get(internalValue);

                out.println();

                /*
                out.println ();
                if (objectType.equals (typeName)) {
                  out.println ("    internal = " + internalValue + ";");
                } else {
                  out.println ("    internal = new " + objectType
                               + " (" + internalValue + ");");
                }
                */
                out.println("    external = " + externalValue + ";");

                internalValue = fieldName + "Array [" + index + "]";
                out.println("    " + fieldName
                        + "Map.put (" + internalValue + ", external);");
                out.println("    " + fieldName
                        + "Map.put (external, " + internalValue + ");");
            }
        }
    }

    public static void createPropertyValueLookUpFile(File generatedDir) {

        try {
            File propertyValueLookUp = getPropertyValueLookUpFile(generatedDir);

            propertyValueLookUpInsertionPos = generatePropertyValueLookUp(generatedDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getPropertyValueLookUpFile(File generatedDir) {
        File propertyValueLookUp =
                GenerateUtilities.
                getFileFromQualifiedClassName(generatedDir,
                                              "com.volantis.mcs.objects.PropertyValueLookUp");
        return propertyValueLookUp;
    }

    /**
     * Update the PropertyValueLookUp class if necessary.
     * @param repInfo
     * @param generatedDir The directory to write the PropertyValueLookUp code
     */
    public static void updatePropertyValueLookUp(RepositoryObjectInfo repInfo, File generatedDir) {
        try {
            File propertyValueLookUp = getPropertyValueLookUpFile(generatedDir);

            ClassInfo objectClassInfo = repInfo.getObjectClassInfo();
            String objectClassName = objectClassInfo.getName();
            List allObjectFields = repInfo.getAllObjectFields();
            String utilitiesName = objectClassName + "Utilities";

            // Read the PropertyValueLookUp class into a StringBuffer.
            StringBuffer buffer = new StringBuffer();
            FileReader fr = new FileReader(propertyValueLookUp);
            BufferedReader br = new BufferedReader(fr);

            String line = br.readLine();
            while (line != null) {
                buffer.append(line).append('\n');
                line = br.readLine();
            }
            br.close();

            boolean initializedPropertyToArray = false;
            boolean initializedPropertyToControlType = false;
            boolean initializedPropertyToMap = false;
            boolean initializedPropertyToMaxLength = false;
            boolean initializedPropertyToRequired = false;
            boolean initializedPropertyNametoXMLAttrName = false;
            boolean initializedDependentsList = false;
            boolean initializedIdentityAttributesList = false;

            StringBuffer initialization = new StringBuffer();

            initialization.append("    // " + objectClassName + "\n");

            String qualifiedClassName = objectClassInfo.getQualifiedName();
            String elementName = repInfo.getObjectElementName();
            if(elementName==null) {
                elementName =
                        Character.toLowerCase(objectClassName.charAt(0)) +
                        objectClassName.substring(1);
            }
            initialization.append("    elementNameToClass.put(\"" + elementName + "\", " + qualifiedClassName + ".class);\n");

            List dependentElementsList = repInfo.getDependentsInfo();
            if (dependentElementsList != null && dependentElementsList.size() > 0) {
                if (!initializedDependentsList) {
                    initialization.append("    dependentElementsList = new ArrayList();\n");
                    initializedDependentsList = true;
                }
                for (int i = 0; i < dependentElementsList.size(); i++) {
                    RepositoryObjectInfo myinfo =
                                (RepositoryObjectInfo)
                                    dependentElementsList.get(i);
                    String name = myinfo.getObjectElementName();
                    if(name == null) {
                        name = myinfo.getObjectClassInfo().getName();
                        name = Character.toLowerCase(name.charAt(0)) +
                                                     name.substring(1);
                    }
                    initialization.append("    dependentElementsList.add(\"" +
                                          name + "\");\n");
                }
            }

            List identityAttributesList = repInfo.getAllIdentityFields();
            if (identityAttributesList != null && identityAttributesList.size() > 0) {
                if (!initializedIdentityAttributesList) {
                    initialization.append("    identityAttributesList = new ArrayList();\n");
                    initializedIdentityAttributesList = true;
                }
                for (int i = 0; i < identityAttributesList.size(); i++) {
                    FieldInfo fieldInfo = (FieldInfo)identityAttributesList.get(i);
                    initialization.append("    identityAttributesList.add(\"" + fieldInfo.getXMLAttributeName() + "\");\n");
                }
            }

            for (Iterator i = allObjectFields.iterator(); i.hasNext();) {
                FieldInfo field = (FieldInfo) i.next();

                String fieldName = field.getName();
                String titledFieldName =
                        GenerateUtilities.getTitledString(fieldName);

                String xmlAttrName = field.getXMLAttributeName();
                if (xmlAttrName != null && !xmlAttrName.equals(fieldName)) {
                    if (!initializedPropertyNametoXMLAttrName) {
                        initialization.append("    propertyNameToXMLAttributeName = new HashMap();\n");
                        initializedPropertyNametoXMLAttrName = true;
                    }
                    initialization.append("    propertyNameToXMLAttributeName.put(\"" + fieldName +
                            "\", \"" + xmlAttrName + "\");\n");
                }

                String controlType = field.getControlType();
                if (controlType!=null) {
                    if (!initializedPropertyToControlType) {
                        initialization.append("    propertyToControlType = new HashMap();\n");
                        initializedPropertyToControlType = true;
                    }
                    initialization.append("    propertyToControlType.put(\"" + fieldName +
                            "\", \"" + controlType + "\");\n");
                }

                int maxLength = field.getMaximumLength();
                if (maxLength > 0) {
                    if (!initializedPropertyToMaxLength) {
                        initialization.append("    propertyToMaxLength = new HashMap();\n");
                        initializedPropertyToMaxLength = true;
                    }
                    initialization.append("    propertyToMaxLength.put(\"" + fieldName +
                            "\", new Integer(" + maxLength + "));\n");
                }

                boolean required = repInfo.isFieldRequired(fieldName);
                if (required) {
                    if (!initializedPropertyToRequired) {
                        initialization.append("    propertyToRequired = new HashMap();\n");
                        initializedPropertyToRequired = true;
                    }
                    initialization.append("    propertyToRequired.put(\"" + fieldName +
                            "\", Boolean.TRUE);\n");
                }

                List values = field.getValues();

                if (values == null || values.size() == 0) {
                    continue;
                }

                if (!initializedPropertyToArray) {
                    initialization.append("    propertyToArray = new HashMap();\n");
                    initializedPropertyToArray = true;
                }

                initialization.append("    propertyToArray.put(\"" +
                        fieldName + "\", " +
                        utilitiesName + ".get" +
                        titledFieldName + "Array());\n");

                if (field.requiresXMLMapping()) {
                    if (!initializedPropertyToMap) {
                        initialization.append("    propertyToMap = new HashMap();\n");
                        initializedPropertyToMap = true;
                    }

                    initialization.append("    propertyToMap.put(\"" +
                            fieldName + "\", " +
                            utilitiesName + ".get" +
                            titledFieldName + "Map());\n");
                }
            }

            if (initializedPropertyToArray) {
                initialization.append("    classToPropertyArray.put(" +
                        objectClassName +
                        ".class, propertyToArray);\n");
            }

            if (initializedPropertyToControlType) {
                initialization.append("    classToControlTypeMap.put(" +
                        objectClassName +
                        ".class, propertyToControlType);\n");
            }

            if (initializedPropertyToMap) {
                initialization.append("    classToPropertyMap.put(" +
                        objectClassName +
                        ".class, propertyToMap);\n");
            }

            if (initializedPropertyToMaxLength) {
                initialization.append("    classToMaxLengthMap.put(" +
                        objectClassName +
                        ".class, propertyToMaxLength);\n");
            }

            if (initializedPropertyToRequired) {
                initialization.append("    classToRequiredMap.put(" +
                        objectClassName +
                        ".class, propertyToRequired);\n");
            }
            if (initializedPropertyNametoXMLAttrName) {
                initialization.append("    classToXMLAttributeMap.put(" +
                        objectClassName +
                        ".class, propertyNameToXMLAttributeName);\n");
            }
            if (initializedDependentsList) {
                initialization.append("    classToDependentElementsMap.put(" +
                        objectClassName +
                        ".class, dependentElementsList);\n");
            }
            if (initializedIdentityAttributesList) {
                initialization.append("    classToIdentityAttributesMap.put(" +
                        objectClassName +
                        ".class, identityAttributesList);\n");
            }
            initialization.append("\n");

            // Insert the initialization in to the buffer
            buffer.insert(propertyValueLookUpInsertionPos, initialization);

            // Re-write the PropertyValueLookUp class.
            FileWriter fw = new FileWriter(propertyValueLookUp);
            PrintWriter bw = new PrintWriter(fw);
            bw.print(buffer.toString());
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate the shell of the PropertyValueLookUp class i.e. the entire
     * class except for most of what will later go into the static
     * initializer. Then write out this class. Later, every Utility class
     * generator that needs to will insert statements into the static initializer
     * of this class. This ensures that the PropertyValueLookUp class is always
     * up-to-date and complete - since we do not know when the last update will
     * be done on this class.
     *
     * @param generatedDir The directory to write the PropertyValueLookUp to
     * @return the insertion position inside the static initializer that
     * later code should write at.
     */
    private static int generatePropertyValueLookUp(File generatedDir) throws IOException {
        PrintStream file =
                openFileForClass("com.volantis.mcs.objects.PropertyValueLookUp", generatedDir);

        // Use a byte array so we can get the string that has been printed
        // and use this to find out the insertion position inside the static
        // initializer.
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(byteArray);

        // Write the header.
        GenerateUtilities.writeHeader(out, "PropertyValueLookupUtilities" );

        // Write the package declaration.
        out.println();
        out.println("package com.volantis.mcs.objects;");

        // Write the imports.
        SortedSet imports = new TreeSet();
        imports.add("com.volantis.mcs.assets.*");
        imports.add("com.volantis.mcs.components.*");
        imports.add("com.volantis.mcs.layouts.*");
        imports.add("com.volantis.mcs.themes.*");
        imports.add("java.util.HashMap");
        imports.add("java.util.Iterator");
        imports.add("java.util.List");
        imports.add("java.util.Map");
        imports.add("java.util.ArrayList");

        GenerateUtilities.writeImports(out, imports);

        out.println();
        out.println("public class PropertyValueLookUp {");

        // Write out static variable declarations
        out.println();
        out.println("  private static HashMap classToControlTypeMap = new HashMap();");
        out.println("  private static HashMap classToMaxLengthMap = new HashMap();");
        out.println("  private static HashMap classToPropertyMap = new HashMap();");
        out.println("  private static HashMap classToRequiredMap = new HashMap();");
        out.println("  private static HashMap classToPropertyArray = new HashMap();");
        out.println("  private static HashMap classToXMLAttributeMap = new HashMap();");
        out.println("  private static HashMap elementNameToClass = new HashMap();");
        out.println("  private static HashMap classToDependentElementsMap = new HashMap();");
        out.println("  private static HashMap classToIdentityAttributesMap = new HashMap();");
        out.println("  private static HashMap propertyToArray;");
        out.println("  private static HashMap propertyToControlType;");
        out.println("  private static HashMap propertyToMap;");
        out.println("  private static HashMap propertyToMaxLength;");
        out.println("  private static HashMap propertyToRequired;");
        out.println("  private static HashMap propertyNameToXMLAttributeName;");
        out.println("  private static List dependentElementsList;");
        out.println("  private static List identityAttributesList;");

        // Write out the empty static initializer ready for later insertions
        out.println();
        out.println("  static {");

        // This is the insertion position.
        int insertionPosition = byteArray.toString().length();

        out.println("  }");

        // Write out the getPropertyArray method
        out.println();
        out.println("  public static Object [] getPropertyArray(Class cls,");
        out.println("                                           String property) {");
        out.println("    Map propertyToArray = (Map) classToPropertyArray.get(cls);");
        out.println("    if(propertyToArray == null) {");
        out.println("      return null;");
        out.println("    }");
        out.println();
        out.println("    Object [] array = (Object []) propertyToArray.get(property);");
        out.println();
        out.println("    return array;");
        out.println("  }");

        // Write out the getPropertyArray method for XML element and attribute
        out.println();
        out.println("  public static Object [] getPropertyArray(String elementName,");
        out.println("                                           String attributeName) {");
        out.println("      Class cls = getClassForXMLElement(elementName);");
        out.println("      if (cls == null) {");
        out.println("          return null;");
        out.println("      }");
        out.println("      String propertyName = getPropertyName(elementName, attributeName);");
        out.println();
        out.println("      return getPropertyArray(cls, propertyName);");
        out.println("  }");

        // Write out the getExternalPropertyArray
        out.println();
        out.println("  public static Object [] getExternalPropertyArray(Class cls,");
        out.println("                                                   String property) {");
        out.println("    Object internalArray [] = getPropertyArray(cls, property);");
        out.println("    if(internalArray==null) {");
        out.println("      return null;");
        out.println("    }");
        out.println();
        out.println("    Object [] externalArray = new String[internalArray.length];");
        out.println("    int i = 0;");
        out.println("    Object s = null;");
        out.println("    do {");
        out.println("      s = getPropertyValue(cls, property,");
        out.println("                                    internalArray[i]);");
        out.println("      if( s != null ) {");
        out.println("        if( s instanceof String ) {");
        out.println("          externalArray[i] = s;");
        out.println("        } else {");
        out.println("          externalArray[i] = s.toString();");
        out.println("        }");
        out.println("      }");
        out.println("      i++;");
        out.println("    } while(s!=null && i<internalArray.length);");
        out.println();
        out.println("    // If s is null it means there is no internal-to-external");
        out.println("    // mapping. So, return the internal array");
        out.println("    return s==null ? internalArray : externalArray;");
        out.println("  }");

        // Write out the getExternalPropertyArray method for XML element and attribute
        out.println();
        out.println("  public static Object [] getExternalPropertyArray(String elementName,");
        out.println("                                           String attributeName) {");
        out.println("      Class cls = getClassForXMLElement(elementName);");
        out.println("      if (cls == null) {");
        out.println("          return null;");
        out.println("      }");
        out.println("      String propertyName = getPropertyName(elementName, attributeName);");
        out.println();
        out.println("      return getExternalPropertyArray(cls, propertyName);");
        out.println("  }");

        // Write out the Object version of the getPropertyValue method
        out.println();
        out.println("  public static Object getPropertyValue(Class cls,");
        out.println("                                        String property,");
        out.println("                                        Object key) {");
        out.println("    Map propertyToMap = (Map) classToPropertyMap.get(cls);");
        out.println("    if(propertyToMap == null) {");
        out.println("      return null;");
        out.println("    }");
        out.println();
        out.println("    Map map = (Map) propertyToMap.get(property);");
        out.println("    if(map == null) {");
        out.println("      return null;");
        out.println("    }");
        out.println();
        out.println("    return map.get(key);");
        out.println("  }");

        // Write out the Object version of getPropertyValue method for XML element and attribute
        out.println();
        out.println("  public static Object getPropertyValue(String elementName,");
        out.println("                                           String attributeName, Object key) {");
        out.println("      Class cls = getClassForXMLElement(elementName);");
        out.println("      if (cls == null) {");
        out.println("          return null;");
        out.println("      }");
        out.println("      String propertyName = getPropertyName(elementName, attributeName);");
        out.println();
        out.println("      return getPropertyValue(cls, propertyName, key);");
        out.println("  }");

        // Write out the int version of the getPropertyValue method
        out.println();
        out.println("  public static Object getPropertyValue(Class cls,");
        out.println("                                        String property,");
        out.println("                                        int key) {");
        out.println("    return getPropertyValue(cls, property, new Integer(key));");
        out.println("  }");

        // Write out the int version of getPropertyValue method for XML element and attribute
        out.println();
        out.println("  public static Object getPropertyValue(String elementName,");
        out.println("                                           String attributeName, int key) {");
        out.println("      Class cls = getClassForXMLElement(elementName);");
        out.println("      if (cls == null) {");
        out.println("          return null;");
        out.println("      }");
        out.println("      String propertyName = getPropertyName(elementName, attributeName);");
        out.println();
        out.println("      return getPropertyValue(cls, propertyName, key);");
        out.println("  }");

        // Write out the isRequired method
        out.println();
        out.println("  public static boolean isRequired(Class cls,");
        out.println("                                   String property) {");
        out.println("    Map map = (Map) classToRequiredMap.get(cls);");
        out.println("    if(map==null) {");
        out.println("      return false;");
        out.println("    }");
        out.println();
        out.println("    Boolean b = (Boolean) map.get(property);");
        out.println();
        out.println("    return b!=null && b.equals(Boolean.TRUE);");
        out.println("  }");

        // Write out the isRequired method for XML elements and attributes
        out.println();
        out.println("  public static boolean isRequired(String elementName,");
        out.println("                                           String attributeName) {");
        out.println("      Class cls = getClassForXMLElement(elementName);");
        out.println("      if (cls == null) {");
        out.println("          return false;");
        out.println("      }");
        out.println("      String propertyName = getPropertyName(elementName, attributeName);");
        out.println();
        out.println("      return isRequired(cls, propertyName);");
        out.println("  }");

        // Write out the getControlType method
        out.println();
        out.println("  public static String getControlType(Class cls,");
        out.println("                                 String property) {");
        out.println("    Map map = (Map) classToControlTypeMap.get(cls);");
        out.println("    if(map==null) {");
        out.println("      return null;");
        out.println("    }");
        out.println();
        out.println("    return (String) map.get(property);");
        out.println("  }");

        // Write out the getControlType method for XML elements and attributes
        out.println();
        out.println("  public static String getControlType(String elementName,");
        out.println("                                 String property) {");
        out.println("      Class cls = getClassForXMLElement(elementName);");
        out.println("      if (cls == null) {");
        out.println("          return null;");
        out.println("      }");
        out.println();
        out.println("    return getControlType(cls, property);");
        out.println("  }");

        // Write out the getMaxLength method
        out.println();
        out.println("  public static int getMaxLength(Class cls,");
        out.println("                                 String property) {");
        out.println("    Map map = (Map) classToMaxLengthMap.get(cls);");
        out.println("    if(map==null) {");
        out.println("      return -1;");
        out.println("    }");
        out.println();
        out.println("    Integer i = (Integer) map.get(property);");
        out.println();
        out.println("    if(i==null) {");
        out.println("      return -1;");
        out.println("    }");
        out.println();
        out.println("    return i.intValue();");
        out.println("  }");

        // Write out the getMaxLength method for XML elements and attributes
        out.println();
        out.println("  public static int getMaxLength(String elementName,");
        out.println("                                           String attributeName) {");
        out.println("      Class cls = getClassForXMLElement(elementName);");
        out.println("      if (cls == null) {");
        out.println("          return -1;");
        out.println("      }");
        out.println("      String propertyName = getPropertyName(elementName, attributeName);");
        out.println();
        out.println("      return getMaxLength(cls, propertyName);");
        out.println("  }");

        // Write out the getXMLAttributeName method
        out.println();
        out.println("  public static String getXMLAttributeName(String elementName,");
        out.println("                                 String property) {");
        out.println("    Class cls = getClassForXMLElement(elementName);");
        out.println("    Map map = (Map) classToXMLAttributeMap.get(cls);");
        out.println("    if (map == null) {");
        out.println("      return property;");
        out.println("    }");
        out.println();
        out.println("    String name = (String) map.get(property);");
        out.println();
        out.println("    return name == null ? property : name;");
        out.println("  }");

        // Write out the getPropertyName method
        out.println();
        out.println("  public static String getPropertyName(String elementName,");
        out.println("                                 String xmlAttributeName) {");
        out.println("    Class cls = getClassForXMLElement(elementName);");
        out.println("    Map map = (Map) classToXMLAttributeMap.get(cls);");
        out.println("    if (map == null) {");
        out.println("        return xmlAttributeName;");
        out.println("    }");
        out.println("    for (Iterator it = map.keySet().iterator(); it.hasNext();) {");
        out.println("        String key = (String) it.next();");
        out.println("        if ((map.get(key)).equals(xmlAttributeName)) {");
        out.println("            return key;");
        out.println("        }");
        out.println("    }");
        out.println("    return xmlAttributeName;");
        out.println("  }");

        // Write out the getClassForXMLElement method
        out.println();
        out.println("  public static Class getClassForXMLElement(String element) {");
        out.println("      return (Class) elementNameToClass.get(element);");
        out.println("  }");

        // Write out the getDependentElements method
        out.println();
        out.println("  public static List getDependentElements(String element) {");
        out.println("    Class cls = getClassForXMLElement(element);");
        out.println("    if (cls == null) {");
        out.println("        return null;");
        out.println("    }");
        out.println("    List dependentElements = (List) classToDependentElementsMap.get(cls);");
        out.println("    if (dependentElements == null) {");
        out.println("        return null;");
        out.println("    }");
        out.println("    ArrayList list = new ArrayList(dependentElements.size());");
        out.println("    list.addAll(dependentElements);");
        out.println("    return list;");
        out.println("  }");

        // Write out the getIdentityAttributes method
        out.println();
        out.println("  public static List getIdentityAttributes(String element) {");
        out.println("    Class cls = getClassForXMLElement(element);");
        out.println("    if (cls == null) {");
        out.println("        return null;");
        out.println("    }");
        out.println("    List identityAttrs = (List) classToIdentityAttributesMap.get(cls);");
        out.println("    if (identityAttrs == null) {");
        out.println("        return null;");
        out.println("    }");
        out.println("    ArrayList list = new ArrayList(identityAttrs.size());");
        out.println("    list.addAll(identityAttrs);");
        out.println("    return list;");
        out.println("  }");

        // Write out the class closing brace
        out.println("}");

        // Write out to the file.
        out.flush();
        byteArray.flush();
        file.println(byteArray.toString());

        return insertionPosition;
    }

    /**
     * Open a file relative to the generated directory.
     * @param qualifiedClassName The fully qualified class name.
     * @param generatedDir The directory to write to.
     * @return A PrintStream.
     */
    private static PrintStream openFileForClass(String qualifiedClassName, File generatedDir) {
        return GenerateUtilities.openFileForClass(generatedDir,
                qualifiedClassName);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 03-May-05	7963/1	pduffin	VBM:2005042906 Removed DDM components, e.g. ApplicationProperties, URLMappers, DDMProxy, etc

 25-Jan-05	6712/2	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Aug-04	5130/4	doug	VBM:2004080310 MCS

 14-Jul-04	4876/1	allan	VBM:2004062501 Fix asset selection filtering.

 28-Jan-04	2524/1	mat	VBM:2004010712 Needed merge

 31-Dec-03	2306/3	richardc	VBM:2003121723 Added UniqueAssetValidator and applied to AssetsSection

 29-Dec-03	2258/1	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 27-Nov-03	2013/2	allan	VBM:2003112501 Candidate commit for AttributesComposite redesign.

 10-Nov-03	1821/3	pcameron	VBM:2003110401 Added PropertyValueLookUp methods for getting dependents of elements

 03-Nov-03	1698/1	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
