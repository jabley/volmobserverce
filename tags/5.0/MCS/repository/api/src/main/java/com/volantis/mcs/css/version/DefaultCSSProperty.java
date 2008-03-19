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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.css.version;

import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.synergetics.cornerstone.utilities.MCSObject;
import com.volantis.styling.properties.StyleProperty;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

/**
 * A default implementation of CSSProperty.
 */
public class DefaultCSSProperty extends MCSObject implements CSSProperty {

    /**
     * The css version that owns this css property.
     */
    private DefaultCSSVersion version;

    /**
     * The canonical MCS definition of the property.
     */
    private StyleProperty property;

    /**
     * The value types supported for this css property, one bit per value type
     * enumeration value.
     */
    private BitSet valueTypes = new BitSet();

    /**
     * The keywords supported for this css property.
     */
    private Set keywords = new HashSet();

    /**
     * Initialise.
     *
     * @param version the owner of this css property.
     * @param property the MCS definition of this css property.
     */
    DefaultCSSProperty(DefaultCSSVersion version,
            StyleProperty property) {

        this.version = version;
        this.property = property;
    }

    /**
     * Add a set of supported value types for this css property.
     *
     * @param valueTypes the value types to add as supported.
     */
    public void addValueTypes(StyleValueType[] valueTypes) {
        version.ensureMutable();
        for (int i=0; i< valueTypes.length; i++) {
            this.valueTypes.set(valueTypes[i].getIndex());
        }
    }

    /**
     * Remove a set of supported value types for this css property.
     *
     * @param valueTypes the value types to remove as supported.
     */
    public void removeValueTypes(StyleValueType[] valueTypes) {
        version.ensureMutable();
        for (int i=0; i< valueTypes.length; i++) {
            this.valueTypes.clear(valueTypes[i].getIndex());
        }
    }

    public void addKeyword(StyleKeyword keyword) {
        version.ensureMutable();
        keywords.add(keyword);
    }

    public void removeKeyword(StyleKeyword keyword) {
        version.ensureMutable();
        keywords.remove(keyword);
    }

    /**
     * Add a set of supported keywords for this css property.
     *
     * @param keywords the keywords to add as supported.
     */
    public void addKeywords(StyleKeyword[] keywords) {
        version.ensureMutable();
        for (int i=0; i< keywords.length; i++) {
            this.keywords.add(keywords[i]);
        }
    }

    public StyleProperty getStyleProperty() {
        return property;
    }

    // Javadoc inherited.
    public boolean supportsValueType(StyleValueType valueType) {
        // version.ensureImmutable();
        return valueTypes.get(valueType.getIndex());
    }

    // Javadoc inherited.
    public boolean supportsKeyword(StyleKeyword keyword) {
        version.ensureImmutable();
        return keywords.contains(keyword);
    }

    // Javadoc inherited.
    protected String toStringData() {
        return "version=" + version + ", " +
                "name=" + property.getName() + ", " +
                "types=" + valueTypes + ", " +
                "keywords=" + keywords;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10829/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 29-Nov-05	10370/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10370/2	geoff	VBM:2005111405 interim commit

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 22-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	5733/3	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
