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
 * $Header: /src/voyager/com/volantis/mcs/build/themes/XMLPropertyConstantsGenerator.java,v 1.2 2002/05/16 12:38:43 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 30-Apr-02    Doug            VBM:2002040803 - Created.
 * 16-May-02    Doug            VBM:2002040803 - Added the method 
 *                              getXMLElementConstant(String).
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.build.themes;

import com.volantis.mcs.build.GenerateUtilities;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;

public class XMLPropertyConstantsGenerator 
  extends AbstractThemeClassGenerator {


    static public String getXMLElementConstant(StylePropertyInfo info) {
    return getXMLElementConstant(info.getNaturalName());
  }

  static public String getXMLElementConstant(String property) {
    String[] words = GenerateUtilities.getWords(property);
    return GenerateUtilities.getConstant(words) + "_ELEMENT";
  }

  static public String getXMLElement(StylePropertyInfo info) {
    return info.getXMLElementName();
  }

  public XMLPropertyConstantsGenerator(File generatedDir, 
				       String packageName) {
    super(generatedDir, packageName);
  }


  /**
   * Open a file relative to the generated directory.
   * @param qualifiedClassName The fully qualified class name.
   * @return A PrintStream.
   */
  protected PrintStream openFileForClass (String qualifiedClassName) {
    return GenerateUtilities.openFileForClass (generatedDir,
                                               qualifiedClassName);
  }

  
  public void generate(Collection styleProperties) {

    System.out.println ("  Generating the XMLPropertyConstants Class");
    
    String className = "XMLPropertyConstants";

    String qualifiedName = packageName + "." + className;

    // Open a file.
    out = openFileForClass (qualifiedName);

    // Write the header.
    GenerateUtilities.writeHeader (out, this.getClass().getName());

    // Write the package declaration.
    out.println ();
    out.println ("package " + packageName + ";");

    
    // Write the class header.
    out.println ();
    out.println("public interface " + className + " {");
    out.println();


    for(Iterator i=styleProperties.iterator(); i.hasNext();) {
      StylePropertyInfo styleInfo = (StylePropertyInfo)i.next();
      String constant = getXMLElementConstant(styleInfo);
      String elementName = getXMLElement(styleInfo);
      out.println("  public static final String " + constant + " =");
      out.println("  \"" + elementName + "\";");  
    }
    // close the class
    out.println ("}");
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

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 09-Jan-04	2524/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 09-Jan-04	2521/1	mat	VBM:2004010712 Remove styleClass validation for multiple styles

 ===========================================================================
*/
