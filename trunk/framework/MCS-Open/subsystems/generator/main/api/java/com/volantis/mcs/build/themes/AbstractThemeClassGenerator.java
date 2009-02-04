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
 * $Header: /src/voyager/com/volantis/mcs/build/themes/AbstractThemeClassGenerator.java,v 1.1 2002/04/27 16:10:54 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Apr-02    Doug            VBM:2002040803 - Created.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes;

import com.volantis.mcs.build.GenerateUtilities;

import java.io.File;
import java.io.PrintStream;

abstract public class AbstractThemeClassGenerator {

  /**
   * The PrintStream to write to.
   */
  protected PrintStream out;

  /**
   * The name of the package that the generated classes will belong to.
   */
  protected String packageName;

  /**
   * The directory into which the generated code will be written.
   */
  protected File generatedDir;


  /**
   * Creates a new AbstractThemeClassGenerator instance.
   * @param generatedDir the directory were generated classes will be located.
   * @param packageName the genertated classes package name.
   */
  public AbstractThemeClassGenerator(File generatedDir, String packageName) {
    this.generatedDir = generatedDir;
    this.packageName = packageName;
  }

  
  /**
   * Open a file relative to the generated directory.
   * @param qualifiedClassName The fully qualified class name.
   * @return A PrintStream.
   */
  protected PrintStream openFileForClass(String qualifiedClassName) {
    return GenerateUtilities.openFileForClass(generatedDir,
					      qualifiedClassName);
  }

    /**
     * Return a constant that identifies an Enumeration element
     * @param name the base name
     * @return a the constant string.
     */
    public static String getConstant(String name) {
      String[] words = GenerateUtilities.getWords(name);
      String constant = GenerateUtilities.getConstant(words);
      return constant.replace('-', '_');
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

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
*/
