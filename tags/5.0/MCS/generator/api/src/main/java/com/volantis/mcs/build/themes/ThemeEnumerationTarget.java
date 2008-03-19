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
/* $Header: /src/voyager/com/volantis/mcs/build/themes/ThemeEnumerationTarget.java,v 1.1 2002/04/27 16:10:54 doug Exp $
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

import com.volantis.mcs.build.*;
import com.volantis.mcs.build.parser.*;

import java.util.Map;

import org.jdom.ProcessingInstruction;

/**
 * Instances of this class handle processing instructions with a target of
 * "enumeration".
 */
public class ThemeEnumerationTarget implements ProcessingInstructionTarget {

  private Map enumerations;

  public ThemeEnumerationTarget(Map enumerations) {
    this.enumerations = enumerations;
  }

  // Javadoc inherited from super class.
  public void handleProcessingInstruction (SchemaParser parser,
                                           String target, String data) {
    
    ProcessingInstruction pi = new ProcessingInstruction (target, data);
    SchemaObject object = parser.getCurrentObject ();    

    ThemeElementInfo info = (ThemeElementInfo) object;
    String label = info.getName();
    String grp = getRequiredValue(pi, "grp");

    EnumerationInfo theEnum = getEnumeration(grp);
    EnumerationInfo.Element enumElement = new EnumerationInfo.Element(label);
    enumElement.setVersions(pi);
    theEnum.addElement(enumElement);

  }

  /**
   * Retrieve a named value from the ProcessingInstruction object. If it does
   * not exist throw an Exception.
   * @param pi the ProcessingInstruction object.
   * @param name the name that identifies the value to retrieve.
   * @return the value.
   */
  protected String getRequiredValue(ProcessingInstruction pi, String name) {
    String value = pi.getValue(name);
    if(value == null) {
      throw new IllegalStateException("Style Value Processing instruction " +
				      "must provide a \"" + name +
				      "\" attribute");
    }
    return value;
  }

  protected EnumerationInfo getEnumeration(String name) {
    EnumerationInfo theEnum = (EnumerationInfo)enumerations.get(name);
    if(theEnum == null) {
      theEnum = new EnumerationInfo();
      theEnum.setName(name);
      enumerations.put(name, theEnum);
    }
    return theEnum;
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

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
