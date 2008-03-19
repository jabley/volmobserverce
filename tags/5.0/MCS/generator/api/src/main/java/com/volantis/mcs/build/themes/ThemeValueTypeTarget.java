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
 * $Header: /src/voyager/com/volantis/mcs/build/themes/ThemeValueTypeTarget.java,v 1.2 2002/05/07 13:14:25 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Apr-02    Doug            VBM:2002040803 - Created.
 * 06-May-02    Doug            VBM:2002040803 - Removed System.out.println()
 *                              statement.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes;

import com.volantis.mcs.build.*;
import com.volantis.mcs.build.parser.*;

import java.util.List;

import org.jdom.ProcessingInstruction;

/**
 * Instances of this class handle processing instructions with a target of
 * "value_type".
 */
public class ThemeValueTypeTarget implements ProcessingInstructionTarget {

  // Javadoc inherited from super class.
  public void handleProcessingInstruction (SchemaParser parser,
                                           String target, String data) {
    
    ProcessingInstruction pi = new ProcessingInstruction (target, data);
    SchemaObject object = parser.getCurrentObject ();

    if (object instanceof ThemeElementInfo) {
      ThemeElementInfo info = (ThemeElementInfo) object;

      String valueTypeName = getRequiredValue(pi, "type");
      ValueTypeInfo valueTypeInfo = new ValueTypeInfo(valueTypeName);
      valueTypeInfo.setVersions(pi);
      info.addValueType(valueTypeInfo);
    }
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

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
