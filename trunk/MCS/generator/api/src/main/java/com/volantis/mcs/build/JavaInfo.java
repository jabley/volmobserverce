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
 * $Header: /src/voyager/com/volantis/mcs/build/JavaInfo.java,v 1.4 2002/04/27 16:10:54 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Dec-01    Paul            VBM:2001120506 - Created.
 * 23-Jan-02    Paul            VBM:2002012202 - Added getJavaVariable method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 27-Apr-02    Doug            VBM:2002040803 - Added methods to support
 *                              java primitives.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build;

import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains Java specific information.
 */
public class JavaInfo {

  /**
   * The set of words which are reserved in java.
   */
  private static Set reservedWords;

  /**
   * The set of words which are primitive java types.
   */
  private static Set primitiveTypes;

  /**
   * The set of words which are primitive java types.
   */
  private static Map primitiveToObjectMap;

  static {
    String [] words = new String [] {
      "abstract",
      "boolean",
      "break",
      "byte",
      "case",
      "catch",
      "char",
      "class",
      "const",
      "continue",
      "default",
      "do",
      "double",
      "else",
      "extends",
      "false",
      "final",
      "finally",
      "float",
      "for",
      "goto",
      "if",
      "implements",
      "import",
      "instanceof",
      "int",
      "interface",
      "long",
      "native",
      "new",
      "package",
      "private",
      "protected",
      "public",
      "return",
      "short",
      "static",
      "strictfp",
      "super",
      "switch",
      "synchronized",
      "this",
      "throws",
      "transient",
      "true",
      "try",
      "void",
      "volatile",
      "while",
    };

    reservedWords = new HashSet ();
    for (int i = 0; i < words.length; i += 1) {
      reservedWords.add (words [i]);
    }

    primitiveTypes = new HashSet ();
    primitiveTypes.add("byte");
    primitiveTypes.add("short");
    primitiveTypes.add("int");
    primitiveTypes.add("long");
    primitiveTypes.add("float");
    primitiveTypes.add("double");
    primitiveTypes.add("char");
    primitiveTypes.add("boolean");

    primitiveToObjectMap = new HashMap();
    primitiveToObjectMap.put("byte", "Byte");
    primitiveToObjectMap.put("short", "Short");
    primitiveToObjectMap.put("int", "Ineteger");
    primitiveToObjectMap.put("long", "Long");
    primitiveToObjectMap.put("float", "Float");
    primitiveToObjectMap.put("double", "Double");
    primitiveToObjectMap.put("char", "Character");
    primitiveToObjectMap.put("boolean", "Boolean");
    
  }

  /**
   * Checks to see whether the specified word is reserved in java or not.
   * @return True if the specified word is reserved in java and false
   * otherwise.
   */
  public static boolean isReservedWord (String word) {
    return reservedWords.contains (word);
  }

  /**
   * Get the name of the java variable which should be used for the specified
   * name.
   * This method makes sure that the returned string is not a java reserved
   * word by appending an _ to it.
   * @return The specified name, unless it is a reserved word in
   * which case it has an _ appended to the end.
   */
  public static String getJavaVariable (String name) {
    if (isReservedWord (name)) {
      return name + "_";
    } else {
      return name;
    }
  }

  /**
   * Returns true if the type String is a java primitive type/
   * @param type A type String
   * @return true if the type is a primitive type.
   */
  public static boolean isPrimitiveType(String type) {
    return primitiveTypes.contains(type);
  }


  /**
   * Return a String that specifies the Java class that wraps the primitive
   * type specified by the type paramater
   * @param primitive A String representation of a java primitive
   * @return The primitive wrapper object.
   */
  public static String getPrimitiveObject(String primitive) {
    if(!primitiveTypes.contains(primitive)) {
      return primitive;
    }
    return ((String)primitiveToObjectMap.get(primitive));
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

 ===========================================================================
*/
