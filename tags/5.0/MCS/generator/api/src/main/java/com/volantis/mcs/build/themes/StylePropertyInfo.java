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
 * $Header: /src/voyager/com/volantis/mcs/build/themes/StylePropertyInfo.java,v 1.2 2002/05/02 08:27:36 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Apr-02    Doug            VBM:2002040803 - Created.
 * 02-May-02    Doug            VBM:2002040803 - Added a naturalName property.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.build.themes;

import com.volantis.mcs.build.themes.definitions.values.Value;
import com.volantis.mcs.build.themes.definitions.values.ValueSource;
import com.volantis.mcs.build.themes.definitions.Property;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class StylePropertyInfo {

    private ThemeElementInfo themeElementInfo;

    private String naturalName;

    private String propertyName;

    private List valueTypeList;

    private String enumerationName;

    private String xmlElementName;

    private Value initialValue;

    private ValueSource initialValueSource;

    /**
     * Whether the property is inherited by default
     */    
    private boolean inherited;

    public ThemeElementInfo getThemeElementInfo() {
        return themeElementInfo;
    }

    public void setThemeElementInfo(ThemeElementInfo themeElementInfo) {
        this.themeElementInfo = themeElementInfo;
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
    public void setEnumerationName(String v) {
        this.enumerationName = v;
    }

    public StylePropertyInfo() {
        this.valueTypeList = new ArrayList();
    }

    /**
     * Get the value of name.
     * @return value of name.
     */
    public String getNaturalName() {
        return naturalName;
    }

    /**
     * Set the value of name.
     * @param v  Value to assign to name.
     */
    public void setNaturalName(String v) {
        this.naturalName = v;
    }

    /**
     * Get the value of name.
     * @return value of name.
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Set the value of name.
     * @param v  Value to assign to name.
     */
    public void setPropertyName(String v) {
        this.propertyName = v;
    }

    public void addValueType(ValueTypeInfo valueType) {
        valueTypeList.add(valueType);
    }

    public Iterator valueTypeIterator() {
        return valueTypeList.iterator();
    }

    /**
     * Get the value of xmlElementName.
     * @return value of xmlElementName.
     */
    public String getXMLElementName() {
        return xmlElementName;
    }

    /**
     * Set the value of xmlElementName.
     * @param v  Value to assign to xmlElementName.
     */
    public void setXMLElementName(String v) {
        this.xmlElementName = v;
    }

    public ValueSource getInitialValueSource() {
        return initialValueSource;
    }

    /**
     * Query whether the property is inherited by default.
     * @return True if the property is inherited by default
     */
    public boolean isInherited() {
        return inherited;
    }

    public void merge(Property property) {
        initialValue = property.getInitialValue();
        inherited = property.isInherited();
        initialValueSource = property.getInitialValueSource();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 16-Sep-05	9512/1	pduffin	VBM:2005091408 Fixed up some issues with theme generator and added support for border-collapse and caption-side

 05-Sep-05	9407/1	pduffin	VBM:2005083007 Removed old themes model

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 26-Oct-04	5968/1	adrianj	VBM:2004083105 Add inherited property to StylePropertyDetails

 07-Apr-04	3272/2	philws	VBM:2004021117 Fix merge conflicts

 06-Apr-04	3746/1	mat	VBM:2004031908 Support short table names

 06-Apr-04	3521/1	mat	VBM:2004031908 Support short table names

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/
