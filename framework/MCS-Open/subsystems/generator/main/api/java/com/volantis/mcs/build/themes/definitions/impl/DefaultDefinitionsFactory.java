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

package com.volantis.mcs.build.themes.definitions.impl;

import com.volantis.mcs.build.themes.definitions.DefinitionsFactory;
import com.volantis.mcs.build.themes.definitions.Property;
import com.volantis.mcs.build.themes.definitions.Rule;
import com.volantis.mcs.build.themes.definitions.Rules;
import com.volantis.mcs.build.themes.definitions.TypeDefinition;
import com.volantis.mcs.build.themes.definitions.types.ChoiceType;
import com.volantis.mcs.build.themes.definitions.types.FractionType;
import com.volantis.mcs.build.themes.definitions.types.Keyword;
import com.volantis.mcs.build.themes.definitions.types.Keywords;
import com.volantis.mcs.build.themes.definitions.types.PairType;
import com.volantis.mcs.build.themes.definitions.types.TypeRef;
import com.volantis.mcs.build.themes.definitions.types.impl.ChoiceTypeImpl;
import com.volantis.mcs.build.themes.definitions.types.impl.FractionTypeImpl;
import com.volantis.mcs.build.themes.definitions.types.impl.KeywordImpl;
import com.volantis.mcs.build.themes.definitions.types.impl.KeywordsImpl;
import com.volantis.mcs.build.themes.definitions.types.impl.PairTypeImpl;
import com.volantis.mcs.build.themes.definitions.types.impl.TypeRefImpl;
import com.volantis.mcs.build.themes.definitions.values.AngleValue;
import com.volantis.mcs.build.themes.definitions.values.ColorValue;
import com.volantis.mcs.build.themes.definitions.values.FractionValue;
import com.volantis.mcs.build.themes.definitions.values.FrequencyValue;
import com.volantis.mcs.build.themes.definitions.values.InheritValue;
import com.volantis.mcs.build.themes.definitions.values.IntegerValue;
import com.volantis.mcs.build.themes.definitions.values.KeywordReference;
import com.volantis.mcs.build.themes.definitions.values.LengthValue;
import com.volantis.mcs.build.themes.definitions.values.ListValue;
import com.volantis.mcs.build.themes.definitions.values.PairValue;
import com.volantis.mcs.build.themes.definitions.values.PercentageValue;
import com.volantis.mcs.build.themes.definitions.values.StringValue;
import com.volantis.mcs.build.themes.definitions.values.TimeValue;
import com.volantis.mcs.build.themes.definitions.values.impl.AngleValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.ColorValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.FractionValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.FrequencyValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.InheritValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.IntegerValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.KeywordReferenceImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.LengthValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.ListValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.PairValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.PercentageValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.StringValueImpl;
import com.volantis.mcs.build.themes.definitions.values.impl.TimeValueImpl;

/**
 * Default concrete implementation of DefinitionsFactory, creating simple
 * implementation classes for each of the object types.
 */
public class DefaultDefinitionsFactory extends DefinitionsFactory {

    // Javadoc inherited
    public Property createProperty() {
        return new PropertyImpl();
    }

    public Rules createRules() {
        return new RulesImpl();
    }

    public Rule createRule() {
        return new RuleImpl();
    }

    // Javadoc inherited
    public KeywordReference createKeywordReference() {
        return new KeywordReferenceImpl();
    }

    // Javadoc inherited
    public Keywords createKeywords() {
        return new KeywordsImpl();
    }

    // Javadoc inherited
    public Keyword createKeyword() {
        return new KeywordImpl();
    }

    // Javadoc inherited
    public TypeDefinition createTypeDefinition() {
        return new TypeDefinitionImpl();
    }

    // Javadoc inherited
    public ChoiceType createChoiceType() {
        return new ChoiceTypeImpl();
    }

    // Javadoc inherited
    public PairType createPairType() {
        return new PairTypeImpl();
    }

    // Javadoc inherited.
    public FractionType createFractionType() {
        return new FractionTypeImpl();
    }

    // Javadoc inherited
    public TypeRef createTypeRef() {
        return new TypeRefImpl();
    }

    // Javadoc inherited
    public IntegerValue createIntegerValue() {
        return new IntegerValueImpl();
    }

    // Javadoc inherited
    public AngleValue createAngleValue() {
        return new AngleValueImpl();
    }

    // Javadoc inherited
    public StringValue createStringValue() {
        return new StringValueImpl();
    }

    // Javadoc inherited
    public PercentageValue createPercentageValue() {
        return new PercentageValueImpl();
    }

    // Javadoc inherited
    public PairValue createPairValue() {
        return new PairValueImpl();
    }

    // Javadoc inherited
    public ListValue createListValue() {
        return new ListValueImpl();
    }
    
    // Javadoc inherited
    public InheritValue createInheritValue() {
        return new InheritValueImpl();
    }

    // Javadoc inherited
    public TimeValue createTimeValue() {
        return new TimeValueImpl();
    }

    // Javadoc inherited
    public ColorValue createColorValue() {
        return new ColorValueImpl();
    }

    public LengthValue createLengthValue() {
        return new LengthValueImpl();
    }

    public FrequencyValue createFrequencyValue() {
        return new FrequencyValueImpl();
    }

    public FractionValue createFractionValue() {
        return new FractionValueImpl();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 21-Oct-04	5833/3	adrianj	VBM:2004082605 Fix initial values for StylePropertyDetails

 25-Mar-04	3550/1	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
*/
