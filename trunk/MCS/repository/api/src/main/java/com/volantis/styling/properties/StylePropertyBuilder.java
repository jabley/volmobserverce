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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.styling.properties;

import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.AllowableKeywords;
import com.volantis.styling.values.InitialValueSource;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;

/**
 * A {@link StyleProperty}.
 */
public class StylePropertyBuilder {

    /**
     * The name of the property
     */
    private String name;

    private StyleValue fixedInitialValue;
    private String propertyInitialValue;
    private Map initialValueRules;

    private PropertyDetailsBuilder detailsBuilder;


    /**
     * Initialise.
     */
    public StylePropertyBuilder() {
        detailsBuilder = new PropertyDetailsBuilder();
    }

    // Javadoc inherited.
    public void setName(String name) {
        this.name = name;
    }

    // Javadoc inherited.
    public final String getName() {
        return name;
    }

    // Javadoc inherited.
    public void setInherited(boolean inherited) {
        detailsBuilder.setInherited(inherited);
    }

    // Javadoc inherited.
    public void setTypes(StyleValueType[] types) {
        Set supportedTypes = new HashSet();
        for (int i = 0; i < types.length; i++) {
            StyleValueType type = types[i];
            supportedTypes.add(type);
        }

        detailsBuilder.setSupportedTypes(supportedTypes);
    }

    public void setInitialValueSource(InitialValueSource source) {
        detailsBuilder.setInitialValueSource(source);
    }

    public StyleValue getFixedInitialValue() {
        return fixedInitialValue;
    }

    public void setFixedInitialValue(StyleValue value) {
        this.fixedInitialValue = value;
    }

    public String getPropertyInitialValue() {
        return propertyInitialValue;
    }

    public void setPropertyInitialValue(String propertyName) {
        this.propertyInitialValue = propertyName;
    }

    public Map getInitialValueRules() {
        return initialValueRules;
    }

    public void setInitialValueRules(Map value) {
        initialValueRules = value;
    }

    public void setAllowableKeywords(AllowableKeywords allowableKeywords) {
        detailsBuilder.setAllowableKeywords(allowableKeywords);
    }

    public PropertyDetailsBuilder getDetailsBuilder() {
        return detailsBuilder;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 27-Oct-05	9965/1	ianw	VBM:2005101811 interim commit

 22-Jul-05	9110/1	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
