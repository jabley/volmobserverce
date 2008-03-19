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

package com.volantis.mcs.themes.model;

import com.volantis.mcs.model.descriptor.AbstractPropertyAccessor;
import com.volantis.mcs.model.descriptor.BeanDescriptorBuilder;
import com.volantis.mcs.model.descriptor.ClassDescriptor;
import com.volantis.mcs.model.descriptor.ModelDescriptorBuilder;
import com.volantis.mcs.model.descriptor.ModelObjectFactory;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.policies.InternalPolicyFactory;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.variants.theme.InternalThemeContent;
import com.volantis.mcs.policies.variants.theme.InternalThemeContentBuilder;
import com.volantis.mcs.themes.CombinedSelector;
import com.volantis.mcs.themes.InvalidSelector;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.SelectorSequence;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleSheetFactory;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.Rule;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ThemeModel {

    private static final Map STYLE_PROPERTY_2_PROPERTY_IDENTIFIER =
            new HashMap();
    private static final Map PROPERTY_IDENTIFIER_2_STYLE_PROPERTY =
            new HashMap();

    public static final PropertyIdentifier RULES =
            new PropertyIdentifier(StyleSheet.class, "rules");
    
    public static final PropertyIdentifier STYLE_SHEET =
            new PropertyIdentifier(InternalThemeContent.class, "styleSheet");
    
    public static final PropertyIdentifier IMPORT_PARENT_DEVICE_THEME =
            new PropertyIdentifier(InternalThemeContent.class,
                    "importParentDeviceTheme");

    static {
            StylePropertyDefinitions definitions =
                    StylePropertyDetails.getDefinitions();
            for (Iterator i = definitions.stylePropertyIterator(); i.hasNext();) {
                StyleProperty property = (StyleProperty) i.next();
                PropertyIdentifier identifier = new PropertyIdentifier(
                        StyleProperty.class, property.getName());
                STYLE_PROPERTY_2_PROPERTY_IDENTIFIER.put(property, identifier);
                PROPERTY_IDENTIFIER_2_STYLE_PROPERTY.put(identifier, property);
            }
    }

    /**
     * Get the identifier for a style property.
     *
     * @param property The style property.
     * @return The property identifier.
     * @throws IllegalArgumentException if the style property was unrecognized.
     */
    public static PropertyIdentifier getPropertyIdentifierForStyleProperty(
            StyleProperty property) {
        final PropertyIdentifier identifier = (PropertyIdentifier)
                STYLE_PROPERTY_2_PROPERTY_IDENTIFIER.get(property);
        if (identifier == null) {
            throw new IllegalArgumentException(
                    "Unrecognised style property " + property.getName());
        }
        return identifier;
    }

    /**
     * Get the style property for a particular property identifier.
     *
     * @param identifier    of the style property
     * @return StyleProperty which maps to the given property identifier
     */
    public static StyleProperty getStylePropertyForPropertyIdentifier(
            PropertyIdentifier identifier) {
        final StyleProperty property = (StyleProperty)
                PROPERTY_IDENTIFIER_2_STYLE_PROPERTY.get(identifier);
        if (property == null) {
            throw new IllegalArgumentException(
                    "Unrecognised property identifier " + identifier.getName());
        }
        return property;
    }

    public static void buildThemeModelDescriptor(ModelDescriptorBuilder builder) {
        final StyleSheetFactory styleSheetFactory = StyleSheetFactory.getDefaultInstance();

        final InternalPolicyFactory policyFactory = (InternalPolicyFactory)
                PolicyFactory.getDefaultInstance();

        // Create the bean descriptors for InternalThemeContent.
        BeanDescriptorBuilder themeContentBuilder = builder.getBeanBuilder(
                InternalThemeContentBuilder.class, new ModelObjectFactory() {
                    public Object createObject() {
                        return policyFactory.createThemeContentBuilder();
                    }
                });

        // Create the bean descriptors for StyleSheet.
        BeanDescriptorBuilder styleSheetBuilder = builder.getBeanBuilder(
                StyleSheet.class, new ModelObjectFactory() {
                    public Object createObject() {
                        return styleSheetFactory.createStyleSheet();
                    }
                });

        // Create the class descriptor for Rule.
        BeanDescriptorBuilder ruleBuilder = builder.getBeanBuilder(
                ThemeFactory.getDefaultInstance().getRuleClass(), null);

        // Create the class descriptor for StyleProperties.
        BeanDescriptorBuilder stylePropertiesBuilder = builder.getBeanBuilder(
                StyleProperties.class, new ModelObjectFactory() {
                    public Object createObject() {
                        final ThemeFactory themeFactory =
                            ThemeFactory.getDefaultInstance();
                        return themeFactory.createMutableStyleProperties();
                    }
                });

        // Make this opaque for now.
        ClassDescriptor propertyValueDescriptor =
                builder.addOpaqueClassDescriptor(PropertyValue.class);

//        ClassDescriptor styleValueDescriptor =
//                builder.addBaseClassDescriptor(StyleValue.class);

        ClassDescriptor selectorDescriptor =
                builder.addBaseClassDescriptor(Selector.class);

        // Treat all the selectors as opaque.
        builder.addOpaqueClassDescriptor(SelectorSequence.class);
        builder.addOpaqueClassDescriptor(CombinedSelector.class);
        builder.addOpaqueClassDescriptor(InvalidSelector.class);

        themeContentBuilder.addPropertyDescriptor(
                IMPORT_PARENT_DEVICE_THEME,
                builder.getTypeDescriptor(Boolean.TYPE),
                new AbstractPropertyAccessor() {
                    public Object get(Object object) {
                        return ((InternalThemeContentBuilder) object)
                                .getImportParent()
                                ? Boolean.TRUE : Boolean.FALSE;
                    }

                    public void set(Object object, Object value) {
                        ((InternalThemeContentBuilder) object).setImportParent(
                                ((Boolean) value).booleanValue());
                    }
                }, false);

        themeContentBuilder.addPropertyDescriptor(
                STYLE_SHEET,
                builder.getTypeDescriptor(StyleSheet.class),
                new AbstractPropertyAccessor() {
                    public Object get(Object object) {
                        return ((InternalThemeContentBuilder) object)
                                .getStyleSheet();
                    }

                    public void set(Object object, Object value) {
                        ((InternalThemeContentBuilder) object).setStyleSheet(
                                (StyleSheet) value);
                    }
                }, false);

        styleSheetBuilder.addPropertyDescriptor(
                RULES,
                builder.getStandardListDescriptor(
                        ArrayList.class,
                        builder.getClassDescriptor(
                            ThemeFactory.getDefaultInstance().getRuleClass())),
                new AbstractPropertyAccessor() {
                    public Object get(Object object) {
                        return ((StyleSheet) object).getRules();
                    }

                    public void set(Object object, Object value) {
                        throw new UnsupportedOperationException();
                    }
                }, true);

        ruleBuilder.addPropertyDescriptor(
                Rule.SELECTORS,
                builder.getStandardListDescriptor(
                        ArrayList.class, selectorDescriptor),
                new AbstractPropertyAccessor() {
                    public Object get(Object object) {
                        return ((Rule) object).getSelectors();
                    }

                    public void set(Object object, Object value) {
                        ((Rule) object).setSelectors((List) value);
                    }
                },
                true);

        ruleBuilder.addPropertyDescriptor(
                Rule.STYLE_PROPERTIES,
                builder.getClassDescriptor(StyleProperties.class),
                new AbstractPropertyAccessor() {
                    public Object get(Object object) {
                        return ((Rule) object).getProperties();
                    }

                    public void set(Object object, Object value) {
                        ((Rule) object).setProperties((StyleProperties) value);
                    }
                },
                false);

        StylePropertyDefinitions definitions = StylePropertyDetails.getDefinitions();
        for (Iterator i = definitions.stylePropertyIterator(); i.hasNext();) {
            StyleProperty property = (StyleProperty) i.next();
            PropertyIdentifier identifier = new PropertyIdentifier(
                    StyleProperty.class, property.getName());
            stylePropertiesBuilder.addPropertyDescriptor(
                    identifier,
                    propertyValueDescriptor,
                    new PropertyValueAccessor(property),
                    false);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/3	emma	VBM:2005111705 Interim commit

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 01-Dec-05	10512/1	pduffin	Quick commit for GUI fixes

 29-Nov-05	10505/3	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/2	pduffin	VBM:2005111405 Massive changes for performance

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 31-Oct-05	9961/3	pduffin	VBM:2005101811 Committing restructuring

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 26-Oct-05	9961/1	pduffin	VBM:2005101811 Improved validation, checked for duplicate devices, added support for validation in the runtime

 ===========================================================================
*/
