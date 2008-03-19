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

package com.volantis.mcs.runtime.policies.theme;

import com.volantis.mcs.context.BaseURLProvider;
import com.volantis.mcs.context.CurrentProjectProvider;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.runtime.RuntimeProject;
import com.volantis.mcs.runtime.policies.expression.PolicyExpressionParser;
import com.volantis.mcs.runtime.policies.expression.PolicyExpressionParserImpl;
import com.volantis.mcs.runtime.policies.expression.RuntimePolicyReferenceExpression;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleList;
import com.volantis.mcs.themes.StylePair;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.mcs.themes.StyleValueFactory;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.properties.StyleProperty;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.ArrayList;

public class StyleSheetActivatorImpl
        implements StyleSheetActivator, CurrentProjectProvider, BaseURLProvider {

    private static final StyleValueFactory STYLE_VALUE_FACTORY =
        StyleValueFactory.getDefaultInstance();

    private final RuntimeProject project;
    private final MarinerURL baseURL;
    private final PolicyExpressionParser parser;

    public StyleSheetActivatorImpl(RuntimeProject project, MarinerURL baseURL) {
        this.project = project;
        this.baseURL = baseURL;

        parser = new PolicyExpressionParserImpl(this, this, null);
    }

    public RuntimeProject getCurrentProject() {
        return project;
    }

    public void activate(StyleSheet styleSheet) {
        List rules = styleSheet.getRules();
        for (Iterator i = rules.iterator(); i.hasNext();) {
            Rule rule = (Rule) i.next();
            MutableStyleProperties properties = (MutableStyleProperties)
                    rule.getProperties();

            // Although it is a little stupid it is allowed, at least for now
            // to have no properties for a selector.
            if (properties != null) {
                Iterator p = properties.propertyValueIterator();
                while (p.hasNext()) {
                    PropertyValue propertyValue = (PropertyValue) p.next();
                    StyleProperty property = propertyValue.getProperty();
                    StyleValue value = propertyValue.getValue();
                    StyleValue activated = activateStyleValue(property, value);
                    if (activated != value) {
                        propertyValue =
                            ThemeFactory.getDefaultInstance().createPropertyValue(
                                property, activated,
                                propertyValue.getPriority());
                        properties.setPropertyValue(propertyValue);
                    }
                }
            }
        }
    }

    private StyleValue activateStyleValue(
            StyleProperty property, StyleValue value) {

        if (value instanceof StyleList) {
            return activateStyleList(property, (StyleList) value);
        } else if (value instanceof StylePair) {
            return activateStylePair(property, (StylePair) value);
        } else if (value instanceof StyleComponentURI) {
            StyleComponentURI componentURI = (StyleComponentURI) value;
            return activateStyleComponentURI(property, componentURI);
        } else {
            return value;
        }
    }

    private StyleValue activateStyleComponentURI(
            StyleProperty property, StyleComponentURI componentURI) {

        String expression = componentURI.getExpressionAsString();

        PolicyType expectedPolicyType;
        if (property == StylePropertyDetails.MCS_BACKGROUND_DYNAMIC_VISUAL) {
            expectedPolicyType = PolicyType.VIDEO;
        } else {
            expectedPolicyType = PolicyType.IMAGE;
        }

        RuntimePolicyReferenceExpression runtime =
                parser.parsePolicyOrUnquotedExpression(expression,
                        expectedPolicyType);

        return STYLE_VALUE_FACTORY.getComponentURI(null, runtime);
    }

    private StyleValue activateStylePair(
            StyleProperty property, StylePair pair) {

        StyleValue first = pair.getFirst();
        StyleValue second = pair.getSecond();

        StyleValue activatedFirst = activateStyleValue(property, first);
        StyleValue activatedSecond = activateStyleValue(property, second);

        if (activatedFirst != first || activatedSecond != second) {
            return STYLE_VALUE_FACTORY.getPair(first, second);
        } else {
            return pair;
        }
    }

    private StyleValue activateStyleList(
            StyleProperty property, StyleList list) {

        List items = new ArrayList(list.getList());
        boolean changed = false;
        for (ListIterator i = items.listIterator(); i.hasNext();) {
            StyleValue value = (StyleValue) i.next();
            StyleValue activatedValue = activateStyleValue(property, value);
            if (activatedValue != value) {
                i.set(activatedValue);
                changed = true;
            }
        }

        if (changed) {
            return STYLE_VALUE_FACTORY.getList(items);
        } else {
            return list;
        }
    }

    public MarinerURL getBaseURL() {
        return baseURL;
    }
}
