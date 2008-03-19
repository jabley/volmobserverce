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

package com.volantis.mcs.build.themes.definitions;

import com.volantis.mcs.build.themes.definitions.impl.DefaultDefinitionsFactory;
import com.volantis.mcs.build.themes.definitions.types.ChoiceType;
import com.volantis.mcs.build.themes.definitions.types.FractionType;
import com.volantis.mcs.build.themes.definitions.types.Keyword;
import com.volantis.mcs.build.themes.definitions.types.Keywords;
import com.volantis.mcs.build.themes.definitions.types.PairType;
import com.volantis.mcs.build.themes.definitions.types.TypeRef;
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

/**
 * Abstract factory for creating objects related to theme definitions.
 */
public abstract class DefinitionsFactory {

    /**
     * The default instance of the factory.
     */
    private static DefinitionsFactory defaultInstance
        = new DefaultDefinitionsFactory();

    /**
     * Get the default instance of the factory.
     * @return The default instance of the factory.
     */
    public static DefinitionsFactory getDefaultInstance() {
        return defaultInstance;
    }

    /**
     * Create a Property instance to hold the meta information about a theme
     * stylistic property.
     * @return A new Property instance.
     */
    public abstract Property createProperty();

    /**
     * Create a Rules instance to hold the set of mapping rules.
     *
     * @return A new Rules instance.
     */
    public abstract Rules createRules();

    /**
      * Create a Rule instance to hold a single from-to mapping.
      *
      * @return A new Rules instance.
      */
     public abstract Rule createRule();

     /**
     * Create a KeywordReference instance.
     * @return A new KeywordReference instance.
     */
    public abstract KeywordReference createKeywordReference();

    /**
     * Create a Keywords instance.
     * @return A new Keywords instance.
     */
    public abstract Keywords createKeywords();

    /**
     * Create a Keyword instance.
     * @return A new Keyword instance.
     */
    public abstract Keyword createKeyword();

    /**
     * Create a TypeDefinition instance.
     * @return A new TypeDefinition instance.
     */
    public abstract TypeDefinition createTypeDefinition();

    /**
     * Create a ChoiceType instance.
     * @return A new ChoiceType instance.
     */
    public abstract ChoiceType createChoiceType();

    /**
     * Create a PairType instance.
     * @return A new PairType instance.
     */
    public abstract PairType createPairType();

    /**
     * Create a FractionType instance.
     * @return A new FractionType instance.
     */
    public abstract FractionType createFractionType();

    /**
     * Create a TypeRef instance.
     * @return A new TypeRef instance.
     */
    public abstract TypeRef createTypeRef();

    /**
     * Create a IntegerValue instance.
     * @return A new IntegerValue instance.
     */
    public abstract IntegerValue createIntegerValue();

    /**
     * Create a AngleValue instance.
     * @return A new AngleValue instance.
     */
    public abstract AngleValue createAngleValue();

    /**
     * Create a StringValue instance.
     * @return A new StringValue instance.
     */
    public abstract StringValue createStringValue();

    /**
     * Create a PercentageValue instance.
     * @return A new PercentageValue instance.
     */
    public abstract PercentageValue createPercentageValue();

    /**
     * Create a PairValue instance.
     * @return A new PairValue instance.
     */
    public abstract PairValue createPairValue();
    
    /**
     * Create a ListValue instance.
     * @return A new PairValue instance.
     */
    public abstract ListValue createListValue();

    /**
     * Create a InheritValue instance.
     * @return A new InheritValue instance.
     */
    public abstract InheritValue createInheritValue();

    /**
     * Create a TimeValue instance.
     * @return A new TimeValue instance.
     */
    public abstract TimeValue createTimeValue();

    /**
     * Create a ColorValue instance.
     * @return A new ColorValue instance.
     */
    public abstract ColorValue createColorValue();

    /**
     * Create a LengthValue instance.
     * @return A new LengthValue instance.
     */
    public abstract LengthValue createLengthValue();

    /**
     * Create a FrequencyValue instance.
     * @return A new FrequencyValue instance.
     */
    public abstract FrequencyValue createFrequencyValue();

    /**
     * Create a FractionValue instance.
     * @return A new FractionValue instance.
     */
    public abstract FractionValue createFractionValue();
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
