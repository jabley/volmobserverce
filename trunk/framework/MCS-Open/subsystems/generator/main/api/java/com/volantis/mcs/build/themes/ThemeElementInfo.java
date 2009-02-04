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
 * $Header: /src/voyager/com/volantis/mcs/build/themes/ThemeElementInfo.java,v 1.3 2002/05/16 08:42:21 pduffin Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Apr-02    Doug            VBM:2002040803 - Created.
 * 02-May-02    Doug            VBM:2002040803 - Added a naturalName property.
 * 16-May-02    Paul            VBM:2002032501 - Updated as some classes have
 *                              moved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes;

import com.volantis.mcs.build.marlin.ElementInfo;

import com.volantis.mcs.build.parser.Scope;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.ProcessingInstruction;

public class ThemeElementInfo extends ElementInfo implements ThemeVersionInfo {

  private boolean supportedInCSS2;

  private boolean supportedInCSS1;

  private boolean supportedInCSSMobile;

  private boolean supportedInCSSWAP;

  private boolean stylePropertyElement;

  private List valueTypeList;

  private String enumerationName;

  public ThemeElementInfo (Scope scope, String name) {
    super(scope, name);
    valueTypeList = new ArrayList();
  }

    /**
     * Set the versions information in this info object by inspecting the
     * related values in a processing instruction.
     *
     * @param pi the processing instruction with version information.
     */
    public void setVersions(ProcessingInstruction pi) {
        supportedInCSS2 = !"false".equals(pi.getValue("css2"));
        supportedInCSS1 = !"false".equals(pi.getValue("css1"));
        supportedInCSSMobile = !"false".equals(pi.getValue("cssmobile"));
        supportedInCSSWAP = !"false".equals(pi.getValue("csswap"));
    }

    // Javadoc inherited.
    public boolean isSupportedInCSS1() {
        return supportedInCSS1;
    }

    // Javadoc inherited.
    public boolean isSupportedInCSS2() {
        return supportedInCSS2;
    }

    // Javadoc inherited.
    public boolean isSupportedInCSSMobile() {
        return supportedInCSSMobile;
    }

    // Javadoc inherited.
    public boolean isSupportedInCSSWAP() {
        return supportedInCSSWAP;
    }

  /**
   * Get the value of stylePropertyElement.
   * @return value of stylePropertyElement.
   */
  public boolean isStylePropertyElement() {
    return stylePropertyElement;
  }
  
  /**
   * Set the value of stylePropertyElement.
   * @param v  Value to assign to stylePropertyElement.
   */
  public void setStylePropertyElement(boolean  v) {
    this.stylePropertyElement = v;
  }

    /**
   * Get the value of enumerationName.
   * @return value of enumerationName.
   */
  public String getEnumerationName() {
    return enumerationName;
  }
  
  /**
   * Set the value of enumerationName.
   * @param v  Value to assign to enumerationName.
   */
  public void setEnumerationName(String  v) {
    this.enumerationName = v;
  }

  
  public void addValueType(ValueTypeInfo valueType) {
    valueTypeList.add(valueType);
  }

  public Iterator valueTypeIterator() {
    return valueTypeList.iterator();
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

 16-Sep-05	9512/1	pduffin	VBM:2005091408 Fixed up some issues with theme generator and added support for border-collapse and caption-side

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/2	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 06-Apr-04	3746/1	mat	VBM:2004031908 Support short table names

 06-Apr-04	3521/1	mat	VBM:2004031908 Support short table names

 ===========================================================================
*/
